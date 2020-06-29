package android.tesseract.jio.covid19.ar.networkcalling.usecases

import android.tesseract.jio.covid19.ar.networkcalling.Callback
import android.tesseract.jio.covid19.ar.networkcalling.model.MyJournalGraphResponse
import android.tesseract.jio.covid19.ar.networkcalling.retrofit.NetworkUtil
import android.tesseract.jio.covid19.ar.utils.Prefs
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_AUTH_TOKEN
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created by Dipanshu Harbola on 18/6/20.
 */
class GraphPlotDataUseCase {

    fun getGraphPlotData(callback: Callback<MyJournalGraphResponse>) {
        NetworkUtil.userService.getGraphPlotData(Prefs.getPrefsString(USER_AUTH_TOKEN), 30)
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