package android.tesseract.jio.covid19.ar.core.sessions.end

import android.graphics.PointF
import android.os.Bundle
import android.tesseract.jio.covid19.ar.ARActivity
import android.tesseract.jio.covid19.ar.R
import android.tesseract.jio.covid19.ar.databinding.FragmentEndSessionBinding
import android.tesseract.jio.covid19.ar.utils.Prefs
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_GLOBAL_RANK
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController


/**
 * Created by Dipanshu Harbola on 5/6/20.
 *
 */
class SessionEndFragment : Fragment() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentEndSessionBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sessionEndViewModel = ViewModelProvider(this).get(SessionEndViewModel::class.java)
        val args = SessionEndFragmentArgs.fromBundle(requireArguments())
        val sessionInfo = args.sessionInfo
        binding.sessionInfo = sessionInfo
        sessionEndViewModel.sendSessionEndInfo(sessionInfo)
        val userGlobalRank = if (Prefs.getPrefsInt(USER_GLOBAL_RANK) == 1) "1st"
        else if (Prefs.getPrefsInt(USER_GLOBAL_RANK) == 2) "2nd"
        else if (Prefs.getPrefsInt(USER_GLOBAL_RANK) == 3) "3rd"
        else "${Prefs.getPrefsInt(USER_GLOBAL_RANK)}th"
        binding.tvRankInfo.text = String.format(getString(R.string.label_global_rank_info), userGlobalRank)
        binding.tvViolationCount.text = "${sessionInfo.violationCount} violation"
        (requireContext() as ARActivity).setupActionButtons()
        binding.fabCloseSession.setOnClickListener { navigateBack() }
    }

    fun navigateBack() {
        findNavController().navigate(R.id.action_sessionEndFragment_to_sessionStartFragment)
    }
}