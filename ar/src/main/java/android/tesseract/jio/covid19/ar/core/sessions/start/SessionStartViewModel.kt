package android.tesseract.jio.covid19.ar.core.sessions.start

import android.tesseract.jio.covid19.ar.networkcalling.Callback
import android.tesseract.jio.covid19.ar.networkcalling.model.GlobalLeaderBoard
import android.tesseract.jio.covid19.ar.networkcalling.model.MyGlobalRank
import android.tesseract.jio.covid19.ar.networkcalling.model.RankResult
import android.tesseract.jio.covid19.ar.networkcalling.retrofit.NetworkUtil
import android.tesseract.jio.covid19.ar.utils.Prefs
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_CREATED_AT
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_GLOBAL_RANK
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_ID
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_NAME
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_SAFETY
import androidx.lifecycle.ViewModel

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
            override fun loading() {

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
            override fun loading() {

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

    interface Navigator {
        fun globalLeaderBoardList(result: MutableList<RankResult>)
        fun showError(msg: String)
    }
}