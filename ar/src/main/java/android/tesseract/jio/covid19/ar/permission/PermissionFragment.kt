package android.tesseract.jio.covid19.ar.permission

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import android.tesseract.jio.covid19.ar.R
import android.tesseract.jio.covid19.ar.databinding.FragmentPermissionBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

/**
 * Created by Dipanshu Harbola on 26/5/20.
 */
class PermissionFragment : Fragment() {

    private val TAG = PermissionFragment::class.java.simpleName

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentPermissionBinding.inflate(
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
        initComponents()
    }

    private fun initComponents() {
        binding.btnGetStarted.setOnClickListener {
            // start permission
            // when receive all permissions move to StartSessionActivity
            Dexter.withContext(requireContext())
                .withPermissions(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (report.areAllPermissionsGranted()) {
                                findNavController().navigate(R.id.action_permissionFragment_to_sessionStartFragment)
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        // Remember to invoke this method when the custom rationale is closed
                        // or just by default if you don't want to use any custom rationale.
                        token?.continuePermissionRequest()
                    }
                })
                .withErrorListener {
                    Log.e(TAG, it?.name.orEmpty())
                }
                .check()
        }
    }
}