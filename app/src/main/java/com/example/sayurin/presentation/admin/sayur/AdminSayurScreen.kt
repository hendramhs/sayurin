package com.example.sayurin.presentation.admin.sayur

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sayurin.data.remote.dto.SayurDto
import com.example.sayurin.presentation.admin.sayur.AdminSayurViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSayurScreen(
    viewModel: AdminSayurViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
    onBack: () -> Unit
) {
    val sayurList by viewModel.sayurList.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var editItem: SayurDto? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        viewModel.loadSayur()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kelola Sayur") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Text("Back") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editItem = null
                    showDialog = true
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Sayur")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {

            if (loading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(sayurList) { item ->
                        SayurAdminItem(
                            sayur = item,
                            onEdit = {
                                editItem = item
                                showDialog = true
                            },
                            onDelete = { viewModel.deleteSayur(item.sayur_id ?: 0) }
                        )
                    }
                }
            }

            if (error != null) {
                Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            if (showDialog) {
                SayurFormDialog(
                    initial = editItem,
                    onDismiss = { showDialog = false },
                    onSubmit = { dto ->
                        if (editItem == null) viewModel.addSayur(dto)
                        else viewModel.updateSayur(editItem!!.sayur_id!!, dto)

                        showDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun SayurAdminItem(
    sayur: SayurDto,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = sayur.nama_sayur, style = MaterialTheme.typography.titleMedium)
                Text(text = "Harga: Rp ${sayur.harga}")
                Text(text = "Stok: ${sayur.stok} ${sayur.satuan}")
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

@Composable
fun SayurFormDialog(
    initial: SayurDto? = null,
    onDismiss: () -> Unit,
    onSubmit: (SayurDto) -> Unit
) {
    var nama by remember { mutableStateOf(initial?.nama_sayur ?: "") }
    var harga by remember { mutableStateOf((initial?.harga ?: "").toString()) }
    var stok by remember { mutableStateOf((initial?.stok ?: "").toString()) }
    var satuan by remember { mutableStateOf(initial?.satuan ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val dto = SayurDto(
                    sayur_id = initial?.sayur_id,
                    nama_sayur = nama,
                    harga = harga.toIntOrNull() ?: 0,
                    stok = stok.toIntOrNull() ?: 0,
                    satuan = satuan
                )
                onSubmit(dto)
            }) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        },
        title = { Text(if (initial == null) "Tambah Sayur" else "Edit Sayur") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = nama, onValueChange = { nama = it }, label = { Text("Nama Sayur") })
                OutlinedTextField(value = harga, onValueChange = { harga = it }, label = { Text("Harga") })
                OutlinedTextField(value = stok, onValueChange = { stok = it }, label = { Text("Stok") })
                OutlinedTextField(value = satuan, onValueChange = { satuan = it }, label = { Text("Satuan (kg, ikat, dst)") })
            }
        }
    )
}
