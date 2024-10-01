package com.example.techinfo.api_connector

import retrofit2.Call
import retrofit2.http.GET
import com.google.gson.annotations.SerializedName

data class TroubleShoot_data(
    val title: String,
    val id: String  // Changed to String
)

data class TroubleshootContent(
    val id: Int,
    val title: String,
    val content: String,

    @SerializedName("video_embed") val videoEmbed: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)

// New data class for user authentication
data class User(
    val id: Int,
    val username: String,
    val password: String,
    val createdAt: String,
    val updatedAt: String
)

interface ApiService {
    @GET("troubleshoot_articles")
    fun getTroubleshootArticles(): Call<List<TroubleshootContent>>

    // New endpoint to fetch user data
    @GET("accounts") // Adjust the endpoint according to your API
    fun getUsers(): Call<List<User>>
}
