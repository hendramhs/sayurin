package com.example.sayurin.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sayurin.data.local.datastore.UserPreferences
import com.example.sayurin.domain.usecase.LoginUseCase
import com.example.sayurin.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val pref: UserPreferences
) : ViewModel() {
    val role = pref.role

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val message: String = "") : AuthState()
        data class Error(val message: String) : AuthState()
    }

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState = _registerState.asStateFlow()

    fun login(noHp: String, password: String) {
        viewModelScope.launch {
            _loginState.value = AuthState.Loading
            try {
                val res = loginUseCase(noHp, password)
                pref.saveUser(res.user_id, res.role, res.nama)
                _loginState.value = AuthState.Success("Login berhasil")
            } catch (e: Exception) {
                _loginState.value = AuthState.Error(e.message ?: "Login gagal")
            }
        }
    }

    fun register(nama: String, noHp: String, password: String) {
        viewModelScope.launch {
            _registerState.value = AuthState.Loading
            try {
                val res = registerUseCase(nama, noHp, password)
                _registerState.value = AuthState.Success(res.message)
            } catch (e: Exception) {
                _registerState.value = AuthState.Error(e.message ?: "Registrasi gagal")
            }
        }
    }
}
