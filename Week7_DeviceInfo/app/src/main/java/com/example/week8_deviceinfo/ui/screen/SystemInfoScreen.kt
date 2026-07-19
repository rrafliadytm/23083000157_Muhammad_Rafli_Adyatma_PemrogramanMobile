package com.example.week8_deviceinfo.ui.screen

import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.week8_deviceinfo.ui.components.InfoItemRow

@Composable
fun SystemInfoScreen() {
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
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "System Information",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "Software and OS details",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
        }

        item {
            InfoItemRow(
                label = "Android Version",
                description = "Running OS version",
                value = Build.VERSION.RELEASE,
                icon = Icons.Default.Android,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            InfoItemRow(
                label = "SDK Level",
                description = "API Level of the device",
                value = Build.VERSION.SDK_INT.toString(),
                icon = Icons.Default.Code,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            InfoItemRow(
                label = "Security Patch",
                description = "Latest security update",
                value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Build.VERSION.SECURITY_PATCH
                } else {
                    "N/A"
                },
                icon = Icons.Default.Security,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            InfoItemRow(
                label = "Bootloader",
                description = "Device bootloader version",
                value = Build.BOOTLOADER,
                icon = Icons.Default.Terminal,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            InfoItemRow(
                label = "Kernel Version",
                description = "System kernel information",
                value = System.getProperty("os.version") ?: "N/A",
                icon = Icons.Default.DeveloperMode,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        item {
            InfoItemRow(
                label = "Build ID",
                description = "System build number",
                value = Build.DISPLAY,
                icon = Icons.Default.Fingerprint,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
