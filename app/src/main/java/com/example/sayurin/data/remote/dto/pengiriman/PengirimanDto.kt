package com.example.sayurin.data.remote.dto.pengiriman

import com.example.sayurin.data.remote.dto.sayur.CommonResponse

// 1. Request untuk hitung ongkir
data class OngkirRequest(
    val origin: Int,        // ID Kecamatan Asal (Gudang)
    val destination: Int,   // ID Kecamatan Tujuan (Alamat User)
    val weight: Int         // Berat dalam gram
)

// 2. Response dari API hitung ongkir
data class OngkirResponse(
    val success: Boolean,
    val message: String,
    val data: List<OngkirData>
)

data class OngkirData(
    val courier_name: String,   // Contoh: "J&T", "Sicepat"
    val service: String,        // Contoh: "REG", "OKE"
    val cost: Int,              // Harga ongkir
    val etd: String             // Estimasi sampai (contoh: "1-2 days")
)

// 3. Request untuk memilih kurir (jika ada fitur pilih kurir)
data class PilihPengirimanRequest(
    val user_id: Int,
    val courier_name: String,
    val service: String,
    val cost: Int
)
