package com.grappus.covidar.android

import android.app.Application
import com.grappus.covidar.android.utils.Prefs
import timber.log.Timber
import timber.log.Timber.DebugTree


/**
 * Created by Dipanshu Harbola on 1/6/20.
 */
class CovidARApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        Prefs.init(applicationContext)
    }
}