package android.tesseract.jio.covid19.ar.preferences

import android.os.Bundle
import android.tesseract.jio.covid19.ar.ARActivity
import android.tesseract.jio.covid19.ar.R
import android.tesseract.jio.covid19.ar.databinding.FragmentPreferencesBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_ar.*
import kotlinx.android.synthetic.main.layout_bottom_action_buttons.view.*

/**
 * Created by Dipanshu Harbola on 11/6/20.
 */
class MyPreferencesFragment : Fragment() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentPreferencesBinding.inflate(
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
        handleSounds()
    }

    private fun handleActionButtons() {
        (requireContext() as ARActivity).setupActionButtons()

        (requireContext() as ARActivity).layoutActionButtons.fabStartSession.setOnClickListener {
            findNavController().navigate(R.id.action_myPreferencesFragment_to_sessionStartFragment)
        }

        (requireContext() as ARActivity).layoutActionButtons.fabJourneyStats.setOnClickListener {
            findNavController().navigate(R.id.action_myPreferencesFragment_to_myJournalFragment)
        }

        (requireContext() as ARActivity).layoutActionButtons.fabSettings.setOnClickListener {
            return@setOnClickListener
        }
    }

    private fun handleSounds() {
        binding.tvScreech.setOnClickListener {
            binding.run {
                tvScreech.setBackgroundResource(R.drawable.bg_selected_sound)
                tvChirp.setBackgroundResource(0)
                tvWhistle.setBackgroundResource(0)
            }
        }
        binding.tvChirp.setOnClickListener {
            binding.run {
                tvScreech.setBackgroundResource(0)
                tvChirp.setBackgroundResource(R.drawable.bg_selected_sound)
                tvWhistle.setBackgroundResource(0)
            }
        }
        binding.tvWhistle.setOnClickListener {
            binding.run {
                tvScreech.setBackgroundResource(0)
                tvChirp.setBackgroundResource(0)
                tvWhistle.setBackgroundResource(R.drawable.bg_selected_sound)
            }
        }
    }
}