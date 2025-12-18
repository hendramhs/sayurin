package com.example.sayurin.data.remote.dto

data class SayurDto(
    val sayur_id: Int? = null,
    val nama_sayur: String,
    val harga: Int,
    val stok: Int,
    val satuan: String
)
