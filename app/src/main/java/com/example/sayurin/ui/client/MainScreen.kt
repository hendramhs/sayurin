package com.example.sayurin.ui.client

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.sayurin.ui.navigation.NavGraph
import com.example.sayurin.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    // Mendapatkan rute saat ini untuk menentukan visibilitas bar
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Daftar rute yang akan menampilkan TopBar dan BottomBar
    val clientBottomRoutes = listOf(
        Screen.ClientHome.route,
        Screen.PesananUser.route,
        Screen.Cart.route
    )

    Scaffold(
        topBar = {
            // Tampilkan Header Global hanya jika berada di rute utama client
            if (currentRoute in clientBottomRoutes) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Sayurin",
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    actions = {
                        // Tombol About di pojok kanan atas
                        IconButton(onClick = { navController.navigate(Screen.About.route) }) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "About App",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        },
        bottomBar = {
            // Tampilkan Bottom Navigation
            if (currentRoute in clientBottomRoutes) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    // Item: HOME
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text("Home") },
                        selected = currentRoute == Screen.ClientHome.route,
                        onClick = {
                            navController.navigate(Screen.ClientHome.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )

                    // Item: RIWAYAT / HISTORY
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.History, contentDescription = null) },
                        label = { Text("Riwayat") },
                        selected = currentRoute == Screen.PesananUser.route,
                        onClick = {
                            navController.navigate(Screen.PesananUser.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )

                    // Item: KERANJANG / CART
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                        label = { Text("Keranjang") },
                        selected = currentRoute == Screen.Cart.route,
                        onClick = {
                            navController.navigate(Screen.Cart.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        // Konten utama aplikasi dibungkus dengan padding agar tidak tertutup bar
        Box(modifier = Modifier.padding(paddingValues)) {
            NavGraph(
                navController = navController,
                onLogout = onLogout
            )
        }
    }
}
