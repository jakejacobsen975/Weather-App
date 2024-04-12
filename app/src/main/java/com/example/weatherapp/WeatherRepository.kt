package com.example.weatherapp

import android.util.Log
import com.example.weatherapp.api.WeatherItem
import com.example.weatherapp.api.openWeatherApi
import com.example.weatherapp.response.WeatherApiResponse
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

private const val TAG = "WeatherRepository"

class WeatherRepository {
    private val openWeatherApi: openWeatherApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        openWeatherApi = retrofit.create()
    }
    suspend fun fetchWeather(input: String): List<WeatherItem> {
        val response : WeatherApiResponse
        if (input.matches(Regex("\\d{5}"))){
            response = openWeatherApi.fetchZipWeather(input)
        }else{
            response = openWeatherApi.fetchCityWeather(input)
        }
        val weatherItems = mutableListOf<WeatherItem>()
        val cityName = response.city.cityName

        for (weatherItemResponse in response.list) {
            weatherItems.add(
                WeatherItem(
                    date = weatherItemResponse.dtTxt,
                    maxTemp = weatherItemResponse.main.tempMax,
                    minTemp = weatherItemResponse.main.tempMin,
                    humidity = weatherItemResponse.main.humidity,
                    weather = weatherItemResponse.weather[0].description,
                    weatherIcon = weatherItemResponse.weather[0].icon,
                    cityName = cityName,
                    rainPercent = weatherItemResponse.rain
                )
            )
        }
        Log.d(TAG,"$weatherItems")
        return weatherItems
    }
}
