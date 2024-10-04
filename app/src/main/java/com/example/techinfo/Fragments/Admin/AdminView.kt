package com.example.techinfo.Fragments.Admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private lateinit var adminList: ArrayList<admin_data_class> // Assuming admin_data_class exists
    private lateinit var adminAdapter: admin_adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_admin_view, container, false)

        // Initialize views here
        adminList = ArrayList()
        addFab = view.findViewById(R.id.Add)
        recyclerViewAdmin = view.findViewById(R.id.recyclerViewAdmin)

        // Setup the adapter
        adminAdapter = admin_adapter(requireContext(), adminList)
        recyclerViewAdmin.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewAdmin.adapter = adminAdapter

        // Set click listener for add button
        addFab.setOnClickListener {
            addInfo()
        }

        // Sample data to populate RecyclerView (if needed)
        val sampleDataList = listOf(
            admin_data_class("Brand Title 1", "specs Subtitle 1"),
            admin_data_class("Brand Title 2", "specs Subtitle 2"),
            admin_data_class("Brand Title 3", "specs Subtitle 3"),
            admin_data_class("Brand Title 4", "specs Subtitle 4"),
            admin_data_class("Brand Title 5", "specs Subtitle 5")
        )

        adminList.addAll(sampleDataList) // Add sample data to the list
        adminAdapter.notifyDataSetChanged() // Notify adapter of data change

        return view // Return the inflated view
    }

    private fun addInfo() {
        val inflater = LayoutInflater.from(requireContext())
        val addView = inflater.inflate(R.layout.troubleshoot_add_item, null)
        val adminTitle = addView.findViewById<EditText>(R.id.ArticleTitle) // Assuming you have this ID
        val adminSubtitle = addView.findViewById<EditText>(R.id.article_subtitle) // Assuming you have this ID

        val addDialog = AlertDialog.Builder(requireContext())
        addDialog.setView(addView)
        addDialog.setPositiveButton("Ok") { dialog, _ ->
            val title = adminTitle.text.toString()
            val subtitle = adminSubtitle.text.toString()
            adminList.add(admin_data_class(title, subtitle)) // Use the title directly
            adminAdapter.notifyDataSetChanged()
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
