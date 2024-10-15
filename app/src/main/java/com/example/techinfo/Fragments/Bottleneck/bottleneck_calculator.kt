package com.example.techinfo.Fragments.Bottleneck

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.techinfo.R

class bottleneck_calculator : Fragment() {

    // Declare view components
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var cardView: CardView
    private lateinit var videoThumbnailImageView: ImageView
    private lateinit var articleTitleTextView: TextView
    private lateinit var contentTextView: TextView
    private lateinit var createdTimeTextView: TextView
    private lateinit var updatedTimeTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bottleneck_calculator, container, false)

        // Initialize views
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        cardView = view.findViewById(R.id.cardView)
        videoThumbnailImageView = view.findViewById(R.id.videoThumbnailImageView)
        articleTitleTextView = view.findViewById(R.id.articleTitleTextView)
        contentTextView = view.findViewById(R.id.contentTextView)
        createdTimeTextView = view.findViewById(R.id.createdTimeTextView)
        updatedTimeTextView = view.findViewById(R.id.updatedTimeTextView)

        // Set up any necessary logic, listeners, etc.
        swipeRefreshLayout.setOnRefreshListener {
            // Code to handle refresh action
            swipeRefreshLayout.isRefreshing = false
        }

        // You can set sample data for testing here

        articleTitleTextView.text = "Sample Article Title"
        contentTextView.text = "This is a sample article content to be displayed in the TextView."
        createdTimeTextView.text = "Created: 01 Jan 2024"
        updatedTimeTextView.text = "Updated: 05 Jan 2024"

        return view
    }
}
