package android.tesseract.jio.covid19.ar.core.sessions.start

import android.view.View.SYSTEM_UI_FLAG_VISIBLE
import android.view.WindowManager
import com.google.ar.sceneform.ux.ArFragment

/**
 * Created by Dipanshu Harbola on 21/6/20.
 */
class JioARFragment: ArFragment() {

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        requireActivity().window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requireActivity().window?.decorView?.systemUiVisibility = SYSTEM_UI_FLAG_VISIBLE
    }
}