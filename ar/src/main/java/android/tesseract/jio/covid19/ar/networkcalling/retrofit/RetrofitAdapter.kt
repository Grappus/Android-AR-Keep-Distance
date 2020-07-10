package android.tesseract.jio.covid19.ar.networkcalling.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by Dipanshu Harbola on 18/6/20.
 */
class RetrofitAdapter {
    private var retrofit: Retrofit? = null
    private var gson: Gson? = null
    private val BASE_URL = "https://apps.tesseract.in/jio-covid19/api/v1/"

    @Synchronized
    fun getInstance(): Retrofit? {
        if (retrofit == null) {
            if (gson == null) {
                gson = GsonBuilder().setLenient().create()
            }
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson!!))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
        return retrofit
    }
}