package com.example.sayurin.data.remote.dto.auth

// Response umum untuk Register & Login
data class AuthResponse(
    val success: Boolean,
    val message: String? = null,
    val user_id: Int? = null,
    val nama: String? = null,
    val role: String? = null // 'admin' atau 'client'
)

// Request untuk Login (sesuai auth.controller.js)
data class LoginRequest(
    val no_hp: String,
    val password: String
)

// Request untuk Register
data class RegisterRequest(
    val nama: String,
    val no_hp: String,
    val password: String
)
