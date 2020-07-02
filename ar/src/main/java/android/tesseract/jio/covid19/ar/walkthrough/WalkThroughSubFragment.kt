package android.tesseract.jio.covid19.ar.walkthrough

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.tesseract.jio.covid19.ar.R
import android.tesseract.jio.covid19.ar.databinding.FragmentSubWalkthroughBinding

/**
 * Created by Dipanshu Harbola on 4/6/20.
 */
class WalkThroughSubFragment: Fragment() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) { FragmentSubWalkthroughBinding.inflate(layoutInflater) }

    private var pageNumber: Int = 0

    companion object {
        fun getInstance(args: Bundle): WalkThroughSubFragment {
            val frag =
                WalkThroughSubFragment()
            frag.arguments = args
            return frag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        // For the Page transformer in Activity
        pageNumber = arguments?.getInt("ARG_EXTRA")!!
        binding.root.tag = pageNumber

        when (arguments?.getInt("ARG_EXTRA")) {
            0 -> {
                binding.topIcon.setImageResource(R.drawable.group_3_group_17_mask)
                binding.tvWalkThrough.text = getString(R.string.label_walk_through_one)
            }

            1 -> {
                binding.topIcon.setImageResource(R.drawable.group_7)
                binding.tvWalkThrough.text = getString(R.string.label_walk_through_two)
            }
        }
    }
}