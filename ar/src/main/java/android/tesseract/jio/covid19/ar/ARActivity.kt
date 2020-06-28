package android.tesseract.jio.covid19.ar

import android.content.res.ColorStateList
import android.os.Bundle
import android.tesseract.jio.covid19.ar.core.sessions.end.SessionEndFragment
import android.tesseract.jio.covid19.ar.core.sessions.start.SessionStartFragment
import android.tesseract.jio.covid19.ar.databinding.ActivityArBinding
import android.tesseract.jio.covid19.ar.journal.MyJournalFragment
import android.tesseract.jio.covid19.ar.preferences.MyPreferencesFragment
import android.tesseract.jio.covid19.ar.utils.Prefs
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.activity_ar.*


class ARActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityArBinding.inflate(
            layoutInflater
        )
    }
    private lateinit var constraintLayout: ConstraintLayout
    private val constraintSet = ConstraintSet()
    private val animateConstraintSet = ConstraintSet()
    private val transition = ChangeBounds()

    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = getColor(R.color.baseBgColor)
        window.navigationBarColor = getColor(R.color.baseBgColor)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Prefs.init(applicationContext)

        initComponents()
    }

    override fun onBackPressed() {
        val currentFragment = getCurrentFragment()
        currentFragment?.let {
            if (it is SessionEndFragment) {
                it.navigateBack()
                return
            } else if (it is SessionStartFragment) {
                finish()
                return
            }
        }
        super.onBackPressed()
    }

    private fun initComponents() {
        constraintLayout = binding.layoutActionButtons.clBottomActionButtonView
        constraintSet.clone(constraintLayout)
        animateConstraintSet.clone(this, R.layout.layout_bottom_action_buttons_animate)
        transition.interpolator = OvershootInterpolator()
        transition.duration = 500
    }

    private fun getCurrentFragment(): Fragment? {
        try {
            val manager = (supportFragmentManager
                .findFragmentById(R.id.splashNavFragment) as NavHostFragment).childFragmentManager
            return manager.fragments[0]
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun setupActionButtons() {
        when(getCurrentFragment()) {
            is SessionStartFragment -> {
                layoutActionButtons.visibility = View.VISIBLE
                TransitionManager.beginDelayedTransition(constraintLayout, transition)
                constraintSet.applyTo(constraintLayout)
                binding.layoutActionButtons.run {
                    fabSettings.run {
                        backgroundTintList = ColorStateList.valueOf(getColor(R.color.white))
                        imageTintList = ColorStateList.valueOf(getColor(R.color.buttonGrayColor))
                    }
                    fabStartSession.run {
                        backgroundTintList = ColorStateList.valueOf(getColor(R.color.baseBgColor))
                        imageTintList = ColorStateList.valueOf(getColor(R.color.white))
                    }
                    fabJourneyStats.run {
                        backgroundTintList = ColorStateList.valueOf(getColor(R.color.white))
                        imageTintList = ColorStateList.valueOf(getColor(R.color.buttonGrayColor))
                    }
                }

            }
            is MyPreferencesFragment -> {
                layoutActionButtons.visibility = View.VISIBLE
                TransitionManager.beginDelayedTransition(constraintLayout, transition)
                animateConstraintSet.applyTo(constraintLayout)
                binding.layoutActionButtons.run {
                    fabSettings.run {
                        backgroundTintList = ColorStateList.valueOf(getColor(R.color.white))
                        imageTintList = ColorStateList.valueOf(getColor(R.color.startButtonColor))
                    }
                    fabStartSession.run {
                        backgroundTintList = ColorStateList.valueOf(getColor(R.color.startButtonColor))
                        imageTintList = ColorStateList.valueOf(getColor(R.color.baseBgColor))
                    }
                    fabJourneyStats.run {
                        backgroundTintList = ColorStateList.valueOf(getColor(R.color.startButtonColor))
                        imageTintList = ColorStateList.valueOf(getColor(R.color.baseBgColor))
                    }
                }
            }
            is MyJournalFragment -> {
                layoutActionButtons.visibility = View.VISIBLE
                TransitionManager.beginDelayedTransition(constraintLayout, transition)
                animateConstraintSet.applyTo(constraintLayout)
                binding.layoutActionButtons.run {
                    fabSettings.run {
                        backgroundTintList = ColorStateList.valueOf(getColor(R.color.white))
                        imageTintList = ColorStateList.valueOf(getColor(R.color.buttonGrayColor))
                    }
                    fabStartSession.run {
                        backgroundTintList = ColorStateList.valueOf(getColor(R.color.white))
                        imageTintList = ColorStateList.valueOf(getColor(R.color.buttonGrayColor))
                    }
                    fabJourneyStats.run {
                        backgroundTintList = ColorStateList.valueOf(getColor(R.color.baseBgColor))
                        imageTintList = ColorStateList.valueOf(getColor(R.color.white))
                    }
                }
            }
            else -> {
                layoutActionButtons.visibility = View.GONE
            }
        }
    }
}