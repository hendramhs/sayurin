package com.example.sayurin.ui.client.checkout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onBack: () -> Unit,
    onNavigateToAddress: () -> Unit,
    onSuccessOrder: () -> Unit,
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val cartItems = viewModel.cartItems
    val address = viewModel.selectedAddress
    val totalHarga = viewModel.calculateTotal()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            BottomOrderBar(
                totalHarga = totalHarga,
                onOrderClick = {
                    viewModel.createOrder {
                        onSuccessOrder() // Ini akan memicu navigasi di NavGraph
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // SEKSI ALAMAT
            item {
                Text("Alamat Pengiriman", fontWeight = FontWeight.Bold)
                Card(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    onClick = onNavigateToAddress // User bisa ganti alamat
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            if (address != null) {
                                Text("${address.penerima_nama} | ${address.penerima_hp}", fontWeight = FontWeight.Bold)
                                Text(address.alamat_lengkap, style = MaterialTheme.typography.bodySmall)
                            } else {
                                Text("Pilih Alamat Pengiriman", color = Color.Red)
                            }
                        }
                    }
                }
            }

            // SEKSI RINGKASAN ITEM
            item { Text("Ringkasan Pesanan", fontWeight = FontWeight.Bold) }
            items(cartItems) { item ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${item.nama_sayur} x${item.jumlah}")
                    Text("Rp ${item.harga * item.jumlah}")
                }
            }

            item { Divider() }

            // RINCIAN BIAYA PENGIRIMAN
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Ongkos Kirim")
                        // Menampilkan kurir jika tersedia
                        Text(
                            text = if (viewModel.ongkir > 0) "Rp ${viewModel.ongkir}" else "Gratis/Belum dihitung",
                            color = if (viewModel.ongkir > 0) Color.Unspecified else Color.Gray
                        )
                    }
                    if (viewModel.courierName != null) {
                        Text(
                            text = "Kurir: ${viewModel.courierName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }

            item { Divider() }

            // TOTAL TAGIHAN
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Pembayaran", fontWeight = FontWeight.Bold)
                    Text(
                        "Rp $totalHarga",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
fun BottomOrderBar(totalHarga: Int, onOrderClick: () -> Unit) {
    Surface(tonalElevation = 8.dp, shadowElevation = 8.dp) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Total Tagihan", style = MaterialTheme.typography.bodySmall)
                Text("Rp $totalHarga", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Button(onClick = onOrderClick) {
                Text("Buat Pesanan")
            }
        }
    }
}
