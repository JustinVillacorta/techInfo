package com.example.techinfo.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.techinfo.Fragments.Troubleshoot.troubleshoot_content.ApiService
import com.example.techinfo.Fragments.Troubleshoot.troubleshoot_content.troubleshootData
import com.example.techinfo.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Troubleshoot_content : Fragment() {

    private lateinit var detailTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_troubleshoot_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        detailTextView = view.findViewById(R.id.detailTextView)
        val backButton: ImageButton = view.findViewById(R.id.btnBack)

        // Set up the back button to navigate back to the previous fragment
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Fetch articles from the backend and display the content
        fetchArticles()
    }

    private fun fetchArticles() {
        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.100.12/")  // Replace with your actual server URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // Make an asynchronous network request to fetch articles
        apiService.getArticles().enqueue(object : Callback<List<troubleshootData>> {
            override fun onResponse(call: Call<List<troubleshootData>>, response: Response<List<troubleshootData>>) {
                if (response.isSuccessful) {
                    val articles = response.body()
                    if (!articles.isNullOrEmpty()) {
                        // Display the first article's content
                        detailTextView.text = articles[0].content
                    }
                } else {
                    detailTextView.text = "Error: Failed to load data."
                }
            }

            override fun onFailure(call: Call<List<troubleshootData>>, t: Throwable) {
                // Handle network failure
                detailTextView.text = "Failed to load data: ${t.message}"
            }
        })
    }

    companion object {
        // Function to create a new instance of the fragment with the passed data
        fun newInstance(itemName: String): Troubleshoot_content {
            val fragment = Troubleshoot_content()
            val args = Bundle()
            args.putString("ITEM_NAME", itemName)
            fragment.arguments = args
            return fragment
        }
    }
}
