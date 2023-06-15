package com.example.networktask.modules

import android.app.Application
import androidx.room.Room
import com.example.networktask.cache.ImageDao
import com.example.networktask.cache.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDB(context: Application): WeatherDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            WeatherDatabase::class.java,
            "image_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideImageDao(weatherDatabase: WeatherDatabase): ImageDao {
        return weatherDatabase.getImage()
    }
}







