package com.example.networktask.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(entities = [Image::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun getImage(): ImageDao

}