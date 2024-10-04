package com.example.techinfo.Fragments.BuilcPC.ItemCatalog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R

class PartCatalogAdapter(
    private var items: List<Parts>,
    private val itemClickListener: (Parts) -> Unit
) : RecyclerView.Adapter<PartCatalogAdapter.PartViewHolder>() {

    inner class PartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val partSpecsTextView: TextView = view.findViewById(R.id.tvPartSpecs)
        val partImageView: ImageView = view.findViewById(R.id.image)
        val infoButton: Button = view.findViewById(R.id.btnSelect)

        fun bind(part: Parts) {
            partSpecsTextView.text = part.details ?: "Select a part"
            partImageView.setImageResource(R.drawable.ic_launcher_foreground)

            // Handle button click for selecting the part
            infoButton.setOnClickListener {
                // Call the itemClickListener for the button click

            }

            // Make the entire item clickable except for the infoButton
            itemView.setOnClickListener {
                // Handle item click here if needed, you can also pass `part` to a function
                // For example, you might want to navigate to a details view
                itemClickListener(part) // You can also use this for item selection
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_part_catalog, parent, false)
        return PartViewHolder(view)
    }

    override fun onBindViewHolder(holder: PartViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
