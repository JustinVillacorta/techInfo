package com.example.techinfo.Fragments.BuildPC

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
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

        // Log the component to ensure it's passed correctly
        Log.d("ItemsInfo", "Component Data: $component")

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
                        GPU Name: ${it.gpu_name}
                        Interface Type: ${it.interface_type}
                        TDP: ${it.tdp_wattage}W
                        Length: ${it.gpu_length_mm}mm
                        Memory Size: ${it.memory_size_gb}GB
                        Memory Type: ${it.memory_type}
                        Memory Interface: ${it.memory_interface_bits}
                        Game Clock: ${it.game_clock_ghz} GHz
                        Base Clock: ${it.base_clock_ghz} GHz
                        Boost Clock: ${it.boost_clock_ghz} GHz
                        Compute Units: ${it.compute_units}
                        Stream Processors: ${it.stream_processors}
                        Required Power: ${it.required_power}W
                        6-Pin Connectors: ${it.required_6_pin_connectors}
                        8-Pin Connectors: ${it.required_8_pin_connectors}
                        12-Pin Connectors: ${it.required_12_pin_connectors}
                        """.trimIndent()
                    } ?: "No details available for GPU."
                }
                "motherboard" -> {
                    component.motherboard?.let {
                        """
                        Brand: ${it.brand}
                        Motherboard Name: ${it.motherboard_name}
                        Socket Type: ${it.socket_type}
                        Chipset: ${it.chipset}
                        WiFi: ${it.wifi}
                        GPU Support: ${it.gpu_support}
                        """.trimIndent()
                    } ?: "No details available for Motherboard."
                }
                else -> "Component details are unavailable for this type."
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

                // Pop the current ItemsInfo fragment
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }
}
