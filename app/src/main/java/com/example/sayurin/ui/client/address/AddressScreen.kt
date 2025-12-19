package com.example.sayurin.ui.client.address

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
    onNavigateToAddAddress: () -> Unit,
    onBack: () -> Unit,
    viewModel: AddressViewModel = hiltViewModel()
) {
    val addresses by viewModel.addresses
    val isLoading by viewModel.isLoading

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Alamat Pengiriman") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Kembali")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddAddress) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Alamat")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (addresses.isEmpty()) {
                Text("Belum ada alamat", modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(addresses) { address ->
                        AddressItemRow(
                            address = address,
                            onSetDefault = { viewModel.setDefault(address.address_id) },
                            onDelete = { viewModel.deleteAddress(address.address_id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddressItemRow(
    address: com.example.sayurin.data.remote.dto.address.AddressResponse,
    onSetDefault: () -> Unit,
    onDelete: () -> Unit
) {
    val isDefault = address.is_default == 1

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isDefault) 2.dp else 0.dp,
                color = if (isDefault) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = MaterialTheme.shapes.medium
            ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = address.label,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                if (isDefault) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = address.penerima_nama, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Text(text = address.penerima_hp)
            Text(text = "${address.alamat_lengkap}, ${address.city}, ${address.province}")

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                if (!isDefault) {
                    TextButton(onClick = onSetDefault) {
                        Text("Set Utama")
                    }
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color.Red)
                }
            }
        }
    }
}
