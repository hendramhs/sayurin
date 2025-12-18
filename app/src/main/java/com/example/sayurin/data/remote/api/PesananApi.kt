package com.example.sayurin.data.remote.api

import com.example.sayurin.data.remote.dto.PesananRequest
import com.example.sayurin.data.remote.dto.PesananResponse
import com.example.sayurin.data.remote.dto.DetailPesananDto
import com.example.sayurin.data.remote.dto.BaseResponse
import retrofit2.http.*

interface PesananApi {

    @POST("/api/pesanan")
    suspend fun createPesanan(
        @Body request: PesananRequest
    ): PesananResponse

    @GET("/api/pesanan/admin")
    suspend fun getPesananAdmin(): List<PesananResponse>

    @GET("/api/pesanan/{id}")
    suspend fun getDetailPesanan(
        @Path("id") id: Int
    ): List<DetailPesananDto>

    @PUT("/api/pesanan/{id}/status")
    suspend fun updateStatusPesanan(
        @Path("id") id: Int,
        @Body request: Map<String, String> // backend expects: { "status": "Approved" }
    ): BaseResponse
}
