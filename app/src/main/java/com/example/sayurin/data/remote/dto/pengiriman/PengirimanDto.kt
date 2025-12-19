package com.example.sayurin.data.remote.dto.pengiriman

// --- HITUNG ONGKIR V1 (Existing) ---
data class OngkirRequest(
    val origin: Int,
    val destination: Int,
    val weight: Int
)

data class OngkirResponse(
    val success: Boolean,
    val message: String,
    val data: List<OngkirData>
)

data class OngkirData(
    val courier_name: String,
    val service: String,
    val cost: Int,
    val etd: String
)

// --- HITUNG ONGKIR V2 (Baru sesuai Backend hitungOngkirV2) ---

data class OngkirRequestV2(
    val user_id: Int,
    val receiver_destination_id: Int
)

data class OngkirResponseV2(
    val success: Boolean,
    val item_value: Int,
    val weight: Int,
    val shipper_destination_id: Int,
    val layanan: List<LayananPengiriman>
)

data class LayananPengiriman(
    val shipping_name: String,
    val service_name: String,
    val weight: Int,
    val is_cod: Boolean,
    val shipping_cost: Int,
    val service_fee: Int = 0, // Ditambahkan sesuai backend pilihPengiriman
    val etd: String,
    val grandtotal: Int
)

// --- PILIH PENGIRIMAN (Update sesuai Backend pilihPengiriman) ---

data class PilihPengirimanRequest(
    val pesanan_id: Int,
    val shipping_name: String,
    val service_name: String,
    val shipping_cost: Int,
    val service_fee: Int = 0,
    val is_cod: Int = 0, // Menggunakan Int (0/1) sesuai backend tinyint
    val etd: String,
    val shipper_destination_id: Int,
    val receiver_destination_id: Int,
    val berat: Int
)

data class PilihPengirimanResponse(
    val success: Boolean,
    val message: String,
    val ongkir: Int,
    val total_bayar: Int
)
