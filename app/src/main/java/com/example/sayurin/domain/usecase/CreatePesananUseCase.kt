package com.example.sayurin.domain.usecase

import com.example.sayurin.data.remote.dto.PesananRequest
import com.example.sayurin.domain.repository.PesananRepository
import javax.inject.Inject

class CreatePesananUseCase @Inject constructor(
    private val repo: PesananRepository
) {
    suspend operator fun invoke(request: PesananRequest) =
        repo.createPesanan(request)
}
