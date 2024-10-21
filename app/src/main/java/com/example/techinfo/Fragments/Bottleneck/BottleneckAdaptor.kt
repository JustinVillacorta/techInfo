package com.example.techinfo.Fragments.Bottleneck

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R
import com.example.techinfo.api_connector.ApiService
import com.example.techinfo.api_connector.*
import com.example.techinfo.api_connector.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottleneckAdaptor(
    private val bottleList: List<BottleneckData>
) : RecyclerView.Adapter<BottleneckAdaptor.onViewHolder>() {

    private var processorsList: List<String> = emptyList()
    private var gpusList: List<String> = emptyList()
    private var resolutionsList: List<String> = emptyList()

    init {
        fetchProcessors()
        fetchGpus()
        fetchResolutions()
    }

    private fun fetchProcessors() {
        RetrofitInstance.getApiService().getProcessors().enqueue(object : Callback<List<Processor>> {
            override fun onResponse(call: Call<List<Processor>>, response: Response<List<Processor>>) {
                if (response.isSuccessful) {
                    processorsList = response.body()?.map { it.processor_name } ?: emptyList()
                    notifyDataSetChanged()  // Notify adapter to update when the data is received
                }
            }

            override fun onFailure(call: Call<List<Processor>>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun fetchGpus() {
        RetrofitInstance.getApiService().getGpus().enqueue(object : Callback<List<Gpu>> {
            override fun onResponse(call: Call<List<Gpu>>, response: Response<List<Gpu>>) {
                if (response.isSuccessful) {
                    gpusList = response.body()?.map { it.gpu_name } ?: emptyList()
                    notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Gpu>>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun fetchResolutions() {
        RetrofitInstance.getApiService().getResolution().enqueue(object : Callback<List<Resolution>> {
            override fun onResponse(call: Call<List<Resolution>>, response: Response<List<Resolution>>) {
                if (response.isSuccessful) {
                    resolutionsList = response.body()?.map { it.resolutions_name } ?: emptyList()
                    notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Resolution>>, t: Throwable) {
                // Handle failure
            }
        })
    }

    class onViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.hardware_label)
        val imageView: ImageView = itemView.findViewById(R.id.icon)
        val dropdown: AutoCompleteTextView = itemView.findViewById(R.id.dropdown)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): onViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bottleneck_recycler, parent, false)
        return onViewHolder(view)
    }

    override fun onBindViewHolder(holder: onViewHolder, position: Int) {
        val currentItem = bottleList[position]

        holder.textView.text = currentItem.name
        holder.imageView.setImageResource(R.drawable.ic_launcher_foreground) // Placeholder image

        val options = when (currentItem.name) {
            "Central Processing Unit" -> processorsList
            "Graphics Processing Unit" -> gpusList
            "Resolution" -> resolutionsList
            else -> emptyList()
        }

        val arrayAdapter = ArrayAdapter(holder.itemView.context, R.layout.bottleneck_drop_down, options)
        holder.dropdown.setAdapter(arrayAdapter)

        holder.dropdown.setText(currentItem.selectedOption ?: "", false)

        holder.dropdown.setOnItemClickListener { _, _, which, _ ->
            currentItem.selectedOption = arrayAdapter.getItem(which)
        }
    }

    override fun getItemCount(): Int {
        return bottleList.size
    }

    // New method to get selected options
    fun getSelectedOptions(): List<String?> {
        return bottleList.map { it.selectedOption }
    }
}
