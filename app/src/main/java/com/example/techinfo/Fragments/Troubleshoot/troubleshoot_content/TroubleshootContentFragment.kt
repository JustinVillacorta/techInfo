package com.example.techinfo.Fragments.Troubleshoot.troubleshoot_content

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.techinfo.R
import com.example.techinfo.api_connector.RetrofitInstance
import com.example.techinfo.api_connector.TroubleshootContent
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class TroubleshootContentFragment : Fragment() {

    private lateinit var articleTitleTextView: TextView
    private lateinit var youtubePlayerView: YouTubePlayerView // YouTubePlayerView
    private lateinit var contentTextView: TextView
    private lateinit var createdTimeTextView: TextView
    private lateinit var updatedTimeTextView: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var scrollView: ScrollView
    private var articleId: Int? = null // Store article ID
    private var isFullscreen: Boolean = false // Track fullscreen state

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
        youtubePlayerView = view.findViewById(R.id.youtubePlayerView) // Initialize YouTubePlayerView
        contentTextView = view.findViewById(R.id.contentTextView)
        createdTimeTextView = view.findViewById(R.id.createdTimeTextView)
        updatedTimeTextView = view.findViewById(R.id.updatedTimeTextView)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        scrollView = view.findViewById(R.id.scrollView)

        // Add YouTubePlayerView to the lifecycle (important)
        lifecycle.addObserver(youtubePlayerView)

        // Get the article ID from arguments
        articleId = arguments?.getString("ARTICLE_ID")?.toIntOrNull()

        // Fetch and display articles
        articleId?.let { fetchArticlesAndDisplay(it) }

        // Set up swipe refresh layout
        swipeRefreshLayout.setOnRefreshListener {
            articleId?.let { id -> fetchArticlesAndDisplay(id) }
        }

        // Disable swipe refresh when scrolling inside the ScrollView
        scrollView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_MOVE) {
                swipeRefreshLayout.isEnabled = false // Disable refresh when scrolling
            }
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                swipeRefreshLayout.isEnabled = true // Re-enable refresh when touch ends
            }
            false
        }

        // Load the YouTube player listener
        setupYouTubePlayerListener()
    }

    private fun setupYouTubePlayerListener() {
        youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                // Load video when player is ready
                val videoId = "your_video_id_here" // Replace with your video ID extraction logic
                youTubePlayer.loadVideo(videoId, 0f)

                // Handle full-screen toggling
                youtubePlayerView.setOnClickListener {
                    toggleFullScreen()
                }
            }
        })
    }

    // Toggle fullscreen mode manually
    private fun toggleFullScreen() {
        if (isFullscreen) {
            // Exit fullscreen: Portrait mode
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            isFullscreen = false
        } else {
            // Enter fullscreen: Landscape mode
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            isFullscreen = true
        }
    }

    // Load video into YouTubePlayerView
    private fun loadYouTubeVideo(videoUrl: String?) {
        if (!videoUrl.isNullOrEmpty()) {
            youtubePlayerView.visibility = View.VISIBLE

            // Extract the video ID from the YouTube URL
            val videoId = when {
                videoUrl.contains("youtu.be/") -> {
                    videoUrl.substringAfter("youtu.be/").substringBefore("?")
                }
                videoUrl.contains("youtube.com/watch?v=") -> {
                    videoUrl.substringAfter("v=").substringBefore("&")
                }
                else -> ""
            }

            // Load the video using the YouTube Player
            if (videoId.isNotEmpty()) {
                youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.loadVideo(videoId, 0f)
                    }
                })
            } else {
                youtubePlayerView.visibility = View.GONE
            }
        } else {
            youtubePlayerView.visibility = View.GONE
        }
    }

    // Fetch article and update the UI
    private fun fetchArticlesAndDisplay(articleId: Int) {
        swipeRefreshLayout.isRefreshing = true

        // Use RetrofitInstance to get the ApiService
        val apiService = RetrofitInstance.getApiService()

        apiService.getTroubleshootArticles().enqueue(object : Callback<List<TroubleshootContent>> {
            override fun onResponse(
                call: Call<List<TroubleshootContent>>,
                response: Response<List<TroubleshootContent>>
            ) {
                swipeRefreshLayout.isRefreshing = false

                if (response.isSuccessful) {
                    val articles = response.body()
                    if (articles != null) {
                        val article = articles.find { it.id == articleId }
                        if (article != null) {
                            articleTitleTextView.text = article.title
                            contentTextView.text = article.content
                            createdTimeTextView.text = "Created: ${formatDate(article.createdAt)}"
                            updatedTimeTextView.text = "Updated: ${formatDate(article.updatedAt)}"

                            // Load YouTube video if available
                            loadYouTubeVideo(article.videoEmbed)
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
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(requireContext(), "No connection or server error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun formatDate(dateString: String): String {
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

    // Ensure the YouTube player stops playing when the fragment is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        youtubePlayerView.release() // Release the player
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
