package android.tesseract.jio.covid19.ar.networkcalling

/**
 * Created by Dipanshu Harbola on 18/6/20.
 */
abstract class Callback<T> {
    abstract fun loading(isLoading: Boolean)
    abstract fun onSuccessCall(value: T)
    abstract fun onFailureCall(message: String?)
}