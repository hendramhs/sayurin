package com.example.sayurin.ui.admin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.sayurin.ui.navigation.NavGraph
import com.example.sayurin.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMainScreen(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Rute Admin yang menampilkan BottomBar
    val adminBottomRoutes = listOf(
        Screen.AdminSayur.route,
        Screen.AdminPesanan.route
    )

    Scaffold(
        topBar = {
            if (currentRoute in adminBottomRoutes) {
                CenterAlignedTopAppBar(
                    title = {
                        Text("Admin Sayurin", fontWeight = FontWeight.Bold)
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate(Screen.About.route) }) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "About",
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
            if (currentRoute in adminBottomRoutes) {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Inventory, null) },
                        label = { Text("Inventori") },
                        selected = currentRoute == Screen.AdminSayur.route,
                        onClick = {
                            navController.navigate(Screen.AdminSayur.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.ReceiptLong, null) },
                        label = { Text("Pesanan") },
                        selected = currentRoute == Screen.AdminPesanan.route,
                        onClick = {
                            navController.navigate(Screen.AdminPesanan.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            // Memanggil NavGraph yang sama, logika rute diatur di dalamnya
            NavGraph(navController = navController, onLogout = onLogout)
        }
    }
}
