package com.grappus.covidar.android.ar.core.sessions.end

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.grappus.covidar.android.ar.databinding.FragmentEndSessionBinding

/**
 * Created by Dipanshu Harbola on 5/6/20.
 *
 */
class SessionEndFragment : Fragment() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentEndSessionBinding.inflate(
            layoutInflater
        )
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
        //TODO Handle back navigation
        /*binding.fabCloseSession.setOnClickListener {
            findNavController().navigate(R.id.action_sessionEndFragment_to_sessionStartFragment)
        }*/
    }
}