package android.tesseract.jio.covid19.ar.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.tesseract.jio.covid19.ar.tflite.Classifier

/**
 * Created by Dipanshu Harbola on 29/5/20.
 */
class GraphicOverlay(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val lock = Any()

    private val graphics = mutableListOf<Classifier.Recognition>()

    private var rectPaintStyle: Paint? = null

    private var rectFillPaintStyle: Paint? = null

    private val rectPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 2 * resources.displayMetrics.density
    }

    private val rectTransparent = Paint().apply {
        color = Color.TRANSPARENT
        style = Paint.Style.STROKE
        strokeWidth = 2 * resources.displayMetrics.density
    }

    private val textBackgroundPaint = Paint().apply {
        color = Color.parseColor("#66000000")
        style = Paint.Style.FILL
    }

    private val textPaint = Paint().apply {
        color = Color.RED
        textSize = 22 * resources.displayMetrics.density
    }

    private val rect = RectF()
    private val offset = resources.displayMetrics.density * 8
    private val round = resources.displayMetrics.density * 4

    private var scale: Float = 2f

    private var xOffset: Float = 0f
    private var yOffset: Float = 0f

    fun setConfiguration(imageWidth: Int,
                         imageHeight: Int) {
        //setRectPaint(rectColor)
        val overlayRatio = width / height.toFloat()
        val imageRatio = imageWidth / imageHeight.toFloat()

        if (overlayRatio < imageRatio) {
            scale = height / imageHeight.toFloat()

            xOffset = (imageWidth * scale - width) * 0.5f
            yOffset = 0f
        } else {
            scale = width / imageWidth.toFloat()

            xOffset = 0f
            yOffset = (imageHeight * scale - height) * 0.5f
        }
    }

    fun setRectPaint(rectColor: Int){
        rectPaintStyle = Paint().apply{
            color = rectColor
            style = Paint.Style.STROKE
            strokeWidth = 2 * resources.displayMetrics.density
        }
        rectFillPaintStyle = Paint().apply{
            color = rectColor
            setAlpha(0.3f)
            style = Paint.Style.FILL
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        synchronized(lock) {
            for (graphic in graphics) {
                rect.set(
                    graphic.location.left * scale,
                    graphic.location.top * scale,
                    graphic.location.right * scale,
                    graphic.location.bottom * scale
                )

                rect.offset(-xOffset, -yOffset)

                graphic.paint?.let {
                    val rectPaintStyle = it
                    canvas.drawRect(rect, rectPaintStyle) }
                graphic.paint?.let {
                    val rectFillPaintStyle = it
                    rectFillPaintStyle.apply {
                        setAlpha(0.3f)
                        style = Paint.Style.FILL
                    }
                    canvas.drawRoundRect(rect, round, round, rectFillPaintStyle) }
            }
        }
    }

    fun set(list: List<Classifier.Recognition>) {
        synchronized(lock) {
            graphics.clear()
            for (boxData in list) {
                graphics.add(boxData)
            }
        }
        postInvalidate()
    }
}