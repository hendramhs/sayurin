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

    // --- ADMIN ---
    // Ubah AdminDashboard menjadi AdminSayur agar sinkron dengan menu BottomNav "Inventori"
    object AdminSayur : Screen("admin_sayur")
    object AddSayur : Screen("add_sayur")
    object AdminPesanan : Screen("admin_pesanan")

    // --- COMMON ---
    object About : Screen("about")
}
