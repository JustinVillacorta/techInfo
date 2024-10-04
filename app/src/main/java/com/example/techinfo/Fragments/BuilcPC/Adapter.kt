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

class Adapter(
    private val componentList: MutableList<ComponentData>, // Make it mutable to allow updates
    private val itemClickListener: (ComponentData, Int) -> Unit // Update listener to include position
) : RecyclerView.Adapter<Adapter.ComponentViewHolder>() {

    inner class ComponentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val componentImageView: ImageView = view.findViewById(R.id.image)
        val componentNameTextView: TextView = view.findViewById(R.id.details)


        fun bind(component: ComponentData, position: Int) {
            componentNameTextView.text = component.partDetails ?: component.name // Show details if available, else show name


            // Update background color if selected
            itemView.setBackgroundColor(
                if (component.isSelected) Color.LTGRAY else Color.TRANSPARENT
            )

            // Placeholder image for now
            componentImageView.setImageResource(R.drawable.ic_launcher_foreground)

            // Set up the click listener for the item
            itemView.setOnClickListener {
                itemClickListener(component, position) // Pass the position along with the component

                // Toggle the selection state
                if (component.isSelected) {
                    // Unselect the component
                    component.isSelected = false
                } else {
                    // Select the component
                    component.isSelected = true
                }

                // Notify the adapter to refresh the specific item
                notifyItemChanged(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item, parent, false)
        return ComponentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComponentViewHolder, position: Int) {
        holder.bind(componentList[position], position) // Bind the data to the view holder
    }

    override fun getItemCount() = componentList.size // Return the total number of items
}