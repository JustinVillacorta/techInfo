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

class admin_adapter(val context: Context, val adminList: ArrayList<admin_data_class>) : RecyclerView.Adapter<admin_adapter.AdminViewHolder>() {

    inner class AdminViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val ModelName: TextView = view.findViewById(R.id.Model_Name)
        val Specs: TextView = view.findViewById(R.id.SpecsContent)
        val articleMenu: ImageView = view.findViewById(R.id.mMenus)

        init {
            articleMenu.setOnClickListener {
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
                    val v = LayoutInflater.from(context).inflate(R.layout.pc_parts_add, null)
                    val ModelName = v.findViewById<EditText>(R.id.ModelName) // Assuming you have this ID
                    val specs = v.findViewById<EditText>(R.id.specs) // Assuming you have this ID

                    // Create and show the AlertDialog
                    AlertDialog.Builder(context)
                        .setView(v)
                        .setPositiveButton("Ok") { dialog, _ ->
                            // Update the item in the list and notify the adapter
                            adminList[position].ModelName = ModelName.text.toString() // Change to the correct property
                            adminList[position].Specs = specs.text.toString() // Change to the correct property
                            notifyDataSetChanged()
                            Toast.makeText(context, "Admin Information is Edited", Toast.LENGTH_SHORT).show()
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
                            adminList.removeAt(position)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.admin_recycler_item, parent, false)
        return AdminViewHolder(view)
    }

    override fun getItemCount(): Int {
        return adminList.size
    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        val adminItem = adminList[position]
        holder.ModelName.text = adminItem.ModelName // Assuming the property name is 'name'
        holder.Specs.text = adminItem.Specs // Assuming the property name is 'subtitle'
    }
}
