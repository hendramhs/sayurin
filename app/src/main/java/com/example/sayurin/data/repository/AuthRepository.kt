package com.example.sayurin.data.repository

import com.example.sayurin.data.local.UserPreferences
import com.example.sayurin.data.remote.api.AuthApi
import com.example.sayurin.data.remote.dto.auth.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val prefs: UserPreferences
) {
    // 1. Cek status login secara real-time berdasarkan keberadaan role/user_id
    val isLoggedIn: Flow<Boolean> = prefs.userRole.map { role ->
        !role.isNullOrEmpty()
    }

    // 2. Ambil role user
    val userRole: Flow<String?> = prefs.userRole

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

    // 3. Simpan Sesi (Tambahkan ini jika AuthViewModel memanggilnya)
    suspend fun saveSession(token: String, role: String) {
        // Karena di login() Anda sudah memanggil prefs.saveSession,
        // fungsi ini bisa disesuaikan atau dikosongkan jika token tidak dipakai.
    }

    // 4. Handle Logout di Front-End (Hapus data di Preferences)
    suspend fun logout() {
        prefs.clearSession()
    }
}
