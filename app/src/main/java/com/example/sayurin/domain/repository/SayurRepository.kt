package com.example.sayurin.domain.repository

import com.example.sayurin.data.remote.dto.BaseResponse
import com.example.sayurin.data.remote.dto.SayurDto

interface SayurRepository {
    suspend fun getSayur(): List<SayurDto>
    suspend fun addSayur(dto: SayurDto): BaseResponse
    suspend fun updateSayur(id: Int, dto: SayurDto): BaseResponse
    suspend fun deleteSayur(id: Int): BaseResponse
}
