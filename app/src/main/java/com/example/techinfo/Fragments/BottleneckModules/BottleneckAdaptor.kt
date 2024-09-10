package com.example.techinfo.Fragments.BottleneckModules

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.Fragments.Troubleshoot_modules.TroubleShoot_data
import com.example.techinfo.R

class BottleneckAdaptor(
    private val ArrayList : List<BottleneckData>,
) : RecyclerView.Adapter<BottleneckAdaptor.onViewHolder>(){

    class onViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val TextView: TextView = itemView.findViewById(R.id.title)
        val imageView: ImageView = itemView.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): onViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bottleneck_recycler, parent, false)
        return onViewHolder(view)
    }

    override fun onBindViewHolder(holder: onViewHolder, position: Int) {
        val currentItem = ArrayList[position]
        holder.TextView.text = currentItem.name
        holder.imageView.setImageResource(R.drawable.ic_launcher_foreground)
    }

    override fun getItemCount(): Int {
        return ArrayList.size
    }


}