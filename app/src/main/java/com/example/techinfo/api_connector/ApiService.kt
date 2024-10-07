package com.example.techinfo.api_connector

import retrofit2.Call
import retrofit2.http.GET
import com.google.gson.annotations.SerializedName

data class Processor(
    val processor_id: Int,
    val processor_name: String,
    val brand: String,
    val socket_type: String,
    val base_clock_speed: String,
    val max_clock_speed: String,
    val link: String?
)

data class Gpu(
    val gpu_id: Int,
    val gpu_name: String,
    val brand: String,
    val interface_type: String,
    val tdp_wattage: Int,
    val gpu_length_mm: Int,
    val link: String?
)

data class ScreenResolution(
    val screen_resolutions_id: Int,
    val resolution_size: String,
    val resolutions_name: String
)

data class JsonData(
    val processors: List<Processor>,
    val gpuses: List<Gpu>,
    val screen_resolutions: List<ScreenResolution>
)

data class TroubleShoot_data(
    val title: String,
    val id: String
)

data class TroubleshootContent(
    val id: Int,
    val title: String,
    val content: String,
    @SerializedName("video_embed") val videoEmbed: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)

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

    @GET("accounts") // Adjust the endpoint according to your API
    fun getUsers(): Call<List<User>>

    @GET("jsondata") // New endpoint to fetch the JSON data
    fun getJsonData(): Call<JsonData>
}
