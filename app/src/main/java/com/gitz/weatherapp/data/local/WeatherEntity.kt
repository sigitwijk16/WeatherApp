package com.gitz.weatherapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gitz.weatherapp.domain.model.Weather

@Entity(tableName = "weather_table")
data class WeatherEntity(
    @PrimaryKey
    val id: String,
    val cityName: String,
    val temperature: Double,
    val description: String,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Int? = null,
    val feelsLike: Double? = null,
    val tempMin: Double? = null,
    val tempMax: Double? = null,
    val country: String? = null,
    val timestamp: Long,
    val isSynced: Boolean = true
)

fun WeatherEntity.toWeather(): Weather {
    return Weather(
        id = id,
        cityName = cityName,
        temperature = temperature,
        description = description,
        humidity = humidity,
        windSpeed = windSpeed,
        pressure = pressure,
        feelsLike = feelsLike,
        tempMin = tempMin,
        tempMax = tempMax,
        country = country,
        timestamp = timestamp
    )
}

fun Weather.toWeatherEntity(isSynced: Boolean = true): WeatherEntity {
    return WeatherEntity(
        id = id,
        cityName = cityName,
        temperature = temperature,
        description = description,
        humidity = humidity,
        windSpeed = windSpeed,
        pressure = pressure,
        feelsLike = feelsLike,
        tempMin = tempMin,
        tempMax = tempMax,
        country = country,
        timestamp = timestamp,
        isSynced = isSynced
    )
}