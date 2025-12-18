package com.example.sayurin.presentation.admin.pesanan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sayurin.data.remote.dto.DetailPesananDto
import com.example.sayurin.domain.usecase.GetDetailPesananUseCase
import com.example.sayurin.domain.usecase.UpdateStatusPesananUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminDetailPesananViewModel @Inject constructor(
    private val getDetailPesananUseCase: GetDetailPesananUseCase,
    private val updateStatusPesananUseCase: UpdateStatusPesananUseCase
) : ViewModel() {

    private val _detailList = MutableStateFlow<List<DetailPesananDto>>(emptyList())
    val detailList = _detailList.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    fun loadDetail(pesananId: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _detailList.value = getDetailPesananUseCase(pesananId)
            } catch (e: Exception) {
                _message.value = e.message
            }
            _loading.value = false
        }
    }

    fun approve(pesananId: Int) {
        viewModelScope.launch {
            try {
                updateStatusPesananUseCase(pesananId, "Approved")
                _message.value = "Pesanan disetujui"
            } catch (e: Exception) {
                _message.value = e.message
            }
        }
    }

    fun reject(pesananId: Int) {
        viewModelScope.launch {
            try {
                updateStatusPesananUseCase(pesananId, "Rejected")
                _message.value = "Pesanan ditolak"
            } catch (e: Exception) {
                _message.value = e.message
            }
        }
    }
}
