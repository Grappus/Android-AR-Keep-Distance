package android.tesseract.jio.covid19.ar.networkcalling.model

/**
 * Created by Dipanshu Harbola on 18/6/20.
 */
data class SessionEndRequest(
    val startTime: String,
    val totalDuration: Long,
    val violationCount: Int,
    val safetyRate: String,
    val location: UserLocation
)

data class UserLocation(
    val latitude: String,
    val longitude: String
)