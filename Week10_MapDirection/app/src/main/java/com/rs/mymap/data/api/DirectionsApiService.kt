package com.rs.mymap.data.api

import com.rs.mymap.data.model.AutocompleteResponse
import com.rs.mymap.data.model.DirectionsResponse
import com.rs.mymap.data.model.PlaceDetailsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionsApiService {
    @GET("maps/api/directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String,
        @Query("alternatives") alternatives: Boolean = true,
        @Query("mode") mode: String = "driving"
    ): DirectionsResponse

    @GET("maps/api/place/autocomplete/json")
    suspend fun getAutocomplete(
        @Query("input") input: String,
        @Query("key") apiKey: String,
        @Query("types") types: String = "geocode"
    ): AutocompleteResponse

    @GET("maps/api/place/details/json")
    suspend fun getPlaceDetails(
        @Query("place_id") placeId: String,
        @Query("key") apiKey: String
    ): PlaceDetailsResponse
}
