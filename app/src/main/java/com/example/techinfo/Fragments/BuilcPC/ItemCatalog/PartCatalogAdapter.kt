package com.example.techinfo.Fragments.BuildPC.ItemCatalog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.Fragments.BuilcPC.ComponentData
import com.example.techinfo.Fragments.BuilcPC.ItemCatalog.ItemsInfo
import com.example.techinfo.R

// Adapter for displaying parts in a catalog
class PartCatalogAdapter(
    private var items: List<ComponentData>,
    private val itemClickListener: (ComponentData, Int) -> Unit,
    private val fragment: Fragment // Pass Fragment instance
) : RecyclerView.Adapter<PartCatalogAdapter.PartViewHolder>() {

    inner class PartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val partSpecsTextView: TextView = view.findViewById(R.id.tvPartSpecs)
        val partImageView: ImageView = view.findViewById(R.id.image)
        val infoButton: Button = view.findViewById(R.id.btnSelect)

        fun bind(component: ComponentData, position: Int) {
            partSpecsTextView.text = component.partDetails?.ifEmpty { "Select a part" } ?: "Select a part"
            partImageView.setImageResource(R.drawable.ic_launcher_foreground)

            infoButton.setOnClickListener {
                val infoFragment = ItemsInfo.newInstance(
                    articleTitle = component.partDetails ?: "No details available",
                    content = "Detailed information about ${component.partDetails}.",
                    createdTime = "Created on: 2023-01-01",
                    updatedTime = "Updated on: 2023-10-01"
                )
                fragment.parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, infoFragment)
                    .addToBackStack(null)
                    .commit()
            }

            itemView.setOnClickListener {
                itemClickListener(component, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_part_catalog, parent, false)
        return PartViewHolder(view)
    }

    override fun onBindViewHolder(holder: PartViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount() = items.size
}
