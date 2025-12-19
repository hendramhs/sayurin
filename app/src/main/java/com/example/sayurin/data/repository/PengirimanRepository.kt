package com.example.sayurin.data.repository

import com.example.sayurin.data.remote.api.PengirimanApi
import com.example.sayurin.data.remote.dto.pengiriman.*
import javax.inject.Inject

interface PengirimanRepository {
    suspend fun hitungOngkir(origin: Int, destination: Int, weight: Int): Result<OngkirResponse>
}

class PengirimanRepositoryImpl @Inject constructor(
    private val api: PengirimanApi
) : PengirimanRepository {
    override suspend fun hitungOngkir(origin: Int, destination: Int, weight: Int): Result<OngkirResponse> {
        return try {
            val response = api.hitungOngkir(OngkirRequest(origin, destination, weight))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
