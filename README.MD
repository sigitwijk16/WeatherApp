# Weather App

A modern Android weather application built with Kotlin, MVVM architecture, and Clean Architecture principles.

This app follows Clean Architecture principles with:
- Presentation Layer: ViewModels, Fragments, Adapters
- Domain Layer: Use Cases, Repository Interfaces, Models
- Data Layer: Repository Implementation, Local Database, Remote API

## Technologies Used

- Kotlin
- MVVM Architecture
- Room Database
- Retrofit
- Hilt (Dependency Injection)
- Coroutines & Flow
- ViewBinding
- WorkManager

## Setup

1. Clone the repository
2. Get a free API key from [OpenWeatherMap](https://openweathermap.org/api)
3. Add your API key to `local.properties`: