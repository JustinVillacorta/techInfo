package com.example.techinfo.Fragments.BuilcPC.ItemCatalog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R

class PartCatalogAdapter(
    private var items: List<Parts>,
    private val itemClickListener: (Parts) -> Unit,
    private val fragment: Fragment // Pass Fragment instance
) : RecyclerView.Adapter<PartCatalogAdapter.PartViewHolder>() {

    inner class PartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val partSpecsTextView: TextView = view.findViewById(R.id.tvPartSpecs)
        val partImageView: ImageView = view.findViewById(R.id.image)
        val infoButton: Button = view.findViewById(R.id.btnSelect)

        fun bind(part: Parts) {
            partSpecsTextView.text = part.details ?: "Select a part"
            partImageView.setImageResource(R.drawable.ic_launcher_foreground)

            // Handle button click for showing part info
            infoButton.setOnClickListener {
                // Create an instance of ItemsInfo with the required parameters
                val infoFragment = ItemsInfo.newInstance(
                    articleTitle = part.details ?: "No details available", // Set your title here
                    content = "Detailed information about ${part.details}.", // Example content
                    createdTime = "Created on: 2023-01-01", // Example created time
                    updatedTime = "Updated on: 2023-10-01" // Example updated time
                )
                fragment.parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, infoFragment)
                    .addToBackStack(null)
                    .commit()
            }

            // Make the entire item clickable except for the infoButton
            itemView.setOnClickListener {
                itemClickListener(part) // Handle item selection
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
