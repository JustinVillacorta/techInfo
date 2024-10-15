package com.example.techinfo.Fragments.BuildPC

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.techinfo.R

class ItemsInfo : Fragment() {

    companion object {
        fun newInstance(component: ComponentData): ItemsInfo {
            val fragment = ItemsInfo()
            val bundle = Bundle().apply {
                putSerializable("componentData", component)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_items_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the component data passed via arguments
        val component = arguments?.getSerializable("componentData") as? ComponentData

        // Check if the component is not null
        if (component != null) {
            // Update the TextViews based on available data
            view.findViewById<TextView>(R.id.articleTitleTextView).text = component.name

            // Dynamically populate contentTextView with available data (if exists)
            val contentTextView = view.findViewById<TextView>(R.id.contentTextView)

            val content = buildString {
                append("Name: ${component.name}\n")
                append("Type: ${component.type}\n")

                // Check if other details are available, and append them accordingly
                if (component.partDetails.isNotEmpty()) {
                    append("Part Details: ${component.partDetails}\n")
                }
            }

            contentTextView.text = content
        } else {
            Log.e("ItemsInfo", "Component data is null")
        }

        // Set the "OK" Button Listener
        view.findViewById<Button>(R.id.okButton).setOnClickListener {
            // Handle the selection of the CPU
            val selectedComponent = arguments?.getSerializable("componentData") as? ComponentData
            if (selectedComponent != null) {
                // Pass the selected component back to BuildPC Fragment
                val buildPCFragment = BuildPC.newInstance(selectedComponent)

                // Replace the current fragment with BuildPC
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, buildPCFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}
