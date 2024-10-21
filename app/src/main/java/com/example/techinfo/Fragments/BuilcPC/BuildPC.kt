package com.example.techinfo.Fragments.BuildPC

import AlertDialog_Buildpc
import android.content.Context
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
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class BuildPC : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var componentAdapter: Adapter
    private lateinit var buildButton: Button

    private val selectedComponentsMap = mutableMapOf<String, ComponentData>()

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

        parentFragmentManager.setFragmentResultListener("selectedComponent", this) { _, bundle ->
            val selectedComponent = bundle.getSerializable("selectedComponent") as ComponentData
            val type = bundle.getString("type") ?: ""
            updateSelectedComponent(type, selectedComponent)
        }

        buildButton = view.findViewById(R.id.BuildBTN)
        buildButton.setOnClickListener {
            Toast.makeText(requireContext(), "Building your PC...", Toast.LENGTH_SHORT).show()
            checkComponentCompatibility()
        }

        cpuProgressBar = view.findViewById(R.id.progressbarCpu)
        gpuProgressBar = view.findViewById(R.id.progressbarGpu)
        memoryProgressBar = view.findViewById(R.id.progressbarMemory)
        storageProgressBar = view.findViewById(R.id.progressbarStorage)
        psuProgressBar = view.findViewById(R.id.progressbarPsu)

        ProgressBars()

        restoreSelectedComponents()
    }

    private fun ProgressBars() {
        cpuProgressBar.progress = 0
        gpuProgressBar.progress = 0
        memoryProgressBar.progress = 0
        storageProgressBar.progress = 0
        psuProgressBar.progress = 0
    }

    private fun updateSelectedComponent(type: String, component: ComponentData) {
        selectedComponentsMap[type] = component
        val position = componentDataList.indexOfFirst { it.type.equals(type, ignoreCase = true) }
        if (position != -1) {
            componentDataList[position] = component
            componentAdapter.notifyItemChanged(position)
        } else {
            Log.e("BuildPC", "Component type not found: $type")
        }

        Toast.makeText(requireContext(), "${component.name} selected for ${component.type}", Toast.LENGTH_SHORT).show()

        updateProgressBars()

        fetchPerformanceScore()
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

    private fun restoreSelectedComponents() {
        for ((type, component) in selectedComponentsMap) {
            val position = componentDataList.indexOfFirst { it.type.equals(type, ignoreCase = true) }
            if (position != -1) {
                componentDataList[position] = component
                componentAdapter.notifyItemChanged(position)
            }
        }

        updateProgressBars()
    }

    private fun getAllSelectedComponents(): Map<String, String> {
        return selectedComponentsMap.mapValues { it.value.name }
    }

    private fun checkComponentCompatibility() {
        val selectedComponents = getAllSelectedComponents()

        val processorName = selectedComponents["CPU"] ?: ""
        val motherboardName = selectedComponents["Motherboard"] ?: ""
        val ramName = selectedComponents["RAM"] ?: ""
        val gpuName = selectedComponents["GPU"] ?: ""
        val psuName = selectedComponents["PSU"] ?: ""
        val caseName = selectedComponents["Case"] ?: ""
        val coolerName = selectedComponents["CPU Cooler"] ?: ""
        val hddName = selectedComponents["HDD"] ?: ""
        val ssdName = selectedComponents["SSD"] ?: ""

        val missingComponents = mutableListOf<String>()
        if (processorName.isEmpty()) missingComponents.add("CPU")
        if (motherboardName.isEmpty()) missingComponents.add("Motherboard")
        if (ramName.isEmpty()) missingComponents.add("RAM")
        if (gpuName.isEmpty()) missingComponents.add("GPU")
        if (psuName.isEmpty()) missingComponents.add("PSU")
        if (caseName.isEmpty()) missingComponents.add("Case")
        if (coolerName.isEmpty()) missingComponents.add("CPU Cooler")

        if (missingComponents.isNotEmpty()) {
            val errorMessages = missingComponents.joinToString("\n") { "$it is missing" }
            showAlertDialog(errorMessages)
            return
        }

        val apiService = RetrofitInstance.getApiService()
        val call = apiService.checkCompatibility(
            processorName, motherboardName, ramName, gpuName, psuName, caseName, coolerName, hddName, ssdName
        )

        call.enqueue(object : Callback<CompatibilityResponse> {
            override fun onResponse(call: Call<CompatibilityResponse>, response: Response<CompatibilityResponse>) {
                if (response.isSuccessful) {
                    val compatibilityResponse = response.body()
                    if (compatibilityResponse?.is_compatible == true) {
                        Toast.makeText(requireContext(), "Components are compatible!", Toast.LENGTH_SHORT).show()
                        saveSelectedComponents(processorName, gpuName, ramName, ssdName, hddName, psuName, coolerName, caseName, motherboardName)
                    } else {
                        val issues = compatibilityResponse?.issues?.joinToString("\n\n")
                        showAlertDialog(issues ?: "No compatibility issues.")
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

    private fun saveSelectedComponents(processorName: String, gpuName: String, ramName: String, ssdName: String, hddName: String, psuName: String, coolerName: String, caseName: String, motherboardName: String) {
        // Directory where builds will be saved
        val buildsDir = File(requireContext().filesDir, "PC_Builds")

        // Create the directory if it doesn't exist
        if (!buildsDir.exists()) {
            buildsDir.mkdirs()
        }

        // Get the list of existing build files
        val existingBuilds = buildsDir.listFiles { _, name -> name.startsWith("Build") }?.toList() ?: emptyList()

        // Determine the next build number (auto-incrementing)
        val nextBuildNumber = if (existingBuilds.isEmpty()) 1 else {
            val lastBuildFile = existingBuilds.maxByOrNull {
                // Extract the number from the filename by removing "Build " and ".txt"
                it.name.substringAfter("Build ").removeSuffix(".txt").toIntOrNull() ?: 0
            }
            val lastBuildNumber = lastBuildFile?.name?.substringAfter("Build ")?.removeSuffix(".txt")?.toIntOrNull() ?: 0
            lastBuildNumber + 1
        }

        // Define the new build file name (e.g., Build 1, Build 2, etc.)
        val newBuildFileName = "Build $nextBuildNumber.txt"
        val newBuildFile = File(buildsDir, newBuildFileName)

        try {
            val fos = FileOutputStream(newBuildFile)
            fos.write("CPU: $processorName\n".toByteArray())
            fos.write("GPU: $gpuName\n".toByteArray())
            fos.write("RAM: $ramName\n".toByteArray())
            fos.write("SSD: $ssdName\n".toByteArray())
            fos.write("HDD: $hddName\n".toByteArray())
            fos.write("PSU: $psuName\n".toByteArray())
            fos.write("Case: $caseName\n".toByteArray())
            fos.write("Motherboard: $motherboardName\n".toByteArray())
            fos.write("CPU Cooler: $coolerName\n".toByteArray())
            fos.close()

            Toast.makeText(requireContext(), "PC build saved as $newBuildFileName!", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Log.e("BuildPC", "Error saving build", e)
            Toast.makeText(requireContext(), "Error saving build", Toast.LENGTH_SHORT).show()
        }
    }
}
