package com.example.techinfo.Fragments.BuilcPCmodules.ItemCatalog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R

class PartCatalogAdapter(private val items: List<Parts>) :
    RecyclerView.Adapter<PartCatalogAdapter.PartViewHolder>() {

    class PartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val partName: TextView = view.findViewById(R.id.tvPartName)
        val partSpecs: TextView = view.findViewById(R.id.tvPartSpecs)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_part_catalog, parent, false)
        return PartViewHolder(view)
    }

    override fun onBindViewHolder(holder: PartViewHolder, position: Int) {
        val part = items[position]
        holder.partName.text = part.name
        holder.partSpecs.text = part.specs
    }

    override fun getItemCount() = items.size
}

