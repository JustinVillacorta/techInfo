package com.example.techinfo.Fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.techinfo.MainNavigation.MainNavigation
import com.example.techinfo.R

// Splash Fragment with Video Handling
class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        // Initialize the VideoView
        val splashVideoView = view.findViewById<VideoView>(R.id.VideoView)

        // Set the video URI from the raw folder
        val videoUri: Uri = Uri.parse("android.resource://" + requireContext().packageName + "/" + R.raw.splashintro)
        splashVideoView.setVideoURI(videoUri)

        // Start the video playback
        splashVideoView.start()

        // Add a listener for when the video completes
        splashVideoView.setOnCompletionListener {
            // Check if onboarding is finished and navigate accordingly
            if (onBoardingFinished()) {
                // Navigate to MainNavigation Activity
                val intent = Intent(activity, MainNavigation::class.java)
                startActivity(intent)
                activity?.finish() // Finish SplashFragment activity
            } else {
                // Navigate to the ViewPageFragment using NavController
                findNavController().navigate(R.id.action_splashFragment_to_viewPageFragment)
            }
        }

        // In case the video takes a while to finish, set a timeout as a fallback (optional)
        Handler(Looper.getMainLooper()).postDelayed({
            if (!splashVideoView.isPlaying) {
                // Handle cases where the video might not play or takes too long
                if (onBoardingFinished()) {
                    val intent = Intent(activity, MainNavigation::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    findNavController().navigate(R.id.action_splashFragment_to_viewPageFragment)
                }
            }
        }, 1000) // Optional fallback timeout, in case video is not working

        return view
    }

    // Check if the onboarding process is finished
    private fun onBoardingFinished(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }
}
