package com.grappus.covidar.android.ui.walkthrough

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.grappus.covidar.android.R
import com.grappus.covidar.android.databinding.FragmentWalkthroughBinding
import com.grappus.covidar.android.utils.Prefs
import com.grappus.covidar.android.utils.PrefsConstants.FINISHED_WALKTHROUGH

/**
 * Created by Dipanshu Harbola on 4/6/20.
 */
class WalkThroughFragment : Fragment() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) { FragmentWalkthroughBinding.inflate(layoutInflater) }

    private lateinit var pagerAdapter: WalkThroughPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents()
    }

    private fun initComponents() {
        pagerAdapter = WalkThroughPagerAdapter(
            requireActivity().supportFragmentManager,
            createFragmentList()
        )
        binding.vpWalkThrough.adapter = pagerAdapter
        binding.vpWalkThrough.addOnPageChangeListener(vpPageChangeListener)

        binding.btnGetStarted.setOnClickListener {
            Prefs.setPrefs(FINISHED_WALKTHROUGH, true)
            if (!arePermissionsGranted()) {
                findNavController().navigate(R.id.action_walkThroughFragment_to_permissionFragment)
            }
            else {
                findNavController().navigate(R.id.action_walkThroughFragment_to_sessionStartFragment)
            }
        }
    }

    /**
     * View Pager - Fragments, adapter & listeners
     * */
    private fun createFragmentList(): List<WalkThroughSubFragment> {
        val list: MutableList<WalkThroughSubFragment> = ArrayList()

        var args = Bundle()
        args.putInt("ARG_EXTRA", 0)
        list.add(WalkThroughSubFragment.getInstance(args))

        args = Bundle()
        args.putInt("ARG_EXTRA", 1)
        list.add(WalkThroughSubFragment.getInstance(args))

        return list
    }

    private inner class WalkThroughPagerAdapter(
        fragmentManager: FragmentManager,
        private val fragmentList: List<WalkThroughSubFragment>
    ) : FragmentStatePagerAdapter(fragmentManager) {

        override fun getCount(): Int = fragmentList.size

        override fun getItem(position: Int): Fragment = fragmentList[position]
    }

    private val vpPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            animatePageDots(position)
        }
    }

    private fun animatePageDots(selectedPage: Int) {
        when (selectedPage) {
            0 -> {
                binding.ivDot1.alpha = 1f
                binding.ivDot2.alpha = .3f
            }
            1 -> {
                binding.ivDot1.alpha = .3f
                binding.ivDot2.alpha = 1f
            }
        }
    }

    private fun arePermissionsGranted(): Boolean {
        val permissionCamera =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        val permissionLocation =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        return (permissionCamera == PackageManager.PERMISSION_GRANTED) && (permissionLocation == PackageManager.PERMISSION_GRANTED)
    }
}