package com.example.week6_weatherforecast.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class untuk memetakan response dari Geocoding API.
 */
data class GeocodingResponse(
    @SerializedName("results")
    val results: List<GeocodingResult>?
)

data class GeocodingResult(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String?,
    @SerializedName("admin1")
    val admin1: String? // Biasanya Provinsi
)
