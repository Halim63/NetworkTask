package com.networktask.remote

import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

const val API_KEY = "b97049b17f7788359f444e2f8b0b356c"

class WeatherRepository @Inject constructor(
    private val apiServiceInterface: ApiServiceInterface,
) {
    suspend fun getCurrentWeather() = apiServiceInterface.getCurrentWeather(
        "London",
        "metric", API_KEY
    )
}