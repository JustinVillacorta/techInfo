package com.example.techinfo.onBoarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.techinfo.MainNavigation.MainNavigation
import com.example.techinfo.R
import com.example.techinfo.onBoarding.Screen.FirstScreen
import com.example.techinfo.onBoarding.Screen.FourthScreen
import com.example.techinfo.onBoarding.Screen.SecondScreen
import com.example.techinfo.onBoarding.Screen.ThirdScreen

class ViewPageFragment : Fragment() {

    private lateinit var dots: List<View>
    private lateinit var dotsLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_page, container, false)

        dotsLayout = view.findViewById<LinearLayout>(R.id.dotsLayout)

        val fragmentList = arrayListOf<Fragment>(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen(),
            FourthScreen()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = adapter

        // Remove any automatic page change callback
        // viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() { ... })

        createDots(adapter.itemCount)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDots(position)
            }
        })

        return view
    }

    private fun createDots(count: Int) {
        dots = List(count) { index ->
            val dot = View(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(20, 20).apply {
                    setMargins(4, 4, 4, 4)
                }
                setBackgroundResource(R.drawable.dot_inactive)
                }
                dotsLayout.addView(dot)
                dot
        }
        updateDots(0)
    }

    private fun updateDots(position: Int) {
        for (i in dots.indices) {
            dots[i].setBackgroundResource(
                if (i == position) R.drawable.dot_active else R.drawable.dot_inactive
            )
        }
    }
}
