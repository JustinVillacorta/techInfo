package com.example.techinfo.Fragments.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TroubleshootAdmin : Fragment() {
    private lateinit var addBtn: FloatingActionButton
    private lateinit var recycler: RecyclerView
    private lateinit var filteredList: ArrayList<troubleshoot_admin_data_class>
    private lateinit var troubleshoot_admin: ArrayList<troubleshoot_admin_data_class>
    private lateinit var troubleshootAdminAdapter: Troubleshoot_admin_adapter
    private lateinit var autoCompleteTextView: AutoCompleteTextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_troubleshoot__admin, container, false)

        troubleshoot_admin = ArrayList()
        filteredList = ArrayList() // Initialize the filteredList
        addBtn = view.findViewById(R.id.addbtn)
        recycler = view.findViewById(R.id.rview)
        autoCompleteTextView = view.findViewById(R.id.filter) // Initialize AutoCompleteTextView

        troubleshootAdminAdapter = Troubleshoot_admin_adapter(requireContext(), filteredList)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = troubleshootAdminAdapter

        // Sample data to populate RecyclerView
        val componentDataList = listOf(
            troubleshoot_admin_data_class("CPU Title 1", "Sample Subtitle 1"),
            troubleshoot_admin_data_class("GPU Title 2", "Sample Subtitle 2"),
            troubleshoot_admin_data_class("RAM Title 3", "Sample Subtitle 3"),
            troubleshoot_admin_data_class("SSD Title 4", "Sample Subtitle 4"),
            troubleshoot_admin_data_class("HDD Title 5", "Sample Subtitle 5")
        )
        troubleshoot_admin.addAll(componentDataList) // Add sample data to the list
        filteredList.addAll(troubleshoot_admin) // Initially populate the filtered list
        troubleshootAdminAdapter.notifyDataSetChanged() // Notify adapter of data change

        // Setup dropdown adapter
        val filterOptions = resources.getStringArray(R.array.filter)
        val arrayAdapter1 = ArrayAdapter(requireContext(), R.layout.dropdown_filter, filterOptions)
        autoCompleteTextView.setAdapter(arrayAdapter1)

        // Dropdown item selection listener
        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedFilter = filterOptions[position]
            filterAdminData(selectedFilter)
        }

        addBtn.setOnClickListener { addInfo() }

        return view // Return the inflated view
    }

    private fun filterAdminData(filter: String) {
        filteredList.clear()
        if (filter == "All") {
            filteredList.addAll(troubleshoot_admin) // Use the original list for "All"
        } else {
            filteredList.addAll(troubleshoot_admin.filter {
                it.troubleshootTitle.contains(filter, ignoreCase = true)
            })
        }
        troubleshootAdminAdapter.notifyDataSetChanged() // Notify adapter of data change
    }

    private fun addInfo() {
        val inflater = LayoutInflater.from(requireContext())
        val addView = inflater.inflate(R.layout.troubleshoot_add_item, null)
        val TroubleshootTitle = addView.findViewById<EditText>(R.id.ArticleTitle)
        val TroubleshootSubTitle = addView.findViewById<EditText>(R.id.article_subtitle)
        val addDialog = AlertDialog.Builder(requireContext())
        addDialog.setView(addView)
        addDialog.setPositiveButton("Ok") { dialog, _ ->
            val title = TroubleshootTitle.text.toString()
            val subtitle = TroubleshootSubTitle.text.toString()
            troubleshoot_admin.add(troubleshoot_admin_data_class(title, subtitle))
            filterAdminData(autoCompleteTextView.text.toString()) // Apply current filter
            Toast.makeText(requireContext(), "Adding User Information Success", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        addDialog.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        addDialog.create().show()
    }
}
