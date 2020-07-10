package android.tesseract.jio.covid19.ar.splash

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.tesseract.jio.covid19.ar.ARActivity
import android.tesseract.jio.covid19.ar.R
import android.tesseract.jio.covid19.ar.databinding.FragmentSplashBinding
import android.tesseract.jio.covid19.ar.utils.Prefs
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.FINISHED_WALKTHROUGH
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.IS_USER_LOGIN
import android.tesseract.jio.covid19.ar.utils.ProgressLoader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

/**
 * Created by Dipanshu Harbola on 26/5/20.
 */
class SplashFragment : Fragment(), SplashViewModel.Navigator {

    private val binding by lazy(LazyThreadSafetyMode.NONE) { FragmentSplashBinding.inflate(layoutInflater) }
    private var handler: Handler? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        handler = Handler()
        initComponents()
    }

    override fun onPause() {
        super.onPause()
        handler = null
    }

    override fun navigateToNextScreen() {
        if (!Prefs.getPrefsBoolean(FINISHED_WALKTHROUGH)) {
            findNavController().navigate(R.id.action_splashFragment_to_walkThroughFragment)
        }
        else if (!arePermissionsGranted())
            findNavController().navigate(R.id.action_splashFragment_to_permissionFragment)
        else if (!Prefs.getPrefsBoolean(IS_USER_LOGIN))
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        else {
            findNavController().navigate(R.id.action_splashFragment_to_sessionStartFragment)
        }
    }

    override fun showLoading(isLoading: Boolean) {
        if (isLoading)
            ProgressLoader.showLoader(requireContext())
        else
            ProgressLoader.hideLoader()
    }

    override fun showNetworkError(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun initComponents() {
        val splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        splashViewModel.navigator = this
        (requireContext() as ARActivity).setupActionButtons()
        handler?.postDelayed({
            if (Prefs.getPrefsBoolean(IS_USER_LOGIN)) {
                splashViewModel.getUserInfo()
            }
            else navigateToNextScreen()
        }, 2000L)
    }

    // check if all the required permissions are granted by user.
    private fun arePermissionsGranted(): Boolean {
        val permissionCamera =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        val permissionLocation =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        return (permissionCamera == PackageManager.PERMISSION_GRANTED) && (permissionLocation == PackageManager.PERMISSION_GRANTED)
    }
}