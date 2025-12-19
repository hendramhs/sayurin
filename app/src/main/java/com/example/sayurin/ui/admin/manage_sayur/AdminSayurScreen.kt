package com.example.sayurin.ui.admin.manage_sayur

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@Composable
fun AdminSayurScreen(
    onNavigateToAdd: () -> Unit,
    viewModel: AdminSayurViewModel = hiltViewModel()
) {
    val list by viewModel.sayurList

    // Gunakan Box agar FloatingActionButton bisa diletakkan di atas LazyColumn
    Box(modifier = Modifier.fillMaxSize()) {
        if (list.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Belum ada data sayur", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(list) { sayur ->
                    AdminSayurCard(
                        nama = sayur.nama_sayur,
                        stok = sayur.stok,
                        harga = sayur.harga,
                        satuan = sayur.satuan, // Pastikan model data memiliki satuan
                        onDelete = { viewModel.deleteSayur(sayur.sayur_id) }
                    )
                }
                // Spacer bawah agar item terakhir tidak tertutup FAB
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }

        // Floating Action Button (FAB) diletakkan secara manual di pojok kanan bawah
        FloatingActionButton(
            onClick = onNavigateToAdd,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Tambah Sayur")
        }
    }
}

@Composable
fun AdminSayurCard(
    nama: String,
    stok: Int,
    harga: Int,
    satuan: String,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder Image yang segar
            AsyncImage(
                model = "https://placehold.co/150x150/c6d870/556b2f.png?text=${nama}&font=lato",
                contentDescription = nama,
                modifier = Modifier
                    .size(85.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = nama,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF2E3E2E)
                )

                Text(
                    text = "Per $satuan",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Rp $harga",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // Badge Stok yang lebih bersih
                    Surface(
                        color = if (stok > 0) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "Stok: $stok",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (stok > 0) Color(0xFF2E7D32) else Color(0xFFC62828)
                        )
                    }
                }
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.background(Color(0xFFFFEBEE), CircleShape)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Hapus",
                    tint = Color(0xFFC62828),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
