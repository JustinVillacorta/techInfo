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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
    private val allCpuDataList = mutableListOf<ComponentData>()
    private val allMotherboardDataList = mutableListOf<ComponentData>()
    private val filteredDataList = mutableListOf<ComponentData>()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var searchView: SearchView
    private val apiService = RetrofitInstance.getApiService()
    private var componentName: String = ""

    companion object {
        fun newInstance(
            componentName: String,
            selectedMotherboard: Motherboard? = null,
            selectedProcessor: Processor? = null
        ): ItemCatalog {
            val fragment = ItemCatalog()
            val bundle = Bundle()
            bundle.putString("componentName", componentName)
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

        componentName = arguments?.getString("componentName") ?: ""
        Log.d("ItemCatalog", "Component Name: $componentName")  // Log the component name

        fetchAllComponentData(componentName)

        componentAdapter = Adapter(filteredDataList) { component: ComponentData, position: Int ->
            val bundle = Bundle().apply {
                putSerializable("selectedComponent", component)
                putString("type", component.type)  // Pass the type (CPU, GPU, etc.)
            }

            // Send the selected component back to the BuildPC fragment
            parentFragmentManager.setFragmentResult("selectedComponent", bundle)

            // Optionally, you can also navigate to a detailed ItemsInfo screen if needed
            val itemInfoFragment = ItemsInfo.newInstance(component, position)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, itemInfoFragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = componentAdapter

        setupSearchView()

        swipeRefreshLayout.setOnRefreshListener {
            Log.d("ItemCatalog", "Refreshing data...")  // Log refresh action
            fetchAllComponentData(componentName)
        }
    }

    private fun fetchAllComponentData(componentName: String) {
        Log.d("ItemCatalog", "Fetching data for: $componentName")  // Log data fetching
        swipeRefreshLayout.isRefreshing = true
        when (componentName.lowercase()) {
            "cpu" -> {
                apiService.getProcessors().enqueue(object : Callback<List<Processor>> {
                    override fun onResponse(call: Call<List<Processor>>, response: Response<List<Processor>>) {
                        if (response.isSuccessful) {
                            allCpuDataList.clear()
                            response.body()?.forEach { processor ->
                                Log.d("ItemCatalog", "Processor found: ${processor.processor_name}, Socket Type: ${processor.socket_type}")
                                allCpuDataList.add(
                                    ComponentData(
                                        name = processor.processor_name,
                                        type = "CPU",
                                        processor = processor
                                    )
                                )
                            }
                            updateFilteredDataList() // Update filtered data after fetching
                            Log.d("ItemCatalog", "Total CPUs fetched: ${allCpuDataList.size}")  // Log total CPUs fetched
                        } else {
                            Log.e("ItemCatalog", "Error fetching CPUs: ${response.errorBody()?.string()}")
                        }
                        swipeRefreshLayout.isRefreshing = false
                    }

                    override fun onFailure(call: Call<List<Processor>>, t: Throwable) {
                        Log.e("ItemCatalog", "Request failed: ${t.localizedMessage}")
                        swipeRefreshLayout.isRefreshing = false
                    }
                })
            }
            "motherboard" -> {
                apiService.getMotherboards().enqueue(object : Callback<List<Motherboard>> {
                    override fun onResponse(call: Call<List<Motherboard>>, response: Response<List<Motherboard>>) {
                        if (response.isSuccessful) {
                            allMotherboardDataList.clear()
                            response.body()?.forEach { motherboard ->
                                Log.d("ItemCatalog", "Motherboard found: ${motherboard.motherboard_name}, Socket Type: ${motherboard.socket_type}")
                                allMotherboardDataList.add(
                                    ComponentData(
                                        name = motherboard.motherboard_name,
                                        type = "Motherboard",
                                        motherboard = motherboard
                                    )
                                )
                            }
                            updateFilteredDataList() // Update filtered data after fetching
                            Log.d("ItemCatalog", "Total Motherboards fetched: ${allMotherboardDataList.size}")  // Log total Motherboards fetched
                        } else {
                            Log.e("ItemCatalog", "Error fetching Motherboards: ${response.errorBody()?.string()}")
                        }
                        swipeRefreshLayout.isRefreshing = false
                    }

                    override fun onFailure(call: Call<List<Motherboard>>, t: Throwable) {
                        Log.e("ItemCatalog", "Request failed: ${t.localizedMessage}")
                        swipeRefreshLayout.isRefreshing = false
                    }
                })
            }
        }
    }

    private fun updateFilteredDataList() {
        filteredDataList.clear()
        // Always show all items, not filtered by selected components
        if (componentName.lowercase() == "cpu") {
            filteredDataList.addAll(allCpuDataList)
            Log.d("ItemCatalog", "Filtered CPU list updated, count: ${filteredDataList.size}")  // Log updated count of CPUs
        } else if (componentName.lowercase() == "motherboard") {
            filteredDataList.addAll(allMotherboardDataList)
            Log.d("ItemCatalog", "Filtered Motherboard list updated, count: ${filteredDataList.size}")  // Log updated count of Motherboards
        }
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
            Log.d("ItemCatalog", "Query is empty, showing all components")  // Log empty query
            updateFilteredDataList() // Show all components if the query is empty
        } else {
            val lowerCaseQuery = query.lowercase()
            filteredDataList.addAll(
                allCpuDataList.filter { it.name.lowercase().contains(lowerCaseQuery) } +
                        allMotherboardDataList.filter { it.name.lowercase().contains(lowerCaseQuery) }
            )
            Log.d("ItemCatalog", "Filtered list based on query '$query', count: ${filteredDataList.size}")  // Log count of filtered items
        }
        componentAdapter.notifyDataSetChanged()
    }
}
