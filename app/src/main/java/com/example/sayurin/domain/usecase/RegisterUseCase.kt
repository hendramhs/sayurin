package com.example.sayurin.domain.usecase

import com.example.sayurin.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(nama: String, noHp: String, password: String) =
        repo.register(nama, noHp, password)
}
