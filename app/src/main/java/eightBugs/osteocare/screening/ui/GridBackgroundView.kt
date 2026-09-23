package eightBugs.osteocare.screening.ui
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class GridBackgroundView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0x33FFFFFF
        strokeWidth = 1f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        val verticalLines = 4
        val horizontalLines = 4

        for (i in 1 until verticalLines) {
            val x = width * i / verticalLines
            canvas.drawLine(
                x,
                0f,
                x,
                height,
                gridPaint
            )
        }

        for (i in 1 until horizontalLines) {
            val y = height * i / horizontalLines
            canvas.drawLine(
                0f,
                y,
                width,
                y,
                gridPaint
            )
        }
    }
}