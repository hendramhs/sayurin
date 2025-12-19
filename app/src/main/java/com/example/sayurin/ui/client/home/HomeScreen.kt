package com.example.sayurin.ui.client.home

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@Composable
fun HomeScreen(
    // Parameter navigasi dihapus dari sini karena sudah ditangani oleh MainScreen (Global TopBar/BottomBar)
    viewModel: HomeViewModel = hiltViewModel()
) {
    val sayurList by viewModel.sayurList
    val isLoading by viewModel.isLoading
    val context = LocalContext.current

    // Tidak menggunakan Scaffold lagi karena sudah ada di MainScreen
    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (sayurList.isEmpty() && !isLoading) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Sayur belum tersedia", color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { viewModel.fetchSayur() }) {
                    Text("Refresh")
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(sayurList) { sayur ->
                    SayurCard(
                        nama = sayur.nama_sayur,
                        harga = sayur.harga,
                        stok = sayur.stok,
                        satuan = sayur.satuan,
                        onAddClick = {
                            viewModel.addToCart(sayur.sayur_id)
                            Toast.makeText(context, "${sayur.nama_sayur} masuk keranjang", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SayurCard(
    nama: String,
    harga: Int,
    stok: Int,
    satuan: String,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Image Placeholder sesuai permintaan
            AsyncImage(
                model = "https://placehold.co/150x150/c6d870/556b2f.png?text=${nama}&font=lato",
                contentDescription = nama,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = nama,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Text(
                    text = "Per $satuan",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Rp $harga",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Stok: $stok",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (stok > 0) Color(0xFF2E7D32) else Color.Red
                        )
                    }

                    // Tombol Tambah yang lebih cantik
                    IconButton(
                        onClick = onAddClick,
                        enabled = stok > 0,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Tambah ke Keranjang",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
