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
import kotlinx.coroutines.flow.asSharedFlow
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

    // Event untuk navigasi ke Screen yang tepat setelah login sukses
    private val _authEvent = MutableSharedFlow<AuthResult>()
    val authEvent = _authEvent.asSharedFlow()

    fun login(noHp: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.login(LoginRequest(noHp, pass))

            result.onSuccess { response ->
                if (response.success) {
                    _authEvent.emit(AuthResult.Success(response.role ?: "client"))
                } else {
                    _errorMessage.value = response.message ?: "Login Gagal"
                }
            }.onFailure {
                _errorMessage.value = "Koneksi Error: ${it.message}"
            }
            _isLoading.value = false
        }
    }

    fun register(nama: String, noHp: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.register(RegisterRequest(nama, noHp, pass))

            result.onSuccess { response ->
                if (response.success) {
                    _authEvent.emit(AuthResult.RegisterSuccess)
                } else {
                    _errorMessage.value = response.message ?: "Registrasi Gagal"
                }
            }.onFailure {
                _errorMessage.value = "Koneksi Error: ${it.message}"
            }
            _isLoading.value = false
        }
    }
}

sealed class AuthResult {
    data class Success(val role: String) : AuthResult()
    object RegisterSuccess : AuthResult()
}
