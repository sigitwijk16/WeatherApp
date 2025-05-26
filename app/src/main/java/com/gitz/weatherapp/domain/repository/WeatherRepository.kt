package com.gitz.weatherapp.domain.repository

import com.gitz.weatherapp.domain.model.Weather
import com.gitz.weatherapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeatherForCity(cityName: String): Flow<Resource<Weather>>
    fun getSavedWeather(): Flow<List<Weather>>
    suspend fun syncWeatherData()
    suspend fun refreshWeather(cityName: String)
}