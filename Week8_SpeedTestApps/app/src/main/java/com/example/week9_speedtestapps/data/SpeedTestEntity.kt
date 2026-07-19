package com.example.week9_speedtestapps.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "speed_test_history")
data class SpeedTestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long,
    val downloadSpeed: Float,
    val uploadSpeed: Float,
    val ping: Float
)
