package com.example.techinfo.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academic.ItemCatalog
import com.example.techinfo.Fragments.BuilcPCmodules.ComponentData
import com.example.techinfo.Fragments.BuildPCmodules.Adapter
import com.example.techinfo.R

class BuildPC : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var componentAdapter: Adapter
    private val componentDataList = listOf(
        ComponentData("CPU"),
        ComponentData("GPU"),
        ComponentData("RAM"),
        ComponentData("SSD"),
        ComponentData("HDD"),
        ComponentData("PSU"),
        ComponentData("Case"),
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
        componentAdapter = Adapter(componentDataList) { component ->
            // Handle the click here
            val partCatalogFragment = ItemCatalog.newInstance(component.name)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, partCatalogFragment)
                .addToBackStack(null) // Add this transaction to the back stack
                .commit()
        }

        recyclerView.apply {
            adapter = componentAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}
