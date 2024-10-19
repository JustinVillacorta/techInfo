package com.example.techinfo.Fragments.Admin

data class admin_data_class(
    var processorId: Int,
    var ModelName: String,
    var Specs: String,
    var Category: String,
    var brand: String,
    var socket_type: String,
    var base_clock_speed: Float,  // Should be Float as you're converting the string to a float
    var max_clock_speed: Float,   // Should be Float as you're converting the string to a float
    var power: String,
    var compatible_chipsets: String,
    var link: String,
    var created_at: String,
    var updated_at: String
)


