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
            val userId = userPrefs.userId.first() ?: 0
            repository.getAddresses(userId).onSuccess {
                _addresses.value = it.data
            }
            _isLoading.value = false
        }
    }

    // Fungsi Pencarian Destinasi (Komerce)
    fun searchDestinations(query: String) {
        if (query.length < 3) {
            _searchResults.value = emptyList()
            return
        }
        viewModelScope.launch {
            repository.searchDestinations(query).onSuccess { response ->
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
        city: String,
        province: String,
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
                city = city,
                province = province,
                is_default = if (_addresses.value.isEmpty()) 1 else 0, // Default jika alamat pertama
                subdistrict = null, district = null, zip_code = null // Optional
            )

            repository.addAddress(request).onSuccess {
                fetchAddresses() // Refresh list alamat
                onSuccess()      // Kembali ke halaman sebelumnya
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
