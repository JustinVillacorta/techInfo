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
    private val allGpuDataList = mutableListOf<ComponentData>()
    private val allMotherboardDataList = mutableListOf<ComponentData>()
    private val allRamDataList = mutableListOf<ComponentData>()
    private val allPsuDataList = mutableListOf<ComponentData>()
    private val allCaseDataList = mutableListOf<ComponentData>()
    private val allCpuCoolerDataList = mutableListOf<ComponentData>()
    private val allHddDataList = mutableListOf<ComponentData>()
    private val allSsdDataList = mutableListOf<ComponentData>()
    private val filteredDataList = mutableListOf<ComponentData>()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var searchView: SearchView
    private val apiService = RetrofitInstance.getApiService()
    private var componentName: String = ""

    companion object {
        fun newInstance(componentName: String): ItemCatalog {
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

        componentAdapter = Adapter(filteredDataList, { component: ComponentData, position: Int ->
            // Your click handling code here
            val bundle = Bundle().apply {
                putSerializable("selectedComponent", component)
                putString("type", component.type) // Pass the type (CPU, GPU, etc.)
            }

            // Send the selected component back to the BuildPC fragment
            parentFragmentManager.setFragmentResult("selectedComponent", bundle)

            // Optionally, navigate to detailed ItemsInfo screen
            val itemInfoFragment = ItemsInfo.newInstance(component, position)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, itemInfoFragment)
                .addToBackStack(null)
                .commit()
        }, true) // Ensure you pass the correct value for isInBuildPCFragment
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
                                allCpuDataList.add(
                                    ComponentData(
                                        name = processor.processor_name,
                                        type = "CPU",
                                        processor = processor
                                    )
                                )
                            }
                            updateFilteredDataList("cpu")
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

            "gpu" -> {
                apiService.getGpus().enqueue(object : Callback<List<Gpu>> {
                    override fun onResponse(call: Call<List<Gpu>>, response: Response<List<Gpu>>) {
                        if (response.isSuccessful) {
                            allGpuDataList.clear()
                            response.body()?.forEach { gpu ->
                                allGpuDataList.add(
                                    ComponentData(
                                        name = gpu.gpu_name,
                                        type = "GPU",
                                        gpu = gpu
                                    )
                                )
                            }
                            updateFilteredDataList("gpu")
                        } else {
                            Log.e("ItemCatalog", "Error fetching GPUs: ${response.errorBody()?.string()}")
                        }
                        swipeRefreshLayout.isRefreshing = false
                    }

                    override fun onFailure(call: Call<List<Gpu>>, t: Throwable) {
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
                                allMotherboardDataList.add(
                                    ComponentData(
                                        name = motherboard.motherboard_name,
                                        type = "Motherboard",
                                        motherboard = motherboard
                                    )
                                )
                            }
                            updateFilteredDataList("motherboard")
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

            "ram" -> {
                apiService.getRams().enqueue(object : Callback<List<Ram>> {
                    override fun onResponse(call: Call<List<Ram>>, response: Response<List<Ram>>) {
                        if (response.isSuccessful) {
                            allRamDataList.clear()
                            response.body()?.forEach { ram ->
                                allRamDataList.add(
                                    ComponentData(
                                        name = ram.ram_name,
                                        type = "RAM",
                                        ram = ram
                                    )
                                )
                            }
                            updateFilteredDataList("ram")
                        } else {
                            Log.e("ItemCatalog", "Error fetching RAM: ${response.errorBody()?.string()}")
                        }
                        swipeRefreshLayout.isRefreshing = false
                    }

                    override fun onFailure(call: Call<List<Ram>>, t: Throwable) {
                        Log.e("ItemCatalog", "Request failed: ${t.localizedMessage}")
                        swipeRefreshLayout.isRefreshing = false
                    }
                })
            }

            "psu" -> {
                apiService.getPowerSupplyUnits().enqueue(object : Callback<List<PowerSupplyUnit>> {
                    override fun onResponse(call: Call<List<PowerSupplyUnit>>, response: Response<List<PowerSupplyUnit>>) {
                        if (response.isSuccessful) {
                            allPsuDataList.clear()
                            response.body()?.forEach { psu ->
                                allPsuDataList.add(
                                    ComponentData(
                                        name = psu.psu_name,
                                        type = "PSU",
                                        psu = psu
                                    )
                                )
                            }
                            updateFilteredDataList("psu")
                        } else {
                            Log.e("ItemCatalog", "Error fetching PSU: ${response.errorBody()?.string()}")
                        }
                        swipeRefreshLayout.isRefreshing = false
                    }

                    override fun onFailure(call: Call<List<PowerSupplyUnit>>, t: Throwable) {
                        Log.e("ItemCatalog", "Request failed: ${t.localizedMessage}")
                        swipeRefreshLayout.isRefreshing = false
                    }
                })
            }

            "case" -> {
                apiService.getComputerCases().enqueue(object : Callback<List<Case>> {
                    override fun onResponse(call: Call<List<Case>>, response: Response<List<Case>>) {
                        if (response.isSuccessful) {
                            allCaseDataList.clear()
                            response.body()?.forEach { case ->
                                allCaseDataList.add(
                                    ComponentData(
                                        name = case.case_name,
                                        type = "Case",
                                        case = case
                                    )
                                )
                            }
                            updateFilteredDataList("case")
                        } else {
                            Log.e("ItemCatalog", "Error fetching Cases: ${response.errorBody()?.string()}")
                        }
                        swipeRefreshLayout.isRefreshing = false
                    }

                    override fun onFailure(call: Call<List<Case>>, t: Throwable) {
                        Log.e("ItemCatalog", "Request failed: ${t.localizedMessage}")
                        swipeRefreshLayout.isRefreshing = false
                    }
                })
            }

            "cpu cooler" -> {
                apiService.getCpuCoolers().enqueue(object : Callback<List<CpuCooler>> {
                    override fun onResponse(call: Call<List<CpuCooler>>, response: Response<List<CpuCooler>>) {
                        if (response.isSuccessful) {
                            allCpuCoolerDataList.clear()
                            response.body()?.forEach { cooler ->
                                allCpuCoolerDataList.add(
                                    ComponentData(
                                        name = cooler.cooler_name,
                                        type = "CPU Cooler",
                                        cpuCooler = cooler
                                    )
                                )
                            }
                            updateFilteredDataList("cpu cooler")
                        } else {
                            Log.e("ItemCatalog", "Error fetching CPU Coolers: ${response.errorBody()?.string()}")
                        }
                        swipeRefreshLayout.isRefreshing = false
                    }

                    override fun onFailure(call: Call<List<CpuCooler>>, t: Throwable) {
                        Log.e("ItemCatalog", "Request failed: ${t.localizedMessage}")
                        swipeRefreshLayout.isRefreshing = false
                    }
                })
            }

            "ssd" -> {
                apiService.getSsds().enqueue(object : Callback<List<Ssd>> {
                    override fun onResponse(call: Call<List<Ssd>>, response: Response<List<Ssd>>) {
                        if (response.isSuccessful) {
                            allSsdDataList.clear()
                            response.body()?.forEach { ssd ->
                                allSsdDataList.add(
                                    ComponentData(
                                        name = ssd.ssd_name,
                                        type = "SSD",
                                        ssd = ssd
                                    )
                                )
                            }
                            updateFilteredDataList("ssd")
                        } else {
                            Log.e("ItemCatalog", "Error fetching SSDs: ${response.errorBody()?.string()}")
                        }
                        swipeRefreshLayout.isRefreshing = false
                    }

                    override fun onFailure(call: Call<List<Ssd>>, t: Throwable) {
                        Log.e("ItemCatalog", "Request failed: ${t.localizedMessage}")
                        swipeRefreshLayout.isRefreshing = false
                    }
                })
            }

            "hdd" -> {
                apiService.getHdds().enqueue(object : Callback<List<Hdd>> {
                    override fun onResponse(call: Call<List<Hdd>>, response: Response<List<Hdd>>) {
                        if (response.isSuccessful) {
                            allHddDataList.clear()
                            response.body()?.forEach { hdd ->
                                allHddDataList.add(
                                    ComponentData(
                                        name = hdd.hdd_name,
                                        type = "HDD",
                                        hdd = hdd
                                    )
                                )
                            }
                            updateFilteredDataList("hdd")
                        } else {
                            Log.e("ItemCatalog", "Error fetching HDDs: ${response.errorBody()?.string()}")
                        }
                        swipeRefreshLayout.isRefreshing = false
                    }

                    override fun onFailure(call: Call<List<Hdd>>, t: Throwable) {
                        Log.e("ItemCatalog", "Request failed: ${t.localizedMessage}")
                        swipeRefreshLayout.isRefreshing = false
                    }
                })
            }

            else -> {
                Log.e("ItemCatalog", "Unknown component name: $componentName")
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun updateFilteredDataList(componentType: String) {
        filteredDataList.clear()

        when (componentType) {
            "cpu" -> filteredDataList.addAll(allCpuDataList)
            "gpu" -> filteredDataList.addAll(allGpuDataList)
            "motherboard" -> filteredDataList.addAll(allMotherboardDataList)
            "ram" -> filteredDataList.addAll(allRamDataList)
            "psu" -> filteredDataList.addAll(allPsuDataList)
            "case" -> filteredDataList.addAll(allCaseDataList)
            "cpu cooler" -> filteredDataList.addAll(allCpuCoolerDataList)
            "ssd" -> filteredDataList.addAll(allSsdDataList)
            "hdd" -> filteredDataList.addAll(allHddDataList)
        }
        componentAdapter.notifyDataSetChanged()
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterData(newText)
                return true
            }
        })
    }

    private fun filterData(query: String?) {
        filteredDataList.clear()
        val searchText = query?.lowercase()?.trim() ?: ""
        if (searchText.isEmpty()) {
            updateFilteredDataList(componentName.lowercase())
        } else {
            val originalList = when (componentName.lowercase()) {
                "cpu" -> allCpuDataList
                "gpu" -> allGpuDataList
                "motherboard" -> allMotherboardDataList
                "ram" -> allRamDataList
                "psu" -> allPsuDataList
                "case" -> allCaseDataList
                "cpu cooler" -> allCpuCoolerDataList
                "ssd" -> allSsdDataList
                "hdd" -> allHddDataList
                else -> emptyList()
            }
            originalList.filterTo(filteredDataList) { it.name.lowercase().contains(searchText) }
        }
        componentAdapter.notifyDataSetChanged()
    }
}
