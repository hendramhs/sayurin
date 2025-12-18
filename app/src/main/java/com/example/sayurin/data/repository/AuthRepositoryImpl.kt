package com.example.sayurin.data.repository

import com.example.sayurin.data.remote.api.AuthApi
import com.example.sayurin.data.remote.dto.LoginRequest
import com.example.sayurin.data.remote.dto.RegisterRequest
import com.example.sayurin.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {

    override suspend fun login(noHp: String, password: String) =
        api.login(LoginRequest(noHp, password))

    override suspend fun register(nama: String, noHp: String, password: String) =
        api.register(RegisterRequest(nama, noHp, password))
}
