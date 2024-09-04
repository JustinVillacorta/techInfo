package com.example.techinfo.onBoarding.Screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.techinfo.MainNavigation.MainNavigation
import com.example.techinfo.R

class FourthScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fourth_screen, container, false)

        // Find the "Next4" TextView and set up the click listener
        val finishButton = view.findViewById<TextView>(R.id.Next4)
        finishButton.setOnClickListener {
            onFinishClicked()
        }

        return view
    }

    private fun onFinishClicked() {
        // Save onboarding completion status
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("Finished", true)
            apply()
        }

        // Navigate to MainNavigation Activity
        val intent = Intent(activity, MainNavigation::class.java)
        startActivity(intent)
        activity?.finish() // Close the current activity to prevent going back
    }
}
