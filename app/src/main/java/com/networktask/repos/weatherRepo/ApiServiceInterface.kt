package com.networktask.repos.weatherRepo

import com.networktask.models.Image
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceInterface {
    @GET("weather?")
     fun getCurrentWeather(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String,
    ): Observable<Response<Image>>


}