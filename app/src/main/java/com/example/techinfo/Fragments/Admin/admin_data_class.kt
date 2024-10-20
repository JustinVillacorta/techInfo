package com.example.techinfo.Fragments.Admin

data class admin_data_class(
    val processorId: Int,
    var ModelName: String,  // Change from val to var
    var Specs: String,      // Change from val to var
    val Category: String,
    val brand: String,
    val socket_type: String,
    val base_clock_speed: Float,
    val max_clock_speed: Float,
    val power: String,
    val compatible_chipsets: String,
    val link: String?,
    val created_at: String,
    var updated_at: String // Consider changing this to var if you plan to modify it
)
