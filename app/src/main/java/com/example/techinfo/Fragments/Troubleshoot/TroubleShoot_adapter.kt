package com.example.techinfo.Fragments.Troubleshoot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R

class TroubleShoot_adapter(
    private val itemList: List<TroubleShoot_data>,
    private val onItemClick: (TroubleShoot_data) -> Unit
) : RecyclerView.Adapter<TroubleShoot_adapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title) // Only TextView now
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.troubleshoot_recycler, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.titleTextView.text = item.title // Set title in TextView

        holder.itemView.setOnClickListener {
            onItemClick(item) // Handle click event
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
