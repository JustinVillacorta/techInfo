package com.example.techinfo.Fragments.PcComparisonModules

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R

class PCBuildComparisonAdapter(private val metricsList: List<PcbuildComparisonData>) : RecyclerView.Adapter<PCBuildComparisonAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val metricName: TextView = itemView.findViewById(R.id.metricName)
        val progressLeft: ProgressBar = itemView.findViewById(R.id.progressLeft)
        val percentageLeft: TextView = itemView.findViewById(R.id.percentageLeft)
        val progressRight: ProgressBar = itemView.findViewById(R.id.progressRight)
        val percentageRight: TextView = itemView.findViewById(R.id.percentageRight)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pc_comparison_progressbars_items, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val metric = metricsList[position]

        holder.metricName.text = metric.metricName
        holder.progressLeft.progress = metric.percentageLeft
        holder.percentageLeft.text = "${metric.percentageLeft}%"
        holder.progressRight.progress = metric.percentageRight
        holder.percentageRight.text = "${metric.percentageRight}%"
    }

    override fun getItemCount(): Int {
        return metricsList.size
    }
}
