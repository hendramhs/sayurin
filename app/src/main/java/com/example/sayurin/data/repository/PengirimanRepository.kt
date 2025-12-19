package com.example.sayurin.data.repository

import com.example.sayurin.data.remote.api.PengirimanApi
import com.example.sayurin.data.remote.dto.pengiriman.*
import com.example.sayurin.data.remote.dto.sayur.CommonResponse
import javax.inject.Inject

interface PengirimanRepository {
    // Legacy / V1
    suspend fun hitungOngkir(origin: Int, destination: Int, weight: Int): Result<OngkirResponse>

    // --- HITUNG ONGKIR V2 ---
    suspend fun hitungOngkirV2(userId: Int, receiverDestId: Int): Result<OngkirResponseV2>

    // --- PILIH PENGIRIMAN (Update ke DB) ---
    suspend fun pilihPengiriman(request: PilihPengirimanRequest): Result<PilihPengirimanResponse>
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

    override suspend fun hitungOngkirV2(userId: Int, receiverDestId: Int): Result<OngkirResponseV2> {
        return try {
            // Mengirim request sesuai DTO OngkirRequestV2
            val response = api.hitungOngkirV2(OngkirRequestV2(userId, receiverDestId))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun pilihPengiriman(request: PilihPengirimanRequest): Result<PilihPengirimanResponse> {
        return try {
            // Memanggil endpoint pilih pengiriman untuk simpan data ke tabel pengiriman & update pesanan
            val response = api.pilihPengiriman(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
