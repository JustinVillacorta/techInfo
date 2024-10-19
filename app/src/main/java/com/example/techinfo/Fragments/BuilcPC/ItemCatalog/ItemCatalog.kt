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

        val componentName = arguments?.getString("componentName") ?: ""
        fetchComponentData(componentName)

        componentAdapter = Adapter(componentDataList) { component: ComponentData, position: Int ->
            // Pass the ComponentData (which already has the type) to ItemsInfo
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
                            response.body()?.forEachIndexed { index, processor ->
                                componentDataList.add(
                                    ComponentData(
                                        name = processor.processor_name,
                                        type = "CPU",
                                        processor = processor
                                    )
                                )
                            }
                            componentAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun onFailure(call: Call<List<Processor>>, t: Throwable) {
                        Log.e("APIFailure", "Request failed: ${t.localizedMessage}")
                    }
                })
            }

            "gpu" -> {
                apiService.getGpus().enqueue(object : Callback<List<Gpu>> {
                    override fun onResponse(call: Call<List<Gpu>>, response: Response<List<Gpu>>) {
                        if (response.isSuccessful) {
                            componentDataList.clear()
                            response.body()?.forEachIndexed { index, gpu ->
                                componentDataList.add(
                                    ComponentData(
                                        name = gpu.gpu_name,
                                        type = "GPU",
                                        gpu = gpu
                                    )
                                )
                            }
                            componentAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun onFailure(call: Call<List<Gpu>>, t: Throwable) {
                        Log.e("APIFailure", "Request failed: ${t.localizedMessage}")
                    }
                })
            }

            "ram" -> {
                apiService.getRams().enqueue(object : Callback<List<Ram>> {
                    override fun onResponse(call: Call<List<Ram>>, response: Response<List<Ram>>) {
                        if (response.isSuccessful) {
                            componentDataList.clear()
                            response.body()?.forEachIndexed { index, ram ->
                                componentDataList.add(
                                    ComponentData(
                                        name = ram.ram_name,
                                        type = "RAM",
                                        ram = ram
                                    )
                                )
                            }
                            componentAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun onFailure(call: Call<List<Ram>>, t: Throwable) {
                        Log.e("APIFailure", "Request failed: ${t.localizedMessage}")
                    }
                })
            }

            "motherboard" -> {
                apiService.getMotherboards().enqueue(object : Callback<List<Motherboard>> {
                    override fun onResponse(call: Call<List<Motherboard>>, response: Response<List<Motherboard>>) {
                        if (response.isSuccessful) {
                            componentDataList.clear()
                            response.body()?.forEachIndexed { index, motherboard ->
                                componentDataList.add(
                                    ComponentData(
                                        name = motherboard.motherboard_name,
                                        type = "Motherboard",
                                        motherboard = motherboard
                                    )
                                )
                            }
                            componentAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun onFailure(call: Call<List<Motherboard>>, t: Throwable) {
                        Log.e("APIFailure", "Request failed: ${t.localizedMessage}")
                    }
                })
            }

            "psu" -> {
                apiService.getPowerSupplyUnits().enqueue(object : Callback<List<PowerSupplyUnit>> {
                    override fun onResponse(call: Call<List<PowerSupplyUnit>>, response: Response<List<PowerSupplyUnit>>) {
                        if (response.isSuccessful) {
                            componentDataList.clear()
                            response.body()?.forEachIndexed { index, psu ->
                                componentDataList.add(
                                    ComponentData(
                                        name = psu.psu_name,
                                        type = "PSU",
                                        psu = psu
                                    )
                                )
                            }
                            componentAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun onFailure(call: Call<List<PowerSupplyUnit>>, t: Throwable) {
                        Log.e("APIFailure", "Request failed: ${t.localizedMessage}")
                    }
                })
            }

            "case" -> {
                apiService.getComputerCases().enqueue(object : Callback<List<Case>> {
                    override fun onResponse(call: Call<List<Case>>, response: Response<List<Case>>) {
                        if (response.isSuccessful) {
                            componentDataList.clear()
                            response.body()?.forEachIndexed { index, case ->
                                componentDataList.add(
                                    ComponentData(
                                        name = case.case_name,
                                        type = "Case",
                                        case = case
                                    )
                                )
                            }
                            componentAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun onFailure(call: Call<List<Case>>, t: Throwable) {
                        Log.e("APIFailure", "Request failed: ${t.localizedMessage}")
                    }
                })
            }

            "cpu cooler" -> {
                apiService.getCpuCoolers().enqueue(object : Callback<List<CpuCooler>> {
                    override fun onResponse(call: Call<List<CpuCooler>>, response: Response<List<CpuCooler>>) {
                        if (response.isSuccessful) {
                            componentDataList.clear()
                            response.body()?.forEachIndexed { index, cpuCooler ->
                                componentDataList.add(
                                    ComponentData(
                                        name = cpuCooler.cooler_name,
                                        type = "CPU Cooler",
                                        cpuCooler = cpuCooler
                                    )
                                )
                            }
                            componentAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun onFailure(call: Call<List<CpuCooler>>, t: Throwable) {
                        Log.e("APIFailure", "Request failed: ${t.localizedMessage}")
                    }
                })
            }

            "hdd" -> {
                apiService.getHdds().enqueue(object : Callback<List<Hdd>> {
                    override fun onResponse(call: Call<List<Hdd>>, response: Response<List<Hdd>>) {
                        if (response.isSuccessful) {
                            componentDataList.clear()
                            response.body()?.forEachIndexed { index, hdd ->
                                componentDataList.add(
                                    ComponentData(
                                        name = hdd.hdd_name,
                                        type = "HDD",
                                        hdd = hdd
                                    )
                                )
                            }
                            componentAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun onFailure(call: Call<List<Hdd>>, t: Throwable) {
                        Log.e("APIFailure", "Request failed: ${t.localizedMessage}")
                    }
                })
            }

            "ssd" -> {
                apiService.getSsds().enqueue(object : Callback<List<Ssd>> {
                    override fun onResponse(call: Call<List<Ssd>>, response: Response<List<Ssd>>) {
                        if (response.isSuccessful) {
                            componentDataList.clear()
                            response.body()?.forEachIndexed { index, ssd ->
                                componentDataList.add(
                                    ComponentData(
                                        name = ssd.ssd_name,
                                        type = "SSD",
                                        ssd = ssd
                                    )
                                )
                            }
                            componentAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun onFailure(call: Call<List<Ssd>>, t: Throwable) {
                        Log.e("APIFailure", "Request failed: ${t.localizedMessage}")
                    }
                })
            }

            // Add other component fetch logic as needed
        }
    }
}
