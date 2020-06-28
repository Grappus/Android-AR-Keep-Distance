package android.tesseract.jio.covid19.ar.preferences

import android.os.Bundle
import android.tesseract.jio.covid19.ar.ARActivity
import android.tesseract.jio.covid19.ar.R
import android.tesseract.jio.covid19.ar.databinding.FragmentPreferencesBinding
import android.tesseract.jio.covid19.ar.utils.Prefs
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_NAME
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_SOUND_ON
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_VIB_ON
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.VIOLATION_SOUND_EFFECT
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
        handleSelectedSounds()
        handleVibration()
        val name = Prefs.getPrefsString(USER_NAME)
        binding.tvUserName.text = name
        binding.tvNameImg.text = name[0].toString()
        //handleNotification()
    }

    private fun handleActionButtons() {
        (requireContext() as ARActivity).setupActionButtons()

        try {
            (requireContext() as ARActivity).layoutActionButtons.fabStartSession.setOnClickListener {
                val action = MyPreferencesFragmentDirections.actionMyPreferencesFragmentToSessionStartFragment()
                findNavController().navigate(action)
            }

            (requireContext() as ARActivity).layoutActionButtons.fabJourneyStats.setOnClickListener {
                val action = MyPreferencesFragmentDirections.actionMyPreferencesFragmentToMyJournalFragment()
                findNavController().navigate(action)
            }

            (requireContext() as ARActivity).layoutActionButtons.fabSettings.setOnClickListener {
                return@setOnClickListener
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    private fun handleSelectedSounds() {
        when(Prefs.getPrefsInt(VIOLATION_SOUND_EFFECT)) {
            R.raw.space_drop -> {
                binding.run {
                    tvSpaceDrop.setBackgroundResource(R.drawable.bg_selected_sound)
                    tvAccentSound.setBackgroundResource(0)
                    tvAlarmSound.setBackgroundResource(0)
                }
            }
            R.raw.alarm -> {
                binding.run {
                    tvSpaceDrop.setBackgroundResource(0)
                    tvAccentSound.setBackgroundResource(0)
                    tvAlarmSound.setBackgroundResource(R.drawable.bg_selected_sound)
                }
            }
            else -> {
                binding.run {
                    tvSpaceDrop.setBackgroundResource(R.drawable.bg_selected_sound)
                    tvAccentSound.setBackgroundResource(0)
                    tvAlarmSound.setBackgroundResource(0)
                }
            }
        }
    }

    private fun handleSounds() {
        binding.swSound.isChecked = Prefs.getPrefsBoolean(USER_SOUND_ON)

        binding.swSound.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Prefs.setPrefs(USER_SOUND_ON, true)
                // user update call
            }
            else {
                Prefs.setPrefs(USER_SOUND_ON, false)
                // user update call
            }
        }
        binding.tvSpaceDrop.setOnClickListener {
            binding.run {
                tvSpaceDrop.setBackgroundResource(R.drawable.bg_selected_sound)
                tvAccentSound.setBackgroundResource(0)
                tvAlarmSound.setBackgroundResource(0)
            }
            Prefs.setPrefs(VIOLATION_SOUND_EFFECT, R.raw.space_drop)
        }
        binding.tvAccentSound.setOnClickListener {
            binding.run {
                tvSpaceDrop.setBackgroundResource(0)
                tvAccentSound.setBackgroundResource(R.drawable.bg_selected_sound)
                tvAlarmSound.setBackgroundResource(0)
            }
            Prefs.setPrefs(VIOLATION_SOUND_EFFECT, R.raw.accent)
        }
        binding.tvAlarmSound.setOnClickListener {
            binding.run {
                tvSpaceDrop.setBackgroundResource(0)
                tvAccentSound.setBackgroundResource(0)
                tvAlarmSound.setBackgroundResource(R.drawable.bg_selected_sound)
            }
            Prefs.setPrefs(VIOLATION_SOUND_EFFECT, R.raw.alarm)
        }
    }

    private fun handleVibration() {
        binding.swVibration.isChecked = Prefs.getPrefsBoolean(USER_VIB_ON)

        binding.swVibration.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Prefs.setPrefs(USER_VIB_ON, true)
                // user update call
            }
            else {
                Prefs.setPrefs(USER_VIB_ON, false)
                // user update call
            }
        }
    }

    /*private fun handleNotification() {
        binding.swNotification.isChecked = Prefs.getPrefsBoolean(USER_NOTIF_ON)

        binding.swNotification.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Prefs.setPrefs(USER_NOTIF_ON, true)
                // user update call
            }
            else {
                Prefs.setPrefs(USER_NOTIF_ON, false)
                // user update call
            }
        }
    }*/
}