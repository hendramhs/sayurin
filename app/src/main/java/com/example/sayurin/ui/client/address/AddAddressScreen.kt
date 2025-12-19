package com.example.sayurin.ui.client.address

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sayurin.data.remote.dto.address.KomerceDestination

@Composable
fun AddAddressScreen(
    onBack: () -> Unit,
    viewModel: AddressViewModel = hiltViewModel()
) {
    var query by remember { mutableStateOf("") }
    var selectedDest by remember { mutableStateOf<KomerceDestination?>(null) }
    var alamatLengkap by remember { mutableStateOf("") }
    var namaPenerima by remember { mutableStateOf("") }
    var hpPenerima by remember { mutableStateOf("") }

    val destinations by viewModel.searchResults // Pastikan buat state ini di ViewModel

    Column(modifier = Modifier
        .padding(16.dp)
        .verticalScroll(rememberScrollState())) {
        Text("Tambah Alamat Baru", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = namaPenerima,
            onValueChange = { namaPenerima = it },
            label = { Text("Nama Penerima") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = hpPenerima,
            onValueChange = { hpPenerima = it },
            label = { Text("No HP Penerima") },
            modifier = Modifier.fillMaxWidth()
        )

        // SEARCH FIELD
        OutlinedTextField(
            value = if (selectedDest != null) selectedDest!!.label else query,
            onValueChange = {
                query = it
                selectedDest = null
                viewModel.searchDestinations(it) // Panggil fungsi pencarian di ViewModel
            },
            label = { Text("Cari Kecamatan / Kota") },
            modifier = Modifier.fillMaxWidth()
        )

        // HASIL PENCARIAN (Drop down sederhana)
        if (destinations.isNotEmpty() && selectedDest == null) {
            Card(elevation = CardDefaults.cardElevation(4.dp)) {
                destinations.forEach { dest ->
                    DropdownMenuItem(
                        text = { Text(dest.label) },
                        onClick = { selectedDest = dest }
                    )
                }
            }
        }

        OutlinedTextField(
            value = alamatLengkap,
            onValueChange = { alamatLengkap = it },
            label = { Text("Alamat Lengkap (Jalan, No Rumah)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Button(
            onClick = {
                selectedDest?.let {
                    viewModel.saveAddress(
                        nama = namaPenerima,
                        hp = hpPenerima,
                        alamat = alamatLengkap,
                        destId = it.id,
                        label = "Rumah", // Bisa dibuat dinamis
                        city = it.city_name ?: "",
                        province = it.province_name ?: ""
                    ) { onBack() }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            enabled = selectedDest != null && namaPenerima.isNotEmpty()
        ) {
            Text("Simpan Alamat")
        }
    }
}
