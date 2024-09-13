package com.example.techinfo.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.Fragments.Troubleshoot_modules.TroubleShoot_adapter
import com.example.techinfo.Fragments.Troubleshoot_modules.TroubleShoot_data
import com.example.techinfo.R
import com.example.techinfo.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Sample data list
//        val items = listOf(
//            TroubleShoot_data("Article 1"),
//            TroubleShoot_data("Article 2"),
//            TroubleShoot_data("Article 3"),
//            TroubleShoot_data("Article 4"),
//            TroubleShoot_data("Article 5"),
//            TroubleShoot_data("Article 6"),
//            TroubleShoot_data("Article 7"),
//            TroubleShoot_data("Article 8")
//        )

        RetrofitClient.instance.getArticles().enqueue(object : Callback<List<TroubleShoot_data>> {
            override fun onResponse(
                call: Call<List<TroubleShoot_data>>,
                response: Response<List<TroubleShoot_data>>
            ) {
                if (response.isSuccessful) {
                    val items = response.body() ?: emptyList()

                    Log.d("DATA", "Item count: ${items.size}")


                    // Setup adapter and RecyclerView
                    itemAdapter = TroubleShoot_adapter(items) { item ->
                        // Handle item click
                        val fragment = Troubleshoot_content.newInstance(item.title, item.content)
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    recyclerView.adapter = itemAdapter
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                } else {
                    Log.e("DATA_ERROR", "Response failed")
                }
            }

            override fun onFailure(call: Call<List<TroubleShoot_data>>, t: Throwable) {
                // Handle failure
                Log.e("API_FAILURE", "Error fetching articles", t)
            }
        })
    }
}
