package com.example.week6_weatherforecast.data.repository

import com.example.week6_weatherforecast.data.api.WeatherApiService
import com.example.week6_weatherforecast.data.model.DayForecast
import com.example.week6_weatherforecast.data.model.GeocodingResult

/**
 * Repository untuk menangani pencarian kota dan pengambilan data cuaca.
 */
class WeatherRepository(private val apiService: WeatherApiService) {

    /**
     * Mencari koordinat kota berdasarkan nama.
     */
    suspend fun searchCity(name: String): GeocodingResult? {
        val response = apiService.searchCity(name, count = 1)
        return response.results?.firstOrNull()
    }

    /**
     * Mencari daftar kota berdasarkan nama untuk saran pencarian.
     */
    suspend fun searchCities(name: String): List<GeocodingResult> {
        val response = apiService.searchCity(name, count = 10)
        return response.results ?: emptyList()
    }

    /**
     * Mengambil prakiraan cuaca 7 hari berdasarkan koordinat.
     */
    suspend fun get7DayForecast(lat: Double, lon: Double): List<DayForecast> {
        val response = apiService.getForecast(lat, lon, days = 7)
        val daily = response.daily

        return daily.time.indices.map { index ->
            DayForecast(
                date = daily.time[index],
                tempMax = daily.temperatureMax[index],
                tempMin = daily.temperatureMin[index],
                precipitationSum = daily.precipitationSum[index],
                precipitationProbability = daily.precipitationProbabilityMax[index]
            )
        }
    }
}
