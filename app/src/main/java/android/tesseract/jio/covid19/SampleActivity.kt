package android.tesseract.jio.covid19

import android.content.Intent
import android.os.Bundle
import android.tesseract.jio.covid19.ar.ARActivity
import androidx.fragment.app.FragmentActivity

/**
 * Created by C P Singh on 06/06/2020.
 */

class SampleActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        initComponents()
    }

    private fun initComponents() {
        startActivity(Intent(this, ARActivity::class.java))
        finish()
    }
}