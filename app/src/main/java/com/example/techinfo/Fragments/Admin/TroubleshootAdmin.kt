package com.example.techinfo.Fragments.Admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TroubleshootAdmin : Fragment() {
    private lateinit var addBtn: FloatingActionButton
    private lateinit var recycler: RecyclerView
    private lateinit var troubleshootlist: ArrayList<troubleshoot_admin_data_class>
    private lateinit var filteredList: ArrayList<troubleshoot_admin_data_class>
    private lateinit var troubleshootAdminAdapter: Troubleshoot_admin_adapter
    private lateinit var autoCompleteTextView: AutoCompleteTextView

    // Hold the last selected filter
    private var lastSelectedFilter: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_troubleshoot__admin, container, false)

        troubleshootlist = ArrayList()
        filteredList = ArrayList() // Initialize the filteredList
        addBtn = view.findViewById(R.id.addbtn)
        recycler = view.findViewById(R.id.rview)
        autoCompleteTextView = view.findViewById(R.id.filter) // Initialize AutoCompleteTextView

        troubleshootAdminAdapter = Troubleshoot_admin_adapter(requireContext(), filteredList)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = troubleshootAdminAdapter

        // Sample data to populate RecyclerView
        val componentDataList = listOf(
            troubleshoot_admin_data_class("CPU Title 1", "Sample Subtitle 1", "CPU"),
            troubleshoot_admin_data_class("GPU Title 2", "Sample Subtitle 2", "GPU"),
            troubleshoot_admin_data_class("RAM Title 3", "Sample Subtitle 3","RAM"),
            troubleshoot_admin_data_class("SSD Title 4", "Sample Subtitle 4","SSD"),
            troubleshoot_admin_data_class("HDD Title 5", "Sample Subtitle 5","HDD")
        )
        troubleshootlist.addAll(componentDataList) // Add sample data to the list
        filteredList.addAll(troubleshootlist) // Populate the filtered list with the original data
        troubleshootAdminAdapter.notifyDataSetChanged() // Notify adapter of data change

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
        addBtn.setOnClickListener {
            addInfo()
        }

        return view
    }

    private fun filterAdminData(filter: String) {
        filteredList.clear()
        Log.d("TroubleshootAdmin", "Filtering with: $filter")

        if (filter.equals("All", ignoreCase = true)) {
            filteredList.addAll(troubleshootlist)
        } else {
            val filtered = troubleshootlist.filter {
                it.troubleshootTitle.contains(filter, ignoreCase = true) ||
                        it.troubleshootcategory.equals(filter, ignoreCase = true)
            }
            filteredList.addAll(filtered)
            Log.d("TroubleshootAdmin", "Filtered items: $filtered")
        }

        troubleshootAdminAdapter.notifyDataSetChanged()
    }


    private fun addInfo() {
        val inflater = LayoutInflater.from(requireContext())
        val addView = inflater.inflate(R.layout.troubleshoot_add_item, null)

        val adminTitle = addView.findViewById<EditText>(R.id.ModelName)
        val troubleshoot1 = addView.findViewById<EditText>(R.id.troubleshoot1)
        val troubleshoot2 = addView.findViewById<EditText>(R.id.troubleshoot2)

        val selectedCategory = addView.findViewById<AutoCompleteTextView>(R.id.Models_info)

        val label1 = addView.findViewById<TextView>(R.id.troubleshoot1_label)
        val label2 = addView.findViewById<TextView>(R.id.troubleshoot2_label)

        // Initialize the visibility of EditTexts and TextViews
        troubleshoot1.visibility = View.VISIBLE
        troubleshoot2.visibility = View.VISIBLE
        label1.visibility = View.VISIBLE
        label2.visibility = View.VISIBLE

        // Dropdown category options
        val categories = listOf("CPU", "GPU", "RAM", "Storage")
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_filter, categories)
        selectedCategory.setAdapter(arrayAdapter)
        selectedCategory.setText("CPU", false) // Default selection

        // AlertDialog to add new info
        val addDialog = AlertDialog.Builder(requireContext())
        addDialog.setView(addView)
        addDialog.setPositiveButton("Ok") { dialog, _ ->
            val title = adminTitle.text.toString()
            val specsCombined = listOf(
                troubleshoot1.text.toString(),
                troubleshoot2.text.toString()
            ).joinToString(", ")

            troubleshootlist.add(troubleshoot_admin_data_class(title, specsCombined, selectedCategory.text.toString()))
            filterAdminData(selectedCategory.text.toString())
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
