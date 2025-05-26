package com.gitz.weatherapp.presentation.weather_list

import com.gitz.weatherapp.domain.model.Weather

data class WeatherListState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val currentWeather: Weather? = null,
    val savedWeather: List<Weather> = emptyList(),
    val error: String? = null
)