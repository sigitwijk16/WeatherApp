package com.gitz.weatherapp.data.remote

import com.gitz.weatherapp.domain.model.Weather
import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val coord: Coord,
    val weather: List<WeatherData>,
    val base: String,
    val main: MainData,
    val visibility: Int,
    val wind: WindData,
    val clouds: CloudsData,
    val dt: Long,
    val sys: SysData,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)

data class Coord(
    val lon: Double,
    val lat: Double
)

data class WeatherData(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class MainData(
    val temp: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int,
    @SerializedName("sea_level")
    val seaLevel: Int? = null,
    @SerializedName("grnd_level")
    val grndLevel: Int? = null
)

data class WindData(
    val speed: Double,
    val deg: Int? = null,
    val gust: Double? = null
)

data class CloudsData(
    val all: Int
)

data class SysData(
    val type: Int? = null,
    val id: Int? = null,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)


fun WeatherResponse.toWeather(): Weather {
    return Weather(
        id = id.toString(),
        cityName = name,
        temperature = main.temp,
        description = weather.firstOrNull()?.description ?: "No description",
        humidity = main.humidity,
        windSpeed = wind.speed,
        pressure = main.pressure,
        feelsLike = main.feelsLike,
        tempMin = main.tempMin,
        tempMax = main.tempMax,
        country = sys.country,
        timestamp = System.currentTimeMillis()
    )
}