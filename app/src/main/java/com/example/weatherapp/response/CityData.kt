package com.example.weatherapp.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CityData(
    @Json(name = "name") val cityName: String,
    @Json(name = "sunrise") val sunrise: Int,
    @Json(name = "sunset") val sunset: Int,
)
//"name": "Bountiful",
//"coord": {
//    "lat": 40.8775,
//    "lon": -111.8727
//},
//"country": "US",
//"population": 0,
//"timezone": -21600,
//"sunrise": 1712235891,
//"sunset": 1712282120