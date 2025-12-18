package com.example.sayurin.utils

object Constants {

    // Api base url
    const val BASE_URL = "http://192.168.237.246:3000"
    // 10.0.2.2 = alamat khusus emulator Android ke localhost PC

    // Auth
    const val PREF_USER_ID = "pref_user_id"
    const val PREF_ROLE = "pref_role"
    const val PREF_NAME = "pref_name"

    // Datastore
    const val DATASTORE_NAME = "sayurin_datastore"

    // Endpoint
    const val AUTH_LOGIN = "/api/auth/login"
    const val AUTH_REGISTER = "/api/auth/register"

    const val GET_SAYUR = "/api/sayur"

    const val CREATE_PESANAN = "/api/pesanan"
    const val GET_PESANAN_ADMIN = "/api/pesanan/admin"

    const val HITUNG_ONGKIR = "/api/pengiriman/hitung"
    const val PILIH_ONGKIR = "/api/pengiriman/pilih"

    const val ADD_ADDRESS = "/api/addresses/add"
    const val GET_ADDRESS_BY_USER = "/api/addresses/user/"
    const val GET_DEFAULT_ADDRESS = "/api/addresses/default/"
    const val SET_DEFAULT_ADDRESS = "/api/addresses/set-default"

    // Navigation routes
    const val ROUTE_LOGIN = "login"
    const val ROUTE_REGISTER = "register"
    const val ROUTE_USER_HOME = "user_home"
    const val ROUTE_ADMIN_HOME = "admin_home"
    const val ROUTE_SAYUR = "sayur"
    const val ROUTE_PESANAN = "pesanan"
    const val ROUTE_ADDRESS = "address"
}
