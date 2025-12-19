package com.example.sayurin.data.remote.dto.address

data class KomerceSearchResponse(
    val success: Boolean,
    val destinations: List<KomerceDestination>
)

data class KomerceDestination(
    val id: Int,
    val label: String, // Biasanya: "Kecamatan, Kota, Provinsi"
    val subdistrict_name: String?,
    val district_name: String?,
    val city_name: String?,
    val province_name: String?,
    val zip_code: String?
)
