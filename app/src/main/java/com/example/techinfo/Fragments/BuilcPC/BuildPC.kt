package com.example.techinfo.Fragments.BuildPC

import android.os.Bundle
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

        // To store the selected component data (key: position, value: selected component)
         val selectedComponents = mutableMapOf<Int, ComponentData>()

// When a component is selected and returned from ItemsInfo
        parentFragmentManager.setFragmentResultListener("selectedComponent", this) { _, bundle ->
            val selectedComponent = bundle.getSerializable("selectedComponent") as ComponentData
            val position = bundle.getInt("position")

            if (position in componentDataList.indices) {
                // Update the list and selectedComponents map
                componentDataList[position] = selectedComponent
                selectedComponents[position] = selectedComponent

                componentAdapter.notifyItemChanged(position) // Refresh the item in the RecyclerView
            }
        }


        // Initialize the progress bars
        progressBars = listOf(
            view.findViewById(R.id.progressbarCpu),
            view.findViewById(R.id.progressbarGpu),
            view.findViewById(R.id.progressbarMemory),
            view.findViewById(R.id.progressbarStorage),
            view.findViewById(R.id.progressbarPsu)
        )

        // Build button click listener
        buildButton = view.findViewById(R.id.BuildBTN)
        buildButton.setOnClickListener {
            Toast.makeText(requireContext(), "PC build Saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateProgressBars(position: Int, progress: Int) {
        // Update the progress bar based on the component's position
        if (position in progressBars.indices) {
            progressBars[position].progress = progress
        }
    }
}
