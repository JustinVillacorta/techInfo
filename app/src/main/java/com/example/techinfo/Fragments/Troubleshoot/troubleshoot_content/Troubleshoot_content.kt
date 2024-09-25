package com.example.techinfo.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.techinfo.R

class Troubleshoot_content : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_troubleshoot_content, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        // Function to create a new instance of the fragment with the passed data
        fun newInstance(itemName: String): Troubleshoot_content {
            val fragment = Troubleshoot_content()
            val args = Bundle()
            args.putString("ITEM_NAME", itemName)
            fragment.arguments = args
            return fragment
        }


    }
}
