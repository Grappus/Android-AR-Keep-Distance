package android.tesseract.jio.covid19.ar.core.sessions.end

import android.tesseract.jio.covid19.ar.networkcalling.Callback
import android.tesseract.jio.covid19.ar.networkcalling.UseCases
import android.tesseract.jio.covid19.ar.networkcalling.model.SessionEndRequest
import android.tesseract.jio.covid19.ar.networkcalling.model.SessionEndResponse
import android.tesseract.jio.covid19.ar.networkcalling.model.SessionInfo
import android.tesseract.jio.covid19.ar.networkcalling.model.UserLocation
import android.tesseract.jio.covid19.ar.networkcalling.retrofit.NetworkUtil
import android.tesseract.jio.covid19.ar.utils.Prefs
import android.tesseract.jio.covid19.ar.utils.PrefsConstants
import android.tesseract.jio.covid19.ar.utils.TimeUtils
import android.text.format.Time
import android.util.Log
import androidx.lifecycle.ViewModel
import java.util.concurrent.TimeUnit

/**
 * Created by Dipanshu Harbola on 18/6/20.
 */
class SessionEndViewModel: ViewModel() {

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
            override fun loading() {

            }

            override fun onSuccessCall(value: SessionEndResponse) {
                if (value.statusCode == 200) {
                    Log.d("TAG", "Session Info Success: $value")
                    Prefs.setPrefs(PrefsConstants.USER_SAFETY, value.score.toInt())
                    Prefs.setPrefs(PrefsConstants.USER_GLOBAL_RANK, value.rank)
                }
            }

            override fun onFailureCall(message: String?) {
                Log.e("TAG", "Error to send session Info: $message")
            }

        })

    }

}