package com.example.academic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.Fragments.ItemCatalog.PartCatalogAdapter
import com.example.techinfo.R
import com.example.techinfo.Fragments.ItemCatalog.Parts

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

        // Set up the back button
        val backButton: View = view.findViewById(R.id.btnBack)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack() // Navigate back to the previous fragment
        }

        // Load the catalog based on the part type
        loadCatalog(partType)
    }

    private fun loadCatalog(partType: String) {
        val items = when (partType) {
            "CPU" -> getCpuList()
            "GPU" -> getGpuList()
            "RAM" -> getRamList()
            else -> emptyList()
        }
        recyclerView.adapter = PartCatalogAdapter(items)
    }

    private fun getCpuList(): List<Parts> {
        return listOf(
            Parts("AMD Ryzen 5 5600X", "6 cores, 12 threads"),
            Parts("Intel Core i5-12600", "6 performance cores, 4 efficiency cores")
        )
    }

    private fun getGpuList(): List<Parts> {
        return listOf(
            Parts("NVIDIA RTX 3060", "12GB GDDR6"),
            Parts("AMD RX 6700 XT", "12GB GDDR6")
        )
    }

    private fun getRamList(): List<Parts> {
        return listOf(
            Parts("Corsair Vengeance LPX 16GB", "DDR4 3200MHz"),
            Parts("G.SKILL Trident Z 32GB", "DDR4 3600MHz")
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
