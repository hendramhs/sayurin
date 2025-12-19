package com.example.sayurin.ui.admin.manage_sayur

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sayurin.ui.admin.manage_sayur.AdminSayurViewModel

@Composable
fun AdminSayurScreen(    onNavigateToAdd: () -> Unit,
                         viewModel: AdminSayurViewModel = hiltViewModel()
) {
    val list by viewModel.sayurList

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAdd) {
                Icon(Icons.Default.Add, "Tambah Sayur")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(list) { sayur ->
                ListItem(
                    headlineContent = { Text(sayur.nama_sayur) },
                    supportingContent = { Text("Stok: ${sayur.stok} | Rp ${sayur.harga}") },
                    trailingContent = {
                        IconButton(onClick = { viewModel.deleteSayur(sayur.sayur_id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color.Red)
                        }
                    }
                )
                HorizontalDivider()
            }
        }
    }
}
