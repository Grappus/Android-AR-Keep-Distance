package android.tesseract.jio.covid19.ar.journal

import android.os.Bundle
import android.tesseract.jio.covid19.ar.ARActivity
import android.tesseract.jio.covid19.ar.R
import android.tesseract.jio.covid19.ar.databinding.FragmentJournalBinding
import android.tesseract.jio.covid19.ar.utils.DataPoint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_ar.*
import kotlinx.android.synthetic.main.layout_bottom_action_buttons.view.*
import java.util.*

/**
 * Created by Dipanshu Harbola on 11/6/20.
 */
class MyJournalFragment : Fragment() {

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

    private fun initComponents() {
        handleActionButtons()
        binding.gvSafety.setData(generateRandomDataPoints())
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
        return (0..20).map {
            DataPoint(it, random.nextInt(50) + 1)
        }
    }
}