package com.example.sayurin.ui.client.checkout

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sayurin.data.local.UserPreferences
import com.example.sayurin.data.remote.dto.address.AddressResponse
import com.example.sayurin.data.remote.dto.cart.CartItem
import com.example.sayurin.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.jvm.optionals.getOrNull

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartRepo: CartRepository,
    private val addressRepo: AddressRepository,
    private val pesananRepo: PesananRepository,
    private val pengirimanRepo: PengirimanRepository,
    private val userPrefs: UserPreferences
) : ViewModel() {

    var cartItems by mutableStateOf<List<CartItem>>(emptyList())
    var selectedAddress by mutableStateOf<AddressResponse?>(null)
    var isLoading by mutableStateOf(false)

    var ongkir by mutableStateOf(0)
    var courierName by mutableStateOf<String?>(null)

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            isLoading = true
            val userId = userPrefs.userId.first() ?: 0

            // 1. Ambil Isi Keranjang
            cartRepo.getCart(userId).onSuccess { cartItems = it.data }

            // 2. Ambil Alamat (Akses properti .data sebelum firstOrNull)
            addressRepo.getAddresses(userId).onSuccess { response ->
                // Mencari alamat default, jika tidak ada ambil yang pertama
                selectedAddress = response.data.find { it.is_default == 1 } ?: response.data.firstOrNull()

                selectedAddress?.let {
                    getOngkir(it.destination_id) // Gunakan destination_id dari alamat
                }
            }

            isLoading = false
        }
    }

    fun calculateTotal(): Int {
        val subtotal = cartItems.sumOf { it.harga * it.jumlah }
        return subtotal + ongkir
    }

    fun getOngkir(destId: Int) {
        viewModelScope.launch {
            try {
                // Misalnya berat total belanjaan adalah 1kg (1000g)            // Origin ID 123 adalah contoh ID kecamatan gudang Anda
                val response = pengirimanRepo.hitungOngkir(123, destId, 1000)

                // Ambil opsi pengiriman pertama (biasanya yang termurah/reguler)
                val selectedOption = response.getOrNull()?.data?.firstOrNull()

                if (selectedOption != null) {
                    this@CheckoutViewModel.ongkir = selectedOption.cost
                    this@CheckoutViewModel.courierName = selectedOption.courier_name
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun createOrder(onSuccess: () -> Unit) {
        viewModelScope.launch {
            // Validasi: Jika alamat belum dipilih, jangan lanjut
            if (selectedAddress == null) return@launch

            isLoading = true
            try {
                val userId = userPrefs.userId.first() ?: 0

                // 2. Sekarang pesananRepo sudah bisa digunakan
                pesananRepo.createPesanan(
                    userId = userId,
                    addressId = selectedAddress!!.address_id
                ).onSuccess {
                    onSuccess() // Navigasi ke halaman sukses/home
                }.onFailure {
                    // Handle jika gagal (misal: tampilkan pesan error)
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                isLoading = false
            }
        }
    }
}
