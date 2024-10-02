package com.example.techinfo.Fragments.BuilcPC
// data class for recyclerView In BuildPC
data class ComponentData(
    var name: String?,
    var partDetails: String? = null,
    var isSelected: Boolean = false
) // details can be optional
