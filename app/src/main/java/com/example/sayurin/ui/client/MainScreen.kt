package com.example.sayurin.ui.client

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Daftar rute utama yang akan menampilkan TopBar dan BottomBar aplikasi
    val clientBottomRoutes = listOf(
        Screen.ClientHome.route,
        Screen.PesananUser.route,
        Screen.Cart.route
        // Screen.Chat.route DIHAPUS dari sini agar Chat menjadi Full Screen
    )

    // Cek apakah rute saat ini adalah chat untuk mengatur padding secara dinamis
    val isChatRoute = currentRoute == Screen.Chat.route

    Scaffold(
        topBar = {
            // Hanya tampilkan TopBar global jika rute ada di daftar rute utama
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
                        // TOMBOL UNTUK NAVIGASI KE CHAT
                        IconButton(onClick = { navController.navigate(Screen.Chat.route) }) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = "Chat Admin",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

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
            // Hanya tampilkan BottomBar jika rute ada di daftar rute utama
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

                    // Item: RIWAYAT
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

                    // Item: KERANJANG
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
        // Jika sedang di halaman chat, abaikan padding (set ke 0) agar area input tidak terangkat
        val contentPadding = if (isChatRoute) PaddingValues(0.dp) else paddingValues

        Box(modifier = Modifier.padding(contentPadding)) {
            NavGraph(
                navController = navController,
                onLogout = onLogout
            )
        }
    }
}