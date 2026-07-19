package com.example.week6_weatherforecast.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week6_weatherforecast.data.repository.WeatherRepository
import com.example.week6_weatherforecast.ui.state.WeatherUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel untuk mengelola pencarian lokasi dan pengambilan data cuaca.
 */
class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        // Lokasi default: Malang
        fetchWeather("Malang")
    }

    /**
     * Mencari kota secara real-time untuk saran dropdown.
     */
    fun onSearchQueryChange(query: String) {
        if (query.length < 3) {
            _uiState.update { it.copy(searchResults = emptyList(), isSearching = false) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSearching = true) }
            try {
                val results = repository.searchCities(query)
                _uiState.update { it.copy(searchResults = results, isSearching = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSearching = false) }
            }
        }
    }

    /**
     * Mencari kota dan mengambil cuacanya.
     */
    fun fetchWeather(cityName: String, isRefreshing: Boolean = false) {
        if (cityName.isBlank()) return
        
        // Clear search results after selection
        _uiState.update { it.copy(searchResults = emptyList()) }

        viewModelScope.launch {
            if (isRefreshing) {
                _uiState.update { it.copy(isRefreshing = true) }
            } else {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            }

            try {
                // 1. Cari koordinat kota via Geocoding API
                val cityResult = repository.searchCity(cityName)
                
                if (cityResult != null) {
                    // 2. Jika kota ditemukan, ambil data cuaca
                    val forecast = repository.get7DayForecast(cityResult.latitude, cityResult.longitude)
                    
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            forecasts = forecast,
                            selectedCity = cityResult,
                            errorMessage = null
                        )
                    }
                } else {
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            isRefreshing = false,
                            errorMessage = "Kota '$cityName' tidak ditemukan." 
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = e.localizedMessage ?: "Terjadi kesalahan koneksi."
                    )
                }
            }
        }
    }

    /**
     * Fungsi untuk pull-to-refresh menggunakan lokasi yang sedang dipilih.
     */
    fun refresh() {
        val currentCity = _uiState.value.selectedCity?.name ?: "Malang"
        fetchWeather(currentCity, isRefreshing = true)
    }
}
