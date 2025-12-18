package com.example.sayurin.data.remote.dto

data class HitungOngkirRequest(
    val pesanan_id: Int,
    val shipper_destination_id: String,
    val receiver_destination_id: String,
    val weight: Int
)
