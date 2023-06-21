package com.example.networktask.di

import android.app.Application
import androidx.room.Room
import com.example.networktask.cache.ImageDao
import com.example.networktask.cache.ImageDatabase
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
    fun provideDB(context: Application): ImageDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ImageDatabase::class.java,
            "image_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideImageDao(imageDatabase: ImageDatabase): ImageDao {
        return imageDatabase.getImage()
    }
}







