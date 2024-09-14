package com.example.techinfo.Fragments.Troubleshoot.troubleshoot_content

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("get_articles.php")  // Replace with your actual PHP script URL
    fun getArticles(): Call<List<troubleshootData>>
}
