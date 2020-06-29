package android.tesseract.jio.covid19.ar.networkcalling.retrofit

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.tesseract.jio.covid19.ar.networkcalling.ApiService
import android.tesseract.jio.covid19.ar.networkcalling.UseCasesImpl

/**
 * Created by Dipanshu Harbola on 18/6/20.
 */
object NetworkUtil {

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    val userService: ApiService
    get() = RetrofitAdapter().getInstance()?.create(ApiService::class.java)!!

    val useCase: UseCasesImpl
    get() = UseCasesImpl()
}