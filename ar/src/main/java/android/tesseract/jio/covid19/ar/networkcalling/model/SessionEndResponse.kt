package android.tesseract.jio.covid19.ar.networkcalling.model

/**
 * Created by Dipanshu Harbola on 18/6/20.
 */
data class SessionEndResponse(
    val statusCode: Int,
    val rank: Int,
    val score: Double
)