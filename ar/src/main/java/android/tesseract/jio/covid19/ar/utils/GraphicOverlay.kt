package android.tesseract.jio.covid19.ar.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.tesseract.jio.covid19.ar.tflite.Classifier
import android.util.AttributeSet
import android.view.View

/**
 * Created by Dipanshu Harbola on 29/5/20.
 */
class GraphicOverlay(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val lock = Any()

    private val graphics = mutableListOf<Classifier.Recognition>()

    private val rect = RectF()
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

    fun invalidateOverlay() {
        graphics.clear()
        postInvalidate()
    }
}