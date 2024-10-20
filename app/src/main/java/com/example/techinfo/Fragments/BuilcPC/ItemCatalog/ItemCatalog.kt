package com.example.techinfo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.api_connector.*
import com.example.techinfo.Fragments.BuildPCmodules.Adapter
import com.example.techinfo.Fragments.BuildPC.ComponentData
import com.example.techinfo.Fragments.BuildPC.ItemsInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemCatalog : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var componentAdapter: Adapter
    private val componentDataList = mutableListOf<ComponentData>()
    private val apiService = RetrofitInstance.getApiService()

    // Track the selected motherboard and CPU
    private var selectedMotherboard: Motherboard? = null
    private var selectedProcessor: Processor? = null

    companion object {
        fun newInstance(
            componentName: String,
            selectedMotherboard: Motherboard? = null,
            selectedProcessor: Processor? = null
        ): ItemCatalog {
            val fragment = ItemCatalog()
            val bundle = Bundle()
            bundle.putString("componentName", componentName)
            bundle.putSerializable("selectedMotherboard", selectedMotherboard)
            bundle.putSerializable("selectedProcessor", selectedProcessor)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_catalog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewPartCatalog)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Get selected motherboard and processor (if provided)
        selectedMotherboard = arguments?.getSerializable("selectedMotherboard") as? Motherboard
        selectedProcessor = arguments?.getSerializable("selectedProcessor") as? Processor

        // Log the selected components for debugging
        Log.d("ItemCatalog", "Selected Motherboard: $selectedMotherboard")
        Log.d("ItemCatalog", "Selected Processor: $selectedProcessor")

        val componentName = arguments?.getString("componentName") ?: ""
        fetchComponentData(componentName)

        componentAdapter = Adapter(componentDataList) { component: ComponentData, position: Int ->
            val itemInfoFragment = ItemsInfo.newInstance(component, position)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, itemInfoFragment)
                .addToBackStack(null)
                .commit()
        }

        recyclerView.adapter = componentAdapter
    }

    private fun fetchComponentData(componentName: String) {
        when (componentName.lowercase()) {
            "cpu" -> {
                apiService.getProcessors().enqueue(object : Callback<List<Processor>> {
                    override fun onResponse(call: Call<List<Processor>>, response: Response<List<Processor>>) {
                        if (response.isSuccessful) {
                            componentDataList.clear()
                            response.body()?.forEach { processor ->
                                // Log each processor for debugging
                                Log.d("ItemCatalog", "Processor found: ${processor.processor_name}, Socket Type: ${processor.socket_type}")

                                // Only add compatible CPUs if a motherboard is selected
                                if (selectedMotherboard == null || isCompatible(processor.socket_type, selectedMotherboard?.socket_type)) {
                                    Log.d("ItemCatalog", "Processor ${processor.processor_name} is compatible with motherboard")
                                    componentDataList.add(
                                        ComponentData(
                                            name = processor.processor_name,
                                            type = "CPU",
                                            processor = processor
                                        )
                                    )
                                } else {
                                    Log.d("ItemCatalog", "Processor ${processor.processor_name} is NOT compatible with motherboard")
                                }
                            }
                            componentAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun onFailure(call: Call<List<Processor>>, t: Throwable) {
                        Log.e("APIFailure", "Request failed: ${t.localizedMessage}")
                    }
                })
            }

            "motherboard" -> {
                apiService.getMotherboards().enqueue(object : Callback<List<Motherboard>> {
                    override fun onResponse(call: Call<List<Motherboard>>, response: Response<List<Motherboard>>) {
                        if (response.isSuccessful) {
                            componentDataList.clear()
                            response.body()?.forEach { motherboard ->
                                // Log each motherboard for debugging
                                Log.d("ItemCatalog", "Motherboard found: ${motherboard.motherboard_name}, Socket Type: ${motherboard.socket_type}")

                                // Only add compatible motherboards if a CPU is selected
                                if (selectedProcessor == null || isCompatible(motherboard.socket_type, selectedProcessor?.socket_type)) {
                                    Log.d("ItemCatalog", "Motherboard ${motherboard.motherboard_name} is compatible with CPU")
                                    componentDataList.add(
                                        ComponentData(
                                            name = motherboard.motherboard_name,
                                            type = "Motherboard",
                                            motherboard = motherboard
                                        )
                                    )
                                } else {
                                    Log.d("ItemCatalog", "Motherboard ${motherboard.motherboard_name} is NOT compatible with CPU")
                                }
                            }
                            componentAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun onFailure(call: Call<List<Motherboard>>, t: Throwable) {
                        Log.e("APIFailure", "Request failed: ${t.localizedMessage}")
                    }
                })
            }

            // Add other component types if necessary (GPU, RAM, etc.)
        }
    }

    // Compatibility check between selected component's socket types
    private fun isCompatible(componentSocketType: String?, selectedSocketType: String?): Boolean {
        // Returns true if no selection or both sockets match
        Log.d("ItemCatalog", "Comparing socket types: $componentSocketType with $selectedSocketType")
        return selectedSocketType == null || componentSocketType == selectedSocketType
    }
}
