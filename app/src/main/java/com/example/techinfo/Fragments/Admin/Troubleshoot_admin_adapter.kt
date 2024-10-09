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
import com.example.techinfo.Fragments.Admin.admin_adapter.AdminViewHolder
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
        val selectedAdminItem = troubleshootList[position]
        val selectedCategory = selectedAdminItem.troubleshootcategory

        // Create a PopupMenu
        val popupMenu = PopupMenu(context, view)
        popupMenu.inflate(R.menu.add_menu)

        // Set click listener for menu items
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.update -> {
                    // Inflate the dialog layout
                    val v = LayoutInflater.from(context).inflate(R.layout.troubleshoot_update_item, null)
                    val ModelName = v.findViewById<EditText>(R.id.ModelName)

                    // Initialize EditText fields and their labels
                    val troubleshoot1 = v.findViewById<EditText>(R.id.troubleshoot1)
                    val troubleshoot2 = v.findViewById<EditText>(R.id.troubleshoot2)



                    // Initialize TextView labels for the specs
                    val label1 = v.findViewById<TextView>(R.id.troubleshoot1_label)
                    val label2 = v.findViewById<TextView>(R.id.troubleshoot2_label)



                    // Hide all fields initially
                    hideAllSpecsWithLabels(
                        arrayOf(
                            troubleshoot1 to label1,
                            troubleshoot2 to label2,


                        )
                    )

                    // Map of category to the relevant fields
                    val categorySpecsMap = mapOf(
                        "CPU" to listOf(troubleshoot1, troubleshoot2),
                        "GPU" to listOf(troubleshoot1, troubleshoot2),
                        "RAM" to listOf(troubleshoot1,troubleshoot2),
                        "Storage" to listOf(troubleshoot1, troubleshoot2)
                    )

                    // Show the fields for the selected category
                    showSpecsForCategory(v, selectedCategory, categorySpecsMap)

                    // Set current values of the item
                    ModelName.setText(selectedAdminItem.troubleshootTitle)

                    // Split and set the specs values if available
                    val currentSpecs = selectedAdminItem.troubleshootContent.split(", ").toTypedArray()
                    val editTextFields = categorySpecsMap[selectedCategory] ?: emptyList()
                    for (i in currentSpecs.indices) {
                        if (i < editTextFields.size) {
                            editTextFields[i].setText(currentSpecs[i])
                        }
                    }

                    // Create and show the AlertDialog
                    AlertDialog.Builder(context)
                        .setView(v)
                        .setPositiveButton("Ok") { dialog, _ ->
                            // Concatenate all the specs into one string
                            val specs = editTextFields
                                .map { it.text.toString() }
                                .filter { it.isNotBlank() } // Only add non-blank specs
                                .joinToString(separator = ", ")

                            // Update the item in the list and notify the adapter
                            troubleshootList[position].troubleshootTitle = ModelName.text.toString()
                            troubleshootList[position].troubleshootContent = specs
                            notifyDataSetChanged()
                            Toast.makeText(
                                context,
                                "Admin Information is Edited",
                                Toast.LENGTH_SHORT
                            ).show()
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
                            Toast.makeText(
                                context,
                                "Deleted this Information",
                                Toast.LENGTH_SHORT
                            ).show()
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

    private fun hideAllSpecsWithLabels(specsWithLabels: Array<Pair<EditText, TextView>>) {
        specsWithLabels.forEach { (editText, label) ->
            editText.visibility = View.GONE
            label.visibility = View.GONE
        }
    }

    private fun showSpecsForCategory(
        addView: View,
        category: String,
        categorySpecsMap: Map<String, List<EditText>>
    ) {
        categorySpecsMap[category]?.forEach { editText ->
            editText.visibility = View.VISIBLE
            // Show the corresponding label
            val labelId = context.resources.getIdentifier(
                "${context.resources.getResourceEntryName(editText.id)}_label",
                "id",
                context.packageName
            )
            val label: TextView? = addView.findViewById(labelId)
            label?.visibility = View.VISIBLE
        }
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
        val adminItem = troubleshootList[position]
        holder.article_name.text = adminItem.troubleshootTitle
        holder.article_subtitle.text = adminItem.troubleshootContent
    }
}
