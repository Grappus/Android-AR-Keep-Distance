package android.tesseract.jio.covid19.ar.splash

import android.tesseract.jio.covid19.ar.networkcalling.Callback
import android.tesseract.jio.covid19.ar.networkcalling.model.*
import android.tesseract.jio.covid19.ar.networkcalling.retrofit.NetworkUtil
import android.tesseract.jio.covid19.ar.utils.Prefs
import android.tesseract.jio.covid19.ar.utils.PrefsConstants
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.IS_USER_LOGIN
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_AUTH_TOKEN
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_CREATED_AT
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_GLOBAL_RANK
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_ID
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_NAME
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_NOTIF_ON
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_SAFETY
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_SOUND_ON
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_VIB_ON
import android.util.Log
import androidx.lifecycle.ViewModel

/**
 * Created by Dipanshu Harbola on 18/6/20.
 */
class SplashViewModel: ViewModel() {

    var navigator: Navigator? = null

    override fun onCleared() {
        super.onCleared()
    }

    fun getUserInfo() {
        NetworkUtil.useCase.myInfoUseCase.getMyInfo(object: Callback<GetSelfInfo>() {
            override fun loading() {

            }

            override fun onSuccessCall(value: GetSelfInfo) {
                if (value.statusCode == 200) {
                    updateUserInfo(value.data)
                }
                else {
                    navigator?.navigateToNextScreen()
                }
            }

            override fun onFailureCall(message: String?) {
                navigator?.showNetworkError()
            }

        })
    }

    fun authenticateUser(phoneNumber: String, userName: String) {
        val loginRequest = LoginRequest(credentials = LoginCreds(phoneNumber, userName))
        NetworkUtil.useCase.loginUseCase.userLogin(loginRequest, object: Callback<LoginResponse>() {
            override fun loading() {

            }

            override fun onSuccessCall(value: LoginResponse) {
                if (value.statusCode == 200) {
                    Prefs.setPrefs(IS_USER_LOGIN, true)
                    Prefs.setPrefs(USER_AUTH_TOKEN, value.authToken)
                    updateUserInfo(value.data)
                }
                else {
                    navigator?.navigateToNextScreen()
                }
            }

            override fun onFailureCall(message: String?) {
                navigator?.showNetworkError()
            }

        })
    }

    private fun updateUserInfo(responseData: ResponseData) {
        Prefs.setPrefs(USER_NAME, responseData.fullName ?:"Unknown User")
        Prefs.setPrefs(USER_ID, responseData.id)
        Prefs.setPrefs(USER_SAFETY, responseData.lastNetScore.toInt())
        Prefs.setPrefs(USER_CREATED_AT, responseData.createdAt)
        Prefs.setPrefs(USER_NOTIF_ON, responseData.preferences.notification)
        Prefs.setPrefs(USER_VIB_ON, responseData.preferences.vibration)
        Prefs.setPrefs(USER_SOUND_ON, responseData.preferences.sound)
        navigator?.navigateToNextScreen()
    }

    interface Navigator {
        fun navigateToNextScreen()
        fun showNetworkError()
    }
}