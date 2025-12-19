package com.example.sayurin.data.remote.dto.pesanan

// 1. Request untuk Membuat Pesanan (Sesuai backend: butuh user_id, address_id, dan items)
data class CreatePesananRequest(
    val user_id: Int,
    val address_id: Int,
    val items: List<PesananItemRequest>
)

data class PesananItemRequest(
    val sayur_id: Int,
    val jumlah: Int
)

// 2. Response setelah Pesanan Berhasil dibuat (Untuk menangkap pesanan_id)
data class CreatePesananResponse(
    val success: Boolean,
    val message: String,
    val pesanan_id: Int,      // Ini yang tadi menyebabkan 'Unresolved reference'
    val total_barang: Int,
    val total_berat: Int
)

// 3. Class untuk menerima daftar pesanan (Response List)
data class PesananAdminResponse(
    val pesanan_id: Int,
    val nama: String?,// JOIN u.nama
    val tanggal: String,
    val total_barang: Int,
    val total_berat_kg: Int,     // AS total_berat_kg
    val ongkir: Int,
    val total_bayar: Int,
    val status: String,
    val shipping_name: String?,  // Dari tabel pengiriman
    val service_name: String?,
    val etd: String?,
    val shipper_destination_id: Int?,
    val receiver_destination_id: Int?,
    val kota_asal: String?,
    val kota_tujuan: String?,
    val shipping_cost: Int?,
    val service_fee: Int?
)

data class PesananUserResponse(
    val pesanan_id: Int,
    val tanggal: String,
    val total_barang: Int,
    val total_berat_kg: Int,
    val ongkir: Int,
    val total_bayar: Int,
    val status: String,
    val shipping_name: String?,
    val service_name: String?,
    val etd: String?,
    val kota_asal: String?,
    val kota_tujuan: String?
)

// 4. Class untuk detail pesanan (Sesuai hasil JOIN s.nama_sayur di backend)
data class DetailPesananResponse(
    val nama_sayur: String,
    val satuan: String,
    val jumlah: Int,
    val harga_satuan: Int,
    val subtotal: Int
)

// 5. Class untuk update status
data class StatusRequest(
    val status: String
)
