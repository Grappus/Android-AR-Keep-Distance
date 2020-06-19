package android.tesseract.jio.covid19.ar.networkcalling.retrofit

import android.tesseract.jio.covid19.ar.networkcalling.ApiService
import android.tesseract.jio.covid19.ar.networkcalling.UseCasesImpl

/**
 * Created by Dipanshu Harbola on 18/6/20.
 */
object NetworkUtil {
    private var userService: ApiService? = null

    fun getApiInstance(): ApiService? {
        UseCasesImpl()
        if (userService == null) userService =
            RetrofitAdapter().getInstance()?.create(ApiService::class.java)
        return userService
    }

    val useCase: UseCasesImpl
    get() = UseCasesImpl()
}