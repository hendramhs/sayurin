package com.example.sayurin.data.remote.dto

data class PesananAdminDto(
    val pesanan_id: Int,
    val nama: String,
    val tanggal: String?,
    val total_barang: Int,
    val ongkir: Int,
    val total_bayar: Int,
    val status: String
)
