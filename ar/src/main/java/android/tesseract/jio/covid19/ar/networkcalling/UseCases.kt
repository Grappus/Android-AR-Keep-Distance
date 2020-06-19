package android.tesseract.jio.covid19.ar.networkcalling

import android.tesseract.jio.covid19.ar.networkcalling.usecases.*

/**
 * Created by Dipanshu Harbola on 18/6/20.
 */
interface UseCases {
    val globalLeaderBoardUseCase: GlobalLeaderBoardUseCase
    val globalRankUseCase: GlobalRankUseCase
    val graphPlotDataUseCase: GraphPlotDataUseCase
    val loginUseCase: LoginUseCase
    val myInfoUseCase: MyInfoUseCase
    val myJournalUseCase: MyJournalUseCase
    val sessionActivityUseCase: SessionActivityUseCase
}