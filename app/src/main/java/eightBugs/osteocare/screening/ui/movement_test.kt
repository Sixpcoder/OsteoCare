package eightBugs.osteocare.screening.ui

import android.content.Intent
import eightBugs.osteocare.R
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.PreviewView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.media.MediaMetadataRetriever
import eightBugs.osteocare.screening.data.models.MovementResult
import eightBugs.osteocare.screening.pose.PoseVideoAnalyzer

class movement_test : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var videoView: VideoView
    private lateinit var btnGallery: ImageButton
    private lateinit var btnRecord: ImageButton
    private lateinit var btnChairStand: Button
    private lateinit var btnWalk: Button
    private lateinit var btnKneeBend: Button
    private lateinit var tvTestName: TextView
    private lateinit var tvTestCaption: TextView
    private lateinit var tvRecordCaption: TextView
    private lateinit var tvQualityBadge: TextView
    private lateinit var tvRepCount: TextView
    private lateinit var tvTimer: TextView

    private lateinit var poseAnalyzer: PoseVideoAnalyzer

    private var selectedTest =
        TestType.CHAIR_STAND

    private val videoPicker =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->

            if (uri != null) {

                processSelectedVideo(uri)
            }
        }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_movement_test
        )


        initializeViews()

        initializePoseAnalyzer()

        setupButtons()
    }


    private fun initializeViews() {

        previewView =
            findViewById(
                R.id.previewViewa
            )


        videoView =
            findViewById(
                R.id.videoView
            )


        btnGallery =
            findViewById(
                R.id.btnGallery
            )


        btnRecord =
            findViewById(
                R.id.btnRecord
            )


        btnChairStand =
            findViewById(
                R.id.btnToggleChairStanda
            )


        btnWalk =
            findViewById(
                R.id.btnToggleWalka
            )


        btnKneeBend =
            findViewById(
                R.id.btnToggleKneeBenda
            )


        tvTestName =
            findViewById(
                R.id.tvTestName
            )


        tvTestCaption =
            findViewById(
                R.id.tvTestCaption
            )


        tvRecordCaption =
            findViewById(
                R.id.tvRecordCaption
            )


        tvQualityBadge =
            findViewById(
                R.id.tvQualityBadge
            )


        tvRepCount =
            findViewById(
                R.id.tvRepCount
            )


        tvTimer =
            findViewById(
                R.id.tvTimer
            )
    }


    private fun initializePoseAnalyzer() {

        poseAnalyzer =
            PoseVideoAnalyzer()
    }


    private fun setupButtons() {

        btnGallery.setOnClickListener {

            videoPicker.launch(
                "video/*"
            )
        }


        btnRecord.setOnClickListener {

            Toast.makeText(
                this,
                "Camera recording will be connected next.",
                Toast.LENGTH_SHORT
            ).show()
        }


        btnChairStand.setOnClickListener {

            selectedTest =
                TestType.CHAIR_STAND

            tvTestName.text =
                getString(R.string.chair_stand)

            tvTestCaption.text =
                getString(R.string.sit_down_and_stand_up_naturally)

            tvRecordCaption.text =
                getString(R.string.record_chair_stand)
        }


        btnWalk.setOnClickListener {

            selectedTest =
                TestType.WALK

            tvTestName.text =
                "Walk"

            tvTestCaption.text =
                "Walk naturally through the camera"

            tvRecordCaption.text =
                "Record walking test"
        }


        btnKneeBend.setOnClickListener {

            selectedTest =
                TestType.KNEE_BEND

            tvTestName.text =
                "Knee Bend"

            tvTestCaption.text =
                "Perform a controlled knee bend"

            tvRecordCaption.text =
                "Record knee bend"
        }
    }


    private fun processSelectedVideo(
        uri: Uri
    ) {

        // Show selected video

        previewView.visibility =
            View.GONE

        videoView.visibility =
            View.VISIBLE

        videoView.setVideoURI(uri)

        videoView.start()


        tvQualityBadge.text =
            "Analyzing video..."

        tvRepCount.visibility =
            View.VISIBLE

        tvTimer.visibility =
            View.VISIBLE

        tvRepCount.text =
            "Reps: 0"

        tvTimer.text =
            "Processing..."


        // Process video in background

        CoroutineScope(
            Dispatchers.Default
        ).launch {

            val frames =
                extractAndAnalyzeFrames(
                    uri
                )


            val result =
                poseAnalyzer.analyze(
                    frames
                )


            withContext(
                Dispatchers.Main
            ) {

                displayResults(
                    result
                )
            }
        }
    }

    private fun extractAndAnalyzeFrames(
        uri: Uri
    ): List<PoseVideoAnalyzer.PoseFrame> {

        val frames =
            mutableListOf<
                    PoseVideoAnalyzer.PoseFrame
                    >()


        val retriever =
            MediaMetadataRetriever()


        try {

            retriever.setDataSource(
                this,
                uri
            )


            val durationMs =
                retriever
                    .extractMetadata(
                        MediaMetadataRetriever
                            .METADATA_KEY_DURATION
                    )
                    ?.toLong()
                    ?: 0L


            val frameIntervalUs =
                100_000L


            var timeUs =
                0L


            while (
                timeUs <
                durationMs * 1000
            ) {

                val bitmap =
                    retriever.getFrameAtTime(
                        timeUs,
                        MediaMetadataRetriever
                            .OPTION_CLOSEST
                    )


                if (bitmap != null) {

                    val frame =
                        poseAnalyzer.processBitmap(
                            bitmap,
                            timeUs / 1000
                        )


                    if (frame != null) {

                        frames.add(
                            frame
                        )
                    }


                    bitmap.recycle()
                }


                timeUs +=
                    frameIntervalUs
            }


        } catch (e: Exception) {

            e.printStackTrace()

        } finally {

            retriever.release()
        }


        return frames
    }

    private fun displayResults(
        result: MovementResult
    ) {

        tvQualityBadge.text =
            "Analysis complete"

        tvRepCount.text =
            "Reps: ${result.repetitions}"

        tvTimer.text =
            String.format(
                "STS: %.2f sec",
                result.sitToStandTime
            )

        val message =
            """
        
        Frames analyzed: ${result.analyzedFrames}
        
        LEFT KNEE ROM
        %.1f°
        
        RIGHT KNEE ROM
        %.1f°
        
        ASYMMETRY
        %.1f%%
        
        SIT-TO-STAND
        %.2f seconds
        
        REPETITIONS
        %d
        
        """.trimIndent().format(
                result.leftRom,
                result.rightRom,
                result.asymmetry,
                result.sitToStandTime,
                result.repetitions
            )

        AlertDialog.Builder(this)
            .setTitle("Movement Analysis")
            .setMessage(message)
            .setNegativeButton("Close", null)
            .setPositiveButton("Show Result") { _, _ ->

                val intent =
                    Intent(
                        this,
                        result::class.java
                    )

                intent.putExtra(
                    "leftRom",
                    result.leftRom
                )

                intent.putExtra(
                    "rightRom",
                    result.rightRom
                )

                intent.putExtra(
                    "asymmetry",
                    result.asymmetry
                )

                intent.putExtra(
                    "sitToStandTime",
                    result.sitToStandTime
                )

                intent.putExtra(
                    "repetitions",
                    result.repetitions
                )

                intent.putExtra(
                    "analyzedFrames",
                    result.analyzedFrames
                )

                startActivity(intent)
            }
            .show()
    }


    override fun onDestroy() {

        super.onDestroy()

        if (::poseAnalyzer.isInitialized) {

            poseAnalyzer.close()
        }
    }


    enum class TestType {

        CHAIR_STAND,

        WALK,

        KNEE_BEND
    }
}