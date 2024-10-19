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

    private fun fetchProcessorsFromApi() {
        val apiService = RetrofitInstance.getApiService()
        val call = apiService.getProcessors()

        call.enqueue(object : Callback<List<Processor>> {
            override fun onResponse(call: Call<List<Processor>>, response: Response<List<Processor>>) {
                if (response.isSuccessful) {
                    val processors = response.body()
                    processors?.forEach { processor ->
                        // Safely convert the string values to Float
                        val baseClockSpeed = processor.base_clock_speed.toFloatOrNull()
                        val maxClockSpeed = processor.max_clock_speed.toFloatOrNull()

                        if (baseClockSpeed == null || maxClockSpeed == null) {
                            Log.e("AdminView", "Invalid clock speed values: baseClockSpeed=${processor.base_clock_speed}, maxClockSpeed=${processor.max_clock_speed}")
                        }

                        val adminItem = admin_data_class(
                            processorId = processor.processor_id,
                            ModelName = processor.processor_name,
                            Specs = "Brand: ${processor.brand}, Socket: ${processor.socket_type}, Clock Speed: ${baseClockSpeed} GHz - ${maxClockSpeed} GHz, Power: ${processor.power}W",
                            Category = "CPU",
                            brand = processor.brand,
                            socket_type = processor.socket_type,
                            base_clock_speed = baseClockSpeed ?: 0.0f,
                            max_clock_speed = maxClockSpeed ?: 0.0f,
                            power = processor.power,
                            compatible_chipsets = processor.compatible_chipsets,
                            link = processor.link,
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

                    val updatedProcessor = Processor(
                        processor_id = item.processorId,
                        processor_name = updatedModelName,
                        brand = item.brand,
                        socket_type = item.socket_type,
                        base_clock_speed = "%.2f".format(item.base_clock_speed),
                        max_clock_speed = "%.2f".format(item.max_clock_speed),
                        power = item.power.toString(),
                        compatible_chipsets = item.compatible_chipsets,
                        link = item.link,
                        created_at = item.created_at,
                        updated_at = getCurrentDateTime()  // Dynamically get the current date-time
                    )

                    val apiService = RetrofitInstance.getApiService()
                    apiService.updateProcessor(item.processorId, updatedProcessor).enqueue(object : Callback<Processor> {
                        override fun onResponse(call: Call<Processor>, response: Response<Processor>) {
                            if (response.isSuccessful) {
                                item.ModelName = updatedModelName
                                item.Specs = updatedSpecs

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

    // Helper function to get the current date and time
    private fun getCurrentDateTime(): String {
        // Add logic here to generate the current date-time in the desired format
        return "2024-10-19T12:00:00Z"  // Example, replace with dynamic value
    }
}
