package com.example.techinfo.Fragments.BuildPC

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academic.ItemCatalog
import com.example.techinfo.Fragments.BuilcPC.ComponentData

import com.example.techinfo.Fragments.BuildPCmodules.Adapter
import com.example.techinfo.R

class BuildPC : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var componentAdapter: Adapter
    private lateinit var BuildBTN: Button

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
            // Store the name in a local variable
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

            // Check if the position is valid
            if (position in componentDataList.indices) {
                val component = componentDataList[position]

                // Compare with component.name
                if (component.isSelected && component.partDetails == partDetails) {
                    // If already selected with the same details and name, unselect it
                    component.partDetails = null // Clear part details
                    component.isSelected = false // Mark as unselected
                } else {
                    // If not selected, select it

                    component.partDetails = partDetails // Update the part details
                    component.isSelected = true // Mark as selected

                }

                componentAdapter.notifyItemChanged(position) // Notify the adapter to refresh the specific item
            }
        }


    }
}
