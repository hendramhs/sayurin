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

    var pesananList by mutableStateOf<List<PesananAdminResponse>>(emptyList())
    var isLoading by mutableStateOf(false)
    var detailList by mutableStateOf<List<DetailPesananResponse>>(emptyList())
        private set
    var errorMessage by mutableStateOf<String?>(null)

    init {
        fetchPesanan()
    }

    fun fetchPesanan() {
        viewModelScope.launch {
            isLoading = true
            repo.getPesananAdmin().onSuccess {
                pesananList = it
            }.onFailure {
                errorMessage = it.message
            }
            isLoading = false
        }
    }

    fun fetchDetail(pesananId: Int) {
        viewModelScope.launch {
            repo.getDetailPesanan(pesananId).onSuccess {
                detailList = it
            }.onFailure {
                // Handle error jika perlu
            }
        }
    }

    fun clearDetail() {
        detailList = emptyList()
    }

    fun updateStatus(id: Int, newStatus: String) {
        viewModelScope.launch {
            repo.updateStatus(id, newStatus).onSuccess {
                fetchPesanan() // Refresh data setelah update
            }
        }
    }
}
