package com.example.techinfo.Fragments.PcComparison

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R

class PartsComparison : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var pcMetricAdapter: PCBuildComparisonAdapter
    private lateinit var pcMetricsList: MutableList<PcbuildComparisonData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_parts_comparison, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the list with default data
        pcMetricsList = mutableListOf()
        pcMetricAdapter = PCBuildComparisonAdapter(pcMetricsList)
        recyclerView.adapter = pcMetricAdapter

        // Dropdown Setup
        val componentsDropDown = view.findViewById<AutoCompleteTextView>(R.id.Components1)
        val parts1DropDown = view.findViewById<AutoCompleteTextView>(R.id.Parts1)
        val parts2DropDown = view.findViewById<AutoCompleteTextView>(R.id.Parts2)

        // Sample data for dropdowns
        val componentsList = listOf("CPU", "GPU", "Motherboard", "RAM", "Storage")

        // Parts lists for each component
        val cpuPartsList = listOf("Intel i7", "Intel i9", "AMD Ryzen 7", "AMD Ryzen 9")
        val gpuPartsList = listOf("NVIDIA RTX 3080", "NVIDIA RTX 3090", "AMD Radeon RX 6800", "AMD Radeon RX 6900")
        val motherboardPartsList = listOf("ASUS ROG", "MSI B550", "Gigabyte Aorus", "ASRock X570")
        val ramPartsList = listOf("Corsair Vengeance 16GB", "G.Skill Trident Z 32GB", "Crucial Ballistix 16GB", "Kingston HyperX 16GB")
        val storagePartsList = listOf("Samsung 970 EVO", "WD Blue SSD", "Seagate Barracuda HDD", "Crucial MX500 SSD")

        // Set up ArrayAdapter for Components dropdown
        val componentsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, componentsList)
        componentsDropDown.setAdapter(componentsAdapter)

        // Default: Set CPU as the selected component and populate the parts dropdowns with CPU-related parts
        componentsDropDown.setText("CPU", false)  // Set default selection
        updatePartsDropdowns(parts1DropDown, parts2DropDown, cpuPartsList)

        // Handling selection in Components dropdown
        componentsDropDown.setOnItemClickListener { parent, _, position, _ ->
            val selectedComponent = parent.getItemAtPosition(position).toString()
            Toast.makeText(requireContext(), "Selected Component: $selectedComponent", Toast.LENGTH_SHORT).show()

            // Update the parts dropdowns based on the selected component
            when (selectedComponent) {
                "CPU" -> updatePartsDropdowns(parts1DropDown, parts2DropDown, cpuPartsList)
                "GPU" -> updatePartsDropdowns(parts1DropDown, parts2DropDown, gpuPartsList)
                "Motherboard" -> updatePartsDropdowns(parts1DropDown, parts2DropDown, motherboardPartsList)
                "RAM" -> updatePartsDropdowns(parts1DropDown, parts2DropDown, ramPartsList)
                "Storage" -> updatePartsDropdowns(parts1DropDown, parts2DropDown, storagePartsList)
            }

            // Update RecyclerView based on selected component and parts
            updateRecyclerViewData(selectedComponent, parts1DropDown.text.toString(), parts2DropDown.text.toString())
        }

        // Handling selection in Parts1 and Parts2 dropdowns
        parts1DropDown.setOnItemClickListener { parent, _, position, _ ->
            val selectedPart1 = parent.getItemAtPosition(position).toString()
            Toast.makeText(requireContext(), "Selected Part 1: $selectedPart1", Toast.LENGTH_SHORT).show()

            // Update RecyclerView based on selected part
            updateRecyclerViewData(componentsDropDown.text.toString(), selectedPart1, parts2DropDown.text.toString())
        }

        parts2DropDown.setOnItemClickListener { parent, _, position, _ ->
            val selectedPart2 = parent.getItemAtPosition(position).toString()
            Toast.makeText(requireContext(), "Selected Part 2: $selectedPart2", Toast.LENGTH_SHORT).show()

            // Update RecyclerView based on selected part
            updateRecyclerViewData(componentsDropDown.text.toString(), parts1DropDown.text.toString(), selectedPart2)
        }

        return view
    }

    // Method to update RecyclerView based on selected dropdown values
    private fun updateRecyclerViewData(component: String, part1: String, part2: String) {
        pcMetricsList.clear()  // Clear the current list

        // Example logic: Adjust pcMetricsList based on the component or parts selected
        when (component) {
            "CPU" -> {
                when (part1) {
                    "Intel i7" -> {
                        pcMetricsList.addAll(listOf(
                            PcbuildComparisonData("COMPUTING", 10, 20),
                            PcbuildComparisonData("RENDERING", 30, 25),
                            PcbuildComparisonData("MULTITHREAD", 50, 40)
                        ))
                    }
                    "Intel i9" -> {
                        pcMetricsList.addAll(listOf(
                            PcbuildComparisonData("COMPUTING", 15, 30),
                            PcbuildComparisonData("RENDERING", 35, 28),
                            PcbuildComparisonData("MULTITHREAD", 55, 45)
                        ))
                    }
                    "AMD Ryzen 7" -> {
                        pcMetricsList.addAll(listOf(
                            PcbuildComparisonData("COMPUTING", 12, 22),
                            PcbuildComparisonData("RENDERING", 32, 27),
                            PcbuildComparisonData("MULTITHREAD", 52, 42)
                        ))
                    }
                    "AMD Ryzen 9" -> {
                        pcMetricsList.addAll(listOf(
                            PcbuildComparisonData("COMPUTING", 20, 35),
                            PcbuildComparisonData("RENDERING", 40, 30),
                            PcbuildComparisonData("MULTITHREAD", 60, 50)
                        ))
                    }
                }
            }
            "GPU" -> {
                when (part1) {
                    "NVIDIA RTX 3080" -> {
                        pcMetricsList.addAll(listOf(
                            PcbuildComparisonData("GRAPHICS", 80, 60),
                            PcbuildComparisonData("COMPUTING", 70, 90)
                        ))
                    }
                    "NVIDIA RTX 3090" -> {
                        pcMetricsList.addAll(listOf(
                            PcbuildComparisonData("GRAPHICS", 90, 70),
                            PcbuildComparisonData("COMPUTING", 80, 95)
                        ))
                    }
                    "AMD Radeon RX 6800" -> {
                        pcMetricsList.addAll(listOf(
                            PcbuildComparisonData("GRAPHICS", 70, 55),
                            PcbuildComparisonData("COMPUTING", 60, 85)
                        ))
                    }
                    "AMD Radeon RX 6900" -> {
                        pcMetricsList.addAll(listOf(
                            PcbuildComparisonData("GRAPHICS", 85, 65),
                            PcbuildComparisonData("COMPUTING", 75, 90)
                        ))
                    }
                }
            }
            // Add conditions for Motherboard, RAM, and Storage similarly
        }

        // Notify the adapter about the data change
        pcMetricAdapter.notifyDataSetChanged()
    }

    // Method to update Parts dropdowns based on selected component
    private fun updatePartsDropdowns(parts1DropDown: AutoCompleteTextView, parts2DropDown: AutoCompleteTextView, partsList: List<String>) {
        val partsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, partsList)
        parts1DropDown.setAdapter(partsAdapter)
        parts2DropDown.setAdapter(partsAdapter)

        // Set default parts if you want to pre-select the first item
        parts1DropDown.setText(partsList[0], false)
        parts2DropDown.setText(partsList[0], false)
    }
}
