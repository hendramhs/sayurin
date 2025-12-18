package com.example.sayurin.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sayurin.presentation.auth.AuthViewModel.AuthState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.loginState.collectAsState()

    var noHp by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Collect role after successful login
    LaunchedEffect(state) {
        if (state is AuthState.Success) {
            viewModel.role.collectLatest { role ->
                if (role.isNotEmpty()) {
                    onLoginSuccess(role)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = noHp,
                onValueChange = { noHp = it },
                label = { Text("No HP") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.login(noHp, password) },
                enabled = state !is AuthState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Masuk")
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = onRegisterClick) {
                Text("Belum punya akun? Daftar")
            }

            when (state) {
                is AuthState.Loading -> {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator()
                }

                is AuthState.Error -> {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = (state as AuthState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> {}
            }
        }
    }
}
