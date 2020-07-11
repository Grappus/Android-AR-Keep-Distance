package android.tesseract.jio.covid19.ar.core.sessions.start

import android.content.Context
import android.graphics.Color
import android.tesseract.jio.covid19.ar.R
import android.tesseract.jio.covid19.ar.databinding.ItemLeaderboardBinding
import android.tesseract.jio.covid19.ar.networkcalling.model.RankResult
import android.tesseract.jio.covid19.ar.utils.Prefs
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_GLOBAL_RANK
import android.tesseract.jio.covid19.ar.utils.TimeUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * Created by Dipanshu Harbola on 18/6/20.
 */
class LeaderBoardAdapter :
    RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder>() {

    val leaderBoardList = mutableListOf<RankResult>()
    var isLocalRank = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemLeaderboardBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding, parent.context)
    }

    override fun getItemCount(): Int {
        return leaderBoardList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(leaderBoardList[position], position+1)
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
            val strtDay = TimeUtils.getTime(rankResult.createdAt, TimeUtils.TIME_SERVER)
            val today = Calendar.getInstance().timeInMillis
            val timeGap = today - strtDay
            val days = (timeGap / (1000*60*60*24)).toInt()
            val name = rankResult.fullName ?: "Unknown User"
            binding.run {
                executePendingBindings()
                //cvLeadBoard.setCardBackgroundColor(Color.parseColor("#00FFFFFF"))
                //cvLeadBoard.radius = 0f
                //cvLeadBoard.cardElevation = 0f
                tvRank.text = "$rank"
                tvUserName.text = name
                tvNameImg.text = name[0].toString()
                tvNameImg.setBackgroundResource(R.drawable.bg_name_img)
                when {
                    days == 0 -> binding.tvJourneyDays.visibility = View.GONE
                    days > 1 -> {
                        binding.tvJourneyDays.visibility = View.VISIBLE
                        "$days day"
                    }
                    else -> {
                        binding.tvJourneyDays.visibility = View.VISIBLE
                        "$days days"
                    }
                }
                tvSafetyPercentage.text = if (rankResult.lastNetScore > 100f) "100%"
                else "${(rankResult.lastNetScore).toInt()}%"
            }
        }
    }

}