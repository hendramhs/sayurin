package com.example.sayurin.data.repository

import com.example.sayurin.data.remote.api.PesananApi
import com.example.sayurin.data.remote.dto.pesanan.CreatePesananRequest
import com.example.sayurin.data.remote.dto.pesanan.CreatePesananResponse
import com.example.sayurin.data.remote.dto.pesanan.DashboardResponse
import com.example.sayurin.data.remote.dto.pesanan.DetailPesananResponse
import com.example.sayurin.data.remote.dto.pesanan.PesananAdminResponse
import com.example.sayurin.data.remote.dto.pesanan.PesananItemRequest
import com.example.sayurin.data.remote.dto.pesanan.PesananUserResponse
import com.example.sayurin.data.remote.dto.pesanan.StatusRequest
import com.example.sayurin.data.remote.dto.sayur.CommonResponse
import javax.inject.Inject

interface PesananRepository {
    suspend fun createPesanan(
        userId: Int,
        addressId: Int,
        items: List<PesananItemRequest>
    ): Result<CreatePesananResponse>

    suspend fun getPesananAdmin(): Result<List<PesananAdminResponse>>
    suspend fun getDetailPesanan(id: Int): Result<List<DetailPesananResponse>>
    suspend fun updateStatus(id: Int, status: String): Result<CommonResponse>
    suspend fun getPesananUser(userId: Int): Result<List<PesananUserResponse>>

    // Diperbarui untuk menerima parameter filter
    suspend fun getDashboardStats(month: Int, year: Int): Result<DashboardResponse>
}

class PesananRepositoryImpl @Inject constructor(
    private val api: PesananApi
) : PesananRepository {

    override suspend fun createPesanan(
        userId: Int,
        addressId: Int,
        items: List<PesananItemRequest>
    ): Result<CreatePesananResponse> {
        return try {
            val request = CreatePesananRequest(
                user_id = userId,
                address_id = addressId,
                items = items
            )
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

    override suspend fun getPesananAdmin(): Result<List<PesananAdminResponse>> {
        return try {
            val response = api.getPesananAdmin()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getDetailPesanan(id: Int): Result<List<DetailPesananResponse>> {
        return try {
            val response = api.getDetailPesanan(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateStatus(id: Int, status: String): Result<CommonResponse> {
        return try {
            val response = api.updateStatusPesanan(id, StatusRequest(status))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPesananUser(userId: Int): Result<List<PesananUserResponse>> {
        return try {
            val response = api.getPesananUser(userId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Implementasi terbaru dengan filter bulan dan tahun
    override suspend fun getDashboardStats(month: Int, year: Int): Result<DashboardResponse> {
        return try {
            val response = api.getDashboardStats(month, year)
            if (response.success) {
                Result.success(response)
            } else {
                Result.failure(Exception("Gagal mengambil data statistik"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}