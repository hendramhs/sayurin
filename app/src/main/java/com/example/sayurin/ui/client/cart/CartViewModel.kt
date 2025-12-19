package com.example.sayurin.ui.client.cart

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sayurin.data.local.UserPreferences
import com.example.sayurin.data.remote.dto.cart.CartItem
import com.example.sayurin.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CartRepository,
    private val userPrefs: UserPreferences
) : ViewModel() {

    private val _cartItems = mutableStateOf<List<CartItem>>(emptyList())
    val cartItems: State<List<CartItem>> = _cartItems

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        fetchCart()
    }

    fun fetchCart() {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = userPrefs.userId.first() ?: 0
            repository.getCart(userId).onSuccess { response ->
                _cartItems.value = response.data
            }.onFailure {
                _cartItems.value = emptyList()
            }
            _isLoading.value = false
        }
    }

    fun updateQuantity(cartId: Int, newJumlah: Int) {
        if (newJumlah < 1) return
        viewModelScope.launch {
            repository.updateQuantity(cartId, newJumlah).onSuccess {
                fetchCart() // Refresh data setelah update
            }
        }
    }

    fun deleteItem(cartId: Int) {
        viewModelScope.launch {
            repository.deleteItem(cartId).onSuccess {
                fetchCart() // Refresh data setelah hapus
            }
        }
    }

    // Fungsi tambahan untuk menghitung total harga di UI
    fun getTotalHarga(): Int {
        return _cartItems.value.sumOf { it.harga * it.jumlah }
    }
}
