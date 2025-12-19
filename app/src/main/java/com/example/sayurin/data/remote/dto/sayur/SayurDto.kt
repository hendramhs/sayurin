package com.example.sayurin.data.remote.dto.sayur

// Response Model
data class SayurResponse(
    val sayur_id: Int,
    val nama_sayur: String,
    val harga: Int,
    val stok: Int,
    val satuan: String,
//    val gambar: String? = null // Sesuaikan jika ada field gambar di DB
)

// Request Model untuk Tambah/Update (Admin)
data class SayurRequest(
    val nama_sayur: String,
    val harga: Int,
    val stok: Int,
    val satuan: String
)

data class CommonResponse(
    val success: Boolean,
    val message: String
)