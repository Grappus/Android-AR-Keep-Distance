package android.tesseract.jio.covid19.ar.splash

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.tesseract.jio.covid19.ar.ARActivity
import android.tesseract.jio.covid19.ar.R
import android.tesseract.jio.covid19.ar.databinding.FragmentLoginBinding
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
class LoginFragment : Fragment(), SplashViewModel.Navigator {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentLoginBinding.inflate(
            layoutInflater
        )
    }
    var handler: Handler? = null

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
        findNavController().navigate(R.id.action_loginFragment_to_sessionStartFragment)
    }

    override fun showNetworkError() {
        Toast.makeText(requireContext(), "Something is wrong, please try again", Toast.LENGTH_SHORT)
            .show()
    }

    private fun initComponents() {
        val splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        splashViewModel.navigator = this
        (requireContext() as ARActivity).setupActionButtons()
        binding.btnSubmit.setOnClickListener {
            if (binding.etUserName.text.toString()
                    .isNotBlank() && binding.etPhoneNumber.text.toString().isNotBlank()
            ) {
                splashViewModel.authenticateUser(
                    binding.etPhoneNumber.text.toString(),
                    binding.etUserName.text.toString()
                )
            } else Toast.makeText(requireContext(), "All Details required", Toast.LENGTH_SHORT)
                .show()
        }
    }

    // check if all the required permissions are granted by user.
    private fun arePermissionsGranted(): Boolean {
        val permissionCamera =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        val permissionLocation =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        return (permissionCamera == PackageManager.PERMISSION_GRANTED) && (permissionLocation == PackageManager.PERMISSION_GRANTED)
    }
}