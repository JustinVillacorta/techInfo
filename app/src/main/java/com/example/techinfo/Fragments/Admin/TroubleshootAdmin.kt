package com.example.techinfo.Fragments.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private lateinit var troubleshoot_admin: ArrayList<troubleshoot_admin_data_class>
    private lateinit var troubleshootAdminAdapter: Troubleshoot_admin_adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_troubleshoot__admin, container, false)

        // Initialize views here
        troubleshoot_admin = ArrayList()
        addBtn = view.findViewById(R.id.addbtn)
        recycler = view.findViewById(R.id.rview)

        // Setup the adapter
        troubleshootAdminAdapter = Troubleshoot_admin_adapter(requireContext(), troubleshoot_admin)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = troubleshootAdminAdapter

        // Set click listener for add button
        addBtn.setOnClickListener {
            addInfo()
        }

        // Sample data to populate RecyclerView (if needed)
        val componentDataList = listOf(
            troubleshoot_admin_data_class("Sample Title 1", "Sample Subtitle 1"),
            troubleshoot_admin_data_class("Sample Title 2", "Sample Subtitle 2"),
            troubleshoot_admin_data_class("Sample Title 3", "Sample Subtitle 3"),
            troubleshoot_admin_data_class("Sample Title 4", "Sample Subtitle 4"),
            troubleshoot_admin_data_class("Sample Title 5", "Sample Subtitle 5")
        )

        troubleshoot_admin.addAll(componentDataList) // Add sample data to the list
        troubleshootAdminAdapter.notifyDataSetChanged() // Notify adapter of data change

        return view // Return the inflated view
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
            troubleshoot_admin.add(troubleshoot_admin_data_class(title, subtitle)) // Use the title directly
            troubleshootAdminAdapter.notifyDataSetChanged()
            Toast.makeText(requireContext(), "Adding User Information Success", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        addDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
            Toast.makeText(requireContext(), "Cancel", Toast.LENGTH_SHORT).show()
        }
        addDialog.create().show()
    }
}

