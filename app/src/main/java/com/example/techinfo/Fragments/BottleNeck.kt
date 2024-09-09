package com.example.techinfo.Fragments.BottleneckModules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R


class BottleNeck : Fragment() {

    private lateinit var recyclerView: RecyclerView
   // private lateinit var itemAdapter: BottleneckAdaptor
    private lateinit var bottleArrayList: ArrayList<BottleneckData>

    //private lateinit var textId: Array<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottle_neck_option, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.bottleneck_recycler)

        val drop = listOf("Example1", "Example2")

        val dropItem: AutoCompleteTextView = view.findViewById(R.id.title)

        //val adapter = ArrayAdapter(this, R.layout.dropdown_item, drop)

        //dropItem.setAdapter(adapter)

        dropItem.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->

            val itemSelect = adapterView.getItemAtPosition(i)
            //Toast.makeText(this, "Item: $itemSelect", Toast.LENGTH_SHORT).show()

            val currentItems = arrayOf(
                ("CPU"),
                ("GPU"),
                ("RAM"),
                ("HDD"),
                ("SDD")
            )

            for (i in currentItems.indices) {

                val current = BottleneckData(currentItems[i])
                bottleArrayList.add(current)

            }
            recyclerView.adapter = BottleneckAdaptor(bottleArrayList)
        }
    }
}