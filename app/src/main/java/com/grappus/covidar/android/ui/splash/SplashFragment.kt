package com.grappus.covidar.android.ui.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.grappus.covidar.android.R
import com.grappus.covidar.android.databinding.FragmentSplashBinding
import com.grappus.covidar.android.utils.Prefs
import com.grappus.covidar.android.utils.PrefsConstants.FINISHED_WALKTHROUGH


/**
 * Created by Dipanshu Harbola on 26/5/20.
 */
class SplashFragment : Fragment() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) { FragmentSplashBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        Handler().postDelayed({
            if (!Prefs.getPrefsBoolean(FINISHED_WALKTHROUGH)) {
                findNavController().navigate(R.id.action_splashFragment_to_walkThroughFragment)
            }
            else if (!arePermissionsGranted())
                findNavController().navigate(R.id.action_splashFragment_to_permissionFragment)
            else {
                findNavController().navigate(R.id.action_splashFragment_to_sessionStartFragment)
            }
        }, 2000L)
    }

    private fun arePermissionsGranted(): Boolean {
        val permissionCamera =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        val permissionLocation =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        return (permissionCamera == PackageManager.PERMISSION_GRANTED) && (permissionLocation == PackageManager.PERMISSION_GRANTED)
    }
}