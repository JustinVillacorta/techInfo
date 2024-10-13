package com.example.techinfo.api_connector

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.google.gson.annotations.SerializedName

// Data class for OTP request
data class OTPRequest(
    val email: String
)

// Data class for password reset request
data class PasswordResetRequest(
    val email: String,
    val token: String,
    val password: String
)

// Processor model
data class Processor(
    val processor_id: Int,
    val processor_name: String,   // Can be used as the model
    val brand: String,
    val socket_type: String,
    val compatible_chipsets: String, // Mapping to chipset
    val power: Int,
    val base_clock_speed: String,
    val max_clock_speed: String,
    val link: String?,
    val created_at: String,
    val updated_at: String
)

// GPU model
data class Gpu(
    val gpu_id: Int,
    val gpu_name: String,
    val brand: String,
    val interface_type: String,
    val tdp_wattage: Int,
    val gpu_length_mm: Int,
    val link: String?
)

// Motherboard model
data class Motherboard(
    val motherboard_id: Int,
    val motherboard_name: String,
    val brand: String,
    val socket_type: String,
    val chipset: String,
    val link: String?
)

// RAM model
data class Ram(
    val ram_id: Int,
    val ram_name: String,
    val brand: String,
    val capacity: String,
    val speed: String,
    val link: String?
)

// Power Supply Unit model
data class PowerSupplyUnit(
    val psu_id: Int,
    val psu_name: String,
    val brand: String,
    val wattage: String,
    val link: String?
)

// Case model
data class Case(
    val case_id: Int,
    val case_name: String,
    val brand: String,
    val form_factor: String,
    val link: String?
)

// CPU Cooler model
// CPU Cooler model class
data class CpuCooler(
    val cooler_id: Int,
    val cooler_name: String,
    val brand: String,
    val socket_type_supported: String,
    val max_cooler_height_mm: Int,
    val link: String?,
    val created_at: String,
    val updated_at: String
)


// HDD model
data class Hdd(
    val hdd_id: Int,
    val hdd_name: String,
    val capacity: String,
    val type: String,
    val link: String?
)

// SSD model
data class Ssd(
    val ssd_id: Int,
    val ssd_name: String,
    val capacity: String,
    val type: String,
    val link: String?
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

    // Request to send OTP to email
    @POST("admin/request-reset")
    fun requestOTP(@Body request: OTPRequest): Call<Void>

    // Request to reset password with OTP and new password
    @POST("admin/reset-password")
    fun resetPassword(@Body request: PasswordResetRequest): Call<Void>

    @GET("processors")
    fun getProcessors(): Call<List<Processor>>

    @GET("motherboards")
    fun getMotherboards(): Call<List<Motherboard>>

    @GET("gpuses")
    fun getGpus(): Call<List<Gpu>>

    @GET("rams")
    fun getRams(): Call<List<Ram>>

    @GET("power_supply_units")
    fun getPowerSupplyUnits(): Call<List<PowerSupplyUnit>>

    @GET("computer_cases")
    fun getComputerCases(): Call<List<Case>>

    @GET("cpu_coolers")
    fun getCpuCoolers(): Call<List<CpuCooler>>

    @GET("hdds")
    fun getHdds(): Call<List<Hdd>>

    @GET("ssds")
    fun getSsds(): Call<List<Ssd>>
}
