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
    private val cartRepository: CartRepository, // Tambahkan ini
    private val userPrefs: UserPreferences
) : ViewModel() {

    private val _state = mutableStateOf<List<SayurResponse>>(emptyList())
    val state: State<List<SayurResponse>> = _state

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _message = mutableStateOf<String?>(null)
    val message: State<String?> = _message

    init {
        fetchSayur()
    }

    fun fetchSayur() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getSayur().onSuccess {
                _state.value = it
            }
            _isLoading.value = false
        }
    }

    fun addToCart(sayurId: Int) {
        viewModelScope.launch {
            val userId = userPrefs.userId.first() ?: 0
            if (userId != 0) {
                cartRepository.addToCart(CartRequest(userId, sayurId, 1))
                    .onSuccess {
                        _message.value = "Berhasil masuk keranjang!" // <--- Tambahkan ini
                    }
                    .onFailure {
                        _message.value = "Gagal menambah ke keranjang"
                    }
            }
        }
    }

    fun clearMessage() { _message.value = null }
}
