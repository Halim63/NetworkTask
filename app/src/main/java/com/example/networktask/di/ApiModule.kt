package com.example.networktask.di

import com.example.networktask.remote.ApiServiceInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    @Provides
    @Singleton
    fun getInstance(): ApiServiceInterface {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createLoggingInterceptor())
            .build()
            .create(ApiServiceInterface::class.java)
    }

    private fun createLoggingInterceptor(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

}