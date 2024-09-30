package com.example.techinfo.Fragments.Troubleshoot.troubleshoot_content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R
import com.example.techinfo.api_connector.ApiService
import com.example.techinfo.api_connector.TroubleshootContent
import com.example.techinfo.api_connector.TroubleShoot_data
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TroubleShoot : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: TroubleShoot_adapter
    private val items = mutableListOf<TroubleShoot_data>()

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

        fetchArticles()
    }

    private fun fetchArticles() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.100.74:8000/api/") // Ensure this is your correct server IP
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.getTroubleshootArticles().enqueue(object : Callback<List<TroubleshootContent>> {
            override fun onResponse(
                call: Call<List<TroubleshootContent>>,
                response: Response<List<TroubleshootContent>>
            ) {
                if (response.isSuccessful) {
                    val articles = response.body()
                    if (!articles.isNullOrEmpty()) {
                        items.clear()
                        articles.forEach { article ->
                            items.add(TroubleShoot_data(article.title, article.id.toString())) // Convert Int to String
                        }
                        itemAdapter = TroubleShoot_adapter(items) { item ->
                            val fragment = TroubleshootContentFragment.newInstance(item.id) // Pass String ID
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .addToBackStack(null)
                                .commit()
                        }
                        recyclerView.adapter = itemAdapter
                    }
                }
            }

            override fun onFailure(call: Call<List<TroubleshootContent>>, t: Throwable) {
                // Handle network failure
            }
        })
    }
}
