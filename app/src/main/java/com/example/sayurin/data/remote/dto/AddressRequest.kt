package com.example.sayurin.data.remote.dto

data class AddressRequest(
    val user_id: Int,
    val label: String? = null,
    val penerima_nama: String,
    val penerima_hp: String,
    val alamat_lengkap: String,
    val destination_id: String,
    val subdistrict: String?,
    val district: String?,
    val city: String?,
    val province: String?,
    val zip_code: String?,
    val is_default: Int = 0
)
