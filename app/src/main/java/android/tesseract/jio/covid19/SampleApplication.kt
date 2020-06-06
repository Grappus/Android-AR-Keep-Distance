package android.tesseract.jio.covid19

import android.app.Application
import android.tesseract.jio.covid19.ar.utils.Prefs

/**
 * Created by C P Singh on 07/06/2020.
 */

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Prefs.init(applicationContext)
    }
}