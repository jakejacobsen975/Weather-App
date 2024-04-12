package com.example.weatherapp.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherApiResponse (
    @Json(name = "list") val list: List<WeatherItemResponse>,
    @Json(name = "city") val city: CityData
)