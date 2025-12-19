package com.example.sayurin.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.sayurin.data.remote.dto.sayur.SayurResponse

@Composable
fun SayurItem(
    sayur: SayurResponse,
    onAddClick: () -> Unit, // 1. TAMBAHKAN PARAMETER INI
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            AsyncImage(
//                model = "http://10.0.2.2:5000/uploads/${sayur.gambar}",
                model = "https://placehold.co/150x150/c6d870/556b2f.png?text=${sayur.nama_sayur}&font=lato",
                contentDescription = sayur.nama_sayur,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = sayur.nama_sayur,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = "Rp ${sayur.harga} / ${sayur.satuan}",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 2. TAMBAHKAN TOMBOL BELI
                Button(
                    onClick = onAddClick, // Memanggil parameter onAddClick
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Beli", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}
