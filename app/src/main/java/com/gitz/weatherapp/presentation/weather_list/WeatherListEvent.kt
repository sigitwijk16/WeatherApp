package com.gitz.weatherapp.presentation.weather_list

sealed class WeatherListEvent {
    data class SearchCity(val cityName: String) : WeatherListEvent()
    data class RefreshWeather(val cityName: String) : WeatherListEvent()
}