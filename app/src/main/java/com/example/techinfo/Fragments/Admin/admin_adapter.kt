package com.example.techinfo.Fragments.Admin

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R

class admin_adapter(
    val context: Context,
    val adminList: ArrayList<admin_data_class>,
    val fragment: AdminView  // Explicitly pass the fragment reference
) : RecyclerView.Adapter<admin_adapter.AdminViewHolder>() {

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
        val selectedAdminItem = adminList[position]

        // Create a PopupMenu
        val popupMenu = PopupMenu(context, view)
        popupMenu.inflate(R.menu.add_menu)

        // Set click listener for menu items
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.update -> {
                    // Trigger the update functionality in AdminView (passing item to update)
                    fragment.openEditDialog(selectedAdminItem, position)  // Use fragment reference directly
                    true
                }

                R.id.delete -> {
                    // Confirm delete
                    AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this information?")
                        .setPositiveButton("Yes") { dialog, _ ->
                            adminList.removeAt(position)
                            notifyItemRemoved(position)  // Efficient removal
                            Toast.makeText(context, "Deleted this Information", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                        .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
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

    override fun getItemCount(): Int = adminList.size

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        val adminItem = adminList[position]
        holder.ModelName.text = adminItem.ModelName
        holder.Specs.text = adminItem.Specs
    }
}
