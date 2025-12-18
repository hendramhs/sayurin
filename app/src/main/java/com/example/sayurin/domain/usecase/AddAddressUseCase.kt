package com.example.sayurin.domain.usecase

import com.example.sayurin.data.remote.dto.AddressRequest
import com.example.sayurin.domain.repository.AddressRepository
import javax.inject.Inject

class AddAddressUseCase @Inject constructor(
    private val repo: AddressRepository
) {
    suspend operator fun invoke(request: AddressRequest) =
        repo.addAddress(request)
}
