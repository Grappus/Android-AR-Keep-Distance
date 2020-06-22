package android.tesseract.jio.covid19.ar.utils

import android.graphics.*
import android.media.Image
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Created by Dipanshu Harbola on 1/6/20.
 */
object ImageUtils {

    fun YUV420_888ToNV21(image: Image): ByteArray? {
        val nv21: ByteArray
        val yBuffer: ByteBuffer = image.planes[0].buffer
        val uBuffer: ByteBuffer = image.planes[1].buffer
        val vBuffer: ByteBuffer = image.planes[2].buffer
        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()
        nv21 = ByteArray(ySize + uSize + vSize)

        //U and V are swapped
        yBuffer[nv21, 0, ySize]
        vBuffer[nv21, ySize, vSize]
        uBuffer[nv21, ySize + vSize, uSize]
        return nv21
    }


    fun NV21ToByteArray(
        nv21: ByteArray?,
        width: Int,
        height: Int
    ): ByteArray? {
        val out = ByteArrayOutputStream()
        val yuv = YuvImage(nv21, ImageFormat.NV21, width, height, null)
        yuv.compressToJpeg(Rect(0, 0, width, height), 100, out)
        return out.toByteArray()
    }

    fun convertBitmapToBuffer(bitmap: Bitmap,
                              numOfBytesPerChannel: Int,
                              dimBatchSize: Int,
                              dimPixelSize: Int,
                              quantized: Boolean,
                              image_mean: Float = 127.5f,
                              image_std: Float = 127.5f): ByteBuffer{
        val intValues = IntArray(bitmap.width * bitmap.height)

        val imgData = ByteBuffer.allocateDirect(
            numOfBytesPerChannel *
                    dimBatchSize *
                    bitmap.width *
                    bitmap.height *
                    dimPixelSize)
        imgData.order(ByteOrder.nativeOrder())
        imgData.rewind()

        bitmap.getPixels(
            intValues,
            0,
            bitmap.width,
            0,
            0,
            bitmap.width,
            bitmap.height
        )

        var pixel = 0
        for (i in 0 until bitmap.width) {
            for (j in 0 until bitmap.height) {
                val value = intValues[pixel++]
                addPixelValue(
                    imgData,
                    value,
                    quantized,
                    image_mean,
                    image_std
                )
            }
        }
        return imgData
    }


    fun convertBitmapToResizedBuffer(bitmap: Bitmap,
                                       numOfBytesPerChannel: Int,
                                       dimBatchSize: Int,
                                       dimPixelSize: Int,
                                       dimImgSize: Int,
                                       quantized: Boolean,
                                       image_mean: Float = 127.5f,
                                       image_std: Float = 127.5f): ByteBuffer{
        val intValues = IntArray(dimImgSize * dimImgSize)

        val imgData = ByteBuffer.allocateDirect(
            numOfBytesPerChannel *
                    dimBatchSize *
                    dimImgSize *
                    dimImgSize *
                    dimPixelSize)
        imgData!!.order(ByteOrder.nativeOrder())
        imgData.rewind()

        val resizedBitmap = Bitmap.createScaledBitmap(
            bitmap,
            dimImgSize,
            dimImgSize,
            true)

        resizedBitmap.getPixels(
            intValues,
            0,
            resizedBitmap.width,
            0,
            0,
            resizedBitmap.width,
            resizedBitmap.height
        )

        var pixel = 0
        for (i in 0 until dimImgSize) {
            for (j in 0 until dimImgSize) {
                val value = intValues[pixel++]
                addPixelValue(
                    imgData,
                    value,
                    quantized,
                    image_mean,
                    image_std
                )
            }
        }
        bitmap.recycle()
        resizedBitmap.recycle()
        return imgData
    }

    fun addPixelValue(inBuffer: ByteBuffer,
                      pixelValue: Int,
                      quantized: Boolean,
                      image_mean: Float = 127.5f,
                      image_std: Float = 127.5f) {
        if (quantized) {
            inBuffer.put((pixelValue shr 16 and 0xFF).toByte())
            inBuffer.put((pixelValue shr 8 and 0xFF).toByte())
            inBuffer.put((pixelValue and 0xFF).toByte())
        } else {
            inBuffer.putFloat(((pixelValue shr 16 and 0xFF) - image_mean) / image_std)
            inBuffer.putFloat(((pixelValue shr 8 and 0xFF) - image_mean) / image_std)
            inBuffer.putFloat(((pixelValue and 0xFF) - image_mean) / image_std)
        }
    }

    fun cropImageFromPoints(bitmap: Bitmap, points: Array<Point?>): Bitmap {
        if (points[0] == null){
            return bitmap
        }
        var croppedImage: Bitmap? = null
        val leftTopRightBottom =
            extractFromPoints(
                points
            )
        leftTopRightBottom["left"] = 480 * leftTopRightBottom["left"]!! / 1080
        leftTopRightBottom["right"] = 480 * leftTopRightBottom["right"]!! / 1080
        leftTopRightBottom["top"] = 640 * leftTopRightBottom["top"]!! / 1782
        leftTopRightBottom["bottom"] = 640 * leftTopRightBottom["bottom"]!! / 1782

        val height = leftTopRightBottom["bottom"]!!-leftTopRightBottom["top"]!!
        val width = leftTopRightBottom["right"]!!-leftTopRightBottom["left"]!!

        if (height > 0
            && width > 0){
            croppedImage = Bitmap.createBitmap(bitmap,
                leftTopRightBottom["top"]!!,
                leftTopRightBottom["left"]!!,
                width,
                height,
                null,
                true
            )
        }
        else {
            return bitmap
        }
        return croppedImage
    }

    fun extractFromPoints(points: Array<Point?>): MutableMap<String, Int>{
        val leftTopRightBottom: MutableMap<String, Int> = mutableMapOf()
        leftTopRightBottom["left"] = points[0]!!.x
        leftTopRightBottom["top"] = points[0]!!.y
        leftTopRightBottom["right"] = points[0]!!.x
        leftTopRightBottom["bottom"] = points[0]!!.y

        for (i in 1 until points.size) {
            leftTopRightBottom["left"] = minOf(points[i]!!.x, leftTopRightBottom["left"]!!)
//                if (leftTopRightBottom["left"]!! > points[i]!!.x) points[i]!!.x
//                else leftTopRightBottom["left"]!!
            leftTopRightBottom["top"] = minOf(points[i]!!.y, leftTopRightBottom["top"]!!)
//                if (leftTopRightBottom["top"]!! > points[i]!!.y) points[i]!!.y
//                else leftTopRightBottom["top"]!!
            leftTopRightBottom["right"] = maxOf(points[i]!!.x, leftTopRightBottom["right"]!!)
//                if (leftTopRightBottom["right"]!! < points[i]!!.x) points[i]!!.x
//                else leftTopRightBottom["right"]!!
            leftTopRightBottom["bottom"] = maxOf(points[i]!!.y, leftTopRightBottom["bottom"]!!)
//                if (leftTopRightBottom["bottom"]!! < points[i]!!.y) points[i]!!.y
//                else leftTopRightBottom["bottom"]!!
        }
        return leftTopRightBottom
    }
}

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Bitmap.scaleBitmap(width: Int, height: Int): Bitmap {
    return Bitmap.createScaledBitmap(this, 300, 300, false)
}