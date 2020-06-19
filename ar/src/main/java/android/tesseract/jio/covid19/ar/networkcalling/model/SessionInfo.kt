package android.tesseract.jio.covid19.ar.networkcalling.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Dipanshu Harbola on 17/6/20.
 */
@Parcelize
data class SessionInfo(
    val safetyPercent: String,
    val sessionTime: String,
    val violationCount: String,
    val sessionStartTime: Long,
    val sessionEndTime: Long
): Parcelable