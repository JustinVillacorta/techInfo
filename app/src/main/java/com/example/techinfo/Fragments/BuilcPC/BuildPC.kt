package com.example.techinfo.Fragments.BuildPC

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.Fragments.BuildPCmodules.Adapter
import com.example.techinfo.ItemCatalog
import com.example.techinfo.R

class BuildPC : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var componentAdapter: Adapter
    private lateinit var buildButton: Button

    // Map to store the selected components
    private val selectedComponentsMap = mutableMapOf<String, ComponentData>()

    // Initialize the components list with placeholder data
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_build_p_c, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView and its adapter
        recyclerView = view.findViewById(R.id.componentsRecyclerView)
        componentAdapter = Adapter(componentDataList) { component, position ->
            val componentName = component.name
            if (componentName.isNotEmpty()) {
                val partCatalogFragment = ItemCatalog.newInstance(component.type)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, partCatalogFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = componentAdapter
        }

        // Listen for the result when a component is selected from ItemCatalog
        parentFragmentManager.setFragmentResultListener("selectedComponent", this) { _, bundle ->
            val selectedComponent = bundle.getSerializable("selectedComponent") as ComponentData
            val type = bundle.getString("type") ?: ""
            updateSelectedComponent(type, selectedComponent)
        }

        buildButton = view.findViewById(R.id.BuildBTN)
        buildButton.setOnClickListener {
            Toast.makeText(requireContext(), "Building your PC...", Toast.LENGTH_SHORT).show()
        }

        // Restore previously selected components (if any)
        restoreSelectedComponents()
    }

    private fun updateSelectedComponent(type: String, component: ComponentData) {
        // Store the selected component in the map based on its type
        selectedComponentsMap[type] = component

        // Find the position of the component type in the list
        val position = componentDataList.indexOfFirst { it.type.equals(type, ignoreCase = true) }

        if (position != -1) {
            // Replace the placeholder with the selected component in the list
            componentDataList[position] = component
            componentAdapter.notifyItemChanged(position) // Refresh only the updated item
        } else {
            Log.e("BuildPC", "Component type not found: $type")
        }

        // Provide feedback to the user
        Toast.makeText(requireContext(), "${component.name} selected for ${component.type}", Toast.LENGTH_SHORT).show()
    }


    // This method restores previously selected components in the UI after returning to BuildPC
    private fun restoreSelectedComponents() {
        for ((type, component) in selectedComponentsMap) {
            val position = componentDataList.indexOfFirst { it.type.equals(type, ignoreCase = true) }
            if (position != -1) {
                componentDataList[position] = component
                componentAdapter.notifyItemChanged(position) // Refresh only the updated item
            }
        }
    }
}
