package com.example.techinfo.Fragments.PcComparison

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R
import java.io.File

class PcComparison : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var pcMetricAdapter: PCBuildComparisonAdapter
    private lateinit var pcMetricsList: List<PcbuildComparisonData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pc_comparison, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the list with data
        pcMetricsList = listOf(
            PcbuildComparisonData("COMPUTING", 23, 31),
            PcbuildComparisonData("RENDERING", 34, 51),
            PcbuildComparisonData("MEMORY", 45, -21),
            PcbuildComparisonData("POWERCAP", 37, -59),
            PcbuildComparisonData("DATA STORAGE", 4, -7)
        )

        // Initialize the adapter and set it to the RecyclerView
        pcMetricAdapter = PCBuildComparisonAdapter(pcMetricsList)
        recyclerView.adapter = pcMetricAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the list of all .txt files in the "PC_Builds" directory
        val buildsDirectory = File(requireContext().filesDir, "PC_Builds")
        val txtFiles = buildsDirectory.listFiles { file -> file.isFile && file.name.endsWith(".txt") }

        // If there are .txt files, map them to an array of filenames without the .txt extension
        val buildFileNames = txtFiles?.map { it.name.substringBeforeLast(".txt") }?.toTypedArray() ?: emptyArray()

        // Create the ArrayAdapter using the filenames without the .txt extension
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, buildFileNames)

        // Set the adapter to both AutoCompleteTextViews
        view.findViewById<AutoCompleteTextView>(R.id.Parts1).setAdapter(arrayAdapter)
        view.findViewById<AutoCompleteTextView>(R.id.Parts2).setAdapter(arrayAdapter)
    }
}
