package com.example.techinfo.Fragments.BuildPC

import AlertDialog_Buildpc
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
        ComponentData("SSD", "SSD"),
        ComponentData("HDD", "HDD"),
        ComponentData("PSU", "PSU"),
        ComponentData("Case", "Case"),
        ComponentData("Motherboard", "Motherboard"),
        ComponentData("CPU Cooler", "CPU Cooler")
    )

    // ProgressBar references
    private lateinit var cpuProgressBar: ProgressBar
    private lateinit var gpuProgressBar: ProgressBar
    private lateinit var memoryProgressBar: ProgressBar
    private lateinit var storageProgressBar: ProgressBar
    private lateinit var psuProgressBar: ProgressBar

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

        // Initialize ProgressBars
        cpuProgressBar = view.findViewById(R.id.progressbarCpu)
        gpuProgressBar = view.findViewById(R.id.progressbarGpu)
        memoryProgressBar = view.findViewById(R.id.progressbarMemory)
        storageProgressBar = view.findViewById(R.id.progressbarStorage)
        psuProgressBar = view.findViewById(R.id.progressbarPsu)

        // Set initial progress values
        ProgressBars()

        // Restore previously selected components (if any)
        restoreSelectedComponents()
    }

    private fun ProgressBars() {
        // Set initial values for the progress bars (0 to 100)
        cpuProgressBar.progress = 0
        gpuProgressBar.progress = 0
        memoryProgressBar.progress = 0
        storageProgressBar.progress = 0
        psuProgressBar.progress = 0
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

        // Update progress bars based on the selected component
        updateProgressBars()

        // Fetch performance score for selected CPU, GPU, RAM, SSD, HDD, and PSU
        fetchPerformanceScore()  // Fetch performance scores for all components (can be optimized for specific components if needed)
    }

    private fun updateProgressBars() {
        // Update the progress of each progress bar based on the selected components
        cpuProgressBar.progress = selectedComponentsMap["CPU"]?.let { 0 } ?: 0
        gpuProgressBar.progress = selectedComponentsMap["GPU"]?.let { 0 } ?: 0
        memoryProgressBar.progress = selectedComponentsMap["RAM"]?.let { 0 } ?: 0

        // Combine the SSD and HDD progress for the STORAGE bar
        storageProgressBar.progress = 0
        val ssdProgress = selectedComponentsMap["SSD"]?.let { 0 } ?: 0
        val hddProgress = selectedComponentsMap["HDD"]?.let { 0 } ?: 0
        storageProgressBar.progress = ssdProgress + hddProgress

        psuProgressBar.progress = selectedComponentsMap["PSU"]?.let { 0 } ?: 0
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

        // After restoring components, update the progress bars
        updateProgressBars()
    }

    private fun getAllSelectedComponents(): Map<String, String> {
        // Return the names of the selected components
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

        // Check for missing components before proceeding
        val missingComponents = mutableListOf<String>()
        if (processorName.isEmpty()) missingComponents.add("CPU")
        if (motherboardName.isEmpty()) missingComponents.add("Motherboard")
        if (ramName.isEmpty()) missingComponents.add("RAM")
        if (gpuName.isEmpty()) missingComponents.add("GPU")
        if (psuName.isEmpty()) missingComponents.add("PSU")
        if (caseName.isEmpty()) missingComponents.add("Case")
        if (coolerName.isEmpty()) missingComponents.add("CPU Cooler")

        // If there are missing components, show an error message in the dialog
        if (missingComponents.isNotEmpty()) {
            val errorMessages = missingComponents.joinToString("\n") { "$it is missing" }
            showAlertDialog(errorMessages)
            return // Exit if there are missing components
        }

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
                        showAlertDialog("Some components are not compatible.")
                    }
                } else {
                    showAlertDialog("Failed to check compatibility.")
                }
            }

            override fun onFailure(call: Call<CompatibilityResponse>, t: Throwable) {
                // Handle failure
                showAlertDialog("Error: ${t.message}")
            }
        })
    }

    private fun showAlertDialog(message: String) {
        val alertDialog = AlertDialog_Buildpc(message)
        alertDialog.show(parentFragmentManager, "errorDialog")
    }

    private fun fetchPerformanceScore() {
        val selectedComponents = getAllSelectedComponents()

        val cpuName = selectedComponents["CPU"] ?: ""
        val gpuName = selectedComponents["GPU"] ?: ""
        val ramName = selectedComponents["RAM"] ?: ""
        val ssdName = selectedComponents["SSD"] ?: ""
        val hddName = selectedComponents["HDD"] ?: ""
        val psuName = selectedComponents["PSU"] ?: ""

        val apiService = RetrofitInstance.getApiService()

        // Fetch CPU performance score
        if (cpuName.isNotEmpty()) {
            val cpuCall = apiService.getProcessors()
            cpuCall.enqueue(object : Callback<List<Processor>> {
                override fun onResponse(call: Call<List<Processor>>, response: Response<List<Processor>>) {
                    if (response.isSuccessful) {
                        val selectedCpu = response.body()?.find { it.processor_name == cpuName }
                        selectedCpu?.let {
                            cpuProgressBar.progress = it.performance_score.toInt()  // Update the CPU progress bar
                        }
                    } else {
                        Toast.makeText(requireContext(), "Failed to fetch CPU data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Processor>>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error fetching CPU data: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Fetch GPU performance score
        if (gpuName.isNotEmpty()) {
            val gpuCall = apiService.getGpus()
            gpuCall.enqueue(object : Callback<List<Gpu>> {
                override fun onResponse(call: Call<List<Gpu>>, response: Response<List<Gpu>>) {
                    if (response.isSuccessful) {
                        val selectedGpu = response.body()?.find { it.gpu_name == gpuName }
                        selectedGpu?.let {
                            gpuProgressBar.progress = it.performance_score.toInt()  // Update the GPU progress bar
                        }
                    } else {
                        Toast.makeText(requireContext(), "Failed to fetch GPU data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Gpu>>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error fetching GPU data: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Fetch RAM performance score
        if (ramName.isNotEmpty()) {
            val ramCall = apiService.getRams()
            ramCall.enqueue(object : Callback<List<Ram>> {
                override fun onResponse(call: Call<List<Ram>>, response: Response<List<Ram>>) {
                    if (response.isSuccessful) {
                        val selectedRam = response.body()?.find { it.ram_name == ramName }
                        selectedRam?.let {
                            memoryProgressBar.progress = it.performance_score.toInt()  // Update the RAM progress bar
                        }
                    } else {
                        Toast.makeText(requireContext(), "Failed to fetch RAM data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Ram>>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error fetching RAM data: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Fetch SSD performance score
        if (ssdName.isNotEmpty()) {
            val ssdCall = apiService.getSsds()
            ssdCall.enqueue(object : Callback<List<Ssd>> {
                override fun onResponse(call: Call<List<Ssd>>, response: Response<List<Ssd>>) {
                    if (response.isSuccessful) {
                        val selectedSsd = response.body()?.find { it.ssd_name == ssdName }
                        selectedSsd?.let {
                            storageProgressBar.progress = it.performance_score.toInt()  // Update the storage progress bar
                        }
                    } else {
                        Toast.makeText(requireContext(), "Failed to fetch SSD data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Ssd>>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error fetching SSD data: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Fetch HDD performance score
        if (hddName.isNotEmpty()) {
            val hddCall = apiService.getHdds()
            hddCall.enqueue(object : Callback<List<Hdd>> {
                override fun onResponse(call: Call<List<Hdd>>, response: Response<List<Hdd>>) {
                    if (response.isSuccessful) {
                        val selectedHdd = response.body()?.find { it.hdd_name == hddName }
                        selectedHdd?.let {
                            storageProgressBar.progress = it.performance_score.toInt()  // Update the storage progress bar
                        }
                    } else {
                        Toast.makeText(requireContext(), "Failed to fetch HDD data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Hdd>>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error fetching HDD data: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Fetch PSU performance score
        if (psuName.isNotEmpty()) {
            val psuCall = apiService.getPowerSupplyUnits()
            psuCall.enqueue(object : Callback<List<PowerSupplyUnit>> {
                override fun onResponse(call: Call<List<PowerSupplyUnit>>, response: Response<List<PowerSupplyUnit>>) {
                    if (response.isSuccessful) {
                        val selectedPsu = response.body()?.find { it.psu_name == psuName }
                        selectedPsu?.let {
                            psuProgressBar.progress = it.performance_score.toInt()  // Update the PSU progress bar
                        }
                    } else {
                        Toast.makeText(requireContext(), "Failed to fetch PSU data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<PowerSupplyUnit>>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error fetching PSU data: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
