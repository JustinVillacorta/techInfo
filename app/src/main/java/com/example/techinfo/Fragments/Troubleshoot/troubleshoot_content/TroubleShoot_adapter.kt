package com.example.techinfo.Fragments.Troubleshoot.troubleshoot_content

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R
import com.example.techinfo.api_connector.TroubleShoot_data

class TroubleShoot_adapter(
    private var itemList: MutableList<TroubleShoot_data>,  // MutableList to support changes
    private val onItemClick: (TroubleShoot_data) -> Unit
) : RecyclerView.Adapter<TroubleShoot_adapter.ItemViewHolder>() {

    // Copy of the original list for search functionality
    private var originalItemList: MutableList<TroubleShoot_data> = ArrayList(itemList)

    // ViewHolder class to bind the views
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.troubleshoot_recycler, parent, false)
        return ItemViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.titleTextView.text = item.title

        // Handle item clicks
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return itemList.size
    }

    // Function to filter the list based on the search query
    fun filterList(filteredItems: MutableList<TroubleShoot_data>) {
        itemList = filteredItems
        notifyDataSetChanged()  // Notify the adapter about the data changes
    }

    // Function to reset the list to the original data
    fun resetList() {
        itemList = originalItemList
        notifyDataSetChanged()
    }
}
