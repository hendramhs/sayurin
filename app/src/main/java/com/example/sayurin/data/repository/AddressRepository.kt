package com.example.sayurin.data.repository

import com.example.sayurin.data.remote.api.AddressApi
import com.example.sayurin.data.remote.dto.address.*
import com.example.sayurin.data.remote.dto.sayur.CommonResponse
import javax.inject.Inject

class AddressRepository @Inject constructor(
    private val api: AddressApi
) {
    // Fungsi pencarian destinasi Komerce via Backend
    suspend fun searchDestinations(query: String): Result<KomerceSearchResponse> {
        return try {
            val response = api.searchDestination(query)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Fungsi simpan alamat ke Backend
    suspend fun addAddress(request: AddressRequest): Result<CommonResponse> {
        return try {
            Result.success(api.addAddress(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Fungsi lainnya yang sudah ada (getAddresses, setDefault, delete) tetap dipertahankan
    suspend fun getAddresses(userId: Int) = try {
        Result.success(api.getAddressesByUser(userId))
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun setDefault(userId: Int, addressId: Int) = try {
        Result.success(api.setDefaultAddress(SetDefaultAddressRequest(userId, addressId)))
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun deleteAddress(addressId: Int) = try {
        Result.success(api.deleteAddress(addressId))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
