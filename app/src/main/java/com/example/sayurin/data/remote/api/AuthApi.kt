package com.example.sayurin.data.remote.api

import com.example.sayurin.data.remote.dto.LoginRequest
import com.example.sayurin.data.remote.dto.LoginResponse
import com.example.sayurin.data.remote.dto.RegisterRequest
import com.example.sayurin.data.remote.dto.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("/api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): BaseResponse
}
