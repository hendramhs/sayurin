package com.example.sayurin.domain.usecase

import com.example.sayurin.data.remote.dto.SayurDto
import com.example.sayurin.domain.repository.SayurRepository
import javax.inject.Inject

class UpdateSayurUseCase @Inject constructor(
    private val repo: SayurRepository
) {
    suspend operator fun invoke(id: Int, dto: SayurDto) =
        repo.updateSayur(id, dto)
}
