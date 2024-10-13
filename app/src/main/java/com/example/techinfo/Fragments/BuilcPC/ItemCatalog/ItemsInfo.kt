package com.example.techinfo.Fragments.BuilcPC.ItemCatalog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.techinfo.R

class ItemsInfo : Fragment() {

    private lateinit var articleTitle: String
    private lateinit var content: String
    private lateinit var createdTime: String
    private lateinit var updatedTime: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            articleTitle = it.getString(ARG_ARTICLE_TITLE) ?: "No Title Available"
            content = it.getString(ARG_CONTENT) ?: "No Content Available"
            createdTime = it.getString(ARG_CREATED_TIME) ?: "No Created Time Available"
            updatedTime = it.getString(ARG_UPDATED_TIME) ?: "No Updated Time Available"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_items_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.articleTitleTextView).text = articleTitle
        view.findViewById<TextView>(R.id.contentTextView).text = content
        view.findViewById<TextView>(R.id.createdTimeTextView).text = createdTime
        view.findViewById<TextView>(R.id.updatedTimeTextView).text = updatedTime
    }

    companion object {
        private const val ARG_ARTICLE_TITLE = "article_title"
        private const val ARG_CONTENT = "content"
        private const val ARG_CREATED_TIME = "created_time"
        private const val ARG_UPDATED_TIME = "updated_time"

        fun newInstance(
            articleTitle: String,
            content: String,
            createdTime: String,
            updatedTime: String
        ) = ItemsInfo().apply {
            arguments = Bundle().apply {
                putString(ARG_ARTICLE_TITLE, articleTitle)
                putString(ARG_CONTENT, content)
                putString(ARG_CREATED_TIME, createdTime)
                putString(ARG_UPDATED_TIME, updatedTime)
            }
        }
    }
}