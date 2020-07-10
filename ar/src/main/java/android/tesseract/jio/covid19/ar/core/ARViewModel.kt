package android.tesseract.jio.covid19.ar.core

import android.tesseract.jio.covid19.ar.networkcalling.Callback
import android.tesseract.jio.covid19.ar.networkcalling.model.*
import android.tesseract.jio.covid19.ar.networkcalling.retrofit.NetworkUtil
import android.tesseract.jio.covid19.ar.utils.Prefs
import android.tesseract.jio.covid19.ar.utils.PrefsConstants
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_CREATED_AT
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_GLOBAL_RANK
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_ID
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_LAT
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_LNG
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_LOCAL_RANK
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_NAME
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_SAFETY
import android.tesseract.jio.covid19.ar.utils.TimeUtils
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import java.util.concurrent.TimeUnit

/**
 * Created by Dipanshu Harbola on 10/7/20.
 */
class ARViewModel : ViewModel() {

    var navigator: Navigator? = null

    private var localRankCount = 0
    private var globalRankCount = 0

    var myJournalDataField = ObservableField<JournalData>()
    var journalGraphPlotDataField = ObservableField<MutableList<GraphPlotData>>()

    fun getUserInfo() {
        NetworkUtil.useCase.myInfoUseCase.getMyInfo(object: Callback<GetSelfInfo>() {
            override fun loading(isLoading: Boolean) {
                navigator?.showLoading(isLoading)
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
                navigator?.showNetworkError(message!!)
            }

        })
    }

    fun authenticateUser(clientId: String, userName: String) {
        val loginRequest = LoginRequest(credentials = LoginCreds(clientId, userName))
        NetworkUtil.useCase.loginUseCase.userLogin(loginRequest, object: Callback<LoginResponse>() {
            override fun loading(isLoading: Boolean) {
                navigator?.showLoading(isLoading)
            }

            override fun onSuccessCall(value: LoginResponse) {
                if (value.statusCode == 200) {
                    Prefs.setPrefs(PrefsConstants.IS_USER_LOGIN, true)
                    Prefs.setPrefs(PrefsConstants.USER_AUTH_TOKEN, value.authToken)
                    Prefs.setPrefs(USER_ID, value.data.id)
                    updateUserInfo(value.data)
                }
            }

            override fun onFailureCall(message: String?) {
                navigator?.showNetworkError(message!!)
            }

        })
    }

    private fun updateUserInfo(responseData: ResponseData) {
        Prefs.setPrefs(USER_NAME, responseData.fullName ?:"Unknown User")
        Prefs.setPrefs(USER_ID, responseData.id)
        Prefs.setPrefs(USER_SAFETY, responseData.lastNetScore.toInt())
        Prefs.setPrefs(USER_CREATED_AT, responseData.createdAt)
        Prefs.setPrefs(PrefsConstants.USER_NOTIF_ON, responseData.preferences.notification)
        Prefs.setPrefs(PrefsConstants.USER_VIB_ON, responseData.preferences.vibration)
        Prefs.setPrefs(PrefsConstants.USER_SOUND_ON, responseData.preferences.sound)
        navigator?.navigateToNextScreen()
    }

    fun getMyLocalRank() {
        navigator?.showLeaderBoardLoading(true)
        if (localRankCount == 0) {
            NetworkUtil.useCase.localRankUseCase.getMyLocalRank(object :
                Callback<MyLeaderBoardRank>() {
                override fun loading(isLoading: Boolean) {}

                override fun onSuccessCall(value: MyLeaderBoardRank) {
                    if (value.statusCode == 200) {
                        localRankCount++
                        Prefs.setPrefs(USER_LOCAL_RANK, value.data)
                        getLocalLeaderBoard()
                    } else navigator?.showError("Something is wrong.. ${value.statusCode}")
                }

                override fun onFailureCall(message: String?) {
                    navigator?.showLeaderBoardLoading(false)
                    navigator?.showError(message!!)
                }

            })
        }
        else getLocalLeaderBoard()
    }

    fun getMyGlobalRank() {
        navigator?.showLeaderBoardLoading(true)
        if (globalRankCount == 0) {
            NetworkUtil.useCase.globalRankUseCase.getMyGlobalRank(object :
                Callback<MyLeaderBoardRank>() {
                override fun loading(isLoading: Boolean) {}

                override fun onSuccessCall(value: MyLeaderBoardRank) {
                    if (value.statusCode == 200) {
                        globalRankCount++
                        Prefs.setPrefs(USER_GLOBAL_RANK, value.data)
                        getGlobalLeaderBoard()
                    } else navigator?.showError("Something is wrong.. ${value.statusCode}")
                }

                override fun onFailureCall(message: String?) {
                    navigator?.showLeaderBoardLoading(false)
                    navigator?.showError(message!!)
                }

            })
        }
        else getGlobalLeaderBoard()
    }

    fun getLocalLeaderBoard() {
        NetworkUtil.useCase.localLeaderBoardUseCase.getLocalLeaderBoard(object :
            Callback<LeaderBoard>() {
            override fun loading(isLoading: Boolean) {}

            override fun onSuccessCall(value: LeaderBoard) {
                navigator?.showLeaderBoardLoading(false)
                val safetyPercent = Prefs.getPrefsInt(USER_SAFETY)
                val name = Prefs.getPrefsString(USER_NAME)
                val id = Prefs.getPrefsString(USER_ID)
                val strtDay = Prefs.getPrefsString(USER_CREATED_AT)
                if (value.statusCode == 200) {
                    val myRankResult = RankResult(
                        safetyPercent.toFloat(), name, "", strtDay, id, true
                    )
                    val result = value.data.result
                    val filteredList = result.filter { it.id != id }.toMutableList()
                    filteredList.add(0, myRankResult)
                    navigator?.localLeaderBoardList(filteredList)
                } else navigator?.showError("Something is wrong.. ${value.statusCode}")
            }

            override fun onFailureCall(message: String?) {
                navigator?.showLeaderBoardLoading(false)
                navigator?.showError(message!!)
            }

        })
    }

    fun getGlobalLeaderBoard() {
        NetworkUtil.useCase.globalLeaderBoardUseCase.getGlobalLeaderBoard(object :
            Callback<LeaderBoard>() {
            override fun loading(isLoading: Boolean) {}

            override fun onSuccessCall(value: LeaderBoard) {
                navigator?.showLeaderBoardLoading(false)
                val safetyPercent = Prefs.getPrefsInt(USER_SAFETY)
                val name = Prefs.getPrefsString(USER_NAME)
                val id = Prefs.getPrefsString(USER_ID)
                val strtDay = Prefs.getPrefsString(USER_CREATED_AT)
                if (value.statusCode == 200) {
                    val myRankResult = RankResult(
                        safetyPercent.toFloat(), name, "", strtDay, id, true
                    )
                    val result = value.data.result
                    val filteredList = result.filter { it.id != id }.toMutableList()
                    filteredList.add(0, myRankResult)
                    navigator?.globalLeaderBoardList(filteredList)
                } else navigator?.showError("Something is wrong.. ${value.statusCode}")
            }

            override fun onFailureCall(message: String?) {
                navigator?.showLeaderBoardLoading(false)
                navigator?.showError(message!!)
            }

        })
    }

    fun sendSessionEndInfo(sessionInfo: SessionInfo) {
        val sessionStartTime = TimeUtils.getTime(sessionInfo.sessionStartTime, TimeUtils.TIME_SERVER)
        val sessionTimeGap = sessionInfo.sessionEndTime - sessionInfo.sessionStartTime
        val totalDuration = TimeUnit.MILLISECONDS.toSeconds(sessionTimeGap)
        val violationCount = sessionInfo.violationCount
        val safetyRate =  sessionInfo.safetyPercent.removeSuffix("%")
        val location = UserLocation(
            latitude = sessionInfo.latitude, longitude = sessionInfo.longitude
        )
        val sessionEndRequest = SessionEndRequest(
            startTime = sessionStartTime, totalDuration = totalDuration,
            violationCount = violationCount.toInt(), safetyRate = safetyRate, location = location
        )
        NetworkUtil.useCase.sessionActivityUseCase.postSessionActivity(sessionEndRequest, object: Callback<SessionEndResponse>() {
            override fun loading(isLoading: Boolean) {
                navigator?.showLoading(isLoading)
            }

            override fun onSuccessCall(value: SessionEndResponse) {
                if (value.statusCode == 200) {
                    Prefs.setPrefs(USER_SAFETY, value.score.toInt())
                    Prefs.setPrefs(USER_GLOBAL_RANK, value.rank)
                    Prefs.setPrefs(USER_LOCAL_RANK, value.nearmeRank)
                    navigator?.navigateToEndSession(sessionInfo)
                }
            }

            override fun onFailureCall(message: String?) {
                navigator?.showError(message!!)
            }

        })

    }

    fun updateUserLocation() {
        val location = UserLocation(
            latitude = Prefs.getPrefsFloat(USER_LAT, 0.0f), longitude = Prefs.getPrefsFloat(USER_LNG, 0.0f)
        )
        val user = User(lastActiveLocation = location)
        NetworkUtil.useCase.updateUserInfoUseCase.updateUserInfo(user, object: Callback<GetSelfInfo>() {
            override fun loading(isLoading: Boolean) {

            }

            override fun onSuccessCall(value: GetSelfInfo) {
                Log.d("TAG", "LeaderBoard Location Updated Successfully: ${location.toString()}")
            }

            override fun onFailureCall(message: String?) {
                Log.d("TAG", "LeaderBoard Error to send Location Info: $message")
            }

        })
    }

    fun getMyJournal() {
        if (myJournalDataField.get() != null) {
            navigator?.showJournalData(myJournalDataField.get()!!)
        }
        else {
            NetworkUtil.useCase.myJournalUseCase.getMyJournal(object: Callback<MyJournalResponse>() {
                override fun loading(isLoading: Boolean) {}

                override fun onSuccessCall(value: MyJournalResponse) {
                    if (value.statusCode == 200) {
                        myJournalDataField.set(value.data)
                        navigator?.showJournalData(value.data)
                    }
                }

                override fun onFailureCall(message: String?) {
                    navigator?.showError("Error to get Journal Data")
                }

            })
        }
    }

    fun getGraphPlots() {
        if (journalGraphPlotDataField.get() != null) {
            navigator?.showGraphPlots(journalGraphPlotDataField.get()!!)
        }
        else {
            NetworkUtil.useCase.graphPlotDataUseCase.getGraphPlotData(object : Callback<MyJournalGraphResponse>() {
                override fun loading(isLoading: Boolean) {}

                override fun onSuccessCall(value: MyJournalGraphResponse) {
                    if (value.statusCode == 200) {
                        journalGraphPlotDataField.set(value.data)
                        navigator?.showGraphPlots(value.data)
                    }
                    else navigator?.showError("Error to get Graph Plots")
                }

                override fun onFailureCall(message: String?) {
                    navigator?.showError("Error to get Graph Plots")
                }

            })
        }
    }

    interface Navigator {
        fun globalLeaderBoardList(result: MutableList<RankResult>) {}
        fun localLeaderBoardList(result: MutableList<RankResult>) {}
        fun navigateToEndSession(sessionInfo: SessionInfo) {}
        fun showLeaderBoardLoading(isLoading: Boolean) {}
        fun showJournalData(data: JournalData) {}
        fun showGraphPlots(graphPlotData: MutableList<GraphPlotData>) {}
        fun navigateToNextScreen() {}
        fun showNetworkError(msg: String) {}
        fun showLoading(isLoading: Boolean) {}
        fun showError(msg: String) {}
    }
}