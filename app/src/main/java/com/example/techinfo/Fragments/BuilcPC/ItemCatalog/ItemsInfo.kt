package com.example.techinfo.Fragments.BuildPC

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.techinfo.R
import com.example.techinfo.api_connector.*

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

        // Retrieve the component data passed from ItemCatalog fragment
        val component = arguments?.getSerializable("componentData") as? ComponentData
            ?: run {
                Log.e("ItemsInfo", "Component data is missing or incorrect")
                return
            }

        Log.d("ItemsInfo", "Component Data: $component")

        // Set the image (if available)
        val imageView = view.findViewById<ImageView>(R.id.videoThumbnailImageView)
        val imageUrl = component.gpu?.link ?: component.processor?.link ?: "default_image_url"  // Adjust as needed
        Glide.with(this)
            .load(imageUrl)
            .into(imageView)

        // Set the title of the component (e.g., CPU, GPU, etc.)
        val titleTextView = view.findViewById<TextView>(R.id.articleTitleTextView)
        titleTextView.text = component.type.uppercase()

        // Set the content based on the component type
        val contentTextView = view.findViewById<TextView>(R.id.contentTextView)
        val content = when (component.type.lowercase()) {
            "cpu" -> {
                component.processor?.let {
                    """
                    Brand: ${it.brand}
                    Socket Type: ${it.socket_type}
                    Power: ${it.power}W
                    Base Clock Speed: ${it.base_clock_speed} GHz
                    Max Clock Speed: ${it.max_clock_speed} GHz
                    """.trimIndent()
                } ?: "No details available for Processor."
            }
            "gpu" -> {
                component.gpu?.let {
                    """
                    Brand: ${it.brand}
                    GPU Name: ${it.gpu_name}
                    Cuda Cores: ${it.cuda_cores}
                    Compute Units: ${it.compute_units}
                    Stream Processors: ${it.stream_processors}
                    Game Clock Ghz: ${it.game_clock_ghz}
                    Base Clock Ghz: ${it.base_clock_ghz}
                    Boost Clock Ghz: ${it.boost_clock_ghz}
                    Memory Size Gb: ${it.memory_size_gb}
                    Memory Type: ${it.memory_type}
                    Memory Interface Bits: ${it.memory_interface_bits}
                    Interface Type: ${it.interface_type}
                    GPU Length (mm): ${it.gpu_length_mm}
                    TDP Wattage: ${it.tdp_wattage}
                    Required Power: ${it.required_power}W
                    Product Link: ${it.link ?: "N/A"}
                    Created At: ${it.created_at ?: "N/A"}
                    Updated At: ${it.updated_at ?: "N/A"}
                    """.trimIndent()
                } ?: "No details available for GPU."
            }
            "motherboard" -> {
                component.motherboard?.let {
                    """
                    Motherboard Name: ${it.motherboard_name}
                    Brand: ${it.brand}
                    Socket Type: ${it.socket_type}
                    Chipset: ${it.chipset}
                    Max RAM Slots: ${it.max_ram_slots}
                    Max RAM Capacity: ${it.max_ram_capacity}
                    Max RAM Speed: ${it.max_ram_speed}
                    """.trimIndent()
                } ?: "No details available for Motherboard."
            }
            else -> "Component details are unavailable for this type."
        }

        contentTextView.text = content

        // Set the creation and update times (check if these fields are available in your model)
        val createdTimeTextView = view.findViewById<TextView>(R.id.createdTimeTextView)
        createdTimeTextView.text = "Created At: ${component.processor?.created_at ?: "N/A"}"  // Adjust field name as needed

        val updatedTimeTextView = view.findViewById<TextView>(R.id.updatedTimeTextView)
        updatedTimeTextView.text = "Updated At: ${component.processor?.updated_at ?: "N/A"}"  // Adjust field name as needed

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
