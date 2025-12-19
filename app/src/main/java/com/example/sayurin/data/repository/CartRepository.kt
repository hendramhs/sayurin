package com.example.sayurin.data.repository

import com.example.sayurin.data.remote.api.CartApi
import com.example.sayurin.data.remote.dto.cart.*
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val api: CartApi
) {
    suspend fun addToCart(req: CartRequest) = try {
        Result.success(api.addToCart(req))
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getCart(userId: Int) = try {
        Result.success(api.getCartByUser(userId))
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun updateQuantity(cartId: Int, jumlah: Int) = try {
        Result.success(api.updateQuantity(cartId, UpdateCartRequest(jumlah)))
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun deleteItem(cartId: Int) = try {
        Result.success(api.deleteCartItem(cartId))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
