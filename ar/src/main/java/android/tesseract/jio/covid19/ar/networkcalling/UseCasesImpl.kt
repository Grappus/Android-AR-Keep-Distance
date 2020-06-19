package android.tesseract.jio.covid19.ar.networkcalling

import android.tesseract.jio.covid19.ar.networkcalling.usecases.*

/**
 * Created by Dipanshu Harbola on 18/6/20.
 */

class UseCasesImpl: UseCases {
    override val globalLeaderBoardUseCase: GlobalLeaderBoardUseCase
        get() = GlobalLeaderBoardUseCase()
    override val globalRankUseCase: GlobalRankUseCase
        get() = GlobalRankUseCase()
    override val graphPlotDataUseCase: GraphPlotDataUseCase
        get() = GraphPlotDataUseCase()
    override val loginUseCase: LoginUseCase
        get() = LoginUseCase()
    override val myInfoUseCase: MyInfoUseCase
        get() = MyInfoUseCase()
    override val myJournalUseCase: MyJournalUseCase
        get() = MyJournalUseCase()
    override val sessionActivityUseCase: SessionActivityUseCase
        get() = SessionActivityUseCase()

}