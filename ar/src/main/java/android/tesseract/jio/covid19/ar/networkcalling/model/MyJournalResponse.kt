package android.tesseract.jio.covid19.ar.networkcalling.model

/**
 * Created by Dipanshu Harbola on 18/6/20.
 */
data class MyJournalResponse(
    val statusCode: Int,
    val data: JournalData
)

data class JournalData(
    val totalDuration: Long,
    val totalViolations: Int,
    val safetyRate: Float
) {
    override fun toString(): String {
        return "JournalData(totalDuration=$totalDuration, totalViolations=$totalViolations, safetyRate=$safetyRate)"
    }
}