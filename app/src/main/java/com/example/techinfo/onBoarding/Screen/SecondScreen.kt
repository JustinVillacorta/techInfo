package com.example.techinfo.onBoarding.Screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.example.techinfo.MainNavigation.MainNavigation
import com.example.techinfo.R


class SecondScreen : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_second_screen, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)


        view.findViewById<TextView>(R.id.Next2).setOnClickListener{
            viewPager?.currentItem = 2
        }

        view.findViewById<TextView>(R.id.Skip2).setOnClickListener {
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