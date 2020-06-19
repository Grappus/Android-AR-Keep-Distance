package android.tesseract.jio.covid19.ar.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Dipanshu Harbola on 18/6/20.
 */
class TimeUtils {
    companion object {

        const val TIME_SERVER = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

        private fun getCurrentTimeZoneFormat(timeFormat: String): SimpleDateFormat {
            val sdf = SimpleDateFormat(timeFormat, Locale.getDefault())

            val currentDate = Date()
            val tz = Calendar.getInstance().timeZone

            // String name1 = tz.getDisplayName(tz.inDaylightTime(currentDate), TimeZone.SHORT);
            val name =
                TimeZone.getDefault().getDisplayName(tz.inDaylightTime(currentDate), TimeZone.SHORT)
            sdf.timeZone = TimeZone.getTimeZone("\"" + name + "\"")
            // Log.d("current time zone", sdf.getTimeZone().getDisplayName() + "::" + TimeZone.getDefault().getDisplayName() + ": " + TimeZone.getDefault().getID());

            return sdf
        }

        fun getTime(timeInMilliSeconds: Long, timeFormat: String): String {
            val sdf = SimpleDateFormat(timeFormat, Locale.getDefault())
            val calendar = Calendar.getInstance()
            if (timeInMilliSeconds > 0) calendar.timeInMillis = timeInMilliSeconds
            return sdf.format(calendar.time)
        }

        fun getTime(time: String, inputTimeFormat: String): Long {
            val sdf = getCurrentTimeZoneFormat(inputTimeFormat)
            val calendar = Calendar.getInstance()
            try {
                if (time.isNotEmpty()) calendar.time = sdf.parse(time)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return calendar.timeInMillis
        }
    }
}