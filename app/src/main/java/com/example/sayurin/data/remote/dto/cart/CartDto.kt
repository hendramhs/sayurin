package com.example.sayurin.data.remote.dto.cart

// Request untuk POST /api/cart
data class CartRequest(
    val user_id: Int,
    val sayur_id: Int,
    val jumlah: Int
)

// Response untuk GET /api/cart/user/{user_id}
data class CartListResponse(
    val success: Boolean,
    val data: List<CartItem>
)

data class CartItem(
    val cart_id: Int,
    val user_id: Int,
    val sayur_id: Int,
    val jumlah: Int,
    val nama_sayur: String,
    val harga: Int,
    val satuan: String,
    val gambar: String?
)

// Request untuk PUT /api/cart/{cart_id}
data class UpdateCartRequest(
    val jumlah: Int
)
