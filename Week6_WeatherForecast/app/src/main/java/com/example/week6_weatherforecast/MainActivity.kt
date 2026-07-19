package com.example.week6_weatherforecast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.week6_weatherforecast.data.api.RetrofitInstance
import com.example.week6_weatherforecast.data.repository.WeatherRepository
import com.example.week6_weatherforecast.ui.screen.WeatherScreen
import com.example.week6_weatherforecast.ui.theme.Week6_WeatherForecastTheme
import com.example.week6_weatherforecast.ui.viewmodel.WeatherViewModel

/**
 * Entry point aplikasi. Menangani inisialisasi ViewModel dan menampilkan UI utama.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inisialisasi dependensi secara manual (Sederhana untuk keperluan demo/tugas)
        val apiService = RetrofitInstance.api
        val repository = WeatherRepository(apiService)
        val viewModel = WeatherViewModel(repository)

        enableEdgeToEdge()
        setContent {
            Week6_WeatherForecastTheme {
                WeatherScreen(viewModel = viewModel)
            }
        }
    }
}
