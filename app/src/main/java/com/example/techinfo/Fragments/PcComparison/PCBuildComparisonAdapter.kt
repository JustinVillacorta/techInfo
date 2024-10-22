package com.example.techinfo.Fragments.PcComparison

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R

class PCBuildComparisonAdapter(private var metricsList: List<PcbuildComparisonData>) : RecyclerView.Adapter<PCBuildComparisonAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val metricName: TextView = itemView.findViewById(R.id.metricName)
        val progressLeft: ProgressBar = itemView.findViewById(R.id.progressLeft)
        val progressRight: ProgressBar = itemView.findViewById(R.id.progressRight)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pc_comparison_progressbars_items, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val metric = metricsList[position]

        holder.metricName.text = metric.metricName
        holder.progressLeft.progress = metric.percentageLeft.toInt()
        holder.progressRight.progress = metric.percentageRight.toInt()
    }

    override fun getItemCount(): Int {
        return metricsList.size
    }

    // Method to update the metrics and refresh the RecyclerView
    fun updateMetrics(newMetricsList: List<PcbuildComparisonData>) {
        metricsList = newMetricsList
        notifyDataSetChanged() // Notify the adapter to refresh the view
    }
}
