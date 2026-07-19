package com.example.week8_deviceinfo.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.week8_deviceinfo.data.SystemLogic
import com.example.week8_deviceinfo.ui.components.InfoItemRow
import com.example.week8_deviceinfo.ui.theme.Week8_DeviceInfoTheme

@Composable
fun DeviceInfoScreen() {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    
    // Mengambil data perangkat keras
    val ramDetails = remember { if (isPreview) Pair("8.00 GB", "4.00 GB") else SystemLogic.getRamDetails(context) }
    val storageDetails = remember { if (isPreview) Pair("128.00 GB", "64.00 GB") else SystemLogic.getStorageDetails() }
    val screenSize = remember { if (isPreview) "6.5 Inci" else SystemLogic.getScreenSizeInInches(context) }
    val cameraMp = remember { if (isPreview) "12.0 MP" else SystemLogic.getCameraMegapixels(context) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Hardware Information",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Detailed specs of your device hardware",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
        }

        item {
            InfoItemRow(
                label = "RAM (Total / Sisa)",
                description = "Kapasitas memori sistem",
                value = "${ramDetails.first} / ${ramDetails.second}",
                icon = Icons.Default.Memory,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            InfoItemRow(
                label = "Penyimpanan",
                description = "Kapasitas internal (Total / Bebas)",
                value = "${storageDetails.first} / ${storageDetails.second}",
                icon = Icons.Default.Storage,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            InfoItemRow(
                label = "Ukuran Layar",
                description = "Estimasi ukuran diagonal layar",
                value = screenSize,
                icon = Icons.Default.AspectRatio,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            InfoItemRow(
                label = "Kamera Belakang",
                description = "Resolusi sensor kamera utama",
                value = cameraMp,
                icon = Icons.Default.CameraAlt,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        item {
            InfoItemRow(
                label = "Model Perangkat",
                description = "Nama pabrikan dan model",
                value = "${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}",
                icon = Icons.Default.Smartphone,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeviceInfoScreenPreview() {
    Week8_DeviceInfoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DeviceInfoScreen()
        }
    }
}
