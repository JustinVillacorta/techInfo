package com.example.techinfo.Fragments.BuildPC

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.Fragments.BuildPCmodules.Adapter
import com.example.techinfo.R
import com.example.techinfo.ItemCatalog

class BuildPC : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var componentAdapter: Adapter
    private lateinit var buildButton: Button
    private lateinit var progressBars: List<ProgressBar>

    // Create a central storage for selected components
    private val selectedComponents = BuildPCComponents()

    // List of components for building the PC
    private val componentDataList = mutableListOf(
        ComponentData("CPU", "CPU"),
        ComponentData("GPU", "GPU"),
        ComponentData("RAM", "RAM"),
        ComponentData("SSD", "Storage"),
        ComponentData("HDD", "Storage"),
        ComponentData("PSU", "PSU"),
        ComponentData("Case", "Case"),
        ComponentData("Motherboard", "Motherboard"),
        ComponentData("CPU Cooler", "CPU Cooler")
    )

    companion object {
        // Define the newInstance method to pass the selected component data to the BuildPC fragment
        fun newInstance(component: ComponentData): BuildPC {
            val fragment = BuildPC()
            val bundle = Bundle().apply {
                putSerializable("selectedComponent", component)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_build_p_c, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the selected component passed via arguments
        val selectedComponent = arguments?.getSerializable("selectedComponent") as? ComponentData

        // Initialize RecyclerView and its adapter
        recyclerView = view.findViewById(R.id.componentsRecyclerView)
        componentAdapter = Adapter(componentDataList) { component, position ->
            // When a component is clicked, pass the component name to the ItemCatalog fragment
            val componentName = component.name
            if (!componentName.isNullOrEmpty()) {
                val partCatalogFragment = ItemCatalog.newInstance(componentName)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, partCatalogFragment)  // Replace container with ItemCatalog
                    .addToBackStack(null) // Add to the back stack
                    .commit()
            }
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = componentAdapter
        }

        // When a component is selected and returned from ItemsInfo
        parentFragmentManager.setFragmentResultListener("selectedComponent", this) { _, bundle ->
            val selectedComponent = bundle.getSerializable("selectedComponent") as ComponentData
            val type = bundle.getString("type") ?: ""

            // Update the selectedComponents based on the type
            when (type.lowercase()) {
                "cpu" -> {
                    selectedComponents.cpuList.clear()
                    selectedComponents.cpuList.add(selectedComponent)
                }
                "gpu" -> {
                    selectedComponents.gpuList.clear()
                    selectedComponents.gpuList.add(selectedComponent)
                }
                "ram" -> {
                    selectedComponents.ramList.clear()
                    selectedComponents.ramList.add(selectedComponent)
                }
                "psu" -> {
                    selectedComponents.psuList.clear()
                    selectedComponents.psuList.add(selectedComponent)
                }
                "case" -> {
                    selectedComponents.caseList.clear()
                    selectedComponents.caseList.add(selectedComponent)
                }
                "cpu cooler" -> {
                    selectedComponents.cpuCoolerList.clear()
                    selectedComponents.cpuCoolerList.add(selectedComponent)
                }
                "hdd" -> {
                    selectedComponents.hddList.clear()
                    selectedComponents.hddList.add(selectedComponent)
                }
                "ssd" -> {
                    selectedComponents.ssdList.clear()
                    selectedComponents.ssdList.add(selectedComponent)
                }
                "motherboard" -> {
                    selectedComponents.motherboardList.clear()
                    selectedComponents.motherboardList.add(selectedComponent)
                }
                else -> Log.e("BuildPC", "Unknown type: $type")
            }

            // Update the UI to reflect the selected component
            updateComponent(type, selectedComponent)
        }

        // Handle build button click (for future implementation)
        buildButton = view.findViewById(R.id.BuildBTN)
        buildButton.setOnClickListener {
            Toast.makeText(requireContext(), "Building your PC...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateComponent(type: String, component: ComponentData) {
        // Find the component by its type and update the list
        val position = componentDataList.indexOfFirst { it.type.equals(type, ignoreCase = true) }
        if (position != -1) {
            componentDataList[position] = component
            componentAdapter.notifyItemChanged(position)
        } else {
            Log.e("BuildPC", "Component type not found: $type")
        }
    }
}
