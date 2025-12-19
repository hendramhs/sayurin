package com.example.sayurin.data.repository

import com.example.sayurin.data.local.UserPreferences
import com.example.sayurin.data.remote.api.AuthApi
import com.example.sayurin.data.remote.dto.auth.*
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val prefs: UserPreferences
) {
    suspend fun login(request: LoginRequest): Result<AuthResponse> {
        return try {
            val response = api.login(request)
            if (response.success) {
                // Simpan ke local jika login sukses
                prefs.saveSession(
                    response.user_id ?: 0,
                    response.nama ?: "",
                    response.role ?: "client"
                )
            }
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(request: RegisterRequest): Result<AuthResponse> {
        return try {
            val response = api.register(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getUserRole() = prefs.userRole
}
