package com.example.sayurin.ui.admin.manage_sayur

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sayurin.data.remote.dto.sayur.SayurRequest
import com.example.sayurin.data.remote.dto.sayur.SayurResponse
import com.example.sayurin.data.repository.SayurRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminSayurViewModel @Inject constructor(
    private val repository: SayurRepository
) : ViewModel() {

    val sayurList = mutableStateOf<List<SayurResponse>>(emptyList())
    val isLoading = mutableStateOf(false)

    init { getSayur() }

    fun getSayur() {
        viewModelScope.launch {
            isLoading.value = true
            repository.getSayur().onSuccess { sayurList.value = it }
            isLoading.value = false
        }
    }

    fun deleteSayur(id: Int) {
        viewModelScope.launch {
            repository.deleteSayur(id).onSuccess { getSayur() }
        }
    }

    // Tambahkan fungsi ini di dalam AdminSayurViewModel.kt

    fun addSayur(nama: String, harga: String, stok: String, satuan: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            val request = SayurRequest(
                nama_sayur = nama,
                harga = harga.toIntOrNull() ?: 0,
                stok = stok.toIntOrNull() ?: 0,
                satuan = satuan
            )
            repository.addSayur(request).onSuccess {
                onSuccess() // Kembali ke list setelah sukses
            }.onFailure {
                // Handle error (bisa gunakan errorMessage state seperti di Auth)
            }
            isLoading.value = false
        }
    }

}
