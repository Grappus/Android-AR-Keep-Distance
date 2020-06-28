package android.tesseract.jio.covid19.ar.utils

import android.app.Dialog
import android.content.Context
import android.tesseract.jio.covid19.ar.R
import android.view.Gravity
import android.view.Window
import android.widget.Toast

/**
 * Created by Dipanshu Harbola on 26/6/20.
 */
object ProgressLoader {
    private lateinit var dialog: Dialog

    fun showLoader(context: Context) {
        try {
            hideLoader()
            dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_loder)
            dialog.setCancelable(false)
            val window = dialog.window
            window!!.setBackgroundDrawableResource(android.R.color.transparent)
            window.setGravity(Gravity.CENTER)
            window.setDimAmount(0.80f)
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideLoader() {
        try {
            if (::dialog.isInitialized && dialog.isShowing) {
                dialog.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}