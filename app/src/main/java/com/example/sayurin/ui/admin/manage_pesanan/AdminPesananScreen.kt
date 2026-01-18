package com.example.sayurin.ui.admin.manage_pesanan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sayurin.data.remote.dto.pesanan.PesananAdminResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPesananScreen(
    viewModel: AdminPesananViewModel = hiltViewModel()
) {
    val pesananList = viewModel.pesananList
    val detailList = viewModel.detailList
    val isLoading = viewModel.isLoading

    // Dialog Detail Item
    if (detailList.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { viewModel.clearDetail() },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Receipt, null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(8.dp))
                    Text("Detail Pesanan", fontWeight = FontWeight.ExtraBold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    detailList.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFF1F8E9),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(item.nama_sayur.take(1), fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                                }
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.nama_sayur, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("${item.jumlah} x ${item.satuan}", fontSize = 12.sp, color = Color.Gray)
                            }
                            Text(
                                "Rp ${item.subtotal}",
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { viewModel.clearDetail() }, shape = RoundedCornerShape(8.dp)) {
                    Text("Tutup")
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8FBF8))) {
        if (isLoading && pesananList.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (pesananList.isEmpty()) {
            Text("Belum ada pesanan masuk", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(pesananList) { pesanan ->
                PesananAdminCard(
                    pesanan = pesanan,
                    onClick = { viewModel.fetchDetail(pesanan.pesanan_id) },
                    onApprove = { viewModel.updateStatus(pesanan.pesanan_id, "Approved") },
                    onReject = { viewModel.updateStatus(pesanan.pesanan_id, "Rejected") },
                    onShip = { viewModel.updateStatus(pesanan.pesanan_id, "Shipped") }
                )
            }
        }
    }
}

@Composable
fun PesananAdminCard(
    pesanan: PesananAdminResponse,
    onClick: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onShip: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: ID & Tanggal
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(6.dp)) {
                    Text(
                        "#${pesanan.pesanan_id}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(pesanan.tanggal, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }

            Spacer(Modifier.height(12.dp))

            // User Info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                Spacer(Modifier.width(6.dp))
                Text(pesanan.nama ?: "Pembeli", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            }

            // Shipping Info Section
            if (pesanan.shipping_name != null) {
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth().background(Color(0xFFF9F9F9), RoundedCornerShape(8.dp)).padding(8.dp)) {
                    Icon(Icons.Default.LocalShipping, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text("${pesanan.shipping_name} - ${pesanan.service_name}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("Tujuan: ${pesanan.kota_tujuan}", fontSize = 11.sp, color = Color.Gray)
                        Text("Ongkir: Rp ${pesanan.shipping_cost}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Total Bayar & Status
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Total Pembayaran", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text("Rp ${pesanan.total_bayar}", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                }
                StatusBadge(status = pesanan.status)
            }

            Spacer(Modifier.height(12.dp))

            // Action Buttons Logic
            when (pesanan.status) {
                "Pending" -> {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(
                            onClick = onReject,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                        ) {
                            Icon(Icons.Default.Cancel, null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Tolak")
                        }
                        Button(
                            onClick = onApprove,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) {
                            Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Setuju")
                        }
                    }
                }
                "Approved" -> {
                    Button(
                        onClick = onShip,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)) // Warna Ungu untuk Kirim
                    ) {
                        Icon(Icons.Default.LocalShipping, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Kirim Pesanan")
                    }
                }
                else -> {
                    Text(
                        "Klik untuk riwayat detail item",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.LightGray,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (bgColor, textColor, label) = when (status) {
        "Pending" -> Triple(Color(0xFFFFF3E0), Color(0xFFEF6C00), "Menunggu")
        "Approved" -> Triple(Color(0xFFE3F2FD), Color(0xFF1976D2), "Dikemas")
        "Shipped" -> Triple(Color(0xFFF3E5F5), Color(0xFF7B1FA2), "Dikirim")
        "Completed" -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), "Selesai")
        "Rejected" -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), "Ditolak")
        else -> Triple(Color(0xFFF5F5F5), Color.Gray, status)
    }

    Surface(color = bgColor, shape = RoundedCornerShape(8.dp)) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            color = textColor,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}