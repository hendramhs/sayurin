package com.example.sayurin.domain.usecase

import com.example.sayurin.data.remote.dto.HitungOngkirRequest
import com.example.sayurin.domain.repository.PengirimanRepository
import javax.inject.Inject

class HitungOngkirUseCase @Inject constructor(
    private val repo: PengirimanRepository
) {
    suspend operator fun invoke(request: HitungOngkirRequest) =
        repo.hitungOngkir(request)
}
