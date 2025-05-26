package com.gitz.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather_table")
    fun getAllWeather(): Flow<List<WeatherEntity>>

    @Query("SELECT * FROM weather_table WHERE cityName = :cityName")
    fun getWeatherByCity(cityName: String): Flow<WeatherEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("DELETE FROM weather_table WHERE cityName = :cityName")
    suspend fun deleteWeatherByCity(cityName: String)
}