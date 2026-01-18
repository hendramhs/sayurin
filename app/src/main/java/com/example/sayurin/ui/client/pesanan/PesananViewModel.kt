package com.example.sayurin.ui.client.pesanan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sayurin.data.local.UserPreferences
import com.example.sayurin.data.remote.dto.pesanan.DetailPesananResponse
import com.example.sayurin.data.remote.dto.pesanan.PesananUserResponse
import com.example.sayurin.data.repository.PesananRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PesananViewModel @Inject constructor(
    private val repo: PesananRepository,
    private val userPrefs: UserPreferences
) : ViewModel() {

    var pesananList by mutableStateOf<List<PesananUserResponse>>(emptyList())
    var detailList by mutableStateOf<List<DetailPesananResponse>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set

    init {
        fetchPesanan()
    }

    /**
     * Mengambil daftar pesanan milik user yang sedang login
     */
    fun fetchPesanan() {
        viewModelScope.launch {
            isLoading = true
            val userId = userPrefs.userId.first() ?: 0
            repo.getPesananUser(userId).onSuccess {
                pesananList = it
            }.onFailure {
                // Handle error jika diperlukan (misal: kirim ke state error)
            }
            isLoading = false
        }
    }

    /**
     * Mengambil detail item sayur di dalam satu pesanan
     */
    fun fetchDetail(pesananId: Int) {
        viewModelScope.launch {
            repo.getDetailPesanan(pesananId).onSuccess {
                detailList = it
            }
        }
    }

    /**
     * Menghapus list detail saat dialog ditutup
     */
    fun clearDetail() {
        detailList = emptyList()
    }

    /**
     * Fungsi untuk memperbarui status pesanan oleh Client.
     * Digunakan untuk mengubah status dari "Shipped" ke "Completed".
     */
    fun updateStatus(pesananId: Int, newStatus: String) {
        viewModelScope.launch {
            isLoading = true
            repo.updateStatus(pesananId, newStatus).onSuccess {
                // Refresh data agar UI menampilkan status "Selesai" dan menghilangkan tombol
                fetchPesanan()
            }.onFailure {
                isLoading = false
                // Handle error (misal: tampilkan pesan gagal update)
            }
        }
    }
}