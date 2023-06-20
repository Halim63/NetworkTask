package com.example.networktask.remote

import com.example.networktask.models.Image
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceInterface {
    @GET("weather?")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String,
    ): Response<Image>


}