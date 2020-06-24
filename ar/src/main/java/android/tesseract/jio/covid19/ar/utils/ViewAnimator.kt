package android.tesseract.jio.covid19.ar.utils

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

/**
 * Created by Dipanshu Harbola on 19/6/20.
 */
fun slideView(
    view: View,
    currentHeight: Int,
    newHeight: Int
) {
    val slideAnimator = ValueAnimator
        .ofInt(currentHeight, newHeight)
        .setDuration(500)

    /* We use an update listener which listens to each tick
     * and manually updates the height of the view  */slideAnimator.addUpdateListener { animation1: ValueAnimator ->
        val value = animation1.animatedValue as Int
        view.layoutParams.height = value
        view.requestLayout()
    }

    /*  We use an animationSet to play the animation  */
    val animationSet = AnimatorSet()
    animationSet.interpolator = AccelerateDecelerateInterpolator()
    animationSet.play(slideAnimator)
    animationSet.start()
}