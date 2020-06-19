package android.tesseract.jio.covid19.ar.journal

import android.graphics.PointF
import android.os.Bundle
import android.tesseract.jio.covid19.ar.ARActivity
import android.tesseract.jio.covid19.ar.R
import android.tesseract.jio.covid19.ar.databinding.FragmentJournalBinding
import android.tesseract.jio.covid19.ar.networkcalling.model.GraphPlotData
import android.tesseract.jio.covid19.ar.networkcalling.model.JournalData
import android.tesseract.jio.covid19.ar.utils.DataPoint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_ar.*
import kotlinx.android.synthetic.main.layout_bottom_action_buttons.view.*
import java.util.*

/**
 * Created by Dipanshu Harbola on 11/6/20.
 */
class MyJournalFragment : Fragment(), MyJournalViewModel.Navigator {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentJournalBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().window.navigationBarColor =
            requireActivity().getColor(R.color.baseBgColor)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    override fun showJournalData(data: JournalData) {
        val duration = when {
            data.totalDuration <= 60L -> {
                "${data.totalDuration} sec"
            }
            data.totalDuration <= 3600L -> {
                "${(data.totalDuration/ 60)%60} min"
            }
            else -> "${(data.totalDuration/60)/60} hrs"
        }
        binding.run {
            tvTotalTrackedTime.text = duration
            tvTotalSafetyPercent.text = "${data.safetyRate.toInt()}%"
            tvTotalViolation.text = data.totalViolations.toString()
        }
    }

    override fun showGraphPlots(graphPlotData: MutableList<GraphPlotData>) {
        val dataPointList = mutableListOf<PointF>()
        for (i in graphPlotData) {
            dataPointList.add(PointF(i.withinDuration.toFloat(), i.plotdata.sumBy { it.violationCount }.toFloat()))
            Log.d("TAGGG", "DataPoint: $i")
        }
        /*dataPointList.add(DataPoint(6, 2))
        dataPointList.add(DataPoint(12, 8))
        dataPointList.add(DataPoint(18, 26))
        dataPointList.add(DataPoint(24, 5))*/
       /* binding.gvSafety.setData(arrayOf(
            PointF(6f, 3f),  // {x, y}
            PointF(12f, 21f),
            PointF(18f, 9f),
            PointF(24f, 6f)
        ))*/
        binding.gvSafety.setData(dataPointList)
    }

    override fun showError(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun initComponents() {
        val myJournalViewModel = ViewModelProvider(this).get(MyJournalViewModel::class.java)
        myJournalViewModel.navigator = this
        myJournalViewModel.getMyJournal()
        myJournalViewModel.getGraphPlots()
        handleActionButtons()
    }

    private fun handleActionButtons() {
        (requireContext() as ARActivity).setupActionButtons()

        (requireContext() as ARActivity).layoutActionButtons.fabStartSession.setOnClickListener {
            findNavController().navigate(R.id.action_myJournalFragment_to_sessionStartFragment)
        }

        (requireContext() as ARActivity).layoutActionButtons.fabSettings.setOnClickListener {
            findNavController().navigate(R.id.action_myJournalFragment_to_myPreferencesFragment)
        }

        (requireContext() as ARActivity).layoutActionButtons.fabJourneyStats.setOnClickListener {
            return@setOnClickListener
        }
    }

    private fun generateRandomDataPoints(): List<DataPoint> {
        val random = Random()
        return (0..10).map {
            DataPoint(it, random.nextInt(10))
        }
    }
}