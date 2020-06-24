package android.tesseract.jio.covid19.ar.journal

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
import kotlinx.android.synthetic.main.fragment_journal.*
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
            tvTotalSafetyPercent.text = if(data.safetyRate > 100f) "100%" else "${data.safetyRate.toInt()}%"
            tvTotalViolation.text = data.totalViolations.toString()
        }
    }

    override fun showGraphPlots(graphPlotData: MutableList<GraphPlotData>) {
        val graphDataList = mutableListOf<LineGraph.GraphData>()
        Log.e("GraphPlotData", "$graphPlotData")
        /*for (value in 0 until graphPlotData.size) {
            graphDataList.add(
                LineGraph.GraphData("${graphPlotData[value].withinDuration} hrs",
                    graphPlotData[value].plotdata.sumBy { it.violationCount }.toFloat())
            )
        }*/
        graphDataList.add(LineGraph.GraphData("6 hrs", graphPlotData[0].plotdata.sumBy { it.violationCount }.toFloat()))
        graphDataList.add(LineGraph.GraphData("12 hrs", graphPlotData[1].plotdata.sumBy { it.violationCount }.toFloat()))
        graphDataList.add(LineGraph.GraphData("18 hrs", graphPlotData[2].plotdata.sumBy { it.violationCount }.toFloat()))
        graphDataList.add(LineGraph.GraphData("24 hrs", graphPlotData[2].plotdata.sumBy { it.violationCount }.toFloat()))
        gvSafety.setGraphData(graphDataList.toList())
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

        try {
            (requireContext() as ARActivity).layoutActionButtons.fabStartSession.setOnClickListener {
                val action = MyJournalFragmentDirections.actionMyJournalFragmentToSessionStartFragment()
                findNavController().navigate(action)
            }

            (requireContext() as ARActivity).layoutActionButtons.fabSettings.setOnClickListener {
                val action = MyJournalFragmentDirections.actionMyJournalFragmentToMyPreferencesFragment()
                findNavController().navigate(action)
            }

            (requireContext() as ARActivity).layoutActionButtons.fabJourneyStats.setOnClickListener {
                return@setOnClickListener
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    private fun generateRandomDataPoints(): List<DataPoint> {
        val random = Random()
        return (0..10).map {
            DataPoint(it, random.nextInt(10))
        }
    }
}