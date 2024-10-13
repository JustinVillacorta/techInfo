package com.example.techinfo.Fragments.BuildPC

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.Fragments.BuilcPC.ComponentData
import com.example.techinfo.Fragments.BuildPCmodules.Adapter
import com.example.techinfo.R
import com.example.techinfo.ItemCatalog

class BuildPC : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var componentAdapter: Adapter
    private lateinit var BuildBTN: Button
    private lateinit var progressBars: List<ProgressBar>

    // Component data for different parts
    private val componentDataList = mutableListOf(
        ComponentData("CPU"),
        ComponentData("GPU"),
        ComponentData("RAM"),
        ComponentData("SSD"),
        ComponentData("HDD"),
        ComponentData("PSU"),
        ComponentData("Case"),
        ComponentData("Motherboard"),
        ComponentData("CPU Cooler")
    )

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
            // When a component is clicked, pass the component name to the ItemCatalog fragment
            val componentName = component.name
            if (!componentName.isNullOrEmpty()) { // Check for both null and empty strings
                val partCatalogFragment = ItemCatalog.newInstance(componentName)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, partCatalogFragment)
                    .addToBackStack(null) // Adds transaction to the back stack
                    .commit()
            }
        }

        recyclerView.apply {
            adapter = componentAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // Set up fragment result listener to receive selected part from ItemCatalog
        parentFragmentManager.setFragmentResultListener("selectedPart", this) { _, bundle ->
            val partDetails = bundle.getString("partDetails")
            val position = bundle.getInt("position")
            val progress = bundle.getInt("progress")

            // Update the progress bars based on selected components
            updateProgressBars(position, progress)

            // Handle selected parts
            if (position in componentDataList.indices) {
                val component = componentDataList[position]

                if (component.isSelected && component.partDetails == partDetails) {
                    component.partDetails = null // Clear part details
                    component.isSelected = false // Mark as unselected
                } else {
                    component.partDetails = partDetails // Update the part details
                    component.isSelected = true // Mark as selected
                }

                componentAdapter.notifyItemChanged(position) // Refresh the specific item
            }
        }

        // Initialize progress bars
        progressBars = listOf(
            view.findViewById(R.id.progressbarCpu),
            view.findViewById(R.id.progressbarGpu),
            view.findViewById(R.id.progressbarMemory),
            view.findViewById(R.id.progressbarStorage),
            view.findViewById(R.id.progressbarPsu)
        )

        BuildBTN = view.findViewById(R.id.BuildBTN)
        BuildBTN.setOnClickListener {
            Toast.makeText(requireContext(), "PC build Saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateProgressBars(position: Int, progress: Int) {
        // Update progress bars based on position
        if (position in progressBars.indices) {
            progressBars[position].progress = progress
        }
    }
}
