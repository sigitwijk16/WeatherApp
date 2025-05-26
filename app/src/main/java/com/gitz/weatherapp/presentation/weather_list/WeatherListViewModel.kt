package com.gitz.weatherapp.presentation.weather_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gitz.weatherapp.domain.usecase.GetSavedWeatherUseCase
import com.gitz.weatherapp.domain.usecase.GetWeatherUseCase
import com.gitz.weatherapp.domain.usecase.RefreshWeatherUseCase
import com.gitz.weatherapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherListViewModel @Inject constructor(
    private val getSavedWeatherUseCase: GetSavedWeatherUseCase,
    private val getWeatherUseCase: GetWeatherUseCase,
    private val refreshWeatherUseCase: RefreshWeatherUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherListState())
    val state: StateFlow<WeatherListState> = _state.asStateFlow()

    init {
        getSavedWeather()
    }

    companion object {
        private const val TAG = "WeatherListViewModel"
    }

    fun onEvent(event: WeatherListEvent) {
        when (event) {
            is WeatherListEvent.SearchCity -> {
                Log.d(TAG, "Searching for city: ${event.cityName}")
                getWeatherForCity(event.cityName)
            }
            is WeatherListEvent.RefreshWeather -> {
                refreshWeather(event.cityName)
            }
        }
    }

    private fun getSavedWeather() {
        getSavedWeatherUseCase().onEach { weatherList ->
            _state.value = _state.value.copy(
                savedWeather = weatherList
            )
        }.launchIn(viewModelScope)
    }

    private fun getWeatherForCity(cityName: String) {
        getWeatherUseCase(cityName).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true,
                        error = null
                    )
                }
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        currentWeather = result.data,
                        error = null
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message,
                        currentWeather = result.data
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun refreshWeather(cityName: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isRefreshing = true)
            try {
                refreshWeatherUseCase(cityName)
            } finally {
                _state.value = _state.value.copy(isRefreshing = false)
            }
        }
    }
}