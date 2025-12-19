package com.example.sayurin.ui.navigation

sealed class Screen(val route: String) {
    // Auth
    object Login : Screen("login")
    object Register : Screen("register")

    // Client
    object ClientHome : Screen("client_home")
    object Address : Screen("address")
    object AddAddress : Screen("add_address") // Tambahkan ini jika belum
    object Cart : Screen("cart")              // Tambahkan rute Keranjang
    object Checkout : Screen("checkout")      // Tambahkan rute Checkout

    // Admin
    object AdminDashboard : Screen("admin_dashboard")
    object AddSayur : Screen("add_sayur")
}
