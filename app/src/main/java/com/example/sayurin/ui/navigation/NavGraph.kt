package com.example.sayurin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.sayurin.ui.admin.home.AdminDashboardScreen
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
import com.example.sayurin.ui.about.AboutScreen
// PERBAIKAN IMPORT: Menggunakan dua layar yang berbeda
import com.example.sayurin.ui.client.chat.ChatScreen as ClientChatScreen
import com.example.sayurin.ui.admin.chat.AdminChatScreen
import com.example.sayurin.ui.admin.chat.AdminChatListScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Login.route,
    userId: Int = 0,
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
                    val dest = if (role == "admin") Screen.AdminDashboard.route else Screen.ClientHome.route
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
            HomeScreen()
        }

        composable(Screen.Chat.route) {
            // Menggunakan ClientChatScreen untuk sisi pembeli
            ClientChatScreen(
                onBack = { navController.popBackStack() }
            )
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
        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen()
        }

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

        composable(Screen.AdminChatList.route) {
            AdminChatListScreen(
                onRoomClick = { targetUserId ->
                    navController.navigate(Screen.AdminChatDetail.createRoute(targetUserId))
                }
            )
        }

        composable(
            route = Screen.AdminChatDetail.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val targetUserId = backStackEntry.arguments?.getInt("userId") ?: 0
            // Menggunakan AdminChatScreen untuk sisi admin
            AdminChatScreen(
                userId = targetUserId,
                onBack = { navController.popBackStack() }
            )
        }

        // --- COMMON ---
        composable(Screen.About.route) {
            AboutScreen(onBack = { navController.popBackStack() })
        }
    }
}