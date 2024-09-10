package com.example.techinfo.Fragments.BottleneckModules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R


class BottleNeck : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: BottleneckAdaptor
    private lateinit var bottleList: List<BottleneckData>

    //private lateinit var textId: Array<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottle_neck_option, container, false)


        recyclerView = view.findViewById(R.id.bottleneck_recycler)

        // Sample data list
        bottleList = listOf(
            BottleneckData("CPU"),
            BottleneckData("GPU")
        )

        // Initialize the adapter and set it to the RecyclerView
        itemAdapter = BottleneckAdaptor(bottleList)
        recyclerView.adapter = itemAdapter

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val drop = arrayOf("Example 1", "Example 2", "Example 3" )
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, drop)
        view.findViewById<AutoCompleteTextView>(R.id.dropdown).setAdapter(arrayAdapter)

        }
    }
