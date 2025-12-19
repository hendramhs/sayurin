package com.example.sayurin.data.remote.dto.pesanan

// Class untuk mengirim data (Request)
data class CreatePesananRequest(
    val user_id: Int,
    val address_id: Int
)

// Class untuk menerima daftar pesanan (Response)
data class PesananAdminResponse(
    val pesanan_id: Int,
    val user_id: Int,
    val status: String,
    val total_bayar: Int,
    val tanggal: String,
    val nama_pembeli: String // Tambahan info dari Join Table di Backend
)

// Class untuk detail pesanan
data class DetailPesananResponse(
    val detail_id: Int,
    val nama_sayur: String,
    val jumlah: Int,
    val subtotal: Int
)

// Class untuk update status
data class StatusRequest(
    val status: String
)
