package com.example.academic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.Fragments.BuilcPC.ItemCatalog.PartCatalogAdapter
import com.example.techinfo.Fragments.BuilcPC.ItemCatalog.Parts
import com.example.techinfo.R

class ItemCatalog : Fragment() {

    private lateinit var partType: String
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            partType = it.getString(ARG_PART_TYPE) ?: "CPU"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_catalog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup AutoCompleteTextViews (ensure these IDs exist in your layout)
        val models = resources.getStringArray(R.array.Models)
        val chipset = resources.getStringArray(R.array.Chipset)

        val arrayAdapter1 = ArrayAdapter(requireContext(), R.layout.buildpc_filter, chipset)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.buildpc_filter, models)

        view.findViewById<AutoCompleteTextView>(R.id.chipset_info).setAdapter(arrayAdapter1)
        view.findViewById<AutoCompleteTextView>(R.id.Models_info).setAdapter(arrayAdapter)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewPartCatalog)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Load the catalog and set up the click listener
        loadCatalog(partType)
    }

    private fun loadCatalog(partType: String) {
        val items = when (partType) {
            "CPU" -> getCpuList()
            "GPU" -> getGpuList()
            "RAM" -> getRamList()
            "SSD" -> getSSDList()
            else -> emptyList()
        }

        // Set adapter and handle item selection
        recyclerView.adapter = PartCatalogAdapter(items) { selectedPart ->
            // Toggle the selected state
            selectedPart.isSelected = !selectedPart.isSelected // Toggle selection

            // Create a result bundle and send it to the BuildPC fragment
            val result = Bundle().apply {
                putString("partDetails", selectedPart.details)
                putInt("position", selectedPart.position)
            }
            parentFragmentManager.setFragmentResult("selectedPart", result)

            // Pop back to previous fragment
            parentFragmentManager.popBackStack()
        }
    }


        // Sample data lists (ensure these match the actual structure of your Parts class)
    private fun getCpuList(): List<Parts> {
        return listOf(
            Parts("Intel Core i7", 0),
            Parts("AMD Ryzen 5", 0)
        )
    }

    private fun getGpuList(): List<Parts> {
        return listOf(
            Parts("NVIDIA GeForce RTX 3080",  1),
            Parts("AMD Radeon RX 6800", 1)
        )
    }

    private fun getRamList(): List<Parts> {
        return listOf(
            Parts("Corsair Vengeance 16GB", 2),
            Parts("G.Skill Ripjaws 32GB",  2)
        )
    }

    private fun getSSDList(): List<Parts> {
        return listOf(
            Parts("Kingston 1tb",  3)
        )
    }

    companion object {
        private const val ARG_PART_TYPE = "part_type"

        fun newInstance(partType: String) = ItemCatalog().apply {
            arguments = Bundle().apply {
                putString(ARG_PART_TYPE, partType)
            }
        }
    }
}
