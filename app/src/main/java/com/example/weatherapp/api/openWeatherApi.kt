package com.example.weatherapp.api

import com.example.weatherapp.response.WeatherApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "yourapikeyhere"

interface openWeatherApi {
    @GET("data/2.5/forecast")
    suspend fun fetchZipWeather(
        @Query("zip") zipCode: String,
        @Query("appid") apiKey: String = API_KEY,
        @Query("units") units: String = "imperial"
    ): WeatherApiResponse
    @GET("data/2.5/forecast")
    suspend fun fetchCityWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String = API_KEY,
        @Query("units") units: String = "imperial"
    ): WeatherApiResponse
}

//api.openweathermap.org/data/2.5/forecast?q=saint%20George,us&appid=6979fccbc1290dd0c4d8931f89740a4f
