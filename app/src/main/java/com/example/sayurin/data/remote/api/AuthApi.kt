package com.example.sayurin.data.remote.api

import com.example.sayurin.data.remote.dto.auth.*
import com.example.sayurin.utils.Constants
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST(Constants.AUTH_LOGIN)
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST(Constants.AUTH_REGISTER)
    suspend fun register(@Body request: RegisterRequest): AuthResponse
}
