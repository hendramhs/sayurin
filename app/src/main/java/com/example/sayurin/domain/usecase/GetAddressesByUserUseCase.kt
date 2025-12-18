package com.example.sayurin.domain.usecase

import com.example.sayurin.domain.repository.AddressRepository
import javax.inject.Inject

class GetAddressesByUserUseCase @Inject constructor(
    private val repo: AddressRepository
) {
    suspend operator fun invoke(userId: Int) = repo.getAddressesByUser(userId)
}
