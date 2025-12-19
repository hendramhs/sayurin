package com.example.sayurin.utils

object Constants {

    // Api base url
    const val BASE_URL = "http://192.168.237.246:3000"
    // Tips: Gunakan "http://10.0.2.2:3000" jika menggunakan Emulator

    // Auth & Datastore Keys
    const val PREF_USER_ID = "pref_user_id"
    const val PREF_ROLE = "pref_role"
    const val PREF_NAME = "pref_name"
    const val DATASTORE_NAME = "sayurin_datastore"

    // --- ENDPOINTS ---

    // Auth
    const val AUTH_LOGIN = "api/auth/login"
    const val AUTH_REGISTER = "api/auth/register"

    // Sayur (Produk)
    const val GET_SAYUR = "api/sayur"
    const val ADD_SAYUR = "api/sayur/add"
    const val UPDATE_SAYUR = "api/sayur/update"
    const val DELETE_SAYUR = "api/sayur/delete"


    // Cart (Keranjang)
    const val GET_CART = "api/cart/"
    const val ADD_TO_CART = "api/cart/"
    const val UPDATE_CART = "api/cart/update/{cart_id}"
    const val DELETE_CART = "api/cart/delete/{cart_id}"
    const val GET_CART_BY_USER = "api/cart/user/{user_id}"

    // Address (Alamat)
    const val ADD_ADDRESS = "api/address"
    const val GET_ADDRESSES = "api/address/"
    const val SET_DEFAULT_ADDRESS = "api/address/set-default"
    const val GET_DEFAULT_ADDRESS = "api/address/default/"
    const val DELETE_ADDRESS = "api/address/delete/{address_id}"

    // Alamat - Komerce Search
    const val SEARCH_DESTINATION = "api/pengiriman/search-destination"

    // Pengiriman (Ongkir)
    const val HITUNG_ONGKIR = "api/pengiriman/hitung-ongkir"
    const val PILIH_PENGIRIMAN = "api/pengiriman/pilih"

    // Pesanan (Checkout & History)
    const val CREATE_PESANAN = "api/pesanan"
    const val GET_PESANAN_CLIENT = "api/pesanan/user/"
    const val GET_PESANAN_ADMIN = "api/pesanan/admin"
    const val GET_DETAIL_PESANAN = "api/pesanan/detail/"
    const val UPDATE_STATUS_PESANAN = "api/pesanan/status"

    // --- NAVIGATION ROUTES ---
    const val ROUTE_LOGIN = "login"
    const val ROUTE_REGISTER = "register"
    const val ROUTE_USER_HOME = "user_home"
    const val ROUTE_ADMIN_HOME = "admin_home"
    const val ROUTE_CART = "cart"
    const val ROUTE_CHECKOUT = "checkout"
    const val ROUTE_ADDRESS = "address"
    const val ROUTE_ADD_ADDRESS = "add_address"
}
