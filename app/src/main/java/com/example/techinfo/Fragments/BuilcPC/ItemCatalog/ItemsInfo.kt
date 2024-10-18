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
        fun newInstance(component: ComponentData, position: Int): ItemsInfo {
            val fragment = ItemsInfo()
            val bundle = Bundle().apply {
                putSerializable("componentData", component)  // Pass the selected component
                putInt("position", position)  // Pass the position (used for component order)
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

        // Retrieve the component data and position passed from ItemCatalog fragment
        val component = arguments?.getSerializable("componentData") as? ComponentData
        val position = arguments?.getInt("position") ?: -1  // Get position (default to -1 if not found)

        // Log the component and position to ensure they're passed correctly
        Log.d("ItemsInfo", "Component Data: $component")
        Log.d("ItemsInfo", "Position: $position")

        // Check if the component is not null
        if (component != null) {
            // Update the title with component name
            view.findViewById<TextView>(R.id.articleTitleTextView).text = component.name

            val contentTextView = view.findViewById<TextView>(R.id.contentTextView)

            // Dynamically create content based on component type
            val content = when (component.type.lowercase()) {
                "cpu" -> {
                    component.processor?.let {
                        """
                        Brand: ${it.brand}
                        Socket Type: ${it.socket_type}
                        Power: ${it.power}W
                        Base Clock Speed: ${it.base_clock_speed} GHz
                        Max Clock Speed: ${it.max_clock_speed} GHz
                        Compatible Chipsets: ${it.compatible_chipsets}
                        """.trimIndent()
                    } ?: "No details available for Processor."
                }
                "gpu" -> {
                    component.gpu?.let {
                        """
                        Brand: ${it.brand}
                        Interface Type: ${it.interface_type}
                        TDP: ${it.tdp_wattage}W
                        Length: ${it.gpu_length_mm}mm
                        """.trimIndent()
                    } ?: "No details available for GPU."
                }
                // Handle other types similarly (RAM, SSD, etc.)
                else -> "No details available for this component."
            }

            // Set the formatted content in the contentTextView
            contentTextView.text = content
        } else {
            Log.e("ItemsInfo", "Component data is null")
        }

        // Handle the "OK" button action
        view.findViewById<Button>(R.id.okButton).setOnClickListener {
            if (component != null) {
                // Return the selected component back to BuildPC with the type
                val result = Bundle().apply {
                    putSerializable("selectedComponent", component)  // Send the selected component back
                    putString("type", component.type)  // Pass the type (e.g., CPU, GPU)
                }
                parentFragmentManager.setFragmentResult("selectedComponent", result)

                // Optionally, navigate back to the BuildPC fragment
                val buildPCFragment = BuildPC.newInstance(component)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, buildPCFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}
