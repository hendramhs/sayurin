package com.example.sayurin.ui.client.checkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingBag
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
fun CheckoutScreen(
    onBack: () -> Unit,
    onNavigateToAddress: () -> Unit,
    onSuccessOrder: () -> Unit,
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val cartItems = viewModel.cartItems
    val address = viewModel.selectedAddress
    val layananList = viewModel.layananList
    val selectedLayanan = viewModel.selectedLayanan
    val totalHarga = viewModel.calculateTotal()
    val isLoading = viewModel.isLoading

    Scaffold(
        containerColor = Color(0xFFF8FBF8), // Background hijau sangat muda agar segar
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Checkout", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomOrderBar(
                totalHarga = totalHarga,
                enabled = selectedLayanan != null && address != null && !isLoading,
                onOrderClick = {
                    viewModel.createOrder { onSuccessOrder() }
                }
            )
        }
    ) { padding ->
        if (isLoading && cartItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. SEKSI ALAMAT
            item {
                SectionHeader("Alamat Pengiriman", Icons.Default.LocationOn)
                Card(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    onClick = onNavigateToAddress,
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            if (address != null) {
                                Text("${address.penerima_nama} | ${address.penerima_hp}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                                Text(address.alamat_lengkap, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            } else {
                                Text("Pilih Alamat Pengiriman", color = Color.Red, fontWeight = FontWeight.Medium)
                                Text("Anda belum memilih alamat", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                        }
                        Text("Ubah", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // 2. SEKSI RINGKASAN ITEM
            item { SectionHeader("Ringkasan Pesanan", Icons.Default.ShoppingBag) }
            items(cartItems) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.nama_sayur, fontWeight = FontWeight.SemiBold)
                            Text("${item.jumlah} x Rp ${item.harga}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                        Text("Rp ${item.harga * item.jumlah}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            // 3. SEKSI PILIH PENGIRIMAN
            item {
                SectionHeader("Jasa Pengiriman", null)
                if (address == null) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFFFF3E0),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Silakan pilih alamat terlebih dahulu", modifier = Modifier.padding(12.dp), style = MaterialTheme.typography.bodySmall, color = Color(0xFFE65100))
                    }
                }
            }

            items(layananList) { layanan ->
                val isSelected = selectedLayanan == layanan
                OutlinedCard(
                    onClick = { viewModel.selectedLayanan = layanan },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFFEEEEEE)
                    ),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f) else Color.White
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("${layanan.shipping_name} - ${layanan.service_name}", fontWeight = FontWeight.Bold)
                            Text("Estimasi Tiba: ${layanan.etd}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                        Text("Rp ${layanan.shipping_cost}", fontWeight = FontWeight.ExtraBold, color = if(isSelected) MaterialTheme.colorScheme.primary else Color.Black)
                        if (isSelected) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            // 4. RINCIAN TOTAL
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Rincian Pembayaran", fontWeight = FontWeight.Bold)
                        val subtotal = cartItems.sumOf { it.harga * it.jumlah }

                        PriceRow("Subtotal Produk", "Rp $subtotal")
                        PriceRow("Ongkos Kirim", if (selectedLayanan != null) "Rp ${selectedLayanan.shipping_cost}" else "Rp 0", isHighlight = selectedLayanan != null)

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), thickness = 0.5.dp)

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Pembayaran", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                            Text(
                                "Rp $totalHarga",
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
fun SectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector?) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 4.dp)) {
        if (icon != null) {
            Icon(icon, null, modifier = Modifier.size(18.dp), tint = Color.Gray)
            Spacer(Modifier.width(8.dp))
        }
        Text(title, fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun PriceRow(label: String, value: String, isHighlight: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
        Text(value, fontWeight = if(isHighlight) FontWeight.Bold else FontWeight.Medium, color = if(isHighlight) Color.Black else Color.Gray)
    }
}

@Composable
fun BottomOrderBar(totalHarga: Int, enabled: Boolean, onOrderClick: () -> Unit) {
    Surface(
        color = Color.White,
        tonalElevation = 8.dp,
        shadowElevation = 12.dp,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Total Tagihan", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Text(
                    "Rp $totalHarga",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Button(
                onClick = onOrderClick,
                enabled = enabled,
                modifier = Modifier.height(50.dp).width(160.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = Color.LightGray
                )
            ) {
                Text("Buat Pesanan", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}
