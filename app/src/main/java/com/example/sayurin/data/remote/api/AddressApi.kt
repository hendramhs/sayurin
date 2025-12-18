package com.example.sayurin.data.remote.api

import com.example.sayurin.data.remote.dto.AddressRequest
import com.example.sayurin.data.remote.dto.AddressListResponse
import com.example.sayurin.data.remote.dto.BaseResponse
import com.example.sayurin.data.remote.dto.DefaultAddressResponse
import com.example.sayurin.data.remote.dto.SetDefaultAddressRequest
import retrofit2.http.*

interface AddressApi {

    @POST("/api/addresses/add")
    suspend fun addAddress(
        @Body request: AddressRequest
    ): BaseResponse

    @GET("/api/addresses/user/{user_id}")
    suspend fun getAddressesByUser(
        @Path("user_id") userId: Int
    ): AddressListResponse

    @GET("/api/addresses/default/{user_id}")
    suspend fun getDefaultAddress(
        @Path("user_id") userId: Int
    ): DefaultAddressResponse

    @PUT("/api/addresses/set-default")
    suspend fun setDefaultAddress(
        @Body request: SetDefaultAddressRequest
    ): BaseResponse

    @DELETE("/api/addresses/{address_id}")
    suspend fun deleteAddress(
        @Path("address_id") addressId: Int
    ): BaseResponse
}
