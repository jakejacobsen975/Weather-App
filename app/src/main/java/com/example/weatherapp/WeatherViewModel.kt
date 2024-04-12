package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.photogallery.PreferencesRepository
import com.example.weatherapp.api.WeatherItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


private const val TAG = "WeatherViewModel"
class WeatherViewModel: ViewModel() {
//    private val preferencesRepository = PreferencesRepository.get()
//    val uiState: StateFlow<WeatherUiState>
//        get() = _uiState.asStateFlow()
//
//    init{
//        viewModelScope.launch {
//            preferencesRepository.isPolling.collect { isPolling ->
//                _uiState.update { it.copy(isPolling = isPolling) }
//            }
//        }
//    }
    private val weatherRepository = WeatherRepository()

    private val _weatherItems: MutableStateFlow<List<WeatherItem>> =
        MutableStateFlow(emptyList())
    val weatherItems: StateFlow<List<WeatherItem>>
        get() = _weatherItems.asStateFlow()

    private val _cityName: MutableStateFlow<String?> = MutableStateFlow(null)
    val cityName: StateFlow<String?>
        get() = _cityName.asStateFlow()

    private val _isWeatherDataFetchedSuccessfully: MutableStateFlow<Boolean> =
        MutableStateFlow(true)
    val isWeatherDataFetchedSuccessfully: StateFlow<Boolean>
        get() = _isWeatherDataFetchedSuccessfully.asStateFlow()

    fun fetchWeather(input: String)  {
        viewModelScope.launch {
            try {
                val response = weatherRepository.fetchWeather(input)
                _weatherItems.value = response
                _cityName.value = response.firstOrNull()?.cityName
                _isWeatherDataFetchedSuccessfully.value = true
                Log.d(TAG, "City Name: ${_cityName.value}")
            } catch (ex: Exception) {
                Log.e(TAG, "Failed to fetch weather items", ex)
                _isWeatherDataFetchedSuccessfully.value = false
            }
        }
    }
//    data class WeatherUiState(
//        val isPolling: Boolean = false,
//    )
}