package com.example.sayurin.utils

object Constants {

    // Api base url
    const val BASE_URL = "http://10.112.54.246:3000"
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
    const val ADD_SAYUR = "api/sayur"
    const val UPDATE_SAYUR = "api/sayur/{id}"
    const val DELETE_SAYUR = "api/sayur/{id}"

    // Cart (Keranjang)
    const val ADD_TO_CART = "api/cart/"
    const val UPDATE_CART = "api/cart/{cart_id}"
    const val DELETE_CART = "api/cart/{cart_id}"
    const val CLEAR_CART = "api/cart/user/{user_id}"
    const val GET_CART_BY_USER = "api/cart/user/{user_id}"

    // Address (Alamat)
    const val ADD_ADDRESS = "api/addresses/add"
    const val GET_ADDRESSES = "api/addresses/user/{user_id}"
    const val SET_DEFAULT_ADDRESS = "api/addresses/set-default"
    const val GET_DEFAULT_ADDRESS = "api/addresses/default/{user_id}"
    const val DELETE_ADDRESS = "api/addresses/{address_id}"

    // Alamat - Komerce Search
    const val SEARCH_DESTINATION = "api/addresses/search-destinations"

    // Pengiriman (Ongkir)
    const val HITUNG_ONGKIR = "api/pengiriman/hitung"
    const val HITUNG_ONGKIR_V2 = "api/pengiriman/hitung-v2"
    const val PILIH_PENGIRIMAN = "api/pengiriman/pilih"

    // Pesanan (Checkout & History)
    const val CREATE_PESANAN = "api/pesanan"
    const val GET_PESANAN_CLIENT = "api/pesanan/user/"
    const val GET_PESANAN_ADMIN = "api/pesanan/admin"
    const val GET_PESANAN_USER = "api/pesanan/user/{user_id}"
    const val GET_DETAIL_PESANAN = "api/pesanan/{id}"
    const val UPDATE_STATUS_PESANAN = "api/pesanan/{id}/status"

    // Dashboard Admin (Baru)
    const val GET_DASHBOARD_STATS = "api/pesanan/admin/dashboard-stats"

    // --- NAVIGATION ROUTES ---
    const val ROUTE_LOGIN = "login"
    const val ROUTE_REGISTER = "register"
    const val ROUTE_USER_HOME = "user_home"
    const val ROUTE_ADMIN_HOME = "admin_home"
    const val ROUTE_ADMIN_DASHBOARD = "admin_dashboard" // Baru
    const val ROUTE_ADMIN_SAYUR = "admin_sayur"
    const val ROUTE_ADMIN_PESANAN = "admin_pesanan"
    const val ROUTE_ADMIN_CHAT_LIST = "admin_chat_list"
    const val ROUTE_CART = "cart"
    const val ROUTE_CHECKOUT = "checkout"
    const val ROUTE_ADDRESS = "address"
    const val ROUTE_ADD_ADDRESS = "add_address"
}