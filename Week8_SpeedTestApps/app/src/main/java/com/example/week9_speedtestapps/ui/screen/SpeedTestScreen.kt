package com.example.week9_speedtestapps.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.week9_speedtestapps.model.TestPhase
import com.example.week9_speedtestapps.ui.components.ResultCard
import com.example.week9_speedtestapps.ui.components.SpeedGauge
import com.example.week9_speedtestapps.viewmodel.SpeedTestViewModel

@Composable
fun SpeedTestScreen(
    modifier: Modifier = Modifier,
    viewModel: SpeedTestViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Internet Speed Test",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            SpeedGauge(
                speed = uiState.currentSpeed,
                modifier = Modifier.size(320.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            val statusText = when (uiState.phase) {
                TestPhase.IDLE -> "Siap untuk memulai"
                TestPhase.TESTING_PING -> "Menguji Ping..."
                TestPhase.TESTING_DOWNLOAD -> "Menguji Download..."
                TestPhase.TESTING_UPLOAD -> "Menguji Upload..."
                TestPhase.FINISHED -> "Pengujian selesai"
            }
            Text(
                text = statusText,
                color = Color(0xFF74777F),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResultCard(
                label = "PING",
                value = uiState.ping,
                unit = "ms",
                accentColor = Color(0xFFF39233),
                modifier = Modifier.weight(1f)
            )
            ResultCard(
                label = "DOWNLOAD",
                value = uiState.downloadSpeed,
                unit = "Mbps",
                accentColor = Color(0xFF00A8CC),
                modifier = Modifier.weight(1f)
            )
            ResultCard(
                label = "UPLOAD",
                value = uiState.uploadSpeed,
                unit = "Mbps",
                accentColor = Color(0xFF7B5EDC),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (uiState.phase == TestPhase.IDLE || uiState.phase == TestPhase.FINISHED) {
                    viewModel.startTest()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00A8CC)
            )
        ) {
            Text(
                text = if (uiState.phase == TestPhase.FINISHED) "Test Ulang" else "Mulai Tes",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}
