package com.example.sayurin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
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
                    // Memanggil NavGraph yang sudah kita buat
                    NavGraph(navController = navController)
                }
            }
        }
    }
}
