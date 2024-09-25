package com.example.techinfo.Fragments.Troubleshoot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.Fragments.Troubleshoot_content
import com.example.techinfo.R

class TroubleShoot : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: TroubleShoot_adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trouble_shoot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.troubleShootRecycler)

        // Sample data list
        val items = listOf(
            TroubleShoot_data("Article 1"),
            TroubleShoot_data("Article 2"),
            TroubleShoot_data("Article 3"),
            TroubleShoot_data("Article 4"),
            TroubleShoot_data("Article 5"),
            TroubleShoot_data("Article 6"),
            TroubleShoot_data("Article 7"),
            TroubleShoot_data("Article 8")
        )

        // Setup adapter and RecyclerView
        itemAdapter = TroubleShoot_adapter(items) { item ->
            // Handle item click
            val fragment = Troubleshoot_content.newInstance(item.name)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = itemAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}
