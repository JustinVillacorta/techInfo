package com.example.techinfo.Fragments.BuildPC.ItemCatalog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R

data class Parts(val name: String, val position: Int, var isSelected: Boolean = false, val details: String = "")

class PartCatalogAdapter(
    private val items: List<Parts>,
    private val onPartSelected: (Parts) -> Unit
) : RecyclerView.Adapter<PartCatalogAdapter.PartViewHolder>() {

    inner class PartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val partName: TextView = itemView.findViewById(R.id.tvPartSpecs) // Use the correct ID from your XML

        init {
            itemView.setOnClickListener {
                val part = items[adapterPosition]
                onPartSelected(part)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_part_catalog, parent, false)
        return PartViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PartViewHolder, position: Int) {
        val part = items[position]
        holder.partName.text = part.name
    }

    override fun getItemCount() = items.size
}
