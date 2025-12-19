package com.example.sayurin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sayurin.ui.admin.manage_sayur.AdminSayurScreen
import com.example.sayurin.ui.admin.manage_sayur.AddSayurScreen
import com.example.sayurin.ui.admin.manage_pesanan.AdminPesananScreen
import com.example.sayurin.ui.auth.login.LoginScreen
import com.example.sayurin.ui.auth.register.RegisterScreen
import com.example.sayurin.ui.client.home.HomeScreen
import com.example.sayurin.ui.client.address.AddressScreen
import com.example.sayurin.ui.client.address.AddAddressScreen
import com.example.sayurin.ui.client.cart.CartScreen
import com.example.sayurin.ui.client.checkout.CheckoutScreen
import com.example.sayurin.ui.client.pesanan.PesananScreen
import com.example.sayurin.ui.about.AboutScreen // Import AboutScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Login.route,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // --- AUTH GROUP ---
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = { role ->
                    val dest = if (role == "admin") Screen.AdminSayur.route else Screen.ClientHome.route
                    navController.navigate(dest) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(onBackToLogin = { navController.popBackStack() })
        }

        // --- CLIENT GROUP ---
        composable(Screen.ClientHome.route) {
            // Perbaikan: HomeScreen sekarang tidak perlu parameter navigasi banyak
            // karena navigasi Cart & History sudah ada di BottomBar
            HomeScreen()
        }

        composable(Screen.Cart.route) {
            CartScreen(
                onBack = { navController.popBackStack() },
                onNavigateToCheckout = { navController.navigate(Screen.Checkout.route) }
            )
        }

        composable(Screen.Checkout.route) {
            CheckoutScreen(
                onBack = { navController.popBackStack() },
                onNavigateToAddress = { navController.navigate(Screen.Address.route) },
                onSuccessOrder = {
                    navController.navigate(Screen.ClientHome.route) {
                        popUpTo(Screen.ClientHome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Address.route) {
            AddressScreen(
                onNavigateToAddAddress = { navController.navigate(Screen.AddAddress.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.AddAddress.route) {
            AddAddressScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.PesananUser.route) {
            PesananScreen(onBack = { navController.popBackStack() })
        }

        // --- ADMIN GROUP ---
        composable(Screen.AdminSayur.route) {
            AdminSayurScreen(
                onNavigateToAdd = { navController.navigate(Screen.AddSayur.route) }
            )
        }

        composable(Screen.AddSayur.route) {
            AddSayurScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.AdminPesanan.route) {
            AdminPesananScreen()
        }

        // --- COMMON ---
        composable(Screen.About.route) {
            AboutScreen(onBack = { navController.popBackStack() })
        }
    }
}
