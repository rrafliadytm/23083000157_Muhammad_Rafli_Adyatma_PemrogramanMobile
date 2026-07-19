package com.example.week6_weatherforecast.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton object untuk menyediakan instance Retrofit.
 */
object RetrofitInstance {
    // Base URL bersifat fleksibel karena kita menggunakan URL lengkap di interface untuk geocoding
    private const val BASE_URL = "https://api.open-meteo.com/"

    val api: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }
}
