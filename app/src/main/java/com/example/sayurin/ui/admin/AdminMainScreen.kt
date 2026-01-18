package com.example.sayurin.ui.admin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Info
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
fun AdminMainScreen(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 1. Logika deteksi detail chat (menggunakan contains karena rute ini memiliki argumen ID)
    val isChatDetail = currentRoute?.contains("admin_chat_detail") ?: false

    // 2. Rute utama Admin (HANYA rute yang butuh TopBar & BottomBar aplikasi)
    // Screen.AdminChatDetail DIHAPUS dari list ini agar tidak terhalang bar aplikasi
    val adminBottomRoutes = listOf(
        Screen.AdminDashboard.route,
        Screen.AdminSayur.route,
        Screen.AdminPesanan.route,
        Screen.AdminChatList.route
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (currentRoute in adminBottomRoutes && !isChatDetail) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Admin Sayurin",
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate(Screen.AdminChatList.route) }) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = "Pesan Masuk",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

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
            if (currentRoute in adminBottomRoutes && !isChatDetail) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Dashboard, null) },
                        label = { Text("Dashboard") },
                        selected = currentRoute == Screen.AdminDashboard.route,
                        onClick = {
                            navController.navigate(Screen.AdminDashboard.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )

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

                    // Menu Pesanan
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
        // 3. PENTING: Jika isChatDetail, gunakan Modifier kosong (tanpa padding Scaffold)
        // Agar AdminChatScreen bisa tampil Full Screen dan input area tidak terdorong/terpotong.
        val contentModifier = if (isChatDetail) {
            Modifier.fillMaxSize()
        } else {
            Modifier.fillMaxSize().padding(padding)
        }

        Box(modifier = contentModifier) {
            NavGraph(navController = navController, onLogout = onLogout)
        }
    }
}