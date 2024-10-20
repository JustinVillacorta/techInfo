package com.example.techinfo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout // Add this import
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
    private val filteredDataList = mutableListOf<ComponentData>() // New list for filtered data
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var searchView: SearchView
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

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        searchView = view.findViewById(R.id.searchView)

        // Get selected motherboard and processor (if provided)
        selectedMotherboard = arguments?.getSerializable("selectedMotherboard") as? Motherboard
        selectedProcessor = arguments?.getSerializable("selectedProcessor") as? Processor

        // Log the selected components for debugging
        Log.d("ItemCatalog", "Selected Motherboard: $selectedMotherboard")
        Log.d("ItemCatalog", "Selected Processor: $selectedProcessor")

        val componentName = arguments?.getString("componentName") ?: ""
        fetchComponentData(componentName)

        componentAdapter = Adapter(filteredDataList) { component: ComponentData, position: Int ->
            val itemInfoFragment = ItemsInfo.newInstance(component, position)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, itemInfoFragment)
                .addToBackStack(null)
                .commit()
        }

        recyclerView.adapter = componentAdapter

        // Setup the search functionality
        setupSearchView()

        // Setup the swipe refresh functionality
        swipeRefreshLayout.setOnRefreshListener {
            fetchComponentData(componentName)
        }
    }

    private fun fetchComponentData(componentName: String) {
        swipeRefreshLayout.isRefreshing = true // Show refresh indicator
        when (componentName.lowercase()) {
            "cpu" -> {
                apiService.getProcessors().enqueue(object : Callback<List<Processor>> {
                    override fun onResponse(call: Call<List<Processor>>, response: Response<List<Processor>>) {
                        if (response.isSuccessful) {
                            componentDataList.clear()
                            response.body()?.forEach { processor ->
                                Log.d("ItemCatalog", "Processor found: ${processor.processor_name}, Socket Type: ${processor.socket_type}")

                                if (selectedMotherboard == null || isCompatible(processor.socket_type, selectedMotherboard?.socket_type)) {
                                    componentDataList.add(
                                        ComponentData(
                                            name = processor.processor_name,
                                            type = "CPU",
                                            processor = processor
                                        )
                                    )
                                }
                            }
                            updateFilteredDataList()
                        }
                        swipeRefreshLayout.isRefreshing = false // Hide refresh indicator
                    }

                    override fun onFailure(call: Call<List<Processor>>, t: Throwable) {
                        Log.e("APIFailure", "Request failed: ${t.localizedMessage}")
                        swipeRefreshLayout.isRefreshing = false // Hide refresh indicator
                    }
                })
            }

            "motherboard" -> {
                apiService.getMotherboards().enqueue(object : Callback<List<Motherboard>> {
                    override fun onResponse(call: Call<List<Motherboard>>, response: Response<List<Motherboard>>) {
                        if (response.isSuccessful) {
                            componentDataList.clear()
                            response.body()?.forEach { motherboard ->
                                Log.d("ItemCatalog", "Motherboard found: ${motherboard.motherboard_name}, Socket Type: ${motherboard.socket_type}")

                                if (selectedProcessor == null || isCompatible(motherboard.socket_type, selectedProcessor?.socket_type)) {
                                    componentDataList.add(
                                        ComponentData(
                                            name = motherboard.motherboard_name,
                                            type = "Motherboard",
                                            motherboard = motherboard
                                        )
                                    )
                                }
                            }
                            updateFilteredDataList()
                        }
                        swipeRefreshLayout.isRefreshing = false // Hide refresh indicator
                    }

                    override fun onFailure(call: Call<List<Motherboard>>, t: Throwable) {
                        Log.e("APIFailure", "Request failed: ${t.localizedMessage}")
                        swipeRefreshLayout.isRefreshing = false // Hide refresh indicator
                    }
                })
            }

            // Add other component types if necessary (GPU, RAM, etc.)
        }
    }

    private fun updateFilteredDataList() {
        filteredDataList.clear()
        filteredDataList.addAll(componentDataList) // Reset filtered list to show all components
        componentAdapter.notifyDataSetChanged()
    }


    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterComponents(newText)
                return true
            }
        })
    }

    private fun filterComponents(query: String?) {
        filteredDataList.clear()
        if (query.isNullOrEmpty()) {
            filteredDataList.addAll(componentDataList) // If query is empty, show all components
        } else {
            val lowerCaseQuery = query.lowercase()
            componentDataList.forEach { component ->
                if (component.name.lowercase().contains(lowerCaseQuery)) {
                    filteredDataList.add(component)
                }
            }
        }
        componentAdapter.notifyDataSetChanged()
    }

    // Compatibility check between selected component's socket types
    private fun isCompatible(componentSocketType: String?, selectedSocketType: String?): Boolean {
        // Returns true if no selection or both sockets match
        Log.d("ItemCatalog", "Comparing socket types: $componentSocketType with $selectedSocketType")
        return selectedSocketType == null || componentSocketType == selectedSocketType
    }
}
