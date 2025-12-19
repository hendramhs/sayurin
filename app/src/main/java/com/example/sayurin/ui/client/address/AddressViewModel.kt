package com.example.sayurin.ui.client.address

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sayurin.data.local.UserPreferences
import com.example.sayurin.data.remote.dto.address.*
import com.example.sayurin.data.repository.AddressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val repository: AddressRepository,
    private val userPrefs: UserPreferences
) : ViewModel() {

    // State untuk list alamat di AddressScreen
    private val _addresses = mutableStateOf<List<AddressResponse>>(emptyList())
    val addresses: State<List<AddressResponse>> = _addresses

    // State untuk hasil pencarian di AddAddressScreen
    private val _searchResults = mutableStateOf<List<KomerceDestination>>(emptyList())
    val searchResults: State<List<KomerceDestination>> = _searchResults

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        fetchAddresses()
    }

    fun fetchAddresses() {
        viewModelScope.launch {
            _isLoading.value = true
            // Ambil userId dari Preferences
            val userId = userPrefs.userId.first() ?: 0

            println("DEBUG: Fetching addresses for userId: $userId")

            if (userId != 0) {
                repository.getAddresses(userId).onSuccess { response ->
                    _addresses.value = response.data
                    println("DEBUG: Berhasil memuat ${response.data.size} alamat")
                }.onFailure { error ->
                    println("DEBUG: Gagal memuat alamat: ${error.message}")
                }
            }
            _isLoading.value = false
        }
    }

    // Fungsi Pencarian Destinasi (Komerce)
    fun searchDestinations(keyword: String) {
        if (keyword.length < 3) {
            _searchResults.value = emptyList()
            return
        }
        viewModelScope.launch {
            repository.searchDestinations(keyword).onSuccess { response ->
                _searchResults.value = response.destinations
            }.onFailure {
                _searchResults.value = emptyList()
            }
        }
    }

    // Fungsi Simpan Alamat Baru
    fun saveAddress(
        nama: String,
        hp: String,
        alamat: String,
        destId: Int,
        label: String,
        subdistrict: String,
        district: String,
        city: String,
        province: String,
        zipCode: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = userPrefs.userId.first() ?: 0

            val request = AddressRequest(
                user_id = userId,
                label = label,
                penerima_nama = nama,
                penerima_hp = hp,
                alamat_lengkap = alamat,
                destination_id = destId,
                subdistrict = subdistrict,
                district = district,
                city = city,
                province = province,
                zip_code = zipCode,
                is_default = if (_addresses.value.isEmpty()) 1 else 0, // Default jika alamat pertama
            )

            repository.addAddress(request).onSuccess {
                println("SIMPAN BERHASIL!")
                fetchAddresses()
                onSuccess()
            }.onFailure { error ->
                // Jika gagal, log error-nya untuk tahu penyebab dari server
                println("SIMPAN GAGAL: ${error.message}")
            }
            _isLoading.value = false
        }
    }

    // Fungsi Set Default & Delete (Sudah ada sebelumnya)
    fun setDefault(addressId: Int) {
        viewModelScope.launch {
            val userId = userPrefs.userId.first() ?: 0
            repository.setDefault(userId, addressId).onSuccess { fetchAddresses() }
        }
    }

    fun deleteAddress(addressId: Int) {
        viewModelScope.launch {
            repository.deleteAddress(addressId).onSuccess { fetchAddresses() }
        }
    }
}
