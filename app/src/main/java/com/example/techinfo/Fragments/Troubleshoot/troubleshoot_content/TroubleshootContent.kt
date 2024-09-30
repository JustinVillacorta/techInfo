package com.example.techinfo.Fragments.Troubleshoot.troubleshoot_content

import com.google.gson.annotations.SerializedName

data class TroubleshootContent(
    val id: Int,
    val title: String,
    val content: String,

    // Use SerializedName to map JSON field to Kotlin property
    @SerializedName("video_embed") val videoEmbed: String?, // Correct the property name
    val createdAt: String,
    val updatedAt: String
)
