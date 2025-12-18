package com.example.sayurin.domain.repository

import com.example.sayurin.data.remote.dto.PesananRequest
import com.example.sayurin.data.remote.dto.PesananResponse
import com.example.sayurin.data.remote.dto.DetailPesananDto
import com.example.sayurin.data.remote.dto.BaseResponse

interface PesananRepository {
    suspend fun createPesanan(request: PesananRequest): PesananResponse
    suspend fun getPesananAdmin(): List<PesananResponse>
    suspend fun getDetailPesanan(id: Int): List<DetailPesananDto>
    suspend fun updateStatusPesanan(id: Int, status: String): BaseResponse
}
