package com.example.sayurin.ui.client.pesanan

import androidx.activity.result.launch
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

    init {
        fetchPesanan()
    }

    fun fetchPesanan() {
        viewModelScope.launch {
            isLoading = true
            val userId = userPrefs.userId.first() ?: 0
            repo.getPesananUser(userId).onSuccess {
                pesananList = it
            }
            isLoading = false
        }
    }

    fun fetchDetail(pesananId: Int) {
        viewModelScope.launch {
            // Gunakan repo yang sama dengan Admin karena endpoint detailnya sama
            repo.getDetailPesanan(pesananId).onSuccess {
                detailList = it
            }
        }
    }

    fun clearDetail() {
        detailList = emptyList()
    }
}
