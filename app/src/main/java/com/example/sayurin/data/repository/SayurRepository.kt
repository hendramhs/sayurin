package com.example.sayurin.data.repository

import com.example.sayurin.data.remote.api.SayurApi
import com.example.sayurin.data.remote.dto.sayur.CommonResponse
import com.example.sayurin.data.remote.dto.sayur.SayurRequest
import com.example.sayurin.data.remote.dto.sayur.SayurResponse
import javax.inject.Inject

class SayurRepository @Inject constructor(
    private val api: SayurApi
) {
    suspend fun getSayur(): Result<List<SayurResponse>> {
        return try {
            val response = api.getSayur()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Tambahkan di dalam class SayurRepository
    suspend fun addSayur(request: SayurRequest): Result<CommonResponse> {
        return try {
            Result.success(api.addSayur(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateSayur(id: Int, request: SayurRequest): Result<CommonResponse> {
        return try {
            Result.success(api.updateSayur(id, request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteSayur(id: Int): Result<CommonResponse> {
        return try {
            Result.success(api.deleteSayur(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
