package com.example.academic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.Fragments.BuilcPC.ItemCatalog.PartCatalogAdapter
import com.example.techinfo.R
import com.example.techinfo.Fragments.BuilcPC.ItemCatalog.Parts

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

        recyclerView.adapter = PartCatalogAdapter(items) { selectedPart ->
            parentFragmentManager.setFragmentResult(
                "selectedPart", Bundle().apply {
                    putString("partName", selectedPart.name)
                    putString("partDetails", selectedPart.details)
                    putInt("position", selectedPart.position) // Ensure the correct position is passed
                }
            )
            // Optionally refresh the catalog or do something else after selection
            refreshCatalog() // If you want to refresh the catalog after a selection
            parentFragmentManager.popBackStack()
        }
    }

    private fun refreshCatalog() {
        // Reload the items and notify the adapter
        val items = when (partType) {
            "CPU" -> getCpuList()
            "GPU" -> getGpuList()
            "RAM" -> getRamList()
            "SSD" -> getSSDList()
            else -> emptyList()
        }

        recyclerView.adapter = PartCatalogAdapter(items) { selectedPart ->
            parentFragmentManager.setFragmentResult(
                "selectedPart", Bundle().apply {
                    putString("partName", selectedPart.name)
                    putString("partDetails", selectedPart.details)
                    putInt("position", selectedPart.position) // Ensure the correct position is passed
                }
            )
            parentFragmentManager.popBackStack()
        }
    }

    // Sample data lists
    private fun getCpuList(): List<Parts> {
        return listOf(
            Parts("Intel Core i7", "High performance CPU", 0),
            Parts("AMD Ryzen 5", "Great value for gaming", 0)
        )
    }

    private fun getGpuList(): List<Parts> {
        return listOf(
            Parts("NVIDIA GeForce RTX 3080", "Top-tier GPU for gaming", 1),
            Parts("AMD Radeon RX 6800", "Great performance", 1)
        )
    }

    private fun getRamList(): List<Parts> {
        return listOf(
            Parts("Corsair Vengeance 16GB", "High speed RAM", 2),
            Parts("G.Skill Ripjaws 32GB", "Excellent for multitasking", 2)
        )
    }

    private fun getSSDList(): List<Parts> {
        return listOf(
            Parts("Kingston 1tb", "goods", 3)
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
