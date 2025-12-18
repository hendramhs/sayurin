package com.example.sayurin.data.remote.dto

data class PilihPengirimanRequest(
    val pesanan_id: Int,
    val shipping_name: String,
    val service_name: String,
    val shipping_cost: Int,
    val service_fee: Int = 0,
    val is_cod: Int = 0,
    val etd: String?,
    val shipper_destination_id: String,
    val receiver_destination_id: String,
    val berat: Int
)
