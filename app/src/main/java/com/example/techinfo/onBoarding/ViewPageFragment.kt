package com.example.techinfo.onBoarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
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

        view.findViewById<ViewPager2>(R.id.viewPager).adapter = adapter





        return view
    }
}