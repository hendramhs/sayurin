package com.example.sayurin.ui.client.checkout

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sayurin.data.local.UserPreferences
import com.example.sayurin.data.remote.dto.address.AddressResponse
import com.example.sayurin.data.remote.dto.cart.CartItem
import com.example.sayurin.data.remote.dto.pengiriman.LayananPengiriman
import com.example.sayurin.data.remote.dto.pengiriman.PilihPengirimanRequest
import com.example.sayurin.data.remote.dto.pesanan.PesananItemRequest
import com.example.sayurin.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    // State untuk Ongkir V2 (Daftar layanan kurir yang tersedia)
    var layananList by mutableStateOf<List<LayananPengiriman>>(emptyList())
    var selectedLayanan by mutableStateOf<LayananPengiriman?>(null)

    // Data pendukung dari backend hitungOngkirV2
    private var shipperDestId = 0
    private var totalBerat = 0

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            isLoading = true
            val userId = userPrefs.userId.first() ?: 0

            // 1. Ambil Isi Keranjang
            cartRepo.getCart(userId).onSuccess { cartItems = it.data }

            // 2. Ambil Alamat (Utamakan yang default)
            addressRepo.getAddresses(userId).onSuccess { response ->
                selectedAddress = response.data.find { it.is_default == 1 }
                    ?: response.data.firstOrNull()

                // Jika alamat ditemukan, langsung panggil hitung ongkir
                selectedAddress?.let {
                    getOngkirV2(it.destination_id)
                }
            }
            isLoading = false
        }
    }

    // Fungsi untuk mendapatkan daftar kurir setelah alamat dipilih
    fun getOngkirV2(receiverDestId: Int) {
        viewModelScope.launch {
            val userId = userPrefs.userId.first() ?: 0
            isLoading = true
            pengirimanRepo.hitungOngkirV2(userId, receiverDestId).onSuccess { response ->

                // --- TAMBAHKAN LOG DI SINI ---
                println("DEBUG: Hitung Ongkir V2 Berhasil")
                println("DEBUG: Shipper ID: ${response.shipper_destination_id}")
                println("DEBUG: Total Berat: ${response.weight}")
                println("DEBUG: Item Value: ${response.item_value}")
                println("DEBUG: Jumlah Layanan: ${response.layanan.size}")

                response.layanan.forEach { kurir ->
                    println("DEBUG: Kurir -> ${kurir.shipping_name} (${kurir.service_name}) - Harga: ${kurir.shipping_cost} - ETD: ${kurir.etd}")
                }
                // -----------------------------

                layananList = response.layanan
                shipperDestId = response.shipper_destination_id
                totalBerat = response.weight

                // Reset pilihan kurir jika user mengganti alamat
                selectedLayanan = null
            }.onFailure { error ->
                // Tambahkan log juga saat gagal untuk mempermudah debug
                println("DEBUG: Hitung Ongkir V2 GAGAL: ${error.message}")
                layananList = emptyList()
            }
            isLoading = false
        }
    }

    // Menghitung total biaya: Subtotal Keranjang + Ongkir Kurir terpilih
    fun calculateTotal(): Int {
        val subtotal = cartItems.sumOf { it.harga * it.jumlah }
        val ongkir = selectedLayanan?.shipping_cost ?: 0
        return subtotal + ongkir
    }

    // Alur pembuatan pesanan sampai simpan pengiriman
    fun createOrder(onSuccess: (Int) -> Unit) {
        viewModelScope.launch {
            // Validasi: Pastikan alamat dan kurir sudah dipilih
            if (selectedAddress == null || selectedLayanan == null) return@launch

            isLoading = true
            val userId = userPrefs.userId.first() ?: 0

            // Mapping item keranjang ke format yang diminta backend
            val itemsReq = cartItems.map {
                PesananItemRequest(
                    sayur_id = it.sayur_id,
                    jumlah = it.jumlah
                )
            }

            // STEP 1: Buat Pesanan
            pesananRepo.createPesanan(
                userId = userId,
                addressId = selectedAddress!!.address_id,
                items = itemsReq
            ).onSuccess { pesananResponse ->

                val newPesananId = pesananResponse.pesanan_id

                // STEP 2: Simpan Data Pengiriman
                val pilihReq = PilihPengirimanRequest(
                    pesanan_id = newPesananId,
                    shipping_name = selectedLayanan!!.shipping_name,
                    service_name = selectedLayanan!!.service_name,
                    shipping_cost = selectedLayanan!!.shipping_cost,
                    service_fee = selectedLayanan!!.service_fee,
                    is_cod = if (selectedLayanan!!.is_cod) 1 else 0,
                    etd = selectedLayanan!!.etd,
                    shipper_destination_id = shipperDestId,
                    receiver_destination_id = selectedAddress!!.destination_id,
                    berat = totalBerat
                )

                pengirimanRepo.pilihPengiriman(pilihReq).onSuccess {

                    // STEP 3: Hapus Keranjang (Baru ditambahkan)
                    cartRepo.clearCart(userId).onSuccess {
                        isLoading = false
                        onSuccess(newPesananId)
                    }.onFailure {
                        // Jika gagal hapus cart pun, kita tetap arahkan ke sukses
                        // agar user tidak mengulang pembuatan pesanan yang sudah masuk
                        println("Warning: Gagal menghapus keranjang tapi pesanan berhasil")
                        isLoading = false
                        onSuccess(newPesananId)
                    }
                }
            }.onFailure { error ->
                println("Create Order Failed: ${error.message}")
                isLoading = false
            }
        }
    }
}
