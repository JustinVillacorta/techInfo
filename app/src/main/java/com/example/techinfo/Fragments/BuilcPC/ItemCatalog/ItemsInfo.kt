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
import androidx.fragment.app.FragmentTransaction
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
        contentTextView.text = when (component.type.lowercase()) {
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

            "ram" ->{
                component.ram?.let {
                    """
                   Ram name: ${it.ram_name}
                   brand: ${it.brand}
                   Ram capacity: ${it.ram_capacity_gb}
                   ram_speed: ${it.ram_speed_mhz}
                   power consumption: ${it.power_consumption}
                   """.trimIndent()
                } ?: "No details available for RAM"

            }

            "psu" ->{
                component.psu?.let {
                    """
                    Psu name: ${it.psu_name}
                    brand: ${it.brand}
                    wattage: ${it.wattage}
                    continuous_wattage: ${it.continuous_wattage}
                    gpu_6_pin_connectors: ${it.gpu_6_pin_connectors}
                    gpu_8_pin_connectors: ${it.gpu_8_pin_connectors}
                    gpu_12_pin_connectors: ${it.gpu_12_pin_connectors}
                    efficiency_rating: ${it.efficiency_rating}
                    has_required_connectors: ${it.has_required_connectors}
                    """.trimIndent()
                } ?: "No details available for psu"
            }



            "case" ->{
                component.case?.let {
                    """
                 case_name: ${it.case_name}
                 brand: ${it.brand}
                 form_factor_supported: ${it.form_factor_supported}
                 max_gpu_length_mm: ${it.max_gpu_length_mm}
                 max_hdd_count: ${it.max_hdd_count}
                 max_ssd_count: ${it.max_ssd_count}
                 current_hdd_count: ${it.current_hdd_count}
                 current_ssd_count: ${it.current_ssd_count}
                 max_cooler_height_mm: ${it.max_cooler_height_mm}
                 current_hdd_count: ${it.current_hdd_count}
                 """.trimIndent()

                }?: "No details available for case"
            }
            "cpu cooler" ->{
                component.cpuCooler?.let {
                    """
                 cooler_name: ${it.cooler_name}
                 brand: ${it.brand}
                 tdp_rating: ${it.tdp_rating}
                 socket_type_supported: ${it.socket_type_supported}
                 max_cooler_height_mm: ${it.max_cooler_height_mm}
                 """.trimIndent()

                }?: "No details available for Cpu cooler"
            }

            "hdd" ->{
                component.hdd?.let {
                    """
                 hdd_name: ${it.hdd_name}
                 brand: ${it.brand}
                 capacity_gb: ${it.capacity_gb}
                 """.trimIndent()

                }?: "No details available for hdd"
            }


            "ssd" ->{
                component.ssd?.let {
                    """
                 ssd_name: ${it.ssd_name}
                 capacity_gb: ${it.capacity_gb}
                 interface_type: ${it.interface_type}
                 """.trimIndent()

                }?: "No details available for Cpu cooler"
            }



            else -> "Component details are unavailable for this type."
        }

        // Set the creation and update times (check if these fields are available in your model)
        val createdTimeTextView = view.findViewById<TextView>(R.id.createdTimeTextView)
        createdTimeTextView.text = "Created At: ${component.processor?. created_at ?: "N/A"}"  // Adjust field name as needed

        val updatedTimeTextView = view.findViewById<TextView>(R.id.updatedTimeTextView)
        updatedTimeTextView.text = "Updated At: ${component.processor?. created_at?: "N/A"}"  // Adjust field name as needed


        view.findViewById<Button>(R.id.okButton).setOnClickListener {
            // Only proceed when the OK button is clicked
            val result = Bundle().apply {
                putSerializable("selectedComponent", component)  // Send the selected component back
                putString("type", component.type)  // Pass the type (e.g., CPU, GPU)
            }
            parentFragmentManager.setFragmentResult("selectedComponent", result)

            val fragmentManager = requireActivity().supportFragmentManager
            // Check if BuildPC is already in the back stack
            val buildPCFragment = fragmentManager.findFragmentByTag("BuildPC")

            if (buildPCFragment != null) {
                // Pop back to the existing BuildPC if it's already in the back stack
                fragmentManager.popBackStack("BuildPC", 0)
            } else {
                // Replace the current fragment with a new BuildPC instance
                fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, BuildPC(), "BuildPC")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack("BuildPC")  // Add this transaction to the back stack
                    .commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Ensure that no data is passed when the fragment is destroyed or backed out of.
        // Only pass the selected component when the "OK" button is clicked.
        parentFragmentManager.clearFragmentResult("selectedComponent")
    }
}