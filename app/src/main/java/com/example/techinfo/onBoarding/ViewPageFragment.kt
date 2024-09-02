package com.example.techinfo.onBoarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.techinfo.MainNavigation
import com.example.techinfo.R
import com.example.techinfo.onBoarding.Screen.FirstScreen
import com.example.techinfo.onBoarding.Screen.FourthScreen
import com.example.techinfo.onBoarding.Screen.SecondScreen
import com.example.techinfo.onBoarding.Screen.ThirdScreen

class ViewPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_page, container, false)

        val fragmentList = arrayListOf<Fragment>(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen(),
            FourthScreen(),
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = adapter

        // Add a listener to check when the user reaches the last page
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // If it's the last page, navigate to MainNavigation
                if (position == fragmentList.size - 1) {
                    onOnboardingComplete()
                }
            }
        })

        return view
    }

    private fun onOnboardingComplete() {
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
