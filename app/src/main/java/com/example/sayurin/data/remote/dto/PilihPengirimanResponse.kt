package com.example.sayurin.data.remote.dto

data class PilihPengirimanResponse(
    val success: Boolean,
    val message: String,
    val ongkir: Int? = null,
    val total_bayar: Int? = null
)
