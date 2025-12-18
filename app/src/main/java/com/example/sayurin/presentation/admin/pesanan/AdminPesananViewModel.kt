package com.example.sayurin.presentation.admin.pesanan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sayurin.domain.usecase.GetPesananAdminUseCase
import com.example.sayurin.data.remote.dto.PesananResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminPesananViewModel @Inject constructor(
    private val getPesananAdminUseCase: GetPesananAdminUseCase
) : ViewModel() {

    private val _pesananList = MutableStateFlow<List<PesananResponse>>(emptyList())
    val pesananList = _pesananList.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    fun loadPesanan() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _pesananList.value = getPesananAdminUseCase()
            } catch (e: Exception) {
                _message.value = e.message
            }
            _loading.value = false
        }
    }
}
