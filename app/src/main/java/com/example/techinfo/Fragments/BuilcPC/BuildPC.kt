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
import com.example.techinfo.api_connector.*
import com.example.techinfo.api_connector.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            checkComponentCompatibility()  // Call compatibility check when button is pressed
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

    private fun getAllSelectedComponents(): Map<String, String> {
        // Return only the names of the selected components
        return selectedComponentsMap.mapValues { it.value.name }
    }

    private fun checkComponentCompatibility() {
        // Get the selected components' names
        val selectedComponents = getAllSelectedComponents()

        // Extract the component names from the selected map
        val processorName = selectedComponents["CPU"] ?: ""
        val motherboardName = selectedComponents["Motherboard"] ?: ""
        val ramName = selectedComponents["RAM"] ?: ""
        val gpuName = selectedComponents["GPU"] ?: ""
        val psuName = selectedComponents["PSU"] ?: ""
        val caseName = selectedComponents["Case"] ?: ""
        val coolerName = selectedComponents["CPU Cooler"] ?: ""
        val hddName = selectedComponents["HDD"] ?: ""
        val ssdName = selectedComponents["SSD"] ?: ""

        // Use ApiService to check compatibility
        val apiService = RetrofitInstance.getApiService()
        val call = apiService.checkCompatibility(
            processorName, motherboardName, ramName, gpuName, psuName, caseName, coolerName, hddName, ssdName
        )

        // Make the API call asynchronously
        call.enqueue(object : Callback<CompatibilityResponse> {
            override fun onResponse(call: Call<CompatibilityResponse>, response: Response<CompatibilityResponse>) {
                if (response.isSuccessful) {
                    val compatibilityResponse = response.body()

                    // Handle the response: display compatibility issues if any
                    if (compatibilityResponse?.is_compatible == true) {
                        Toast.makeText(requireContext(), "Components are compatible!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Incompatible components: ${compatibilityResponse?.issues?.joinToString()}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Error checking compatibility", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CompatibilityResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
