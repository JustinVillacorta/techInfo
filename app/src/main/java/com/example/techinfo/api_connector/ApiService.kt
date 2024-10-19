package com.example.techinfo.api_connector

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.google.gson.annotations.SerializedName
import retrofit2.http.PUT
import retrofit2.http.Path
import java.io.Serializable

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
    val processor_name: String,
    val brand: String,
    val socket_type: String,
    val compatible_chipsets: String,
    val power: String,
    val base_clock_speed: String,  // Keep this as String for initial response
    val max_clock_speed: String,   // Keep this as String for initial response
    val link: String,
    val created_at: String,
    val updated_at: String
) : Serializable  // Make it Serializable

// GPU model
data class Gpu(
    val gpu_id: Int,
    val gpu_name: String,
    val brand: String,
    val interface_type: String,
    val tdp_wattage: Int,
    val gpu_length_mm: Int,
    val memory_size_gb: Int,
    val memory_type: String,
    val memory_interface_bits: String,
    val base_clock_ghz: String,
    val game_clock_ghz: String,
    val boost_clock_ghz: String,
    val compute_units: Int,
    val stream_processors: Int,
    val required_power: Int,
    val required_6_pin_connectors: Int,
    val required_8_pin_connectors: Int,
    val required_12_pin_connectors: Int,
    val link: String?,
    val created_at: String,
    val updated_at: String
) : Serializable


// Motherboard model
data class Motherboard(
    val motherboard_id: Int,
    val motherboard_name: String,
    val brand: String,
    val socket_type: String,
    val chipset: String,
    val link: String?,
    val wifi: String,
    val gpu_support: String,
    val created_at: String,
    val updated_at: String
) : Serializable  // Make it Serializable

// RAM model
data class Ram(
    val ram_id: Int,
    val ram_name: String,
    val brand: String,
    val ram_capacity_gb: String,
    val speed: String,
    val link: String?,
    val created_at: String,
    val updated_at: String
) : Serializable  // Make it Serializable

// Power Supply Unit model
data class PowerSupplyUnit(
    val psu_id: Int,
    val psu_name: String,
    val brand: String,
    val wattage: String,
    val link: String?,
    val created_at: String,
    val updated_at: String
) : Serializable  // Make it Serializable

// Case model
data class Case(
    val case_id: Int,
    val case_name: String,
    val brand: String,
    val form_factor: String,
    val link: String?,
    val created_at: String,
    val updated_at: String
) : Serializable  // Make it Serializable

// CPU Cooler model
data class CpuCooler(
    val cooler_id: Int,
    val cooler_name: String,
    val brand: String,
    val socket_type_supported: String,
    val max_cooler_height_mm: Int,
    val link: String?,
    val created_at: String,
    val updated_at: String
) : Serializable  // Make it Serializable

// HDD model
data class Hdd(
    val hdd_id: Int,
    val hdd_name: String,
    val brand: String,
    val capacity_gb: String,
    val link: String?,
    val created_at: String,
    val updated_at: String
) : Serializable  // Make it Serializable

// SSD model
data class Ssd(
    val ssd_id: Int,
    val ssd_name: String,
    val capacity_gb: String,
    val interface_type: String,
    val link: String?,
    val created_at: String,
    val updated_at: String
) : Serializable  // Make it Serializable

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

    @GET("accounts")
    fun getUsers(): Call<List<User>>

    // Request to send OTP to email
    @POST("admin/request-reset")
    fun requestOTP(@Body request: OTPRequest): Call<Void>

    // Request to reset password with OTP and new password
    @POST("admin/reset-password")
    fun resetPassword(@Body request: PasswordResetRequest): Call<Void>

    // PUT method to update a processor
    @PUT("processors/{id}")
    fun updateProcessor(@Path("id") processorId: Int, @Body processor: Processor): Call<Processor>

    // Component APIs
    @GET("processors")
    fun getProcessors(): Call<List<Processor>>

    @GET("gpuses")
    fun getGpus(): Call<List<Gpu>>

    @GET("rams")
    fun getRams(): Call<List<Ram>>

    @GET("motherboards")
    fun getMotherboards(): Call<List<Motherboard>>  // Added motherboard endpoint

    @GET("power_supply_units")
    fun getPowerSupplyUnits(): Call<List<PowerSupplyUnit>>  // Added PSU endpoint

    @GET("computer_cases")
    fun getComputerCases(): Call<List<Case>>  // Added case endpoint

    @GET("cpu_coolers")
    fun getCpuCoolers(): Call<List<CpuCooler>>  // Added CPU cooler endpoint

    @GET("hdds")
    fun getHdds(): Call<List<Hdd>>  // Added HDD endpoint

    @GET("ssds")
    fun getSsds(): Call<List<Ssd>>  // Added SSD endpoint
}

