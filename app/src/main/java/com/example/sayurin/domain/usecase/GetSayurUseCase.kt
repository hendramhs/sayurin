package com.example.sayurin.domain.usecase

import com.example.sayurin.domain.repository.SayurRepository
import javax.inject.Inject

class GetSayurUseCase @Inject constructor(
    private val repo: SayurRepository
) {
    suspend operator fun invoke() = repo.getSayur()
}
