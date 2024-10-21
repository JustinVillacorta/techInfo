package com.example.techinfo.Fragments.Admin

data class admin_data_class(
    val processorId: String,  // Processor ID as String, matches processor_id in Processor class
    var ModelName: String,    // Model Name, corresponds to processor_name
    var Specs: String,        // Specifications field (not in Processor, but in admin_data_class for UI purposes)
    val Category: String,     // Category field (e.g., "CPU") for UI
    val brand: String,        // Brand, matches 'brand' in Processor class
    val socket_type: String,  // Socket type, matches 'socket_type' in Processor class
    val cores: String,        // Cores, matches 'cores' in Processor class
    val threads: String,      // Threads, matches 'threads' in Processor class
    val base_clock_speed: String,  // Base clock speed, matches 'base_clock_speed' in Processor class
    val max_turbo_boost_clock_speed: String,  // Max clock speed, matches 'max_turbo_boost_clock_speed' in Processor class
    val tdp: String,          // Thermal design power, matches 'tdp' in Processor class
    val cache_size_mb: String,  // Cache size in MB, matches 'cache_size_mb' in Processor class
    val integrated_graphics: String? = null,  // Integrated graphics, matches 'integrated_graphics' in Processor class
    val compatible_chipsets: String,  // Compatible chipsets, matches 'compatible_chipsets' in Processor class
    val link: String?,        // Link (URL), nullable as in Processor class
    val performance_score: String,
    val created_at: String,   // Created timestamp, matches 'created_at' in Processor class
    var updated_at: String    // Updated timestamp, matches 'updated_at' in Processor class
)

