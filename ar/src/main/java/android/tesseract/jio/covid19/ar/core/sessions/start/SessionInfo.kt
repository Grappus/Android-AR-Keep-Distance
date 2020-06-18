package android.tesseract.jio.covid19.ar.core.sessions.start

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Dipanshu Harbola on 17/6/20.
 */
@Parcelize
data class SessionInfo(
    val safetyPercent: String,
    val sessionTime: String,
    val violationCount: String
): Parcelable