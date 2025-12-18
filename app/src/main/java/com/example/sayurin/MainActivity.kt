package com.example.sayurin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.sayurin.presentation.navigation.AppNavGraph
import com.example.sayurin.ui.theme.SayurinTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SayurinTheme {
                val navController = rememberNavController()
                AppNavGraph(navController)
            }
        }
    }
}
