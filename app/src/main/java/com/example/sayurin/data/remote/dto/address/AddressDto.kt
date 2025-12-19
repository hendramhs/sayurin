package com.example.sayurin.data.remote.dto.address

import com.example.sayurin.data.remote.dto.sayur.CommonResponse// Model untuk Add & Update Address
data class AddressRequest(
    val user_id: Int,
    val label: String?, // Contoh: Rumah, Kantor
    val penerima_nama: String,
    val penerima_hp: String,
    val alamat_lengkap: String,
    val destination_id: Int, // Penting untuk Komerce/Ongkir
    val subdistrict: String?,
    val district: String?,
    val city: String?,
    val province: String?,
    val zip_code: String?,
    val is_default: Int // 0 atau 1
)

// Response untuk List Alamat
data class AddressListResponse(
    val success: Boolean,
    val data: List<AddressResponse>
)

// Response untuk Single Alamat (Default)
data class DefaultAddressResponse(
    val success: Boolean,
    val data: AddressResponse?
)

data class AddressResponse(
    val address_id: Int,
    val user_id: Int,
    val label: String,
    val penerima_nama: String,
    val penerima_hp: String,
    val alamat_lengkap: String,
    val destination_id: Int,
    val subdistrict: String,
    val district: String,
    val city: String,
    val province: String,
    val zip_code: String,
    val is_default: Int
)

data class SetDefaultAddressRequest(
    val user_id: Int,
    val address_id: Int
)
