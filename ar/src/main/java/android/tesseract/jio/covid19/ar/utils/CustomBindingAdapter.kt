package android.tesseract.jio.covid19.ar.utils

import android.graphics.Typeface
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton

/**
 * Created by Dipanshu Harbola on 26/5/20.
 */

@BindingAdapter("customFont")
fun setCustomFont(mTextView: TextView, fontName: String) {
    val font = Typeface.createFromAsset(mTextView.context.assets, "fonts/$fontName")
    mTextView.typeface = font
}

@BindingAdapter("customFont")
fun setCustomFont(mButton: MaterialButton, fontName: String) {
    val font = Typeface.createFromAsset(mButton.context.assets, "fonts/$fontName")
    mButton.typeface = font
}