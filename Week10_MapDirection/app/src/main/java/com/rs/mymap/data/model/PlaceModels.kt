package com.rs.mymap.data.model

import com.google.gson.annotations.SerializedName

data class AutocompleteResponse(
    @SerializedName("predictions")
    val predictions: List<Prediction>,
    @SerializedName("status")
    val status: String
)

data class Prediction(
    @SerializedName("description")
    val description: String,
    @SerializedName("place_id")
    val placeId: String
)

data class PlaceDetailsResponse(
    @SerializedName("result")
    val result: PlaceDetailsResult,
    @SerializedName("status")
    val status: String
)

data class PlaceDetailsResult(
    @SerializedName("geometry")
    val geometry: Geometry
)

data class Geometry(
    @SerializedName("location")
    val location: Location
)

data class Location(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)
