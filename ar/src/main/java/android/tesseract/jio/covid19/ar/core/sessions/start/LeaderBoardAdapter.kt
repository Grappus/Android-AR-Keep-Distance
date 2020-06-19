package android.tesseract.jio.covid19.ar.core.sessions.start

import android.content.Context
import android.tesseract.jio.covid19.ar.R
import android.tesseract.jio.covid19.ar.databinding.ItemLeaderboardBinding
import android.tesseract.jio.covid19.ar.networkcalling.model.RankResult
import android.tesseract.jio.covid19.ar.utils.Prefs
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_GLOBAL_RANK
import android.tesseract.jio.covid19.ar.utils.TimeUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat
import java.util.*

/**
 * Created by Dipanshu Harbola on 18/6/20.
 */
class LeaderBoardAdapter :
    RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder>() {

    val leaderBoardList = mutableListOf<RankResult>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemLeaderboardBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding, parent.context)
    }

    override fun getItemCount(): Int {
        return leaderBoardList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(leaderBoardList[position], position)
    }

    fun setData(data: MutableList<RankResult>) {
        leaderBoardList.clear()
        leaderBoardList.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ItemLeaderboardBinding,
        val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(rankResult: RankResult, rank: Int) {
            binding.rankResult = rankResult
            binding.executePendingBindings()

            if (rankResult.isMe) {
                binding.cvLeadBoard.setCardBackgroundColor(context.getColor(R.color.white))
                binding.cvLeadBoard.radius = 18f
                binding.ivLiveIndicator.visibility = View.VISIBLE
                binding.tvRank.text = Prefs.getPrefsInt(USER_GLOBAL_RANK).toString()
            }
            else {
                binding.cvLeadBoard.setCardBackgroundColor(context.getColor(R.color.leadBoardCardBackgroundColor2))
                binding.cvLeadBoard.radius = 0f
                binding.ivLiveIndicator.visibility = View.GONE
                binding.tvRank.text = "$rank"
            }

            val strtDay = TimeUtils.getTime(rankResult.createdAt, TimeUtils.TIME_SERVER)
            val today = Calendar.getInstance().timeInMillis
            val timeGap = today - strtDay
            val days = (timeGap / (1000*60*60*24)).toInt()
            Log.e("TAGIT", "today: $today strtDay: $strtDay  timeGap: $timeGap  days: $days")
            binding.tvJourneyDays.text = if (days > 1) "$days day" else "$days days"
            binding.tvSafetyPercentage.text = "${(rankResult.lastNetScore).toInt()}%"
            val name = rankResult.fullName ?: "Unknown User"
            binding.tvUserName.text = name
            binding.tvNameImg.text = name[0].toString()
        }
    }

}