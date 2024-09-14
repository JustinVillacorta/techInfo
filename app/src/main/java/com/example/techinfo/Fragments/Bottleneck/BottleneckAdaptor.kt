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
        val textView: TextView = itemView.findViewById(R.id.hardware_label) // Corrected ID
        val imageView: ImageView = itemView.findViewById(R.id.icon) // Corrected ID
        val dropdown: AutoCompleteTextView = itemView.findViewById(R.id.dropdown) // Corrected ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): onViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bottleneck_recycler, parent, false)
        return onViewHolder(view)
    }

    override fun onBindViewHolder(holder: onViewHolder, position: Int) {
        val currentItem = bottleList[position]

        // Set text and image for the current item
        holder.textView.text = currentItem.name
        holder.imageView.setImageResource(R.drawable.ic_launcher_foreground) // Placeholder image

        // Dropdown adapter (AutoCompleteTextView)
        val options = arrayOf("Option 1", "Option 2", "Option 3") // Sample dropdown data
        val arrayAdapter = ArrayAdapter(holder.itemView.context, R.layout.bottleneck_drop_down, options)
        holder.dropdown.setAdapter(arrayAdapter)

        // Set the current selection in the dropdown based on the saved selection in the model
        holder.dropdown.setText(currentItem.selectedOption ?: "", false)

        // Handle dropdown item selection and save the selected option in the model
        holder.dropdown.setOnItemClickListener { _, _, which, _ ->
            currentItem.selectedOption = arrayAdapter.getItem(which)
        }
    }

    override fun getItemCount(): Int {
        return bottleList.size
    }
}