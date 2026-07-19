package com.example.week6_weatherforecast.data.api

import com.example.week6_weatherforecast.data.model.GeocodingResponse
import com.example.week6_weatherforecast.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Interface Retrofit untuk mendefinisikan endpoint API Cuaca dan Geocoding.
 */
interface WeatherApiService {
    
    @GET("https://geocoding-api.open-meteo.com/v1/search")
    suspend fun searchCity(
        @Query("name") name: String,
        @Query("count") count: Int = 1,
        @Query("language") language: String = "id",
        @Query("format") format: String = "json"
    ): GeocodingResponse

    @GET("https://api.open-meteo.com/v1/forecast")
    suspend fun getForecast(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,precipitation_sum,precipitation_probability_max",
        @Query("timezone") timezone: String = "Asia/Bangkok",
        @Query("forecast_days") days: Int = 7
    ): WeatherResponse
}
