package com.gitz.weatherapp.domain.usecase

import com.gitz.weatherapp.domain.model.Weather
import com.gitz.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(): Flow<List<Weather>> {
        return repository.getSavedWeather()
    }
}