package com.gitz.weatherapp.domain.usecase

import com.gitz.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class RefreshWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(cityName: String) {
        repository.refreshWeather(cityName)
    }
}