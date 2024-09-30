package com.example.techinfo.Fragments.Troubleshoot.troubleshoot_content

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


data class ApiResponse(
    val status: Boolean,
    val message: String,
    val data: TroubleshootContent // `data` contains the article object
)

interface ApiService {
    @GET("troubleshoot_articles")
    fun getTroubleshootArticles(): Call<List<TroubleshootContent>>

    @GET("troubleshoot_articles/{id}") // Change to fetch single article by ID
    fun getTroubleshootArticle(@Path("id") id: String): Call<ApiResponse> // Return ApiResponse
}
