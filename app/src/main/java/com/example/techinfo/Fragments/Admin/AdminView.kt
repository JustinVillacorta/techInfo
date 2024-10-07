package com.example.techinfo.Fragments.Admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AdminView : Fragment() {
    private lateinit var addFab: FloatingActionButton
    private lateinit var recyclerViewAdmin: RecyclerView
    private lateinit var adminList: ArrayList<admin_data_class> // Full list of items
    private lateinit var filteredList: ArrayList<admin_data_class> // Filtered list for display
    private lateinit var adminAdapter: admin_adapter
    private lateinit var autoCompleteTextView: AutoCompleteTextView

    // Hold the last selected filter
    private var lastSelectedFilter: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_admin_view, container, false)

        // Initialize views
        adminList = ArrayList()
        filteredList = ArrayList() // For filtered data

        addFab = view.findViewById(R.id.Add)
        recyclerViewAdmin = view.findViewById(R.id.recyclerViewAdmin)
        autoCompleteTextView = view.findViewById(R.id.filter)

        // Setup adapter for RecyclerView
        adminAdapter = admin_adapter(requireContext(), filteredList)
        recyclerViewAdmin.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewAdmin.adapter = adminAdapter

        // Sample data
        val sampleDataList = listOf(
            admin_data_class("CPU Brand 1", "CPU Specs 1"),
            admin_data_class("GPU Brand 2", "GPU Specs 2"),
            admin_data_class("RAM Brand 3", "RAM Specs 3"),
            admin_data_class("SSD Brand 4", "SSD Specs 4"),
            admin_data_class("HDD Brand 5", "HDD Specs 5"),
            admin_data_class("PSU Brand 6", "PSU Specs 6"),
            admin_data_class("CASE Brand 7", "CASE Specs 7"),
            admin_data_class("CPU Cooler Brand 8", "CPU Cooler Specs 8")
        )
        adminList.addAll(sampleDataList)
        filteredList.addAll(adminList) // Initially show all data
        adminAdapter.notifyDataSetChanged()

        // Setup dropdown adapter
        val filterOptions = resources.getStringArray(R.array.filter).toList()
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_filter, filterOptions)
        autoCompleteTextView.setAdapter(arrayAdapter)

        // Dropdown item selection listener
        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedFilter = filterOptions[position]

            // Hide the previously selected item
            if (lastSelectedFilter != null) {
                arrayAdapter.add(lastSelectedFilter) // Re-add the last selected filter
            }

            // Filter the data
            filterAdminData(selectedFilter)

            // Hide selected item
            arrayAdapter.remove(selectedFilter) // Remove the current selection from the dropdown
            lastSelectedFilter = selectedFilter // Update last selected filter
            arrayAdapter.notifyDataSetChanged() // Notify adapter about the changes
        }

        // Set click listener for add button
        addFab.setOnClickListener {
            addInfo()
        }

        return view
    }

    private fun filterAdminData(filter: String) {
        filteredList.clear()
        if (filter == "All") {
            filteredList.addAll(adminList)
        } else {
            // Assuming the `admin_data_class` has a field like `ModelName` for filtering
            filteredList.addAll(adminList.filter {
                it.ModelName.contains(filter, ignoreCase = true) // Update the field name
            })
        }
        adminAdapter.notifyDataSetChanged()
    }

    private fun addInfo() {
        val inflater = LayoutInflater.from(requireContext())
        val addView = inflater.inflate(R.layout.troubleshoot_add_item, null)
        val adminTitle = addView.findViewById<EditText>(R.id.ArticleTitle)
        val adminSubtitle = addView.findViewById<EditText>(R.id.article_subtitle)

        val addDialog = AlertDialog.Builder(requireContext())
        addDialog.setView(addView)
        addDialog.setPositiveButton("Ok") { dialog, _ ->
            val title = adminTitle.text.toString()
            val subtitle = adminSubtitle.text.toString()
            adminList.add(admin_data_class(title, subtitle))
            filterAdminData(autoCompleteTextView.text.toString()) // Apply current filter
            Toast.makeText(requireContext(), "Adding Admin Information Success", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        addDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
            Toast.makeText(requireContext(), "Cancel", Toast.LENGTH_SHORT).show()
        }
        addDialog.create().show()
    }
}
