package android.tesseract.jio.covid19.ar.permission

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.tesseract.jio.covid19.ar.R
import android.tesseract.jio.covid19.ar.databinding.FragmentPermissionBinding
import android.tesseract.jio.covid19.ar.utils.Prefs
import android.tesseract.jio.covid19.ar.utils.PrefsConstants
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
                                if (!Prefs.getPrefsBoolean(PrefsConstants.IS_USER_LOGIN))
                                    findNavController().navigate(R.id.action_permissionFragment_to_loginFragment)
                                else
                                    findNavController().navigate(R.id.action_permissionFragment_to_sessionStartFragment)
                            }

                            if (report.isAnyPermissionPermanentlyDenied) {
                                showSettingsDialog()
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        // default rationale invoke.
                        token?.continuePermissionRequest()
                    }
                })
                .withErrorListener {
                    Log.e(TAG, it?.name.orEmpty())
                }
                .check()
        }
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     */
    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(requireContext().getString(R.string.title_required_permission))
        builder.setMessage(requireContext().getString(R.string.msg_required_permission))
        builder.setPositiveButton(requireContext().getString(R.string.btn_goto_settings)) { dialog, which ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(requireContext().getString(R.string.btn_cancel)) { dialog, which -> dialog.cancel() }
        builder.show()
    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.parse("package:${requireContext().packageName}")
        intent.data = uri
        startActivity(intent)
    }
}