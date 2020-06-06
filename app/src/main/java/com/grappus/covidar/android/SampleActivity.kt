package com.grappus.covidar.android

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.grappus.covidar.android.ar.ARActivity
import com.grappus.covidar.android.R

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