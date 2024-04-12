package com.example.weatherapp.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class WeatherItem(
    @Json(name = "dt_txt") val date: String,
    @Json(name = "temp_max") val maxTemp: Double,
    @Json(name = "temp_min") val minTemp: Double,
    val humidity: Int,
    @Json(name = "description") val weather: String,
    @Json(name = "url_s") val weatherIcon: String,
    @Json(name = "name") val cityName: String,
    @Json(name = "pop") val rainPercent: Double,

)
