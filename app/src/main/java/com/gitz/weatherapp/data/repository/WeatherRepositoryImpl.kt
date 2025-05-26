package com.gitz.weatherapp.data.repository

import android.util.Log
import com.gitz.weatherapp.data.local.WeatherDao
import com.gitz.weatherapp.data.local.toWeather
import com.gitz.weatherapp.data.local.toWeatherEntity
import com.gitz.weatherapp.data.remote.WeatherApi
import com.gitz.weatherapp.data.remote.toWeather
import com.gitz.weatherapp.domain.model.Weather
import com.gitz.weatherapp.domain.repository.WeatherRepository
import com.gitz.weatherapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    private val dao: WeatherDao,
    private val apiKey: String
): WeatherRepository {
    override fun getWeatherForCity(cityName: String): Flow<Resource<Weather>> = flow {
        emit(Resource.Loading())

        try {
            val cachedWeather = dao.getWeatherByCity(cityName).first()
            cachedWeather?.let {
                emit(Resource.Loading(it.toWeather()))
            }
        } catch (e: Exception) {
            // No cached data, continue
        }

        try {
            Log.d("Remote Weather", "Searching for city weather")
            val remoteWeather = api.getWeatherForCity(cityName, apiKey)
            val weatherDomain = remoteWeather.toWeather()
            Log.d("Remote Weather", weatherDomain.toString())

            dao.insertWeather(weatherDomain.toWeatherEntity(true))
            emit(Resource.Success(weatherDomain))

        } catch (e: HttpException) {
            try {
                val cachedWeather = dao.getWeatherByCity(cityName).first()
                if (cachedWeather != null) {
                    emit(Resource.Error("Couldn't reach server. Using cached data.", cachedWeather.toWeather()))
                } else {
                    emit(Resource.Error("Couldn't reach server. No cached data available."))
                }
            } catch (dbException: Exception) {
                emit(Resource.Error("Couldn't reach server. No cached data available."))
            }
        } catch (e: IOException) {
            try {
                val cachedWeather = dao.getWeatherByCity(cityName).first()
                if (cachedWeather != null) {
                    emit(Resource.Error("Couldn't reach server. Check your internet connection. Using cached data.", cachedWeather.toWeather()))
                } else {
                    emit(Resource.Error("Couldn't reach server. Check your internet connection. No cached data available."))
                }
            } catch (dbException: Exception) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection. No cached data available."))
            }
        } catch (e: Exception) {
            emit(Resource.Error("An unexpected error occurred: ${e.message}"))
        }

    }

    override fun getSavedWeather(): Flow<List<Weather>> {
        return dao.getAllWeather().map { entities ->
            entities.map { it.toWeather() }
        }
    }

    override suspend fun syncWeatherData() {
        try {
            val entities = dao.getAllWeather().first()
            entities.forEach { entity ->
                if (!entity.isSynced) {
                    try {
                        val remoteWeather = api.getWeatherForCity(entity.cityName, apiKey)
                        val weatherDomain = remoteWeather.toWeather()

                        dao.insertWeather(weatherDomain.toWeatherEntity(true))
                    } catch (e: Exception) {
                        // Handle error
                    }
                }
            }
        } catch (e: Exception) {
            // Handle error
        }
    }

    override suspend fun refreshWeather(cityName: String) {
        try {
            val remoteWeather = api.getWeatherForCity(cityName, apiKey)
            val weatherDomain = remoteWeather.toWeather()

            dao.insertWeather(weatherDomain.toWeatherEntity(true))
        } catch (e: Exception) {
            Log.e("RefreshDataError","Failed to refresh: ${e.message}")
        }
    }

}