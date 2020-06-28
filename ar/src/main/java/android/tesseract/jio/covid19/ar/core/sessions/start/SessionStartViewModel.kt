package android.tesseract.jio.covid19.ar.core.sessions.start

import android.tesseract.jio.covid19.ar.networkcalling.Callback
import android.tesseract.jio.covid19.ar.networkcalling.model.*
import android.tesseract.jio.covid19.ar.networkcalling.retrofit.NetworkUtil
import android.tesseract.jio.covid19.ar.utils.Prefs
import android.tesseract.jio.covid19.ar.utils.PrefsConstants
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_CREATED_AT
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_GLOBAL_RANK
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_ID
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_NAME
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_SAFETY
import android.tesseract.jio.covid19.ar.utils.TimeUtils
import android.util.Log
import androidx.lifecycle.ViewModel
import java.util.concurrent.TimeUnit

/**
 * Created by Dipanshu Harbola on 18/6/20.
 */
class SessionStartViewModel : ViewModel() {

    var navigator: Navigator? = null

    override fun onCleared() {
        super.onCleared()
    }

    fun getMyGlobalRank() {
        NetworkUtil.useCase.globalRankUseCase.getMyGlobalRank(object : Callback<MyGlobalRank>() {
            override fun loading(isLoading: Boolean) {
                navigator?.showLoading(isLoading)
            }

            override fun onSuccessCall(value: MyGlobalRank) {
                if (value.statusCode == 200) {
                    Prefs.setPrefs(USER_GLOBAL_RANK, value.data)
                    getGlobalLeaderBoard()
                } else navigator?.showError("Something is wrong.. ${value.statusCode}")
            }

            override fun onFailureCall(message: String?) {
                navigator?.showError(message!!)
            }

        })
    }

    fun getGlobalLeaderBoard() {
        NetworkUtil.useCase.globalLeaderBoardUseCase.getGlobalLeaderBoard(object :
            Callback<GlobalLeaderBoard>() {
            override fun loading(isLoading: Boolean) {
                navigator?.showLoading(isLoading)
            }

            override fun onSuccessCall(value: GlobalLeaderBoard) {
                val safetyPercent = Prefs.getPrefsInt(USER_SAFETY)
                val name = Prefs.getPrefsString(USER_NAME)
                val id = Prefs.getPrefsString(USER_ID)
                val strtDay = Prefs.getPrefsString(USER_CREATED_AT)
                if (value.statusCode == 200) {
                    val myRankResult = RankResult(
                        safetyPercent.toFloat(), name, "", id, strtDay, true
                    )
                    val result = value.data.result
                    result.add(0, myRankResult)
                    navigator?.globalLeaderBoardList(result.filter { it.id != id }.toMutableList())
                } else navigator?.showError("Something is wrong.. ${value.statusCode}")
            }

            override fun onFailureCall(message: String?) {
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
            latitude = "0.0", longitude = "0.0"
        )
        val sessionEndRequest = SessionEndRequest(
            startTime = sessionStartTime, totalDuration = totalDuration,
            violationCount = violationCount.toInt(), safetyRate = safetyRate, location = location
        )
        Log.d("TAG", "sessionEndRequest: $sessionEndRequest")
        NetworkUtil.useCase.sessionActivityUseCase.postSessionActivity(sessionEndRequest, object: Callback<SessionEndResponse>() {
            override fun loading(isLoading: Boolean) {
                navigator?.showLoading(isLoading)
            }

            override fun onSuccessCall(value: SessionEndResponse) {
                if (value.statusCode == 200) {
                    Log.d("TAG", "Session Info Success: $value")
                    Prefs.setPrefs(USER_SAFETY, value.score.toInt())
                    Prefs.setPrefs(USER_GLOBAL_RANK, value.rank)
                    navigator?.navigateToEndSession(sessionInfo)
                }
            }

            override fun onFailureCall(message: String?) {
                Log.e("TAG", "Error to send session Info: $message")
                navigator?.showError(message!!)
            }

        })

    }

    interface Navigator {
        fun globalLeaderBoardList(result: MutableList<RankResult>)
        fun navigateToEndSession(sessionInfo: SessionInfo)
        fun showLoading(isLoading: Boolean)
        fun showError(msg: String)
    }
}