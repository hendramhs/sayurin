package com.example.sayurin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.sayurin.ui.admin.AdminMainScreen
import com.example.sayurin.ui.auth.AuthViewModel
import com.example.sayurin.ui.client.MainScreen
import com.example.sayurin.ui.navigation.NavGraph
import com.example.sayurin.ui.theme.SayurinTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SayurinTheme {
                Surface {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = hiltViewModel()

                    // Pastikan di AuthViewModel variabel ini adalah StateFlow atau LiveData
                    val userRole by authViewModel.userRole.collectAsState(initial = null)
                    val isLoggedIn by authViewModel.isLoggedIn.collectAsState(initial = false)

                    if (!isLoggedIn) {
                        // NavGraph murni untuk Login & Register
                        NavGraph(
                            navController = navController,
                            onLogout = { authViewModel.logout() }
                        )
                    } else {
                        when (userRole) {
                            "admin" -> {
                                AdminMainScreen(
                                    navController = navController,
                                    onLogout = { authViewModel.logout() }
                                )
                            }
                            // Menggunakan rujukan eksplisit jika ada ambiguasi
                            else -> {
                                com.example.sayurin.ui.client.MainScreen(
                                    navController = navController,
                                    onLogout = { authViewModel.logout() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
