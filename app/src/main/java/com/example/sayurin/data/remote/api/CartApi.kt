package com.example.sayurin.data.remote.api

import com.example.sayurin.data.remote.dto.cart.*
import com.example.sayurin.data.remote.dto.sayur.CommonResponse
import com.example.sayurin.utils.Constants
import retrofit2.http.*

interface CartApi {
    @POST(Constants.ADD_TO_CART)
    suspend fun addToCart(@Body request: CartRequest): CommonResponse

    @GET(Constants.GET_CART_BY_USER)
    suspend fun getCartByUser(@Path("user_id") userId: Int): CartListResponse

    @PUT(Constants.UPDATE_CART)
    suspend fun updateQuantity(
        @Path("cart_id") cartId: Int,
        @Body request: UpdateCartRequest
    ): CommonResponse

    @DELETE(Constants.DELETE_CART)
    suspend fun deleteCartItem(@Path("cart_id") cartId: Int): CommonResponse
}
