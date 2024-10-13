package com.example.techinfo.Fragments.BuilcPC

data class ComponentData(
    var name: String,
    var partDetails: String? = null,
    var isSelected: Boolean = false,
    val model: String? = null,
    val chipset: String? = null

)
