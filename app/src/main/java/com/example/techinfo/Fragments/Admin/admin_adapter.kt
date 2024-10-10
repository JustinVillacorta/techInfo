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
        val selectedAdminItem = adminList[position]
        val selectedCategory = selectedAdminItem.Category

        // Create a PopupMenu
        val popupMenu = PopupMenu(context, view)
        popupMenu.inflate(R.menu.add_menu)

        // Set click listener for menu items
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.update -> {
                    // Inflate the dialog layout
                    val v = LayoutInflater.from(context).inflate(R.layout.adminview_pcparts_popupmenu, null)
                    val ModelName = v.findViewById<EditText>(R.id.ModelName)

                    // Initialize EditText fields and their labels
                    val Specs1 = v.findViewById<EditText>(R.id.specs1)
                    val Specs2 = v.findViewById<EditText>(R.id.specs2)
                    val Specs3 = v.findViewById<EditText>(R.id.specs3)
                    val Specs4 = v.findViewById<EditText>(R.id.specs4)
                    val Specs5 = v.findViewById<EditText>(R.id.specs5)
                    val Specs6 = v.findViewById<EditText>(R.id.specs6)
                    val Specs7 = v.findViewById<EditText>(R.id.specs7)
                    val Specs8 = v.findViewById<EditText>(R.id.specs8)
                    val Specs9 = v.findViewById<EditText>(R.id.specs9)
                    val Specs10 = v.findViewById<EditText>(R.id.specs10)

                    // Initialize TextView labels for the specs
                    val label1 = v.findViewById<TextView>(R.id.specs1_label)
                    val label2 = v.findViewById<TextView>(R.id.specs2_label)
                    val label3 = v.findViewById<TextView>(R.id.specs3_label)
                    val label4 = v.findViewById<TextView>(R.id.specs4_label)
                    val label5 = v.findViewById<TextView>(R.id.specs5_label)
                    val label6 = v.findViewById<TextView>(R.id.specs6_label)
                    val label7 = v.findViewById<TextView>(R.id.specs7_label)
                    val label8 = v.findViewById<TextView>(R.id.specs8_label)
                    val label9 = v.findViewById<TextView>(R.id.specs9_label)
                    val label10 = v.findViewById<TextView>(R.id.specs10_label)

                    // Hide all fields initially
                    hideAllSpecsWithLabels(
                        arrayOf(
                            Specs1 to label1,
                            Specs2 to label2,
                            Specs3 to label3,
                            Specs4 to label4,
                            Specs5 to label5,
                            Specs6 to label6,
                            Specs7 to label7,
                            Specs8 to label8,
                            Specs9 to label9,
                            Specs10 to label10
                        )
                    )

                    // Map of category to the relevant fields
                    val categorySpecsMap = mapOf(
                        "CPU" to listOf(Specs1, Specs2, Specs3, Specs4, Specs5),
                        "GPU" to listOf(Specs1, Specs2, Specs3, Specs4),
                        "RAM" to listOf(Specs1, Specs2, Specs3),
                        "Storage" to listOf(Specs1, Specs2, Specs3, Specs4, Specs5)
                    )

                    // Show the fields for the selected category
                    showSpecsForCategory(v, selectedCategory, categorySpecsMap)

                    // Set current values of the item
                    ModelName.setText(selectedAdminItem.ModelName)

                    // Split and set the specs values if available
                    val currentSpecs = selectedAdminItem.Specs.split(", ").toTypedArray()
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
                            adminList[position].ModelName = ModelName.text.toString()
                            adminList[position].Specs = specs
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
                            adminList.removeAt(position)
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
        holder.ModelName.text = adminItem.ModelName
        holder.Specs.text = adminItem.Specs
    }
}