package com.example.sayurin.ui.client.address

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sayurin.data.remote.dto.address.KomerceDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAddressScreen(
    onBack: () -> Unit,
    viewModel: AddressViewModel = hiltViewModel()
) {
    var keyword by remember { mutableStateOf("") }
    var selectedDest by remember { mutableStateOf<KomerceDestination?>(null) }
    var alamatLengkap by remember { mutableStateOf("") }
    var namaPenerima by remember { mutableStateOf("") }
    var hpPenerima by remember { mutableStateOf("") }

    // Observasi hasil pencarian dari ViewModel
    val destinations by viewModel.searchResults

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Alamat Baru") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // FORM NAMA PENERIMA
            OutlinedTextField(
                value = namaPenerima,
                onValueChange = { namaPenerima = it },
                label = { Text("Nama Penerima") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // FORM NO HP
            OutlinedTextField(
                value = hpPenerima,
                onValueChange = { hpPenerima = it },
                label = { Text("No HP Penerima") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // FIELD PENCARIAN KECAMATAN / KOTA
            Text("Wilayah", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = if (selectedDest != null) selectedDest!!.label else keyword,
                onValueChange = {
                    keyword = it
                    selectedDest = null // Reset pilihan jika user mengetik ulang
                    viewModel.searchDestinations(it) // Panggil pencarian di ViewModel
                },
                label = { Text("Cari Kecamatan / Kota") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Contoh: Kebon Jeruk") },
                supportingText = {
                    if (selectedDest == null && keyword.isNotEmpty()) {
                        Text("Pilih wilayah dari daftar yang muncul", color = Color.Gray)
                    }
                }
            )

            // DROPDOWN HASIL PENCARIAN
            if (destinations.isNotEmpty() && selectedDest == null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    destinations.forEach { dest ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(dest.label, style = MaterialTheme.typography.bodyMedium)
                                    Text(
                                        "${dest.city_name}, ${dest.province_name}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
                            },
                            onClick = {
                                selectedDest = dest
                                keyword = dest.label
                            }
                        )
                        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // FORM ALAMAT DETAIL
            OutlinedTextField(
                value = alamatLengkap,
                onValueChange = { alamatLengkap = it },
                label = { Text("Alamat Lengkap (Jalan, No Rumah, Patokan)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(24.dp))

            // TOMBOL SIMPAN
            Button(
                onClick = {
                    selectedDest?.let {
                        viewModel.saveAddress(
                            nama = namaPenerima,
                            hp = hpPenerima,
                            alamat = alamatLengkap,
                            destId = it.id,
                            label = it.label,
                            subdistrict = it.subdistrict_name ?: "",
                            district = it.district_name ?: "",
                            city = it.city_name ?: "",
                            province = it.province_name ?: "",
                            zipCode = it.zip_code ?: ""
                        ) {
                            onBack() // Kembali ke halaman sebelumnya setelah berhasil simpan
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedDest != null && namaPenerima.isNotEmpty() && alamatLengkap.isNotEmpty(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Simpan Alamat")
            }
        }
    }
}
