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

            // Add other components like RAM, SSD, etc. using similar approach
        }
    }
}
