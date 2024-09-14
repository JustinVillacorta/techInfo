package com.example.techinfo.Fragments.BuildPCmodules

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.Fragments.BuilcPC.ComponentData
import com.example.techinfo.R

class Adapter(
    private val componentList: List<ComponentData>,
    private val itemClickListener: (ComponentData) -> Unit
) : RecyclerView.Adapter<Adapter.ComponentViewHolder>() {

    inner class ComponentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val componentImageView: ImageView = view.findViewById(R.id.image)
        val componentNameTextView: TextView = view.findViewById(R.id.title)

        fun bind(component: ComponentData) {
            componentNameTextView.text = component.name
            componentImageView.setImageResource(R.drawable.ic_launcher_foreground) // Example placeholder
            itemView.setOnClickListener {
                itemClickListener(component)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item, parent, false)
        return ComponentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComponentViewHolder, position: Int) {
        holder.bind(componentList[position])
    }

    override fun getItemCount() = componentList.size
}