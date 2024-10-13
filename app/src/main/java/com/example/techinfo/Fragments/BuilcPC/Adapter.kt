package com.example.techinfo.Fragments.BuildPCmodules

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.Fragments.BuilcPC.ComponentData
import com.example.techinfo.R

// Adapter class for handling components in the RecyclerView
class Adapter(
    private val componentList: MutableList<ComponentData>, // Make the list mutable to update it
    private val itemClickListener: (ComponentData, Int) -> Unit // Update listener to include position
) : RecyclerView.Adapter<Adapter.ComponentViewHolder>() {

    // ViewHolder that binds the data to the view
    inner class ComponentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val componentImageView: ImageView = view.findViewById(R.id.image) // Image view for the component
        val componentNameTextView: TextView = view.findViewById(R.id.details) // Text view for the component name

        fun bind(component: ComponentData, position: Int) {
            // Show details if available, else show name
            componentNameTextView.text = component.partDetails ?: component.name

            // Set the placeholder image for now
            componentImageView.setImageResource(R.drawable.ic_launcher_foreground)

            // Set up the click listener for the item
            itemView.setOnClickListener {
                itemClickListener(component, position) // Pass the component and its position to the listener

                // Toggle the selection state
                component.isSelected = !component.isSelected

                // Update the background color based on selection state
                itemView.setBackgroundColor(
                    if (component.isSelected) Color.LTGRAY else Color.WHITE
                )

                // Notify the adapter to refresh the specific item
                notifyItemChanged(position)
            }
        }
    }

    // Creates a new ViewHolder from the item view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item, parent, false) // Inflate your layout for each item
        return ComponentViewHolder(view)
    }

    // Binds the data to the ViewHolder
    override fun onBindViewHolder(holder: ComponentViewHolder, position: Int) {
        holder.bind(componentList[position], position) // Bind the component at the current position
    }

    // Returns the total number of items in the list
    override fun getItemCount() = componentList.size
}
