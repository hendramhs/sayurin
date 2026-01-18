package com.example.sayurin.ui.navigation

sealed class Screen(val route: String) {
    // --- AUTH ---
    object Login : Screen("login")
    object Register : Screen("register")

    // --- CLIENT / USER ---
    object ClientHome : Screen("client_home")
    object Address : Screen("address")
    object AddAddress : Screen("add_address")
    object Cart : Screen("cart")
    object Checkout : Screen("checkout")
    object PesananUser : Screen("pesanan_user")
    object Chat : Screen("chat") // Chat untuk Client ke Admin

    // --- ADMIN ---
    object AdminDashboard : Screen("admin_dashboard")
    object AdminSayur : Screen("admin_sayur")
    object AddSayur : Screen("add_sayur")
    object AdminPesanan : Screen("admin_pesanan")
    object AdminChatList : Screen("admin_chat_list") // Daftar chat pembeli
    // Rute dinamis untuk admin membalas chat tertentu
    object AdminChatDetail : Screen("admin_chat_detail/{userId}") {
        fun createRoute(userId: Int) = "admin_chat_detail/$userId"
    }

    // --- COMMON ---
    object About : Screen("about")
}