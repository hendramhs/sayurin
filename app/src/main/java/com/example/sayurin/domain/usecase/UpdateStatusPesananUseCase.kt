package com.example.sayurin.domain.usecase

import com.example.sayurin.domain.repository.PesananRepository
import javax.inject.Inject

class UpdateStatusPesananUseCase @Inject constructor(
    private val repo: PesananRepository
) {
    suspend operator fun invoke(id: Int, status: String) =
        repo.updateStatusPesanan(id, status)
}
