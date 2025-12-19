package com.example.sayurin.data.remote.api

import com.example.sayurin.data.remote.dto.address.*
import com.example.sayurin.data.remote.dto.sayur.CommonResponse
import com.example.sayurin.utils.Constants
import retrofit2.http.*

interface AddressApi {
    @POST(Constants.ADD_ADDRESS)
    suspend fun addAddress(@Body request: AddressRequest): CommonResponse

    @GET(Constants.GET_ADDRESSES)
    suspend fun getAddressesByUser(@Path("user_id") userId: Int): AddressListResponse

    @GET(Constants.GET_DEFAULT_ADDRESS)
    suspend fun getDefaultAddress(@Path("user_id") userId: Int): DefaultAddressResponse

    @POST(Constants.SET_DEFAULT_ADDRESS)
    suspend fun setDefaultAddress(@Body request: SetDefaultAddressRequest): CommonResponse

    @DELETE(Constants.DELETE_ADDRESS)
    suspend fun deleteAddress(@Path("address_id") addressId: Int): CommonResponse

    @GET(Constants.SEARCH_DESTINATION)
    suspend fun searchDestination(@Query("keyword") keyword: String): KomerceSearchResponse
}
