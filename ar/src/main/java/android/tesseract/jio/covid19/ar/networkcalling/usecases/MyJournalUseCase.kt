package android.tesseract.jio.covid19.ar.networkcalling.usecases

import android.tesseract.jio.covid19.ar.networkcalling.Callback
import android.tesseract.jio.covid19.ar.networkcalling.model.MyJournalResponse
import android.tesseract.jio.covid19.ar.networkcalling.retrofit.NetworkUtil
import android.tesseract.jio.covid19.ar.utils.Prefs
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_AUTH_TOKEN
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created by Dipanshu Harbola on 18/6/20.
 */
class MyJournalUseCase {

    fun getMyJournal(callback: Callback<MyJournalResponse>) {
        NetworkUtil.getApiInstance()!!
            .getMyJournal(Prefs.getPrefsString(USER_AUTH_TOKEN))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                callback.loading()
            }
            .subscribe(
                {
                    callback.onSuccessCall(it)
                }, {
                    callback.onFailureCall(it.message)
                })
    }
}