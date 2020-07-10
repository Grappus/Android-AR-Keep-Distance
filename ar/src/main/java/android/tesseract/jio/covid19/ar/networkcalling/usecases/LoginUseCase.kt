package android.tesseract.jio.covid19.ar.networkcalling.usecases

import android.annotation.SuppressLint
import android.tesseract.jio.covid19.ar.networkcalling.Callback
import android.tesseract.jio.covid19.ar.networkcalling.model.LoginRequest
import android.tesseract.jio.covid19.ar.networkcalling.model.LoginResponse
import android.tesseract.jio.covid19.ar.networkcalling.retrofit.NetworkUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Dipanshu Harbola on 18/6/20.
 */
class LoginUseCase {
    @SuppressLint("CheckResult")
    fun userLogin(loginRequest: LoginRequest, callback: Callback<LoginResponse>) {
        NetworkUtil.userService.userLogin(loginRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                callback.loading(true)
            }
            .subscribe(
                {
                    callback.loading(false)
                    callback.onSuccessCall(it)
                }, {
                    callback.loading(false)
                    callback.onFailureCall(it.message)
                })
    }
}