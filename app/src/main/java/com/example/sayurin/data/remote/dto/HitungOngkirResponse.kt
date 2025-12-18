package com.example.sayurin.data.remote.dto

data class HitungOngkirResponse(
    val success: Boolean,
    val layanan: List<LayananOngkirDto>
)

data class LayananOngkirDto(
    val service_name: String,
    val shipping_name: String,
    val shipping_cost: Int,
    val etd: String?,
    val is_cod: Int?
)
