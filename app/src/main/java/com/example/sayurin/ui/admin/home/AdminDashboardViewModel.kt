package com.example.sayurin.ui.admin.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sayurin.data.remote.dto.pesanan.DashboardResponse
import com.example.sayurin.data.repository.PesananRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val repository: PesananRepository
) : ViewModel() {

    // State untuk data Dashboard
    private val _dashboardData = MutableStateFlow<DashboardResponse?>(null)
    val dashboardData: StateFlow<DashboardResponse?> = _dashboardData.asStateFlow()

    // State Loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // State Error Message
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        // Mengambil data bulan dan tahun saat ini sebagai default awal
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)

        fetchDashboard(currentMonth, currentYear)
    }

    /**
     * Mengambil data statistik dashboard berdasarkan bulan dan tahun.
     * Fungsi ini dipanggil setiap kali Admin mengubah filter di UI.
     */
    fun fetchDashboard(month: Int, year: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            // Memanggil repository dengan parameter filter
            repository.getDashboardStats(month, year).onSuccess { result ->
                _dashboardData.value = result
            }.onFailure { exception ->
                _errorMessage.value = "Gagal memuat data dashboard: ${exception.message}"
            }

            _isLoading.value = false
        }
    }
}