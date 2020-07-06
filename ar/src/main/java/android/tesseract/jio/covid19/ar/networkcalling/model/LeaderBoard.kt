package android.tesseract.jio.covid19.ar.networkcalling.model

/**
 * Created by Dipanshu Harbola on 18/6/20.
 */
data class LeaderBoard(
    val statusCode: Int,
    val data: LeaderBoardData
)

data class LeaderBoardData(
    val result: MutableList<RankResult>,
    val count: Int,
    val next: String,
    val totalCount: Int,
    val prev: String
)

data class RankResult(
    val lastNetScore: Float,
    val fullName: String? = null,
    val imageUrl: String,
    val createdAt: String,
    val id: String,
    val isMe: Boolean = false
)