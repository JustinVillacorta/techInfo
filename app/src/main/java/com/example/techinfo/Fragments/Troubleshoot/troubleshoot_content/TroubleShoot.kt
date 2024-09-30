package com.example.techinfo.Fragments.Troubleshoot.troubleshoot_content

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.techinfo.R
import com.example.techinfo.api_connector.ApiService
import com.example.techinfo.api_connector.TroubleshootContent
import com.example.techinfo.api_connector.TroubleShoot_data
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class TroubleShoot : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: TroubleShoot_adapter
    private val items = mutableListOf<TroubleShoot_data>()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val handler = Handler()
    private val autoRefreshRunnable = Runnable {
        fetchArticles() // Fetch articles automatically every specified interval
    }

    // Inflate the fragment layout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trouble_shoot, container, false)
    }

    // Called after onCreateView, where the view is fully created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.troubleShootRecycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Setup SwipeRefreshLayout
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            fetchArticles() // Refresh articles when user pulls to refresh
        }

        // Initial fetch of articles
        fetchArticles()

        // Start auto-refresh every 10 seconds
        handler.postDelayed(autoRefreshRunnable, 10000)
    }

    private fun fetchArticles() {
        // Create a custom OkHttpClient with timeout settings
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Connection timeout
            .readTimeout(30, TimeUnit.SECONDS)    // Read timeout
            .writeTimeout(30, TimeUnit.SECONDS)   // Write timeout
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.100.74:8000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient) // Set the custom OkHttpClient
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // Asynchronous call to fetch articles
        apiService.getTroubleshootArticles().enqueue(object : Callback<List<TroubleshootContent>> {
            override fun onResponse(
                call: Call<List<TroubleshootContent>>,
                response: Response<List<TroubleshootContent>>
            ) {
                swipeRefreshLayout.isRefreshing = false // Stop the refresh indicator

                // Check if response is successful
                if (response.isSuccessful) {
                    val articles = response.body()
                    if (!articles.isNullOrEmpty()) {
                        items.clear()
                        articles.forEach { article ->
                            items.add(TroubleShoot_data(article.title, article.id.toString()))
                        }
                        itemAdapter = TroubleShoot_adapter(items) { item ->
                            val fragment = TroubleshootContentFragment.newInstance(item.id)
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .addToBackStack(null)
                                .commit()
                        }
                        recyclerView.adapter = itemAdapter
                    } else {
                        // Show a message when there are no articles
                        Toast.makeText(requireContext(), "No articles found.", Toast.LENGTH_SHORT).show()
                        recyclerView.adapter = null // Ensure RecyclerView is cleared
                    }
                } else {
                    // Handle error response without hiding refresh
                    Toast.makeText(requireContext(), "Error retrieving data", Toast.LENGTH_SHORT).show()
                    recyclerView.adapter = null // Ensure RecyclerView is cleared
                }
            }

            override fun onFailure(call: Call<List<TroubleshootContent>>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false // Stop the refresh indicator
                // Handle network failure
                Toast.makeText(requireContext(), "No connection or server error", Toast.LENGTH_LONG).show()
                recyclerView.adapter = null // Ensure RecyclerView is cleared
            }
        })
    }

    // Stop the auto-refresh when the fragment is stopped
    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(autoRefreshRunnable)
    }
}
