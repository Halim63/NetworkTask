package com.networktask.repos.weatherRepo

import com.networktask.models.Image
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import javax.inject.Inject

const val API_KEY = "b97049b17f7788359f444e2f8b0b356c"

class WeatherRepository @Inject constructor(
    private val apiServiceInterface: ApiServiceInterface,
) {
     fun getCurrentWeather(): Observable<Response<Image>> =
    apiServiceInterface.getCurrentWeather("London","metric", API_KEY)
}