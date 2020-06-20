package android.tesseract.jio.covid19.ar.journal

import android.tesseract.jio.covid19.ar.networkcalling.Callback
import android.tesseract.jio.covid19.ar.networkcalling.model.GraphPlotData
import android.tesseract.jio.covid19.ar.networkcalling.model.JournalData
import android.tesseract.jio.covid19.ar.networkcalling.model.MyJournalGraphResponse
import android.tesseract.jio.covid19.ar.networkcalling.model.MyJournalResponse
import android.tesseract.jio.covid19.ar.networkcalling.retrofit.NetworkUtil
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

/**
 * Created by Dipanshu Harbola on 18/6/20.
 */
class MyJournalViewModel: ViewModel() {

    var navigator: Navigator? = null

    val fieldTotalDuration = ObservableField("0 hrs")
    val fieldTotalViolations = ObservableField("0 hrs")
    val fieldSafetyRate = ObservableField("100%")

    fun getMyJournal() {
        NetworkUtil.useCase.myJournalUseCase.getMyJournal(object: Callback<MyJournalResponse>() {
            override fun loading() {

            }

            override fun onSuccessCall(value: MyJournalResponse) {
                if (value.statusCode == 200) {
                    Log.i("TAG", "Successfully get result: $value")
                    navigator?.showJournalData(value.data)
                }
            }

            override fun onFailureCall(message: String?) {
                navigator?.showError("Error to get Journal Data")
            }

        })
    }

    fun getGraphPlots() {
        NetworkUtil.useCase.graphPlotDataUseCase.getGraphPlotData(object : Callback<MyJournalGraphResponse>() {
            override fun loading() {

            }

            override fun onSuccessCall(value: MyJournalGraphResponse) {
                if (value.statusCode == 200) {
                    navigator?.showGraphPlots(value.data)
                    Log.e("GraphData: ", "${value.data}")
                }
                else navigator?.showError("Error to get Graph Plots")
            }

            override fun onFailureCall(message: String?) {
                navigator?.showError("Error to get Graph Plots")
            }

        })
    }

    interface Navigator {
        fun showJournalData(data: JournalData)
        fun showGraphPlots(graphPlotData: MutableList<GraphPlotData>)
        fun showError(msg: String)
    }
}