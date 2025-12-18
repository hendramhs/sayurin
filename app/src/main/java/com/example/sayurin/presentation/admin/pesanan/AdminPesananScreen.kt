package com.example.sayurin.presentation.admin.pesanan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sayurin.presentation.admin.pesanan.AdminPesananViewModel
import com.example.sayurin.data.remote.dto.PesananResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPesananScreen(
    viewModel: AdminPesananViewModel = hiltViewModel(),
    onDetail: (Int) -> Unit,
    onBack: () -> Unit
) {
    val pesananList by viewModel.pesananList.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val message by viewModel.message.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPesanan()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kelola Pesanan") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {

            if (loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(pesananList) { item ->
                        PesananAdminItem(
                            item = item,
                            onDetail = { onDetail(item.pesanan_id) }
                        )
                    }
                }
            }

            if (message != null) {
                Text(
                    text = message ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun PesananAdminItem(
    item: PesananResponse,
    onDetail: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Pesanan #${item.pesanan_id}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text("User: ${item.nama ?: "-"}")
            Text("Tanggal: ${item.tanggal ?: "-"}")
            Text("Total Barang: Rp ${item.total_barang}")
            Text("Ongkir: Rp ${item.ongkir}")
            Text("Total Bayar: Rp ${item.total_bayar}")
            Text("Status: ${item.status}")

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onDetail,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Detail")
            }
        }
    }
}
