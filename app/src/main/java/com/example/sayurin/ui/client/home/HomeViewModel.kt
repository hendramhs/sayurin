package com.example.sayurin.ui.client.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sayurin.data.local.UserPreferences
import com.example.sayurin.data.remote.dto.cart.CartRequest
import com.example.sayurin.data.remote.dto.sayur.SayurResponse
import com.example.sayurin.data.repository.CartRepository
import com.example.sayurin.data.repository.SayurRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: SayurRepository,
    private val cartRepository: CartRepository,
    private val userPrefs: UserPreferences
) : ViewModel() {

    // Menggunakan State Standar (Bukan Resource)
    private val _sayurList = mutableStateOf<List<SayurResponse>>(emptyList())
    val sayurList: State<List<SayurResponse>> = _sayurList

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    // Pesan feedback untuk keranjang (Opsional)
    private val _cartMessage = mutableStateOf<String?>(null)
    val cartMessage: State<String?> = _cartMessage

    init {
        fetchSayur()
    }

    fun fetchSayur() {
        viewModelScope.launch {
            _isLoading.value = true

            // Menggunakan .onSuccess dan .onFailure bawaan Result Kotlin
            repository.getSayur().onSuccess { data ->
                _sayurList.value = data
            }.onFailure {
                _sayurList.value = emptyList()
            }

            _isLoading.value = false
        }
    }

    fun addToCart(sayurId: Int) {
        viewModelScope.launch {
            val userId = userPrefs.userId.first() ?: 0
            if (userId == 0) {
                _cartMessage.value = "Silakan login terlebih dahulu"
                return@launch
            }

            cartRepository.addToCart(CartRequest(userId, sayurId, 1))
                .onSuccess {
                    _cartMessage.value = "Berhasil masuk keranjang!"
                }
                .onFailure { error ->
                    _cartMessage.value = error.message ?: "Gagal menambah ke keranjang"
                }
        }
    }

    fun clearCartMessage() {
        _cartMessage.value = null
    }
}
