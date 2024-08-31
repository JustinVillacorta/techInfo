package com.example.techinfo

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay

class SplashFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        Handler(Looper.getMainLooper()).postDelayed(
            {
                if(onBoardingFinished()){
                    findNavController().navigate(R.id.action_splashFragment_to_buildPC)
                }else{
                    findNavController().navigate(R.id.action_splashFragment_to_viewPageFragment)
                }
            }, 2000)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    private fun onBoardingFinished(): Boolean{
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)

    }
}

