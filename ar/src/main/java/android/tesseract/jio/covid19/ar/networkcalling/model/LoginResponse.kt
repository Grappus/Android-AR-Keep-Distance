package android.tesseract.jio.covid19.ar.networkcalling.model

/**
 * Created by Dipanshu Harbola on 18/6/20.
 */
data class LoginResponse(
    val statusCode: Int,
    val data: ResponseData,
    val authToken: String
)

data class ResponseData(
    val lastActiveLocation: UserLocation,
    val lastNetScore: Float,
    val preferences: UserPreferences,
    val isPhoneVerified: Boolean,
    val isBlocked: Boolean,
    val fullName: String? = null,
    val clientId: String,
    val imageUrl: String? = null,
    val createdAt: String,
    val id: String
)

data class UserPreferences(
    val notification: Boolean,
    val vibration: Boolean,
    val sound: Boolean
)