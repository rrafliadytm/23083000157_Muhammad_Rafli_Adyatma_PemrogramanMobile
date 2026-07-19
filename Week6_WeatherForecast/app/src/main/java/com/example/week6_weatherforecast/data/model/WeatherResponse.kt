package com.example.week6_weatherforecast.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class untuk memetakan response JSON dari Open-Meteo API.
 */
data class WeatherResponse(
    @SerializedName("daily")
    val daily: DailyData
)

data class DailyData(
    @SerializedName("time")
    val time: List<String>,
    @SerializedName("temperature_2m_max")
    val temperatureMax: List<Double>,
    @SerializedName("temperature_2m_min")
    val temperatureMin: List<Double>,
    @SerializedName("precipitation_sum")
    val precipitationSum: List<Double>,
    @SerializedName("precipitation_probability_max")
    val precipitationProbabilityMax: List<Int>
)

/**
 * Model data yang lebih ramah untuk digunakan di UI.
 */
data class DayForecast(
    val date: String,
    val tempMax: Double,
    val tempMin: Double,
    val precipitationSum: Double,
    val precipitationProbability: Int
)
