package com.example.techinfo.Fragments.Bottleneck

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R

class BottleneckAdaptor(
    private val bottleList: List<BottleneckData>
) : RecyclerView.Adapter<BottleneckAdaptor.onViewHolder>() {

    class onViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.hardware_label)
        val imageView: ImageView = itemView.findViewById(R.id.icon)
        val dropdown: AutoCompleteTextView = itemView.findViewById(R.id.dropdown)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): onViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bottleneck_recycler, parent, false)
        return onViewHolder(view)
    }

    override fun onBindViewHolder(holder: onViewHolder, position: Int) {
        val currentItem = bottleList[position]

        holder.textView.text = currentItem.name
        holder.imageView.setImageResource(R.drawable.ic_launcher_foreground) // Placeholder image

        // Set dropdown options based on item type
        val options = when (currentItem.name) {
            "Central Processing Unit" -> arrayOf(
                "Intel Core i9-13900K",
                "AMD Ryzen 9 7950X",
                "Intel Core i7-13700K",
                "AMD Ryzen 7 7800X",
                "Intel Core i5-13600K"
            )
            "Graphics Processing Unit" -> arrayOf(
                "NVIDIA GeForce RTX 4090",
                "AMD Radeon RX 7900 XTX",
                "NVIDIA GeForce RTX 4080",
                "AMD Radeon RX 6800 XT",
                "NVIDIA GeForce RTX 3070"
            )
            "Resolution" -> arrayOf("1080p", "1440p", "4K")
            else -> emptyArray() // Fallback for any other cases
        }

        val arrayAdapter = ArrayAdapter(holder.itemView.context, R.layout.bottleneck_drop_down, options)
        holder.dropdown.setAdapter(arrayAdapter)

        holder.dropdown.setText(currentItem.selectedOption ?: "", false)

        holder.dropdown.setOnItemClickListener { _, _, which, _ ->
            currentItem.selectedOption = arrayAdapter.getItem(which)
        }
    }

    override fun getItemCount(): Int {
        return bottleList.size
    }

    // New method to get selected options
    fun getSelectedOptions(): List<String?> {
        return bottleList.map { it.selectedOption }
    }
}
