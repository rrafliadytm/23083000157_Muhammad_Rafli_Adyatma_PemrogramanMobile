package com.example.week9_speedtestapps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week9_speedtestapps.data.SpeedTestManager
import com.example.week9_speedtestapps.model.SpeedTestUiState
import com.example.week9_speedtestapps.model.TestPhase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpeedTestViewModel : ViewModel() {
    private val speedTestManager = SpeedTestManager()

    private val _uiState = MutableStateFlow(SpeedTestUiState())
    val uiState: StateFlow<SpeedTestUiState> = _uiState.asStateFlow()

    fun startTest() {
        viewModelScope.launch {
            _uiState.update { SpeedTestUiState(phase = TestPhase.TESTING_PING) }
            
            // 1. Ping Test
            val pingResult = speedTestManager.testPing()
            _uiState.update { it.copy(ping = pingResult, phase = TestPhase.TESTING_DOWNLOAD) }

            // 2. Download Test
            speedTestManager.testDownload().collect { (progress, speed) ->
                _uiState.update { it.copy(
                    progress = progress * 0.5f, // Download is first half of progress
                    downloadSpeed = speed,
                    currentSpeed = speed
                ) }
            }

            _uiState.update { it.copy(phase = TestPhase.TESTING_UPLOAD) }

            // 3. Upload Test
            speedTestManager.testUpload().collect { (progress, speed) ->
                _uiState.update { it.copy(
                    progress = 0.5f + (progress * 0.5f), // Upload is second half
                    uploadSpeed = speed,
                    currentSpeed = speed
                ) }
            }

            _uiState.update { it.copy(
                phase = TestPhase.FINISHED,
                progress = 1f,
                currentSpeed = 0f
            ) }
        }
    }

    fun resetTest() {
        _uiState.update { SpeedTestUiState() }
    }
}
