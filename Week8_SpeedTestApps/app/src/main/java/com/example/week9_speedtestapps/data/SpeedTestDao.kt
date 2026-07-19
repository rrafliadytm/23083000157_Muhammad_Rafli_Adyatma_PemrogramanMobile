package com.example.week9_speedtestapps.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SpeedTestDao {
    @Insert
    suspend fun insertResult(result: SpeedTestEntity)

    @Query("SELECT * FROM speed_test_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<SpeedTestEntity>>

    @Query("DELETE FROM speed_test_history")
    suspend fun clearHistory()
}
