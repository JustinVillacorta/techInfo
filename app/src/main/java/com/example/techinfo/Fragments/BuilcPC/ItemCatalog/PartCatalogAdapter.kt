package com.example.techinfo.Fragments.BuilcPC.ItemCatalog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R

class PartCatalogAdapter(
    private val items: List<Parts>,
    private val itemClickListener: (Parts) -> Unit
) : RecyclerView.Adapter<PartCatalogAdapter.PartViewHolder>() {

    inner class PartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val partNameTextView: TextView = view.findViewById(R.id.tvPartName)

        fun bind(part: Parts) {
            partNameTextView.text = part.name
            itemView.setOnClickListener { itemClickListener(part) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_part_catalog, parent, false) // Ensure this layout exists
        return PartViewHolder(view)
    }

    override fun onBindViewHolder(holder: PartViewHolder, position: Int) {
        holder.bind(items[position])

    }




    override fun getItemCount() = items.size
}


