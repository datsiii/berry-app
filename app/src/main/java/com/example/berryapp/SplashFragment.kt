package com.example.berryapp

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(requireActivity().mainLooper).postDelayed({
            if(true){
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
            else{
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment2)
            }
        }, 3000L)
    }
}