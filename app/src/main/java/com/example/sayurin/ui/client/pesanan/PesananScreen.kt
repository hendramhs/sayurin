package com.example.sayurin.ui.client.pesanan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PesananScreen(
    onBack: () -> Unit,
    viewModel: PesananViewModel = hiltViewModel()
) {
    val pesananList = viewModel.pesananList
    val detailList = viewModel.detailList
    val isLoading = viewModel.isLoading

    // --- DIALOG DETAIL ITEM ---
    if (detailList.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { viewModel.clearDetail() },
            title = {
                Text("Daftar Belanja", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    detailList.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.nama_sayur, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                                Text(
                                    text = "${item.jumlah} x ${item.satuan}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                            Text(
                                text = "Rp ${item.subtotal}",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.clearDetail() },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Tutup")
                }
            }
        )
    }

    Scaffold(
        containerColor = Color(0xFFF8FBF8),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Riwayat Pesanan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        if (isLoading && pesananList.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (pesananList.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Belum ada riwayat pesanan", color = Color.Gray)
            }
        }

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(pesananList) { pesanan ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    onClick = { viewModel.fetchDetail(pesanan.pesanan_id) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Header: ID & Tanggal
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("No. Pesanan #${pesanan.pesanan_id}", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                            Text(pesanan.tanggal, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // --- LOGIKA STATUS BARU ---
                        val (statusLabel, statusColor) = when (pesanan.status) {
                            "Pending" -> "Belum Bayar" to Color(0xFFEF6C00)   // Oranye
                            "Approved" -> "Dikemas" to Color(0xFF1976D2)     // Biru
                            "Shipped" -> "Dikirim" to Color(0xFF673AB7)      // Ungu
                            "Completed" -> "Selesai" to Color(0xFF2E7D32)    // Hijau
                            else -> pesanan.status to Color.Gray
                        }

                        Surface(
                            color = statusColor.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = statusLabel,
                                color = statusColor,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        HorizontalDivider(Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = Color(0xFFEEEEEE))

                        // Info Kurir & Lokasi
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "${pesanan.shipping_name} (${pesanan.service_name ?: "-"})",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF1F8F1))
                                .padding(8.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Asal", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                Text(pesanan.kota_asal ?: "-", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Tujuan", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                Text(pesanan.kota_tujuan ?: "-", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Total Bayar & Tombol Aksi
                        Row(
                            Modifier.fillMaxWidth().padding(top = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Total Bayar", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                Text(
                                    "Rp ${pesanan.total_bayar}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            // TOMBOL KONFIRMASI (Hanya muncul jika status Shipped / Dikirim)
                            if (pesanan.status == "Shipped") {
                                Button(
                                    onClick = { viewModel.updateStatus(pesanan.pesanan_id, "Completed") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text("Pesanan Diterima", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Text(
                                    text = "Lihat Detail >",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}