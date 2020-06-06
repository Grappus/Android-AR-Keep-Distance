package android.tesseract.jio.covid19.ar

import android.os.Bundle
import android.tesseract.jio.covid19.ar.core.sessions.end.SessionEndFragment
import android.tesseract.jio.covid19.ar.core.sessions.start.SessionStartFragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

class ARActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)
    }

    override fun onBackPressed() {
        val currentFragment = getCurrentFragment()
        currentFragment?.let {
            if (it is SessionEndFragment) {
                it.navigateBack()
                return
            } else if (it is SessionStartFragment) {
                finish()
                return
            }
        }
        super.onBackPressed()
    }

    private fun getCurrentFragment(): Fragment? {
        try {
            val manager = (supportFragmentManager
                .findFragmentById(R.id.splash_nav_fragment) as NavHostFragment).childFragmentManager
            return manager.fragments[0]
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}