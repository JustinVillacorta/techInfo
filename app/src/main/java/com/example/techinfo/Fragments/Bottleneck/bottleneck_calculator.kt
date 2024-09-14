package com.example.techinfo.Fragments.Bottleneck

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.techinfo.R

class bottleneck_calculator : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bottleneck_calculator, container, false)

        val Buttonout : Button = view.findViewById(R.id.ButtonOut)
        Buttonout.setOnClickListener{
            val fragment = BottleNeck()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_container, fragment)?.commit()
        }
        return view
    }
}