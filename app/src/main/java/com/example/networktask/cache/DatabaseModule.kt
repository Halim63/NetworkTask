package com.example.networktask.cache

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    //    @Provides
//    @Singleton
//    fun provide(@ApplicationContext context: Context) = Room.databaseBuilder(
//        context, WeatherDatabase::class.java, "image_database")
//        .allowMainThreadQueries()
//        .fallbackToDestructiveMigration()
//        .build()
    @Provides
    @Singleton
    fun provideDB(context: Application): WeatherDatabase {
        return WeatherDatabase.invoke(context)
    }

    @Provides
    @Singleton
    fun provideDao(weatherDatabase: WeatherDatabase): ImageDao {
        return weatherDatabase.getImage()
    }
}







