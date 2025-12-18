package com.example.sayurin.presentation.user.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Model menu lokal dalam file ini
data class UserMenuItem(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun UserHomeScreen(
    onSayur: () -> Unit,
    onPesanan: () -> Unit,
    onAddress: () -> Unit,
    onPengiriman: (() -> Unit)? = null
) {

    val menuItems = listOf(
        UserMenuItem(
            title = "Sayur",
            icon = Icons.Default.ShoppingCart,
            onClick = onSayur
        ),
        UserMenuItem(
            title = "Pesanan",
            icon = Icons.Default.List,
            onClick = onPesanan
        ),
        UserMenuItem(
            title = "Alamat",
            icon = Icons.Default.Place,
            onClick = onAddress
        ),
        UserMenuItem(
            title = "Pengiriman",
            icon = Icons.Default.ShoppingCart,
            onClick = onPengiriman ?: {}
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(16.dp)
    ) {

        Text(
            text = "Sayurin App",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(menuItems) { item ->
                UserMenuCard(item)
            }
        }
    }
}

@Composable
fun UserMenuCard(item: UserMenuItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { item.onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = item.title,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}
