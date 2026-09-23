package eightBugs.osteocare.screening.pose

import android.graphics.Bitmap
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import eightBugs.osteocare.screening.data.models.MovementResult
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.sqrt

class PoseVideoAnalyzer {

    private val detector: PoseDetector

    init {
        val options =
            PoseDetectorOptions.Builder()
                .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
                .build()

        detector = PoseDetection.getClient(options)
    }

    data class PoseFrame(
        val timestampMs: Long,
        val pose: Pose
    )

    fun processBitmap(
        bitmap: Bitmap,
        timestampMs: Long
    ): PoseFrame? {

        return try {
            val image = InputImage.fromBitmap(bitmap, 0)

            val pose = Tasks.await(
                detector.process(image)
            )

            if (pose.allPoseLandmarks.isEmpty()) {
                null
            } else {
                PoseFrame(
                    timestampMs = timestampMs,
                    pose = pose
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun calculateAngle(
        a: PoseLandmark,
        b: PoseLandmark,
        c: PoseLandmark
    ): Double {

        val abX = a.position.x - b.position.x
        val abY = a.position.y - b.position.y

        val cbX = c.position.x - b.position.x
        val cbY = c.position.y - b.position.y

        val dot =
            abX * cbX +
                    abY * cbY

        val magnitudeAB =
            sqrt(
                abX * abX +
                        abY * abY
            )

        val magnitudeCB =
            sqrt(
                cbX * cbX +
                        cbY * cbY
            )

        if (
            magnitudeAB == 0f ||
            magnitudeCB == 0f
        ) {
            return 0.0
        }

        val cosine =
            (
                    dot /
                            (
                                    magnitudeAB *
                                            magnitudeCB
                                    )
                    ).coerceIn(-1f, 1f)

        return Math.toDegrees(
            acos(cosine.toDouble())
        )
    }

    private fun getKneeAngles(
        pose: Pose
    ): Pair<Double, Double>? {

        val leftHip =
            pose.getPoseLandmark(
                PoseLandmark.LEFT_HIP
            ) ?: return null

        val leftKnee =
            pose.getPoseLandmark(
                PoseLandmark.LEFT_KNEE
            ) ?: return null

        val leftAnkle =
            pose.getPoseLandmark(
                PoseLandmark.LEFT_ANKLE
            ) ?: return null

        val rightHip =
            pose.getPoseLandmark(
                PoseLandmark.RIGHT_HIP
            ) ?: return null

        val rightKnee =
            pose.getPoseLandmark(
                PoseLandmark.RIGHT_KNEE
            ) ?: return null

        val rightAnkle =
            pose.getPoseLandmark(
                PoseLandmark.RIGHT_ANKLE
            ) ?: return null

        val leftAngle =
            calculateAngle(
                leftHip,
                leftKnee,
                leftAnkle
            )

        val rightAngle =
            calculateAngle(
                rightHip,
                rightKnee,
                rightAnkle
            )

        return Pair(
            leftAngle,
            rightAngle
        )
    }

    fun calculateRom(
        frames: List<PoseFrame>
    ): Pair<Double, Double> {

        val leftAngles =
            mutableListOf<Double>()

        val rightAngles =
            mutableListOf<Double>()

        for (frame in frames) {

            val angles =
                getKneeAngles(frame.pose)
                    ?: continue

            val leftAngle =
                angles.first

            val rightAngle =
                angles.second

            if (
                leftAngle in 0.0..180.0 &&
                rightAngle in 0.0..180.0
            ) {

                leftAngles.add(
                    leftAngle
                )

                rightAngles.add(
                    rightAngle
                )
            }
        }

        if (leftAngles.isEmpty()) {
            return Pair(0.0, 0.0)
        }

        val leftRom =
            leftAngles.maxOrNull()!! -
                    leftAngles.minOrNull()!!

        val rightRom =
            rightAngles.maxOrNull()!! -
                    rightAngles.minOrNull()!!

        return Pair(
            leftRom,
            rightRom
        )
    }

    fun calculateAsymmetry(
        leftRom: Double,
        rightRom: Double
    ): Double {

        val average =
            (
                    leftRom +
                            rightRom
                    ) / 2.0

        if (average <= 0.0) {
            return 0.0
        }

        return (
                abs(
                    leftRom -
                            rightRom
                ) / average
                ) * 100.0
    }

    fun calculateSitToStand(
        frames: List<PoseFrame>
    ): Pair<Double, Int> {

        if (frames.isEmpty()) {
            return Pair(0.0, 0)
        }

        var state =
            MovementState.SITTING

        var startTime =
            0L

        var firstStandTime =
            0L

        var repetitions =
            0

        for (frame in frames) {

            val angles =
                getKneeAngles(frame.pose)
                    ?: continue

            val averageKnee =
                (
                        angles.first +
                                angles.second
                        ) / 2.0

            when (state) {

                MovementState.SITTING -> {

                    if (startTime == 0L) {
                        startTime =
                            frame.timestampMs
                    }

                    if (averageKnee > 150) {

                        state =
                            MovementState.STANDING

                        if (firstStandTime == 0L) {

                            firstStandTime =
                                frame.timestampMs
                        }
                    }
                }

                MovementState.STANDING -> {

                    if (averageKnee < 120) {

                        repetitions++

                        state =
                            MovementState.SITTING
                    }
                }
            }
        }

        val timeSeconds =
            if (
                startTime > 0 &&
                firstStandTime > startTime
            ) {

                (
                        firstStandTime -
                                startTime
                        ) / 1000.0

            } else {
                0.0
            }

        return Pair(
            timeSeconds,
            repetitions
        )
    }

    fun analyze(
        frames: List<PoseFrame>
    ): MovementResult {

        val rom =
            calculateRom(frames)

        val leftRom =
            rom.first

        val rightRom =
            rom.second

        val asymmetry =
            calculateAsymmetry(
                leftRom,
                rightRom
            )

        val sitStand =
            calculateSitToStand(frames)

        return MovementResult(
            leftRom = leftRom,
            rightRom = rightRom,
            asymmetry = asymmetry,
            sitToStandTime = sitStand.first,
            repetitions = sitStand.second,
            analyzedFrames = frames.size
        )
    }

    fun close() {
        detector.close()
    }

    private enum class MovementState {
        SITTING,
        STANDING
    }
}