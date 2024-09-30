package com.example.techinfo.Fragments.Troubleshoot.troubleshoot_content

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.techinfo.R
import com.example.techinfo.api_connector.ApiService
import com.example.techinfo.api_connector.TroubleshootContent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TroubleshootContentFragment : Fragment() {
    private lateinit var articleTitleTextView: TextView
    private lateinit var videoThumbnailImageView: ImageView
    private lateinit var contentTextView: TextView
    private lateinit var createdTimeTextView: TextView
    private lateinit var updatedTimeTextView: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var articleId: Int? = null // Store article ID

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_troubleshoot_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Views
        articleTitleTextView = view.findViewById(R.id.articleTitleTextView)
        videoThumbnailImageView = view.findViewById(R.id.videoThumbnailImageView)
        contentTextView = view.findViewById(R.id.contentTextView)
        createdTimeTextView = view.findViewById(R.id.createdTimeTextView)
        updatedTimeTextView = view.findViewById(R.id.updatedTimeTextView)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        // Get the article ID from arguments
        articleId = arguments?.getString("ARTICLE_ID")?.toIntOrNull()

        // Fetch and display articles
        articleId?.let { fetchArticlesAndDisplay(it) }

        // Set up swipe refresh layout
        swipeRefreshLayout.setOnRefreshListener {
            articleId?.let { id ->
                fetchArticlesAndDisplay(id)
            }
        }
    }

    private fun fetchArticlesAndDisplay(articleId: Int) {
        // Show the refresh indicator
        swipeRefreshLayout.isRefreshing = true

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

        // Fetch all articles
        apiService.getTroubleshootArticles().enqueue(object : Callback<List<TroubleshootContent>> {
            override fun onResponse(
                call: Call<List<TroubleshootContent>>,
                response: Response<List<TroubleshootContent>>
            ) {
                // Stop the refresh indicator
                swipeRefreshLayout.isRefreshing = false

                if (response.isSuccessful) {
                    val articles = response.body()
                    if (articles != null) {
                        // Find the article with the given ID
                        val article = articles.find { it.id == articleId }
                        if (article != null) {
                            // Update UI with article title
                            articleTitleTextView.text = article.title
                            val videoUrl = article.videoEmbed
                            loadThumbnail(videoUrl)
                            contentTextView.text = article.content
                            createdTimeTextView.text = "Created: ${formatDate(article.createdAt)}"
                            updatedTimeTextView.text = "Updated: ${formatDate(article.updatedAt)}"

                            videoThumbnailImageView.setOnClickListener {
                                openYouTubeVideo(videoUrl)
                            }
                        } else {
                            contentTextView.text = "Article not found."
                        }
                    } else {
                        contentTextView.text = "Error: No articles found."
                    }
                } else {
                    contentTextView.text = "Error: Failed to load articles."
                }
            }

            override fun onFailure(call: Call<List<TroubleshootContent>>, t: Throwable) {
                // Stop the refresh indicator
                swipeRefreshLayout.isRefreshing = false
                // Show a Toast message for network failure
                Toast.makeText(requireContext(), "No connection or server error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun formatDate(dateString: String): String {
        // Input format: "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

        return try {
            val date = inputFormat.parse(dateString)
            if (date != null) {
                outputFormat.format(date)
            } else {
                "Unknown"
            }
        } catch (e: Exception) {
            "Unknown"
        }
    }

    private fun loadThumbnail(videoUrl: String?) {
        if (!videoUrl.isNullOrEmpty()) {
            try {
                // Extract the video ID from the URL
                val videoId = videoUrl.substringAfter("v=").substringBefore("&")
                // Construct the thumbnail URL using the video ID
                val thumbnailUrl = "https://img.youtube.com/vi/$videoId/hqdefault.jpg"

                // Load the thumbnail into the ImageView using Glide
                Glide.with(this)
                    .load(thumbnailUrl)
                    .into(videoThumbnailImageView)
            } catch (e: Exception) {
                // Handle exception if extracting video ID fails
                // Optionally clear the ImageView or leave it unchanged
            }
        } else {
            // Optionally clear the ImageView if no video URL is available
        }
    }

    private fun openYouTubeVideo(videoUrl: String?) {
        if (!videoUrl.isNullOrEmpty()) {
            // Open the YouTube video in a browser or the YouTube app
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
            startActivity(intent)
        } else {
            contentTextView.text = "No video available."
        }
    }

    companion object {
        fun newInstance(articleId: String): TroubleshootContentFragment {
            val fragment = TroubleshootContentFragment()
            val args = Bundle()
            args.putString("ARTICLE_ID", articleId)
            fragment.arguments = args
            return fragment
        }
    }
}
