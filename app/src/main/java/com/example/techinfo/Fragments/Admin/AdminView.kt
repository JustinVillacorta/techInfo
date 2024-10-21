package com.example.techinfo.Fragments.Admin

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
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

        fetchProcessorsFromApi()

        return view
    }

    // Fetch processors data from the API
    private fun fetchProcessorsFromApi() {
        val apiService = RetrofitInstance.getApiService()
        val call = apiService.getProcessors()

        call.enqueue(object : Callback<List<Processor>> {
            override fun onResponse(call: Call<List<Processor>>, response: Response<List<Processor>>) {
                if (response.isSuccessful) {
                    val processors = response.body()
                    processors?.forEach { processor ->
                        // Safely convert values

                        val power = processor.tdp?.let {
                            try {
                                it.toInt()
                            } catch (e: NumberFormatException) {
                                0
                            }
                        } ?: 0

                        // Create admin data class for each processor
                        val adminItem = admin_data_class(
                            processorId = processor.processor_id.toString(),  // processor_id as String
                            ModelName = processor.processor_name,  // processor_name
                            Specs = "Brand: ${processor.brand}, Socket: ${processor.socket_type}, " +
                                    "Clock Speed: ${processor.base_clock_speed} GHz - ${processor.max_turbo_boost_clock_speed} GHz, " +
                                    "TDP: ${processor.tdp}, Cache: ${processor.cache_size_mb}MB, " +
                                    "Graphics: ${processor.integrated_graphics}",  // Specifications description
                            Category = "CPU",  // Assuming CPU for category
                            brand = processor.brand,  // brand
                            socket_type = processor.socket_type,  // socket_type
                            cores = processor.cores,  // cores
                            threads = processor.threads,  // threads
                            base_clock_speed = processor.base_clock_speed,  // base clock speed as String
                            max_turbo_boost_clock_speed = processor.max_turbo_boost_clock_speed,  // max clock speed as String
                            tdp = processor.tdp,  // tdp (thermal design power)
                            cache_size_mb = processor.cache_size_mb,  // cache size in MB
                            integrated_graphics = processor.integrated_graphics,  // integrated graphics
                            compatible_chipsets = processor.compatible_chipsets,  // compatible chipsets
                            link = processor.link ?: "",  // Handle null link with empty string
                            created_at = processor.created_at ?: "",  // created_at timestamp, handle null
                            updated_at = processor.updated_at ?: ""  // updated_at timestamp, handle null
                        )


                        adminList.add(adminItem)  // Add item to the full list
                    }

                    // Initially show all items
                    filteredList.addAll(adminList)
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

    // Open dialog to edit an existing processor item
    fun openEditDialog(item: admin_data_class, position: Int) {
        if (isAdded) {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_item, null)
            val modelNameEditText = dialogView.findViewById<EditText>(R.id.ModelNameEditText)
            val specsEditText = dialogView.findViewById<EditText>(R.id.SpecsEditText)

            // Pre-fill EditTexts with existing values
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

                    // Create a new Processor object for the update
                    // Convert admin_data_class to Processor for API update
                    val updatedProcessor = Processor(
                        processor_id = item.processorId,
                        processor_name = updatedModelName,
                        brand = item.brand,
                        socket_type = item.socket_type,
                        compatible_chipsets = item.compatible_chipsets,
                        cores = item.cores,
                        threads = item.threads,
                        base_clock_speed = item.base_clock_speed,
                        max_turbo_boost_clock_speed = item.max_turbo_boost_clock_speed,
                        tdp = item.tdp,
                        cache_size_mb = item.cache_size_mb,
                        integrated_graphics = item.integrated_graphics,
                        link = item.link ?: "",  // Default empty string if null
                        created_at = item.created_at,
                        updated_at = getCurrentDateTime()  // Dynamically set the updated timestamp
                    )


                    // Update processor on the server
                    val apiService = RetrofitInstance.getApiService()
                    apiService.updateProcessor(item.processorId, updatedProcessor).enqueue(object : Callback<Processor> {
                        override fun onResponse(call: Call<Processor>, response: Response<Processor>) {
                            if (response.isSuccessful) {
                                val updatedItem = item.copy(
                                    ModelName = updatedModelName,
                                    Specs = updatedSpecs,
                                    updated_at = getCurrentDateTime() // Update the timestamp if needed
                                )

                                adminList[position] = updatedItem  // Replace item in the list
                                adminAdapter.notifyItemChanged(position)
                                Toast.makeText(requireContext(), "Item updated successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(requireContext(), "Failed to update on server", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Processor>, t: Throwable) {
                            if (isAdded) {
                                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                            } else {
                                Log.e("AdminView", "Fragment not attached: ${t.message}")
                            }
                        }
                    })

                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    // Helper function to get the current date and time (modify as needed)
    private fun getCurrentDateTime(): String {
        // Example date-time (replace with dynamic value)
        return "2024-10-19T12:00:00Z"
    }
}
