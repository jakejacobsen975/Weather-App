package com.example.weatherapp.response

import com.example.weatherapp.response.MainData
import com.example.weatherapp.response.WeatherData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class WeatherItemResponse(
    @Json(name = "main") val main: MainData,
    @Json(name = "weather") val weather: List<WeatherData>,
    @Json(name = "dt_txt") val dtTxt: String,
    @Json(name = "pop") val rain: Double
)