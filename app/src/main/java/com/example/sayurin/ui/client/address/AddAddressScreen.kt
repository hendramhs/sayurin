package com.example.sayurin.ui.client.address

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var labelAlamat by remember { mutableStateOf("Rumah") } // Default label

    val destinations by viewModel.searchResults
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = Color(0xFFF8FBF8), // Background hijau pucat
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tambah Alamat", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // INFO PENERIMA SECTION
            Text("Informasi Penerima", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)

            AddressTextField(
                value = namaPenerima,
                onValueChange = { namaPenerima = it },
                label = "Nama Lengkap",
                icon = Icons.Default.Person
            )

            AddressTextField(
                value = hpPenerima,
                onValueChange = { hpPenerima = it },
                label = "Nomor Handphone",
                icon = Icons.Default.Phone
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)

            // WILAYAH SECTION
            Text("Detail Lokasi", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)

            Column {
                OutlinedTextField(
                    value = if (selectedDest != null) selectedDest!!.label else keyword,
                    onValueChange = {
                        keyword = it
                        selectedDest = null
                        viewModel.searchDestinations(it)
                    },
                    label = { Text("Cari Kecamatan / Kota") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = MaterialTheme.colorScheme.primary) },
                    placeholder = { Text("Contoh: Kebon Jeruk") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                // HASIL PENCARIAN (DROPDOWN MODEREN)
                if (destinations.isNotEmpty() && selectedDest == null) {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
                    ) {
                        destinations.take(5).forEach { dest ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(dest.label, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text("${dest.city_name}, ${dest.province_name}", fontSize = 12.sp, color = Color.Gray)
                                    }
                                },
                                onClick = {
                                    selectedDest = dest
                                    keyword = dest.label
                                }
                            )
                            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFF1F1F1))
                        }
                    }
                }
            }

            AddressTextField(
                value = alamatLengkap,
                onValueChange = { alamatLengkap = it },
                label = "Alamat Lengkap (Jalan, No Rumah, Patokan)",
                icon = null,
                singleLine = false,
                minLines = 3
            )

            // LABEL ALAMAT (Rumah / Kantor)
            Text("Label Alamat", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                LabelChip(
                    selected = labelAlamat == "Rumah",
                    text = "Rumah",
                    icon = Icons.Default.Home,
                    onClick = { labelAlamat = "Rumah" }
                )
                LabelChip(
                    selected = labelAlamat == "Kantor",
                    text = "Kantor",
                    icon = Icons.Default.Work,
                    onClick = { labelAlamat = "Kantor" }
                )
            }

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
                            label = labelAlamat, // Menggunakan label pilihan
                            subdistrict = it.subdistrict_name ?: "",
                            district = it.district_name ?: "",
                            city = it.city_name ?: "",
                            province = it.province_name ?: "",
                            zipCode = it.zip_code ?: ""
                        ) { onBack() }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                enabled = selectedDest != null && namaPenerima.isNotEmpty() && alamatLengkap.isNotEmpty(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Simpan Alamat Sekarang", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun AddressTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector?,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = icon?.let { { Icon(it, null, tint = MaterialTheme.colorScheme.primary) } },
        singleLine = singleLine,
        minLines = minLines,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

@Composable
fun LabelChip(
    selected: Boolean,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text) },
        leadingIcon = { Icon(icon, null, modifier = Modifier.size(18.dp)) },
        shape = RoundedCornerShape(8.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.primary,
            selectedLeadingIconColor = MaterialTheme.colorScheme.primary
        )
    )
}
