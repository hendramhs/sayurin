package com.example.sayurin.domain.usecase

import com.example.sayurin.data.remote.dto.PilihPengirimanRequest
import com.example.sayurin.domain.repository.PengirimanRepository
import javax.inject.Inject

class PilihPengirimanUseCase @Inject constructor(
    private val repo: PengirimanRepository
) {
    suspend operator fun invoke(request: PilihPengirimanRequest) =
        repo.pilihPengiriman(request)
}
