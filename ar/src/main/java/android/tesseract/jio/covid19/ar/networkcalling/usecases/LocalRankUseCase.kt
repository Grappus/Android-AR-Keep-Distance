package android.tesseract.jio.covid19.ar.networkcalling.usecases

import android.annotation.SuppressLint
import android.tesseract.jio.covid19.ar.networkcalling.Callback
import android.tesseract.jio.covid19.ar.networkcalling.model.MyLeaderBoardRank
import android.tesseract.jio.covid19.ar.networkcalling.retrofit.NetworkUtil
import android.tesseract.jio.covid19.ar.utils.Prefs
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_AUTH_TOKEN
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Dipanshu Harbola on 6/7/20.
 */
class LocalRankUseCase {

    @SuppressLint("CheckResult")
    fun getMyLocalRank(callback: Callback<MyLeaderBoardRank>) {
        NetworkUtil.userService.getMyLocalRank(Prefs.getPrefsString(USER_AUTH_TOKEN))
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