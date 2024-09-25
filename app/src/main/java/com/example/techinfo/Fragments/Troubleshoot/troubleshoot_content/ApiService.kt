package com.example.techinfo.Fragments.Troubleshoot.troubleshoot_content

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.techinfo.Fragments.Troubleshoot.troubleshootContent.TroubleshootContent

interface ApiService {
    @GET("troubleshoot_articles.php")
    fun getTroubleshootArticles(): Call<List<TroubleshootContent>>

    @GET("troubleshoot_articles.php")
    fun getTroubleshootArticle(@Query("id") id: Int): Call<TroubleshootContent> // Use Int here
}
