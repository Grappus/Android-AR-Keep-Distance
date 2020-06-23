package android.tesseract.jio.covid19.ar.networkcalling.model

/**
 * Created by Dipanshu Harbola on 18/6/20.
 */
data class LoginRequest(
    val credentials: LoginCreds,
    val userType: Int = 2,
    val strategy: String = "jio"
)

data class LoginCreds(
    val phone: String,
    val fullName: String
)