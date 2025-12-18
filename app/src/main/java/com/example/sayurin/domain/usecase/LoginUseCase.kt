package com.example.sayurin.domain.usecase

import com.example.sayurin.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(noHp: String, password: String) =
        repo.login(noHp, password)
}
