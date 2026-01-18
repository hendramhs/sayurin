package com.example.sayurin.ui.admin.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.data.LineCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.shape.Shape
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun AdminDashboardScreen(
    viewModel: AdminDashboardViewModel = hiltViewModel()
) {
    val stats by viewModel.dashboardData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedPeriod by remember { mutableStateOf("Harian") }
    val calendar = remember { Calendar.getInstance() }
    var selectedMonth by remember { mutableIntStateOf(calendar.get(Calendar.MONTH) + 1) }
    var selectedYear by remember { mutableIntStateOf(calendar.get(Calendar.YEAR)) }

    LaunchedEffect(selectedMonth, selectedYear) {
        viewModel.fetchDashboard(selectedMonth, selectedYear)
    }

    // ===== Marker (tooltip) FIX =====
    val marker = rememberDefaultCartesianMarker(
        label = rememberTextComponent(
            color = Color.Black,
            background = rememberShapeComponent(
                shape = Shape.Pill,
                color = Color.White
            ),
            padding = Dimensions(horizontalDp = 8f, verticalDp = 4f)
        )
    )

    // ===== Revenue Data Handling (LINE CHART) =====
    val revenueData: List<Float> = when (selectedPeriod) {
        "Harian" -> stats?.revenue?.daily?.map { it.total.toFloat() }
        "Mingguan" -> stats?.revenue?.weekly?.map { it.total.toFloat() }
        else -> stats?.revenue?.monthly?.map { it.total.toFloat() }
    } ?: emptyList()

    val revenueLabels: List<String> = when (selectedPeriod) {
        "Harian" -> stats?.revenue?.daily?.mapIndexed { index, _ -> (index + 1).toString() }
        "Mingguan" -> stats?.revenue?.weekly?.map { it.week.orEmpty() }
        else -> stats?.revenue?.monthly?.map { it.month.orEmpty() }
    } ?: emptyList()

    // Mencegah Crash: Series can't be empty
    val revenueModel = remember(revenueData) {
        if (revenueData.isEmpty()) null
        else CartesianChartModel(LineCartesianLayerModel.build { series(revenueData) })
    }

    // ===== Status Data Handling (BAR CHART) =====
    val statusCounts = stats?.status_distribution?.map { it.count.toFloat() } ?: emptyList()
    val statusLabels = stats?.status_distribution?.map { item ->
        when (item.status) {
            "Pending" -> "Pending"
            "Approved" -> "Dikemas"
            "Shipped" -> "Dikirim"
            "Completed" -> "Selesai"
            "Rejected" -> "Ditolak"
            else -> item.status
        }
    } ?: emptyList()

    val statusModel = remember(statusCounts) {
        if (statusCounts.isEmpty()) null
        else CartesianChartModel(ColumnCartesianLayerModel.build { series(statusCounts) })
    }

    // ===== Top Selling Data Handling (BAR CHART) =====
    val topQty = stats?.top_selling?.map { it.total_qty.toFloat() } ?: emptyList()
    val topNames = stats?.top_selling?.map { it.nama_sayur } ?: emptyList()

    val topSellingModel = remember(topQty) {
        if (topQty.isEmpty()) null
        else CartesianChartModel(ColumnCartesianLayerModel.build { series(topQty) })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FBF8))
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Stats Cards
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatCard("Total Pendapatan", formatRupiah(stats?.summary?.total_pendapatan ?: 0), Icons.Default.Payments, Color(0xFF2E7D32), Modifier.fillMaxWidth())
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            StatCard("Pending", "${stats?.summary?.total_pending ?: 0}", Icons.Default.History, Color(0xFFEF6C00), Modifier.weight(1f))
                            StatCard("Dikemas", "${stats?.summary?.total_approved ?: 0}", Icons.Default.Inventory2, Color(0xFF1976D2), Modifier.weight(1f))
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            StatCard("Dikirim", "${stats?.summary?.total_shipped ?: 0}", Icons.Default.LocalShipping, Color(0xFF673AB7), Modifier.weight(1f))
                            StatCard("Selesai", "${stats?.summary?.total_completed ?: 0}", Icons.Default.CheckCircle, Color(0xFF2E7D32), Modifier.weight(1f))
                        }
                        StatCard("Ditolak", "${stats?.summary?.total_rejected ?: 0}", Icons.Default.Cancel, Color.Red, Modifier.fillMaxWidth())
                    }
                }

                // LINE CHART - Revenue Trend
                item {
                    DashboardSection(title = "Tren Pendapatan") {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            // Row 1: Filter Harian, Mingguan, Bulanan
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("Harian", "Mingguan", "Bulanan").forEach { period ->
                                    FilterChip(
                                        selected = selectedPeriod == period,
                                        onClick = { selectedPeriod = period },
                                        label = { Text(period, fontSize = 12.sp) }
                                    )
                                }
                            }

                            // Row 2: Filter Bulan dan Tahun (DENGAN IKON PANAH)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (selectedPeriod != "Bulanan") {
                                    MonthPicker(
                                        modifier = Modifier.weight(1f),
                                        selectedMonth = selectedMonth
                                    ) { selectedMonth = it }
                                }
                                YearPicker(
                                    modifier = Modifier.weight(1f),
                                    selectedYear = selectedYear
                                ) { selectedYear = it }
                            }

                            if (revenueModel != null) {
                                CartesianChartHost(
                                    chart = rememberCartesianChart(
                                        rememberLineCartesianLayer(),
                                        startAxis = rememberStartAxis(),
                                        bottomAxis = rememberBottomAxis(
                                            valueFormatter = { value, _, _ -> revenueLabels.getOrNull(value.toInt()).orEmpty() }
                                        ),
                                        marker = marker
                                    ),
                                    model = revenueModel,
                                    modifier = Modifier.height(220.dp)
                                )
                            } else {
                                EmptyState("Belum ada data pendapatan.")
                            }
                        }
                    }
                }

                // BAR CHART - Status Distribution
                item {
                    DashboardSection(title = "Distribusi Status Pesanan") {
                        if (statusModel != null) {
                            CartesianChartHost(
                                chart = rememberCartesianChart(
                                    rememberColumnCartesianLayer(
                                        columnProvider = ColumnCartesianLayer.ColumnProvider.series(
                                            rememberLineComponent(color = Color(0xFF1976D2), thickness = 16.dp)
                                        )
                                    ),
                                    startAxis = rememberStartAxis(),
                                    bottomAxis = rememberBottomAxis(
                                        valueFormatter = { value, _, _ -> statusLabels.getOrNull(value.toInt()).orEmpty() }
                                    ),
                                    marker = marker
                                ),
                                model = statusModel,
                                modifier = Modifier.height(200.dp)
                            )
                        } else {
                            EmptyState("Belum ada data status.")
                        }
                    }
                }

                // BAR CHART - Top Selling
                item {
                    DashboardSection(title = "Sayur Terlaris") {
                        if (topSellingModel != null) {
                            CartesianChartHost(
                                chart = rememberCartesianChart(
                                    rememberColumnCartesianLayer(
                                        columnProvider = ColumnCartesianLayer.ColumnProvider.series(
                                            rememberLineComponent(color = Color(0xFFFFA000), thickness = 16.dp)
                                        )
                                    ),
                                    startAxis = rememberStartAxis(),
                                    bottomAxis = rememberBottomAxis(
                                        valueFormatter = { value, _, _ -> topNames.getOrNull(value.toInt()).orEmpty() }
                                    ),
                                    marker = marker
                                ),
                                model = topSellingModel,
                                modifier = Modifier.height(200.dp)
                            )
                        } else {
                            EmptyState("Belum ada data sayur terlaris.")
                        }
                    }
                }
            }
        }
    }
}

// --- UI COMPONENTS ---

@Composable
fun StatCard(title: String, value: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = color, modifier = Modifier.size(32.dp))
            Spacer(Modifier.width(16.dp))
            Column {
                Text(title, fontSize = 11.sp, color = Color.Gray)
                Text(value, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
            }
        }
    }
}

@Composable
fun DashboardSection(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.DarkGray)
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun EmptyState(message: String) {
    Box(Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
        Text(message, color = Color.Gray, fontSize = 12.sp)
    }
}

@Composable
fun MonthPicker(modifier: Modifier, selectedMonth: Int, onMonthSelected: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val months = listOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des")

    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(months[selectedMonth - 1], fontSize = 12.sp, color = Color.Black)
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Gray
                )
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            months.forEachIndexed { i, name ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = { onMonthSelected(i + 1); expanded = false }
                )
            }
        }
    }
}

@Composable
fun YearPicker(modifier: Modifier, selectedYear: Int, onYearSelected: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val years = (2024..2026).toList()

    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(selectedYear.toString(), fontSize = 12.sp, color = Color.Black)
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Gray
                )
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            years.forEach { year ->
                DropdownMenuItem(
                    text = { Text(year.toString()) },
                    onClick = { onYearSelected(year); expanded = false }
                )
            }
        }
    }
}

fun formatRupiah(number: Long): String {
    return NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(number)
}