package com.example.techinfo.Fragments.Admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
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
            admin_data_class("CPU Brand 1", "CPU Specs 1", "CPU"),
            admin_data_class("GPU Brand 2", "GPU Specs 2", "GPU"),
            admin_data_class("RAM Brand 3", "RAM Specs 3", "RAM"),
            admin_data_class("SSD Brand 4", "SSD Specs 4", "Storage"),
            admin_data_class("HDD Brand 5", "HDD Specs 5", "Storage"),
            admin_data_class("PSU Brand 6", "PSU Specs 6", "PSU"),
            admin_data_class("CASE Brand 7", "CASE Specs 7", "Case"),
            admin_data_class("CPU Cooler Brand 8", "CPU Cooler Specs 8", "Cooling")
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

        if (filter.equals("All", ignoreCase = true)) {
            // Ensure all items are added when "All" is selected
            filteredList.addAll(adminList)
        } else {
            // Filter items based on the selected option
            filteredList.addAll(adminList.filter {
                it.ModelName.contains(filter, ignoreCase = true) || it.Category.equals(filter, ignoreCase = true) // Update to filter by both ModelName and Category
            })
        }

        // Notify the adapter to refresh the view
        adminAdapter.notifyDataSetChanged()
    }

    private fun addInfo() {
        val inflater = LayoutInflater.from(requireContext())
        val addView = inflater.inflate(R.layout.pc_parts_add, null)

        // Initialize EditText fields
        val adminTitle = addView.findViewById<EditText>(R.id.ModelName)
        val Specs1 = addView.findViewById<EditText>(R.id.specs1)
        val Specs2 = addView.findViewById<EditText>(R.id.specs2)
        val Specs3 = addView.findViewById<EditText>(R.id.specs3)
        val Specs4 = addView.findViewById<EditText>(R.id.specs4)
        val Specs5 = addView.findViewById<EditText>(R.id.specs5)
        val Specs6 = addView.findViewById<EditText>(R.id.specs6)
        val Specs7 = addView.findViewById<EditText>(R.id.specs7)
        val Specs8 = addView.findViewById<EditText>(R.id.specs8)
        val Specs9 = addView.findViewById<EditText>(R.id.specs9)
        val Specs10 = addView.findViewById<EditText>(R.id.specs10)
        val selectedCategory = addView.findViewById<AutoCompleteTextView>(R.id.Models_info) // Retrieve category

        // Initialize TextView labels for the specs
        val label1 = addView.findViewById<TextView>(R.id.specs1_label)
        val label2 = addView.findViewById<TextView>(R.id.specs2_label)
        val label3 = addView.findViewById<TextView>(R.id.specs3_label)
        val label4 = addView.findViewById<TextView>(R.id.specs4_label)
        val label5 = addView.findViewById<TextView>(R.id.specs5_label)
        val label6 = addView.findViewById<TextView>(R.id.specs6_label)
        val label7 = addView.findViewById<TextView>(R.id.specs7_label)
        val label8 = addView.findViewById<TextView>(R.id.specs8_label)
        val label9 = addView.findViewById<TextView>(R.id.specs9_label)
        val label10 = addView.findViewById<TextView>(R.id.specs10_label)

        // Set up category options (remove "All" from the list)
        val categories = listOf("CPU", "GPU") // Make sure "All" is not included
        val categorySpecsMap = mapOf(
            "CPU" to listOf(Specs1, Specs2, Specs3, Specs4, Specs5),
            "GPU" to listOf(Specs1, Specs2, Specs3, Specs4)
        )

        // Set up the AutoCompleteTextView with category options
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_filter, categories)
        selectedCategory.setAdapter(arrayAdapter)

        // Set the default category to "CPU"
        selectedCategory.setText("CPU", false)

        // Hide all EditText fields and their corresponding labels initially
        hideAllSpecsWithLabels(addView, arrayOf(
            Specs1 to label1,
            Specs2 to label2,
            Specs3 to label3,
            Specs4 to label4,
            Specs5 to label5,
            Specs6 to label6,
            Specs7 to label7,
            Specs8 to label8,
            Specs9 to label9,
            Specs10 to label10
        ))

        // Show the fields for the default selected category (CPU)
        showSpecsForCategory(addView, "CPU", categorySpecsMap)

        // Listener for category selection
        selectedCategory.setOnItemClickListener { _, _, position, _ ->
            val selected = categories[position]

            // Show the appropriate fields based on the selected category
            hideAllSpecsWithLabels(addView, arrayOf(
                Specs1 to label1,
                Specs2 to label2,
                Specs3 to label3,
                Specs4 to label4,
                Specs5 to label5,
                Specs6 to label6,
                Specs7 to label7,
                Specs8 to label8,
                Specs9 to label9,
                Specs10 to label10
            ))
            showSpecsForCategory(addView, selected, categorySpecsMap) // Show relevant fields and labels
        }

        // Create and show the dialog
        val addDialog = AlertDialog.Builder(requireContext())
        addDialog.setView(addView)
        addDialog.setPositiveButton("Ok") { dialog, _ ->
            val title = adminTitle.text.toString()
            val specsCombined = listOf(
                Specs1.text.toString(),
                Specs2.text.toString(),
                Specs3.text.toString(),
                Specs4.text.toString(),
                Specs5.text.toString(),
                Specs6.text.toString(),
                Specs7.text.toString(),
                Specs8.text.toString(),
                Specs9.text.toString(),
                Specs10.text.toString()
            ).joinToString(", ") // Combine all specs into a single string

            // Add the item with its category to the adminList
            adminList.add(admin_data_class(title, specsCombined, selectedCategory.text.toString())) // Include category
            filterAdminData(selectedCategory.text.toString()) // Apply current filter
            Toast.makeText(requireContext(), "Adding Admin Information Success", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        addDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
            Toast.makeText(requireContext(), "Cancel", Toast.LENGTH_SHORT).show()
        }
        addDialog.create().show()
    }

    private fun hideAllSpecsWithLabels(view: View, specsWithLabels: Array<Pair<EditText, TextView>>) {
        specsWithLabels.forEach { (editText, label) ->
            editText.visibility = View.GONE
            label.visibility = View.GONE
        }
    }

    // Function to show specs for the selected category
    private fun showSpecsForCategory(addView: View, category: String, categorySpecsMap: Map<String, List<EditText>>) {

        categorySpecsMap[category]?.forEach { editText ->
            editText.visibility = View.VISIBLE


            val labelId = addView.context.resources.getIdentifier("${resources.getResourceEntryName(editText.id)}_label", "id", requireContext().packageName)
            val label: TextView? = addView.findViewById(labelId)


            label?.visibility = View.VISIBLE
        }
    }

}
