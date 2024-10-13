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
import com.example.techinfo.Fragments.BuilcPC.ComponentData
import com.example.techinfo.api_connector.RetrofitInstance
import com.example.techinfo.Fragments.BuildPCmodules.Adapter
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

        componentAdapter = Adapter(componentDataList) { component, position ->
            val resultBundle = Bundle()
            resultBundle.putString("partDetails", component.partDetails)
            resultBundle.putInt("position", position)
            resultBundle.putInt("progress", 100)

            parentFragmentManager.setFragmentResult("selectedPart", resultBundle)
            parentFragmentManager.popBackStack()
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
                                componentDataList.add(ComponentData(processor.processor_name))
                            }
                            componentAdapter.notifyDataSetChanged()
                        } else {
                            Log.e("APIError", "Error response: ${response.code()}")
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
                            response.body()?.forEach { gpu ->
                                componentDataList.add(ComponentData(gpu.gpu_name))
                            }
                            componentAdapter.notifyDataSetChanged()
                        } else {
                            Log.e("APIError", "Error response: ${response.code()}")
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
                            response.body()?.forEach { ram ->
                                componentDataList.add(ComponentData(ram.ram_name))
                            }
                            componentAdapter.notifyDataSetChanged()
                        } else {
                            Log.e("APIError", "Error response: ${response.code()}")
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
                            response.body()?.forEach { motherboard ->
                                componentDataList.add(ComponentData(motherboard.motherboard_name))
                            }
                            componentAdapter.notifyDataSetChanged()
                        } else {
                            Log.e("APIError", "Error response: ${response.code()}")
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
                            response.body()?.forEach { psu ->
                                componentDataList.add(ComponentData(psu.psu_name))
                            }
                            componentAdapter.notifyDataSetChanged()
                        } else {
                            Log.e("APIError", "Error response: ${response.code()}")
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
                            response.body()?.forEach { computerCase ->
                                componentDataList.add(ComponentData(computerCase.case_name))
                            }
                            componentAdapter.notifyDataSetChanged()
                        } else {
                            Log.e("APIError", "Error response: ${response.code()}")
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
                            response.body()?.forEach { cooler ->
                                componentDataList.add(ComponentData(cooler.cooler_name))
                            }
                            componentAdapter.notifyDataSetChanged()
                        } else {
                            Log.e("APIError", "Error response: ${response.code()}")
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
                            response.body()?.forEach { hdd ->
                                componentDataList.add(ComponentData(hdd.hdd_name))
                            }
                            componentAdapter.notifyDataSetChanged()
                        } else {
                            Log.e("APIError", "Error response: ${response.code()}")
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
                            response.body()?.forEach { ssd ->
                                componentDataList.add(ComponentData(ssd.ssd_name))
                            }
                            componentAdapter.notifyDataSetChanged()
                        } else {
                            Log.e("APIError", "Error response: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<List<Ssd>>, t: Throwable) {
                        Log.e("APIFailure", "Request failed: ${t.localizedMessage}")
                    }
                })
            }

            else -> {
                Log.e("APIError", "Invalid component type: $componentName")
            }
        }
    }
}