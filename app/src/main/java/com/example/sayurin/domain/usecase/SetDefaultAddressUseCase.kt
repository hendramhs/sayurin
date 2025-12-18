package com.example.sayurin.domain.usecase

import com.example.sayurin.domain.repository.AddressRepository
import javax.inject.Inject

class SetDefaultAddressUseCase @Inject constructor(
    private val repo: AddressRepository
) {
    suspend operator fun invoke(userId: Int, addressId: Int) =
        repo.setDefaultAddress(userId, addressId)
}
