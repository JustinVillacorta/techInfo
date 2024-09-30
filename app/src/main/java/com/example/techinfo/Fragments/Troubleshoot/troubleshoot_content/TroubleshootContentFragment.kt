package com.example.techinfo.Fragments.Troubleshoot.troubleshoot_content

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.techinfo.R
import com.example.techinfo.api_connector.ApiService
import com.example.techinfo.api_connector.TroubleshootContent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class TroubleshootContentFragment : Fragment() {
    private lateinit var articleTitleTextView: TextView
    private lateinit var videoThumbnailImageView: ImageView
    private lateinit var contentTextView: TextView
    private lateinit var createdTimeTextView: TextView
    private lateinit var updatedTimeTextView: TextView

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

        // Get the article ID from arguments
        val articleId = arguments?.getString("ARTICLE_ID")?.toIntOrNull()

        if (articleId != null) {
            fetchArticlesAndDisplay(articleId)
        } else {
            contentTextView.text = "Article ID is missing or invalid."
        }
    }

    private fun fetchArticlesAndDisplay(articleId: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.100.74:8000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // Fetch all articles
        apiService.getTroubleshootArticles().enqueue(object : Callback<List<TroubleshootContent>> {
            override fun onResponse(
                call: Call<List<TroubleshootContent>>,
                response: Response<List<TroubleshootContent>>
            ) {
                if (response.isSuccessful) {
                    val articles = response.body()
                    if (articles != null) {
                        // Find the article with the given ID
                        val article = articles.find { it.id == articleId }
                        if (article != null) {
                            // Update UI with article title
                            articleTitleTextView.text = article.title

                            // Load thumbnail into ImageView
                            val videoUrl = article.videoEmbed // Access the videoEmbed property
                            loadThumbnail(videoUrl)

                            // Set the article content to the TextView
                            contentTextView.text = article.content

                            // Format and display the created and updated times
                            val createdAtFormatted = formatDate(article.createdAt)
                            val updatedAtFormatted = formatDate(article.updatedAt)

                            createdTimeTextView.text = "Created: $createdAtFormatted"
                            updatedTimeTextView.text = "Updated: $updatedAtFormatted"

                            // Set up the thumbnail click listener to open the YouTube video
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
                contentTextView.text = "Failed to load articles: ${t.message}"
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
