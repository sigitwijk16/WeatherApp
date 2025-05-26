package com.gitz.weatherapp.domain.usecase

import android.util.Log
import com.gitz.weatherapp.domain.model.Weather
import com.gitz.weatherapp.domain.repository.WeatherRepository
import com.gitz.weatherapp.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(cityName: String): Flow<Resource<Weather>> {
        Log.d("UseCase", "Searching in use case for $cityName")
        return repository.getWeatherForCity(cityName)
    }
}