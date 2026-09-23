package eightBugs.osteocare.screening.ui
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

class PoseOverlayViewML @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var pose: Pose? = null

    private val pointPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFFFFFFF.toInt()
        style = Paint.Style.FILL
    }

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFFFFFFF.toInt()
        style = Paint.Style.STROKE
        strokeWidth = 6f
        strokeCap = Paint.Cap.ROUND
    }

    fun setPose(pose: Pose?) {
        this.pose = pose
        invalidate()
    }

    fun clearPose() {
        pose = null
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val currentPose = pose ?: return

        val landmarks = currentPose.allPoseLandmarks

        for (landmark in landmarks) {
            drawPoint(
                canvas,
                landmark
            )
        }

        drawConnection(
            canvas,
            currentPose,
            PoseLandmark.LEFT_SHOULDER,
            PoseLandmark.RIGHT_SHOULDER
        )

        drawConnection(
            canvas,
            currentPose,
            PoseLandmark.LEFT_SHOULDER,
            PoseLandmark.LEFT_ELBOW
        )

        drawConnection(
            canvas,
            currentPose,
            PoseLandmark.LEFT_ELBOW,
            PoseLandmark.LEFT_WRIST
        )

        drawConnection(
            canvas,
            currentPose,
            PoseLandmark.RIGHT_SHOULDER,
            PoseLandmark.RIGHT_ELBOW
        )

        drawConnection(
            canvas,
            currentPose,
            PoseLandmark.RIGHT_ELBOW,
            PoseLandmark.RIGHT_WRIST
        )

        drawConnection(
            canvas,
            currentPose,
            PoseLandmark.LEFT_SHOULDER,
            PoseLandmark.LEFT_HIP
        )

        drawConnection(
            canvas,
            currentPose,
            PoseLandmark.RIGHT_SHOULDER,
            PoseLandmark.RIGHT_HIP
        )

        drawConnection(
            canvas,
            currentPose,
            PoseLandmark.LEFT_HIP,
            PoseLandmark.RIGHT_HIP
        )

        drawConnection(
            canvas,
            currentPose,
            PoseLandmark.LEFT_HIP,
            PoseLandmark.LEFT_KNEE
        )

        drawConnection(
            canvas,
            currentPose,
            PoseLandmark.LEFT_KNEE,
            PoseLandmark.LEFT_ANKLE
        )

        drawConnection(
            canvas,
            currentPose,
            PoseLandmark.RIGHT_HIP,
            PoseLandmark.RIGHT_KNEE
        )

        drawConnection(
            canvas,
            currentPose,
            PoseLandmark.RIGHT_KNEE,
            PoseLandmark.RIGHT_ANKLE
        )

        drawConnection(
            canvas,
            currentPose,
            PoseLandmark.LEFT_ANKLE,
            PoseLandmark.LEFT_HEEL
        )

        drawConnection(
            canvas,
            currentPose,
            PoseLandmark.LEFT_HEEL,
            PoseLandmark.LEFT_FOOT_INDEX
        )

        drawConnection(
            canvas,
            currentPose,
            PoseLandmark.RIGHT_ANKLE,
            PoseLandmark.RIGHT_HEEL
        )

        drawConnection(
            canvas,
            currentPose,
            PoseLandmark.RIGHT_HEEL,
            PoseLandmark.RIGHT_FOOT_INDEX
        )

        drawConnection(
            canvas,
            currentPose,
            PoseLandmark.NOSE,
            PoseLandmark.LEFT_SHOULDER
        )

        drawConnection(
            canvas,
            currentPose,
            PoseLandmark.NOSE,
            PoseLandmark.RIGHT_SHOULDER
        )
    }

    private fun drawPoint(
        canvas: Canvas,
        landmark: PoseLandmark
    ) {
        val x = landmark.position.x
        val y = landmark.position.y

        canvas.drawCircle(
            x,
            y,
            7f,
            pointPaint
        )
    }

    private fun drawConnection(
        canvas: Canvas,
        pose: Pose,
        firstType: Int,
        secondType: Int
    ) {
        val first =
            pose.getPoseLandmark(firstType)
                ?: return

        val second =
            pose.getPoseLandmark(secondType)
                ?: return

        canvas.drawLine(
            first.position.x,
            first.position.y,
            second.position.x,
            second.position.y,
            linePaint
        )
    }
}