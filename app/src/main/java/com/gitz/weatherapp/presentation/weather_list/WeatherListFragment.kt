package com.gitz.weatherapp.presentation.weather_list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gitz.weatherapp.R
import com.gitz.weatherapp.databinding.FragmentWeatherListBinding
import com.gitz.weatherapp.domain.model.Weather
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class WeatherListFragment : Fragment() {
    private var _binding: FragmentWeatherListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WeatherListViewModel by viewModels()
    private lateinit var weatherAdapter: WeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        weatherAdapter = WeatherAdapter { weather ->
            // Handle item click if needed
        }
        binding.rvWeather.apply {
            adapter = weatherAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupListeners() {
        binding.btnSearch.setOnClickListener {
            searchCity()
        }

        binding.etCityName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchCity()
                true
            } else {
                false
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            val cityName = binding.etCityName.text.toString().trim()
            if (cityName.isNotEmpty()) {
                viewModel.onEvent(WeatherListEvent.RefreshWeather(cityName))
            } else {
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun searchCity() {
        val cityName = binding.etCityName.text.toString().trim()
        if (cityName.isNotEmpty()) {
            Log.d("SearchCity", "City Name: $cityName")
            viewModel.onEvent(WeatherListEvent.SearchCity(cityName))
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                binding.progressBar.isVisible = state.isLoading
                binding.swipeRefresh.isRefreshing = state.isRefreshing

                state.error?.let { error ->
                    binding.tvError.text = error
                    binding.tvError.isVisible = true
                } ?: run {
                    binding.tvError.isVisible = false
                }

                state.currentWeather?.let { weather ->
                    binding.cardCurrentWeather.isVisible = true
                    updateCurrentWeatherCard(weather)
                } ?: run {
                    binding.cardCurrentWeather.isVisible = false
                }

                weatherAdapter.submitList(state.savedWeather)
            }
        }
    }

    private fun updateCurrentWeatherCard(weather: Weather) {
        with(binding.currentWeatherView) {
            tvCityName.text = weather.cityName
            tvTemperature.text = "${weather.temperature.toInt()}°C"
            tvDescription.text = weather.description.capitalize(Locale.getDefault())
            tvHumidity.text = "Humidity: ${weather.humidity}%"
            tvWindSpeed.text = "Wind: ${weather.windSpeed} m/s"

            val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
            val formattedDate = sdf.format(Date(weather.timestamp))
            tvLastUpdated.text = "Last updated: $formattedDate"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}