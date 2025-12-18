package com.example.sayurin.domain.repository

import com.example.sayurin.data.remote.dto.AddressRequest
import com.example.sayurin.data.remote.dto.AddressListResponse
import com.example.sayurin.data.remote.dto.DefaultAddressResponse
import com.example.sayurin.data.remote.dto.BaseResponse

interface AddressRepository {
    suspend fun addAddress(request: AddressRequest): BaseResponse
    suspend fun getAddressesByUser(userId: Int): AddressListResponse
    suspend fun getDefaultAddress(userId: Int): DefaultAddressResponse
    suspend fun setDefaultAddress(userId: Int, addressId: Int): BaseResponse
    suspend fun deleteAddress(addressId: Int): BaseResponse
}
