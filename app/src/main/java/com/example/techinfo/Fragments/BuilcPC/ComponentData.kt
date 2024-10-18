package com.example.techinfo.Fragments.BuildPC

import com.example.techinfo.api_connector.*
import java.io.Serializable

data class ComponentData(
    var name: String,                    // Name of the component
    var type: String,                    // Type of the component (e.g., "CPU", "GPU", etc.)
    var partDetails: String = "",        // Name of Models
    var isSelected: Boolean = false,     // Whether the component is selected or not
    val processor: Processor? = null,    // For Processor (can be a list or single)
    val gpu: Gpu? = null,                // For GPU
    val motherboard: Motherboard? = null,// For Motherboard
    val ram: Ram? = null,                // For RAM
    val psu: PowerSupplyUnit? = null,    // For PSU
    val case: Case? = null,              // For Case
    val cpuCooler: CpuCooler? = null,    // For CPU Cooler
    val hdd: Hdd? = null,                // For HDD
    val ssd: Ssd? = null                 // For SSD
) : Serializable

data class BuildPCComponents(
    var cpuList: MutableList<ComponentData> = mutableListOf(),
    var gpuList: MutableList<ComponentData> = mutableListOf(),
    var ramList: MutableList<ComponentData> = mutableListOf(),
    var psuList: MutableList<ComponentData> = mutableListOf(),
    var caseList: MutableList<ComponentData> = mutableListOf(),
    var cpuCoolerList: MutableList<ComponentData> = mutableListOf(),
    var hddList: MutableList<ComponentData> = mutableListOf(),
    var ssdList: MutableList<ComponentData> = mutableListOf(),
    var motherboardList: MutableList<ComponentData> = mutableListOf()
) : Serializable
