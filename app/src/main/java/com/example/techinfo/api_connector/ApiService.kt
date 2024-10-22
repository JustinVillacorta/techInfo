package com.example.techinfo.api_connector
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.google.gson.annotations.SerializedName
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
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
        val processor_id: String,
        val processor_name: String,
        val brand: String,
        val socket_type: String,
        val compatible_chipsets: String,
        val cores: String,
        val threads: String,
        val base_clock_speed: String,
        val max_turbo_boost_clock_speed: String,
        val tdp: String,
        val cache_size_mb: String,
        val integrated_graphics: String,
        val link: String,
        val performance_score: String,
        val created_at: String,
        val updated_at: String
    ) : Serializable  // Make it Serializable

// Resolution model
data class Resolution(
    val screen_resolutions_id: String,
    val resolution_size: String,
    val resolutions_name: String,
    val created_at: String,
    val updated_at: String
) : Serializable

// GPU model
data class Gpu(
    val gpu_id: Int,
    val gpu_name: String,
    val brand: String,
    val cuda_cores: String,
    val interface_type: String,
    val tdp_wattage: String,
    val gpu_length_mm: String,
    val memory_size_gb: String,
    val memory_type: String,
    val memory_interface_bits: String,
    val base_clock_ghz: String,
    val game_clock_ghz: String,
    val boost_clock_ghz: String,
    val compute_units: String,
    val stream_processors: String,
    val required_power: String,
    val required_6_pin_connectors: String,
    val required_8_pin_connectors: String,
    val required_12_pin_connectors: String,
    val link: String?,
    val performance_score: String,
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
    val link: String?,                // Nullable field
    val wifi: String,                 // Yes/No or other representation for Wi-Fi
    val gpu_support: String,          // GPU support information
    val max_ram_slots: Int,           // Maximum number of RAM slots
    val max_ram_capacity: String,     // Maximum RAM capacity (e.g., "128 GB")
    val max_ram_speed: String,        // Maximum RAM speed (e.g., "5000 MHz")
    val ram_type: String,             // Type of RAM supported (e.g., DDR4)
    val has_pcie_slot: Int,           // Whether it has a PCIe slot (1 for Yes, 0 for No)
    val has_sata_ports: Int,          // Whether it has SATA ports (1 for Yes, 0 for No)
    val has_m2_slot: Int,             // Whether it has an M.2 slot (1 for Yes, 0 for No)
    val gpu_interface: String,        // GPU interface type (e.g., PCIe 4.0)
    val form_factor: String,          // Form factor (e.g., ATX, Micro-ATX)
    val performance_score: String,
    val created_at: String,           // Creation timestamp
    val updated_at: String            // Last updated timestamp
) : Serializable  // Ensure class implements Serializable interface

// RAM model
data class Ram(
    val ram_id: Int,
    val ram_name: String,
    val brand: String,
    val ram_capacity_gb: String,
    val ram_speed_mhz: String,
    val power_consumption: String,
    val link: String?,
    val performance_score: String,
    val created_at: String,
    val updated_at: String
) : Serializable  // Make it Serializable

// Power Supply Unit model
data class PowerSupplyUnit(
    val psu_id: Int,                          // PSU ID
    val psu_name: String,                     // PSU name
    val brand: String,                        // Brand of the PSU
    val wattage: String,                      // Total wattage (e.g., "750W")
    val continuous_wattage: String,           // Continuous wattage (e.g., "750W")
    val gpu_6_pin_connectors: Int,            // Number of 6-pin connectors
    val gpu_8_pin_connectors: Int,            // Number of 8-pin connectors
    val gpu_12_pin_connectors: Int,           // Number of 12-pin connectors
    val efficiency_rating: String,            // Efficiency rating (e.g., "80+ Gold")
    val has_required_connectors: Int,         // Whether it has the required connectors
    val performance_score: String,
    val created_at: String,                   // Creation timestamp
    val updated_at: String                    // Last updated timestamp
) : Serializable  // Make it Serializable

// Case model
data class Case(
    val case_id: Int,                     // Case ID
    val case_name: String,                // Case name
    val brand: String,                    // Brand of the case
    val form_factor_supported: String,    // Supported form factors (e.g., "ATX, Micro-ATX, Mini-ITX")
    val max_gpu_length_mm: String,        // Maximum GPU length in mm
    val max_hdd_count: Int,               // Maximum number of HDDs supported
    val max_ssd_count: Int,               // Maximum number of SSDs supported
    val current_hdd_count: Int,           // Current number of installed HDDs
    val current_ssd_count: Int,           // Current number of installed SSDs
    val airflow_rating: String,           // Airflow rating (e.g., low, medium, high)
    val max_cooler_height_mm: String,     // Maximum CPU cooler height in mm
    val created_at: String,               // Creation timestamp
    val updated_at: String                // Last updated timestamp
) : Serializable  // Make it Serializable

// CPU Cooler model
data class CpuCooler(
    val cooler_id: Int,
    val cooler_name: String,
    val brand: String,
    val tdp_rating: String,
    val socket_type_supported: String,
    val max_cooler_height_mm: String,
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
    val performance_score: String,
    val updated_at: String
) : Serializable  // Make it Serializable

// SSD model
data class Ssd(
    val ssd_id: Int,
    val ssd_name: String,
    val capacity_gb: String,
    val interface_type: String,
    val link: String?,
    val performance_score: String,
    val created_at: String,
    val updated_at: String
) : Serializable  // Make it Serializable

data class CompatibilityResponse(
    val is_compatible: Boolean,  // A flag indicating if the components are compatible
    val issues: List<String>,    // List of compatibility issues, if any
    val components: SelectedComponents // A nested class to hold selected components
)

// A data class to hold the selected components for troubleshooting purposes
data class SelectedComponents(
    val processor: String?,
    val motherboard: String?,
    val ram: String?,
    val gpu: String?,
    val psu: String?,
    val case: String?,
    val cooler: String?,
    val hdd: String?,
    val ssd: String?
)


data class BottleneckRequest(
    val processor_name: String,
    val gpu_name: String,
    val resolutions_name: String,
)

data class BottleneckResponse(
    val bottleneck: String,
    val cpuScore: String,
    val gpuScore: String,
    val resolution_modifier: String,
    val percentage_difference: Double,
    val message: String
)

data class BuildComparisonRequest(
    val build_one: BuildData,
    val build_two: BuildData
)

data class BuildData(
    val processor_name: String,
    val gpu_name: String,
    val ram_name: String,
    val psu_name: String,
    val ssd_name: String,
    val hdd_name: String
)

data class BuildComparisonResponse(
    val build_one: BuildMetrics,
    val build_two: BuildMetrics
)

data class BuildMetrics(
    val processor_percentage: Double,
    val gpu_percentage: Double,
    val ram_percentage: Double,
    val psu_percentage: Double,
    val ssd_percentage: Double
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
    fun updateProcessor(@Path("id") processorId: String, @Body processor: Processor): Call<Processor>

    // Component APIs
    @GET("processors")
    fun getProcessors(): Call<List<Processor>>

    @GET("gpuses")
    fun getGpus(): Call<List<Gpu>>

    @GET("screen_resolutions")
    fun getResolution(): Call<List<Resolution>>

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

    @GET("compatibility_checker")
    fun checkCompatibility(
        @Query("processor_name") processorName: String,
        @Query("motherboard_name") motherboardName: String,
        @Query("ram_name") ramName: String,
        @Query("gpu_name") gpuName: String,
        @Query("psu_name") psuName: String,
        @Query("case_name") caseName: String,
        @Query("cooler_name") coolerName: String,
        @Query("hdd_name") hddName: String,
        @Query("ssd_name") ssdName: String
    ): Call<CompatibilityResponse> // Use a different return type if the API returns a response body

    @POST("bottleneck")
    fun postBottleneckData(@Body data: BottleneckRequest): Call<BottleneckResponse>

    @POST("pc_compare")
    fun getBuildComparison(@Body data: BuildComparisonRequest): Call<BuildComparisonResponse>

}
