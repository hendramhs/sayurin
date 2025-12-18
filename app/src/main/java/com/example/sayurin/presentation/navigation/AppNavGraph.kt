package com.example.sayurin.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.sayurin.presentation.admin.home.AdminHomeScreen
import com.example.sayurin.presentation.admin.sayur.AdminSayurScreen
import com.example.sayurin.presentation.auth.LoginScreen
import com.example.sayurin.presentation.auth.RegisterScreen
import com.example.sayurin.presentation.user.home.UserHomeScreen

@Composable
fun AppNavGraph(nav: NavHostController) {

    NavHost(
        navController = nav,
        startDestination = Routes.LOGIN
    ) {

        // ===========================
        // AUTH
        // ===========================
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { role ->
                    if (role == "admin") {
                        nav.navigate(Routes.ADMIN_HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    } else {
                        nav.navigate(Routes.USER_HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                },
                onRegisterClick = { nav.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onBack = { nav.popBackStack() }
            )
        }

        // ===========================
        // USER FLOW
        // ===========================
        composable(Routes.USER_HOME) {
            UserHomeScreen(
                onSayur = { nav.navigate(Routes.USER_SAYUR) },
                onPesanan = { nav.navigate(Routes.USER_PESANAN) },
                onAddress = { nav.navigate(Routes.USER_ADDRESS) }
            )
        }

        composable(Routes.USER_SAYUR) {
            UserSayurScreen(
                onBack = { nav.popBackStack() }
            )
        }

        composable(Routes.USER_PESANAN) {
            UserPesananScreen(
                onDetail = { id ->
                    nav.navigate("user_detail_pesanan/$id")
                }
            )
        }

        composable(
            Routes.USER_DETAIL_PESANAN,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("id") ?: 0
            UserDetailPesananScreen(
                id = id,
                onBack = { nav.popBackStack() }
            )
        }

        composable(Routes.USER_ADDRESS) {
            AddressScreen(
                onBack = { nav.popBackStack() }
            )
        }

        composable(Routes.USER_PENGIRIMAN) {
            PengirimanScreen(
                onBack = { nav.popBackStack() }
            )
        }

        // ===========================
        // ADMIN FLOW
        // ===========================
        composable(Routes.ADMIN_HOME) {
            AdminHomeScreen(
                onManageSayur = { nav.navigate(Routes.ADMIN_SAYUR) },
                onManagePesanan = { nav.navigate(Routes.ADMIN_PESANAN) }
            )
        }

        composable(Routes.ADMIN_SAYUR) {
            AdminSayurScreen(
                onBack = { nav.popBackStack() }
            )
        }

        composable(Routes.ADMIN_PESANAN) {
            AdminPesananScreen(
                onDetail = { id -> nav.navigate("admin_detail_pesanan/$id") },
                onBack = { nav.popBackStack() }
            )
        }

        composable(
            Routes.ADMIN_DETAIL_PESANAN,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("id") ?: 0
            AdminDetailPesananScreen(
                id = id,
                onBack = { nav.popBackStack() }
            )
        }
    }
}
