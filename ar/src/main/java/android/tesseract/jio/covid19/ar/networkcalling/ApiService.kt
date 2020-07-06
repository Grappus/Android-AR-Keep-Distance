package android.tesseract.jio.covid19.ar.networkcalling

import android.tesseract.jio.covid19.ar.networkcalling.model.*
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

/**
 * Created by Dipanshu Harbola on 18/6/20.
 */
interface ApiService {

    @POST("auth/login")
    fun userLogin(@Body loginRequest: LoginRequest): Single<LoginResponse>

    @POST("activity/")
    fun postSessionActivity(@Header("Authorization") authorization: String, @Body sessionEndRequest: SessionEndRequest): Single<SessionEndResponse>

    @GET("activity/graph-plot-data")
    fun getGraphPlotData(@Header("Authorization") authorization: String, @Query("days") days: Int): Single<MyJournalGraphResponse>

    @GET("activity/journal")
    fun getMyJournal(@Header("Authorization") authorization: String): Single<MyJournalResponse>

    @GET("leaderboards/rank/global")
    fun getMyGlobalRank(@Header("Authorization") authorization: String): Single<MyLeaderBoardRank>

    @GET("leaderboards/rank/nearme")
    fun getMyLocalRank(@Header("Authorization") authorization: String): Single<MyLeaderBoardRank>

    @GET("leaderboards/global")
    fun getGlobalLeaderBoard(@Header("Authorization") authorization: String): Single<LeaderBoard>

    @GET("leaderboards/nearme")
    fun getMyLocalLeaderBoard(@Header("Authorization") authorization: String): Single<LeaderBoard>

    @GET("users/me")
    fun getMyInfo(@Header("Authorization") authorization: String): Single<GetSelfInfo>

    @PUT("users/{id}")
    fun updateUserInfo(@Header("Authorization") authorization: String, @Path("id") userId: String, @Body user: User): Single<GetSelfInfo>
}