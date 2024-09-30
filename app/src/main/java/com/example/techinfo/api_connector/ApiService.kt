package com.example.techinfo.api_connector

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import com.google.gson.annotations.SerializedName

data class TroubleShoot_data(
    val title: String,
    val id: String  // Change to String
)

data class TroubleshootContent(
    val id: Int,
    val title: String,
    val content: String,

    // Use SerializedName to map JSON field to Kotlin property
    @SerializedName("video_embed") val videoEmbed: String?, // Correct the property name
    @SerializedName("created_at") val createdAt: String,   // Correct the property name
    @SerializedName("updated_at") val updatedAt: String    // Correct the property name
)

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
