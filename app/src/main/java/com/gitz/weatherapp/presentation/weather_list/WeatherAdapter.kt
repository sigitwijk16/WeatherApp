package com.gitz.weatherapp.presentation.weather_list

import com.gitz.weatherapp.databinding.ItemWeatherBinding
import com.gitz.weatherapp.domain.model.Weather
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherAdapter(
    private val onItemClick: (Weather) -> Unit
) : ListAdapter<Weather, WeatherAdapter.WeatherViewHolder>(WeatherDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = ItemWeatherBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = getItem(position)
        holder.bind(weather)
    }

    inner class WeatherViewHolder(
        private val binding: ItemWeatherBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(weather: Weather) {
            binding.apply {
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
    }

    class WeatherDiffCallback : DiffUtil.ItemCallback<Weather>() {
        override fun areItemsTheSame(oldItem: Weather, newItem: Weather): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Weather, newItem: Weather): Boolean {
            return oldItem == newItem
        }
    }
}