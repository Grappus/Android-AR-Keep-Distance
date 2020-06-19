package android.tesseract.jio.covid19.ar.networkcalling.model

/**
 * Created by Dipanshu Harbola on 18/6/20.
 */
data class MyJournalGraphResponse(
    val statusCode: Int,
    val data: MutableList<GraphPlotData>
)

data class GraphPlotData(
    val plotdata: MutableList<PlotData>,
    val withinDuration: Int
)

data class PlotData(
    val violationCount: Int,
    val createdAt: String
)
