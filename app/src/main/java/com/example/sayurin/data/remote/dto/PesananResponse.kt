package com.example.sayurin.data.remote.dto

data class PesananResponse(
    val success: Boolean,
    val message: String? = null,
    val pesanan_id: Int,
    val total_barang: Int,
    val total_berat: Int
)