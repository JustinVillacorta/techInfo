package com.example.techinfo.Fragments.Admin

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R

class Troubleshoot_admin_adapter(val context: Context, val  troubleshootList:ArrayList<troubleshoot_admin_data_class>):RecyclerView.Adapter<Troubleshoot_admin_adapter.AdminTroubleshootViewHolder>()
{

    inner class AdminTroubleshootViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val article_name: TextView = view.findViewById(R.id.article_name)
        val article_subtitle: TextView = view.findViewById(R.id.article_subtitle)
        val article_menu: ImageView = view.findViewById(R.id.mMenus)

        init {
            article_menu.setOnClickListener {
                popupMenus(it, adapterPosition)
            }
        }
    }


        private fun popupMenus(view: View, position: Int) {
            // Create a PopupMenu
            val popupMenu = PopupMenu(context, view)
            popupMenu.inflate(R.menu.add_menu)

            // Set click listener for menu items
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.update -> {
                        // Inflate the dialog layout
                        val v = LayoutInflater.from(context).inflate(R.layout.troubleshoot_add_item, null)
                        val TroubleshootTitle = v.findViewById<EditText>(R.id.ArticleTitle) // Changed to ArticleTitle
                        val TroubleshootSubTitle = v.findViewById<EditText>(R.id.article_subtitle) // Changed to article_subtitle

                        // Create and show the AlertDialog
                        AlertDialog.Builder(context)
                            .setView(v)
                            .setPositiveButton("Ok") { dialog, _ ->
                                // Update the item in the list and notify the adapter
                                troubleshootList[position].troubleshootTitle = TroubleshootTitle.text.toString()
                                troubleshootList[position].troubleshootContent = TroubleshootSubTitle.text.toString()
                                notifyDataSetChanged()
                                Toast.makeText(context, "User Information is Edited", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .setNegativeButton("Cancel") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()

                        true
                    }
                    R.id.delete -> {
                        // Create a confirmation dialog for deletion
                        AlertDialog.Builder(context)
                            .setTitle("Delete")
                            .setMessage("Are you sure you want to delete this information?")
                            .setPositiveButton("Yes") { dialog, _ ->
                                // Remove the item from the list and notify the adapter
                                troubleshootList.removeAt(position)
                                notifyDataSetChanged()
                                Toast.makeText(context, "Deleted this Information", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .setNegativeButton("No") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()

                        true
                    }
                    else -> true
                }
            }

            // Show the popup menu
            popupMenu.show()
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminTroubleshootViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.troubleshoot_admin_recycler, parent, false)
        return AdminTroubleshootViewHolder(view)
    }

    override fun getItemCount(): Int {
        return troubleshootList.size
    }

    override fun onBindViewHolder(holder: AdminTroubleshootViewHolder, position: Int) {
        val troubleshootItem = troubleshootList[position]
        holder.article_name.text = troubleshootItem.troubleshootTitle
        holder.article_subtitle.text = troubleshootItem.troubleshootContent
    }
}