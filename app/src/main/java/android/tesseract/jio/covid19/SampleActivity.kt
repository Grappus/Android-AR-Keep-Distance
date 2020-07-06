package android.tesseract.jio.covid19

import android.content.Intent
import android.os.Bundle
import android.tesseract.jio.covid19.ar.ARActivity
import android.tesseract.jio.covid19.ar.utils.PrefsConstants.USER_UID
import androidx.fragment.app.FragmentActivity
import kotlin.random.Random

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
        val randomValue = Random.nextLong((999999999 - 100) + 1) + 10
        startActivity(Intent(this, ARActivity::class.java).apply {
            putExtra(USER_UID, randomValue.toString())
        })
        finish()
    }
}