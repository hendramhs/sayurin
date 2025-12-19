package com.example.sayurin.data.remote.api

import com.example.sayurin.data.remote.dto.sayur.*
import com.example.sayurin.utils.Constants
import retrofit2.http.*

interface SayurApi {
    @GET(Constants.GET_SAYUR)
    suspend fun getSayur(): List<SayurResponse>

    @POST(Constants.ADD_SAYUR)
    suspend fun addSayur(@Body request: SayurRequest): CommonResponse

    @PUT(Constants.UPDATE_SAYUR)
    suspend fun updateSayur(
        @Path("id") id: Int,
        @Body request: SayurRequest
    ): CommonResponse

    @DELETE(Constants.DELETE_SAYUR)
    suspend fun deleteSayur(@Path("id") id: Int): CommonResponse
}
