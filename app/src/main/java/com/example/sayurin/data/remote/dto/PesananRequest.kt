package com.example.sayurin.data.remote.dto

data class PesananRequest(
    val user_id: Int,
    val address_id: Int,
    val items: List<PesananItemDto>
)

data class PesananItemDto(
    val sayur_id: Int,
    val jumlah: Int
)