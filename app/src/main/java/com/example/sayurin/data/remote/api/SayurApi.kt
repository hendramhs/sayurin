package com.example.sayurin.data.remote.api

import com.example.sayurin.data.remote.dto.SayurDto
import com.example.sayurin.data.remote.dto.BaseResponse
import retrofit2.http.*

interface SayurApi {

    @GET("/api/sayur")
    suspend fun getSayur(): List<SayurDto>

    @POST("/api/sayur")
    suspend fun addSayur(
        @Body request: SayurDto
    ): BaseResponse

    @PUT("/api/sayur/{id}")
    suspend fun updateSayur(
        @Path("id") id: Int,
        @Body request: SayurDto
    ): BaseResponse

    @DELETE("/api/sayur/{id}")
    suspend fun deleteSayur(
        @Path("id") id: Int
    ): BaseResponse
}
