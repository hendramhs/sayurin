package com.example.sayurin.ui.admin.manage_pesanan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sayurin.data.remote.dto.pesanan.DetailPesananResponse
import com.example.sayurin.data.remote.dto.pesanan.PesananAdminResponse
import com.example.sayurin.data.repository.PesananRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminPesananViewModel @Inject constructor(
    private val repo: PesananRepository
) : ViewModel() {

    // List untuk menampung semua pesanan yang masuk ke Admin
    var pesananList by mutableStateOf<List<PesananAdminResponse>>(emptyList())
        private set

    // State untuk loading indicator
    var isLoading by mutableStateOf(false)
        private set

    // State untuk detail item di dalam satu pesanan
    var detailList by mutableStateOf<List<DetailPesananResponse>>(emptyList())
        private set

    // State untuk pesan error
    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchPesanan()
    }

    /**
     * Mengambil daftar pesanan dari server
     */
    fun fetchPesanan() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            repo.getPesananAdmin().onSuccess {
                pesananList = it
            }.onFailure {
                errorMessage = "Gagal memuat pesanan: ${it.message}"
            }
            isLoading = false
        }
    }

    /**
     * Mengambil detail item (produk) dari sebuah pesanan_id
     */
    fun fetchDetail(pesananId: Int) {
        viewModelScope.launch {
            repo.getDetailPesanan(pesananId).onSuccess {
                detailList = it
            }.onFailure {
                errorMessage = "Gagal memuat detail: ${it.message}"
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
     * Fungsi untuk memperbarui status pesanan.
     * Alur: Approved -> Shipped -> Completed
     * @param id Pesanan ID
     * @param newStatus Status baru (Shipped / Completed / Rejected)
     */
    fun updateStatus(id: Int, newStatus: String) {
        viewModelScope.launch {
            isLoading = true
            repo.updateStatus(id, newStatus).onSuccess {
                // Refresh data list agar status di UI berubah otomatis
                fetchPesanan()
                errorMessage = null
            }.onFailure {
                errorMessage = "Gagal memperbarui status ke $newStatus"
                isLoading = false
            }
        }
    }
}