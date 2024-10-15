package com.example.techinfo.Fragments.BuildPCmodules

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.Fragments.BuildPC.ComponentData
import com.example.techinfo.R

class Adapter(
    private val componentList: MutableList<ComponentData>, // List of component data
    private val itemClickListener: (ComponentData, Int) -> Unit // Listener for item clicks
) : RecyclerView.Adapter<Adapter.ComponentViewHolder>() {

    // ViewHolder that binds data to views
    inner class ComponentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val componentImageView: ImageView = view.findViewById(R.id.image) // Image for the component
        val componentNameTextView: TextView = view.findViewById(R.id.details) // Text for the component name

        fun bind(component: ComponentData, position: Int) {
            // Log the component data to verify it's passed correctly
            Log.d("Adapter", "Binding Component: ${component.name}, Part Details: ${component.partDetails}")

            // Display the part details or name
            componentNameTextView.text = if (component.partDetails.isNotEmpty()) component.partDetails else component.name

            // Placeholder image (You can later replace this with actual images)
            componentImageView.setImageResource(R.drawable.ic_launcher_foreground)

            // Set up item click listener
            itemView.setOnClickListener {
                itemClickListener(component, position) // Pass the component and position to listener

                // Toggle selection state
                component.isSelected = !component.isSelected

                // Change the background color to show selection
                itemView.setBackgroundColor(
                    if (component.isSelected) Color.LTGRAY else Color.WHITE
                )

                // Notify adapter to refresh the specific item
                notifyItemChanged(position)
            }
        }
    }

    // Create new ViewHolder instances from layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item, parent, false) // Inflate your recycler_item.xml layout
        return ComponentViewHolder(view)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: ComponentViewHolder, position: Int) {
        Log.d("Adapter", "onBindViewHolder - Position: $position")
        holder.bind(componentList[position], position) // Bind the data to each item
    }

    // Return the total number of items in the list
    override fun getItemCount() = componentList.size
}
