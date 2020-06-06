package com.grappus.covidar.android

import android.app.Application
import com.grappus.covidar.android.ar.utils.Prefs

/**
 * Created by C P Singh on 07/06/2020.
 */

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Prefs.init(applicationContext)
    }
}