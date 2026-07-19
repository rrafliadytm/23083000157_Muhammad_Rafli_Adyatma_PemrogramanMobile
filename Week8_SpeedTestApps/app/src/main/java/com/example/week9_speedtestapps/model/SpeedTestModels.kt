package com.example.week9_speedtestapps.model

sealed class TestPhase {
    object IDLE : TestPhase()
    object TESTING_PING : TestPhase()
    object TESTING_DOWNLOAD : TestPhase()
    object TESTING_UPLOAD : TestPhase()
    object FINISHED : TestPhase()
}

data class SpeedTestUiState(
    val ping: Float = 0f,
    val downloadSpeed: Float = 0f,
    val uploadSpeed: Float = 0f,
    val progress: Float = 0f,
    val phase: TestPhase = TestPhase.IDLE,
    val currentSpeed: Float = 0f
)
