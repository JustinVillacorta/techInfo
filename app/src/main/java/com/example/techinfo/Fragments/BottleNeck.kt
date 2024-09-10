package com.example.techinfo.Fragments.BottleneckModules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academic.ItemCatalog
import com.example.techinfo.Fragments.BuildPCmodules.Adapter
import com.example.techinfo.R

class BottleNeck : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: BottleneckAdaptor
    private lateinit var bottleList: List<BottleneckData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottle_neck, container, false)

        recyclerView = view.findViewById(R.id.recyclerView_bottleneck)

        // Sample data list
        bottleList = listOf(
            BottleneckData("Central Processing Unit"),
            BottleneckData("Graphics Processing Unit"),
            BottleneckData("Random Access Memory"),
            BottleneckData("Solid-State Drive"),
            BottleneckData("Power Supply Unit"),
            BottleneckData("Motherboard")
        )

        // Initialize the adapter
        itemAdapter = BottleneckAdaptor(bottleList)

        // Set LayoutManager for the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = itemAdapter

        val ButtonIn : Button = view.findViewById(R.id.button)
        ButtonIn.setOnClickListener{
            val fragment = bottleneck_calculator()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_container, fragment)?.commit()
        }

        return view
    }

}
