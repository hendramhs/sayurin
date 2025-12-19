package com.example.sayurin.ui.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sayurin.data.remote.dto.auth.LoginRequest
import com.example.sayurin.data.remote.dto.auth.RegisterRequest
import com.example.sayurin.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    // 1. Flow untuk memantau status login secara Real-time (dibaca MainActivity)
    val isLoggedIn = repository.isLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    // 2. Flow untuk memantau Role secara Real-time (Admin/Client)
    val userRole = repository.userRole
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _authEvent = MutableSharedFlow<AuthResult>()
    val authEvent = _authEvent.asSharedFlow()

    /**
     * Fungsi Login:
     * Menghapus pemanggilan .token jika tidak ada di DTO response.
     * Logika penyimpanan sesi (DataStore) sebaiknya sudah dihandle di dalam repository.login()
     */
    fun login(noHp: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.login(LoginRequest(noHp, pass))

            result.onSuccess { response ->
                if (response.success) {
                    // Jika di repository.login() Anda SUDAH memanggil prefs.saveSession,
                    // maka baris di bawah ini tidak diperlukan lagi (mencegah error token).
                    // repository.saveSession(response.role ?: "client")

                    _authEvent.emit(AuthResult.Success(response.role ?: "client"))
                } else {
                    _errorMessage.value = response.message ?: "Nomor HP atau Password salah"
                }
            }.onFailure {
                _errorMessage.value = "Koneksi gagal: ${it.message}"
            }
            _isLoading.value = false
        }
    }

    /**
     * Fungsi Logout:
     * Menghapus sesi langsung di Front-End (Local Storage/DataStore)
     */
    fun logout() {
        viewModelScope.launch {
            repository.logout()
            // Setelah repository.logout() dipanggil, isLoggedIn otomatis menjadi false
            // karena dipantau lewat StateFlow di atas.
        }
    }

    /**
     * Fungsi Registrasi
     */
    fun register(nama: String, noHp: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.register(RegisterRequest(nama, noHp, pass))

            result.onSuccess { response ->
                if (response.success) {
                    _authEvent.emit(AuthResult.RegisterSuccess)
                } else {
                    _errorMessage.value = response.message ?: "Gagal mendaftarkan akun"
                }
            }.onFailure {
                _errorMessage.value = "Terjadi kesalahan jaringan"
            }
            _isLoading.value = false
        }
    }
}

/**
 * Event wrapper untuk navigasi satu kali (One-time event)
 */
sealed class AuthResult {
    data class Success(val role: String) : AuthResult()
    object RegisterSuccess : AuthResult()
}
