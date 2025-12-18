package com.example.sayurin.data.remote.dto

data class LoginResponse(
    val success: Boolean,
    val user_id: Int,
    val nama: String,
    val role: String
)