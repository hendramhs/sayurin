package com.example.sayurin.data.remote.api

import com.example.sayurin.data.remote.dto.sayur.CommonResponse // Gunakan yang sudah ada
import com.example.sayurin.data.remote.dto.pesanan.* // Pastikan path ini benar
import com.example.sayurin.utils.Constants
import retrofit2.http.*

interface PesananApi {
    @POST(Constants.CREATE_PESANAN)
    suspend fun createPesanan(@Body request: CreatePesananRequest): CommonResponse // Biasanya responnya cukup CommonResponse

    @GET(Constants.GET_PESANAN_ADMIN)
    suspend fun getPesananAdmin(): List<PesananAdminResponse>

    @GET(Constants.GET_DETAIL_PESANAN)
    suspend fun getDetailPesanan(@Path("id") id: Int): List<DetailPesananResponse>

    @PUT(Constants.UPDATE_STATUS_PESANAN)
    suspend fun updateStatusPesanan(@Path("id") id: Int, @Body request: StatusRequest): CommonResponse
}
