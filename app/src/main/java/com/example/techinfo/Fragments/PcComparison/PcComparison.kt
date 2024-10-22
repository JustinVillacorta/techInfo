package com.example.techinfo.Fragments.PcComparison

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R
import com.example.techinfo.api_connector.BuildComparisonRequest
import com.example.techinfo.api_connector.BuildComparisonResponse
import com.example.techinfo.api_connector.BuildData
import com.example.techinfo.api_connector.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PcComparison : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var pcMetricAdapter: PCBuildComparisonAdapter
    private lateinit var pcMetricsList: List<PcbuildComparisonData>
    private lateinit var buildsDirectory: File

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pc_comparison, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Set initial placeholder data
        pcMetricsList = listOf(
            PcbuildComparisonData("COMPUTING", 0, 0),
            PcbuildComparisonData("RENDERING", 0, 0),
            PcbuildComparisonData("MEMORY", 0, 0),
            PcbuildComparisonData("POWERCAP", 0, 0),
            PcbuildComparisonData("DATA STORAGE", 0, 0)
        )

        // Initialize the adapter with placeholder data
        pcMetricAdapter = PCBuildComparisonAdapter(pcMetricsList)
        recyclerView.adapter = pcMetricAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the directory containing build files
        buildsDirectory = File(requireContext().filesDir, "PC_Builds")
        val txtFiles = buildsDirectory.listFiles { file -> file.isFile && file.name.endsWith(".txt") }

        // Map the filenames without the .txt extension
        val buildFileNames = txtFiles?.map { it.name.substringBeforeLast(".txt") }?.toTypedArray() ?: emptyArray()

        // Create the ArrayAdapter using the filenames without the .txt extension
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, buildFileNames)

        // Set the adapter to both AutoCompleteTextViews
        view.findViewById<AutoCompleteTextView>(R.id.Parts1).setAdapter(arrayAdapter)
        view.findViewById<AutoCompleteTextView>(R.id.Parts2).setAdapter(arrayAdapter)

        // Get the compare button and set its click listener
        val compareButton: Button = view.findViewById(R.id.compareButton)

        compareButton.setOnClickListener {
            // Get selected build names
            val buildOneName = view.findViewById<AutoCompleteTextView>(R.id.Parts1).text.toString()
            val buildTwoName = view.findViewById<AutoCompleteTextView>(R.id.Parts2).text.toString()

            // Get the file contents based on selected build names
            val buildOneFile = File(buildsDirectory, "$buildOneName.txt")
            val buildTwoFile = File(buildsDirectory, "$buildTwoName.txt")

            if (buildOneFile.exists() && buildTwoFile.exists()) {
                val buildOneData = readBuildData(buildOneFile)
                val buildTwoData = readBuildData(buildTwoFile)

                // Prepare BuildComparisonRequest
                val jsonInput = BuildComparisonRequest(
                    build_one = BuildData(
                        processor_name = buildOneData["CPU"] ?: "",
                        gpu_name = buildOneData["GPU"] ?: "",
                        ram_name = buildOneData["RAM"] ?: "",
                        psu_name = buildOneData["PSU"] ?: "",
                        ssd_name = buildOneData["SSD"] ?: "",
                        hdd_name = buildOneData["HDD"] ?: ""
                    ),
                    build_two = BuildData(
                        processor_name = buildTwoData["CPU"] ?: "",
                        gpu_name = buildTwoData["GPU"] ?: "",
                        ram_name = buildTwoData["RAM"] ?: "",
                        psu_name = buildTwoData["PSU"] ?: "",
                        ssd_name = buildTwoData["SSD"] ?: "",
                        hdd_name = buildTwoData["HDD"] ?: ""
                    )
                )

                // Call the API method with the JSON input
                RetrofitInstance.getApiService().getBuildComparison(jsonInput).enqueue(object : Callback<BuildComparisonResponse> {
                    override fun onResponse(call: Call<BuildComparisonResponse>, response: Response<BuildComparisonResponse>) {
                        if (response.isSuccessful) {
                            response.body()?.let { buildComparisonResponse ->
                                // Prepare the metrics list based on the response
                                pcMetricsList = listOf(
                                    PcbuildComparisonData("COMPUTING",
                                        buildComparisonResponse.build_one.processor_percentage.toInt(),
                                        buildComparisonResponse.build_two.processor_percentage.toInt()
                                    ),
                                    PcbuildComparisonData("RENDERING",
                                        buildComparisonResponse.build_one.gpu_percentage.toInt(),
                                        buildComparisonResponse.build_two.gpu_percentage.toInt()
                                    ),
                                    PcbuildComparisonData("MEMORY",
                                        buildComparisonResponse.build_one.ram_percentage.toInt(),
                                        buildComparisonResponse.build_two.ram_percentage.toInt()
                                    ),
                                    PcbuildComparisonData("POWERCAP",
                                        buildComparisonResponse.build_one.psu_percentage.toInt(),
                                        buildComparisonResponse.build_two.psu_percentage.toInt()
                                    ),
                                    PcbuildComparisonData("DATA STORAGE",
                                        buildComparisonResponse.build_one.ssd_percentage.toInt(),
                                        buildComparisonResponse.build_two.ssd_percentage.toInt()
                                    )
                                )

                                // Update the adapter with new data
                                pcMetricAdapter.updateMetrics(pcMetricsList)
                            }
                        } else {
                            // Handle the error case (e.g., show an error message)
                            Toast.makeText(requireContext(), "Error: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<BuildComparisonResponse>, t: Throwable) {
                        // Handle network failure (e.g., show an error message)
                        Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                // Handle cases where build files are not found (e.g., show an error message)
                Toast.makeText(requireContext(), "Build files not found.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun readBuildData(file: File): Map<String, String> {
        val buildData = mutableMapOf<String, String>()

        file.bufferedReader().use { reader ->
            reader.lines().forEach { line ->
                val (key, value) = line.split(": ", limit = 2)
                buildData[key.trim()] = value.trim()
            }
        }

        return buildData
    }
}
