package com.example.sayurin.domain.repository

import com.example.sayurin.data.remote.dto.BaseResponse
import com.example.sayurin.data.remote.dto.LoginResponse

interface AuthRepository {
    suspend fun login(noHp: String, password: String): LoginResponse
    suspend fun register(nama: String, noHp: String, password: String): BaseResponse
}
