package com.example.techinfo.Fragments.Troubleshoot.troubleshoot_content

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.techinfo.R
import com.example.techinfo.api_connector.RetrofitInstance
import com.example.techinfo.api_connector.TroubleshootContent
import com.example.techinfo.api_connector.TroubleShoot_data
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TroubleShoot : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: TroubleShoot_adapter
    private val items = mutableListOf<TroubleShoot_data>()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var searchView: SearchView
    private val handler = Handler()
    private val autoRefreshRunnable = Runnable {
        fetchArticles() // Auto-refresh articles every specified interval
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trouble_shoot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.troubleShootRecycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize SwipeRefreshLayout
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            fetchArticles() // Refresh articles on swipe down
        }

        // Initialize SearchView
        searchView = view.findViewById(R.id.searchView)
        setupSearchView()

        // Initial fetch of articles
        fetchArticles()

        // Start auto-refresh every 10 seconds
        handler.postDelayed(autoRefreshRunnable, 10000)
    }

    private fun setupSearchView() {
        // Set up listener for search input
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Filter list when user submits the query
                filterList(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter list when user types each letter
                filterList(newText)
                return true
            }
        })
    }

    private fun filterList(query: String?) {
        // When query is empty, reset the list to original data
        if (query.isNullOrEmpty()) {
            itemAdapter.resetList()
            return
        }

        // Filter the list based on the query
        val filteredItems = items.filter {
            it.title.contains(query, ignoreCase = true)
        }.toMutableList()

        // Update adapter with filtered data
        itemAdapter.filterList(filteredItems)
    }

    private fun fetchArticles() {
        // Use RetrofitInstance to get the ApiService
        val apiService = RetrofitInstance.getApiService()

        // Asynchronous call to fetch articles
        apiService.getTroubleshootArticles().enqueue(object : Callback<List<TroubleshootContent>> {
            override fun onResponse(
                call: Call<List<TroubleshootContent>>,
                response: Response<List<TroubleshootContent>>
            ) {
                swipeRefreshLayout.isRefreshing = false // Stop the refresh indicator

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
                        // Show message when no articles are found
                        Toast.makeText(requireContext(), "No articles found.", Toast.LENGTH_SHORT).show()
                        recyclerView.adapter = null
                    }
                } else {
                    // Handle error response
                    Toast.makeText(requireContext(), "Error retrieving data", Toast.LENGTH_SHORT).show()
                    recyclerView.adapter = null
                }
            }

            override fun onFailure(call: Call<List<TroubleshootContent>>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(requireContext(), "No connection or server error", Toast.LENGTH_LONG).show()
                recyclerView.adapter = null
            }
        })
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(autoRefreshRunnable)
    }
}
