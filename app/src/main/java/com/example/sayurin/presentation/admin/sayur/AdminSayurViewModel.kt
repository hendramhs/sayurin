package com.example.sayurin.presentation.admin.sayur

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sayurin.data.remote.dto.SayurDto
import com.example.sayurin.domain.usecase.AddSayurUseCase
import com.example.sayurin.domain.usecase.DeleteSayurUseCase
import com.example.sayurin.domain.usecase.GetSayurUseCase
import com.example.sayurin.domain.usecase.UpdateSayurUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminSayurViewModel @Inject constructor(
    private val getSayurUseCase: GetSayurUseCase,
    private val addSayurUseCase: AddSayurUseCase,
    private val updateSayurUseCase: UpdateSayurUseCase,
    private val deleteSayurUseCase: DeleteSayurUseCase
) : ViewModel() {

    private val _sayurList = MutableStateFlow<List<SayurDto>>(emptyList())
    val sayurList = _sayurList.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun loadSayur() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                _sayurList.value = getSayurUseCase()
            } catch (e: Exception) {
                _error.value = e.message
            }
            _loading.value = false
        }
    }

    fun addSayur(dto: SayurDto) {
        viewModelScope.launch {
            try {
                addSayurUseCase(dto)
                loadSayur()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateSayur(id: Int, dto: SayurDto) {
        viewModelScope.launch {
            try {
                updateSayurUseCase(id, dto)
                loadSayur()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteSayur(id: Int) {
        viewModelScope.launch {
            try {
                deleteSayurUseCase(id)
                loadSayur()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
