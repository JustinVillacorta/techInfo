package com.example.techinfo.Fragments.Admin

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R
import com.example.techinfo.api_connector.Processor
import com.example.techinfo.api_connector.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminView : Fragment() {
    private lateinit var recyclerViewAdmin: RecyclerView
    private lateinit var adminList: ArrayList<admin_data_class> // Full list of items
    private lateinit var filteredList: ArrayList<admin_data_class> // Filtered list for display
    private lateinit var adminAdapter: admin_adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_view, container, false)

        adminList = ArrayList()
        filteredList = ArrayList() // For filtered data

        recyclerViewAdmin = view.findViewById(R.id.recyclerViewAdmin)

        adminAdapter = admin_adapter(requireContext(), filteredList)
        recyclerViewAdmin.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewAdmin.adapter = adminAdapter

        val fab = view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.Add)

        fab.setOnClickListener {
            openAddDialog()  // Call the method to open the add dialog
        }

        fetchProcessorsFromApi()

        return view
    }

    private fun openAddDialog() {
        if (isAdded) {  // Ensure fragment is attached
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.pc_parts_add, null)

            // Find RecyclerView in the dialog layout
            val recyclerView = dialogView.findViewById<RecyclerView>(R.id.modelsRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            // Sample data for the adapter (replace this with your real data)
            val dataList = listOf(
                update_and_add_data_class("Model 1", "Specs 1"),
                update_and_add_data_class("Model 2", "Specs 2")
            )

            val adapter = update_and_add_adapter(dataList)
            recyclerView.adapter = adapter

            // Build the AlertDialog
            AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Save") { dialog, _ ->
                    // Handle save logic here
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            showExitConfirmationDialog()
        }
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Exit")
            .setMessage("Are you sure you want to exit the app?")
            .setPositiveButton("Yes") { dialog, _ ->
                requireActivity().finish() // Finish the activity to exit the app
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss() // Dismiss the dialog
            }
            .create()
            .show()
    }

    private fun fetchProcessorsFromApi() {
        val apiService = RetrofitInstance.getApiService()
        val call = apiService.getProcessors()

        call.enqueue(object : Callback<List<Processor>> {
            override fun onResponse(call: Call<List<Processor>>, response: Response<List<Processor>>) {
                if (response.isSuccessful) {
                    val processors = response.body()
                    processors?.forEach { processor ->
                        val adminItem = admin_data_class(
                            processorId = processor.processor_id,
                            ModelName = processor.processor_name,
                            Specs = "Brand: ${processor.brand}, Socket: ${processor.socket_type}, Clock Speed: ${processor.base_clock_speed} GHz - ${processor.max_turbo_boost_clock_speed} GHz",
                            Category = "CPU",
                            brand = processor.brand,
                            socket_type = processor.socket_type,
                            base_clock_speed = processor.base_clock_speed,
                            max_turbo_boost_clock_speed = processor.max_turbo_boost_clock_speed,
                            compatible_chipsets = processor.compatible_chipsets,
                            link = processor.link ?: "",  // Provide a default value or handle null
                            cache_size_mb = processor.cache_size_mb,
                            performance_score = processor.performance_score,
                            integrated_graphics = processor.integrated_graphics,
                            tdp = processor.tdp,
                            cores = processor.cores,
                            threads = processor.threads,
                            created_at = processor.created_at,
                            updated_at = processor.updated_at
                        )
                        adminList.add(adminItem)
                    }
                    filteredList.addAll(adminList)  // Initially show all data
                    adminAdapter.notifyDataSetChanged()
                } else {
                    Log.e("AdminView", "API Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Processor>>, t: Throwable) {
                if (isAdded) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("AdminView", "Fragment not attached: ${t.message}")
                }
            }
        })
    }

    // Method to open the edit dialog for an existing item
    fun openEditDialog(item: admin_data_class, position: Int) {
        if (isAdded) {  // Ensure fragment is attached
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_item, null)
            val modelNameEditText = dialogView.findViewById<EditText>(R.id.ModelNameEditText)
            val specsEditText = dialogView.findViewById<EditText>(R.id.SpecsEditText)

            // Pre-fill the EditTexts with current data
            modelNameEditText.setText(item.ModelName)
            specsEditText.setText(item.Specs)

            AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Save") { dialog, _ ->
                    val updatedModelName = modelNameEditText.text.toString().trim()
                    val updatedSpecs = specsEditText.text.toString().trim()

                    if (updatedModelName.isEmpty() || updatedSpecs.isEmpty()) {
                        Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    // Update logic to update the item
                    val updatedItem = item.copy(
                        ModelName = updatedModelName,
                        Specs = updatedSpecs,
                        updated_at = getCurrentDateTime() // Update the timestamp if needed
                    )

                    adminList[position] = updatedItem // Replace the old item with the updated item
                    adminAdapter.notifyItemChanged(position)
                    Toast.makeText(requireContext(), "Item updated successfully", Toast.LENGTH_SHORT).show()

                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    // Helper function to get the current date and time
    private fun getCurrentDateTime(): String {
        // Add logic here to generate the current date-time in the desired format
        return "2024-10-19T12:00:00Z"  // Example, replace with dynamic value
    }
}
