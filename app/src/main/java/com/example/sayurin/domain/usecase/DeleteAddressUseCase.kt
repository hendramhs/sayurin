package com.example.sayurin.domain.usecase

import com.example.sayurin.domain.repository.AddressRepository
import javax.inject.Inject

class DeleteAddressUseCase @Inject constructor(
    private val repo: AddressRepository
) {
    suspend operator fun invoke(addressId: Int) = repo.deleteAddress(addressId)
}
