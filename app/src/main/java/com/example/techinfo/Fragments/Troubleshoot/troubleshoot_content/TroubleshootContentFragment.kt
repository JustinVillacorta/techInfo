package com.example.techinfo.Fragments.Troubleshoot.troubleshoot_content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.techinfo.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.techinfo.Fragments.Troubleshoot.troubleshootContent.TroubleshootContent

class TroubleshootContentFragment : Fragment() {
    private lateinit var detailTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_troubleshoot_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailTextView = view.findViewById(R.id.detailTextView)
        val backButton: ImageButton = view.findViewById(R.id.btnBack)

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val articleId = arguments?.getString("ARTICLE_ID") ?: "" // Retrieve as String
        fetchArticleContent(articleId) // Fetch article content using ID
    }

    private fun fetchArticleContent(id: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.226.205/") // Adjust to match your IP address
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.getTroubleshootArticle(id.toInt()).enqueue(object : Callback<TroubleshootContent> { // Convert to Int if needed
            override fun onResponse(
                call: Call<TroubleshootContent>,
                response: Response<TroubleshootContent>
            ) {
                if (response.isSuccessful) {
                    val article = response.body()
                    if (article != null) {
                        // Display title and content
                        detailTextView.text = "${article.title}\n\n${article.content}"
                    }
                } else {
                    detailTextView.text = "Error: Failed to load article."
                }
            }

            override fun onFailure(call: Call<TroubleshootContent>, t: Throwable) {
                detailTextView.text = "Failed to load article: ${t.message}"
            }
        })
    }

    companion object {
        fun newInstance(articleId: String): TroubleshootContentFragment {
            val fragment = TroubleshootContentFragment()
            val args = Bundle()
            args.putString("ARTICLE_ID", articleId) // Use putString
            fragment.arguments = args
            return fragment
        }
    }
}
