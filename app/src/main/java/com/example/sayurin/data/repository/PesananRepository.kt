package com.example.sayurin.data.repository

import com.example.sayurin.data.remote.api.PesananApi
import com.example.sayurin.data.remote.dto.sayur.CommonResponse
import com.example.sayurin.data.remote.dto.pesanan.CreatePesananRequest // 1. Tambahkan import ini
import javax.inject.Inject

interface PesananRepository {
    suspend fun createPesanan(userId: Int, addressId: Int): Result<CommonResponse>
}

class PesananRepositoryImpl @Inject constructor(
    private val api: PesananApi
) : PesananRepository {

    override suspend fun createPesanan(userId: Int, addressId: Int): Result<CommonResponse> {
        return try {
            // 2. Bungkus userId dan addressId ke dalam objek CreatePesananRequest
            val request = CreatePesananRequest(userId, addressId)

            // 3. Kirim objek request tersebut ke API
            val response = api.createPesanan(request)

            if (response.success) {
                Result.success(response)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
