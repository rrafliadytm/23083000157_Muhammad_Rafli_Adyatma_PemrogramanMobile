package com.example.week6_weatherforecast.ui.state

import com.example.week6_weatherforecast.data.model.DayForecast
import com.example.week6_weatherforecast.data.model.GeocodingResult

/**
 * State UI untuk mengelola data cuaca, informasi kota, dan status loading/error.
 */
data class WeatherUiState(
    val isLoading: Boolean = false,
    val forecasts: List<DayForecast> = emptyList(),
    val selectedCity: GeocodingResult? = null,
    val errorMessage: String? = null,
    val isRefreshing: Boolean = false,
    val searchResults: List<GeocodingResult> = emptyList(),
    val isSearching: Boolean = false
)
