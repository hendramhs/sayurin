package com.example.sayurin.data.repository

import com.example.sayurin.data.remote.api.AddressApi
import com.example.sayurin.data.remote.dto.AddressRequest
import com.example.sayurin.data.remote.dto.SetDefaultAddressRequest
import com.example.sayurin.domain.repository.AddressRepository
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val api: AddressApi
) : AddressRepository {

    override suspend fun addAddress(request: AddressRequest) =
        api.addAddress(request)

    override suspend fun getAddressesByUser(userId: Int) =
        api.getAddressesByUser(userId)

    override suspend fun getDefaultAddress(userId: Int) =
        api.getDefaultAddress(userId)

    override suspend fun setDefaultAddress(userId: Int, addressId: Int) =
        api.setDefaultAddress(SetDefaultAddressRequest(userId, addressId))

    override suspend fun deleteAddress(addressId: Int) =
        api.deleteAddress(addressId)
}
