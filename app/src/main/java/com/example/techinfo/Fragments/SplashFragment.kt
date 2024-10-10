package com.example.techinfo.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.techinfo.MainNavigation.MainNavigation
import com.example.techinfo.R

// Splash
class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        // Set the animation for the logo
        val splashImageView = view.findViewById<ImageView>(R.id.splashImageView)
        val fadeIn = AlphaAnimation(0f, 1f).apply {
            duration = 1500 // 1.5 seconds for fade-in
            fillAfter = true
        }

        splashImageView.startAnimation(fadeIn)

        Handler(Looper.getMainLooper()).postDelayed({
            if (onBoardingFinished()) {
                // Navigate to MainNavigation Activity
                val intent = Intent(activity, MainNavigation::class.java)
                startActivity(intent)
                activity?.finish() // Finish SplashFragment activity
            } else {
                // Navigate to the ViewPageFragment using NavController
                findNavController().navigate(R.id.action_splashFragment_to_viewPageFragment)
            }
        }, 3000) // Increased to 3 seconds delay

        return view
    }

    private fun onBoardingFinished(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }
}
