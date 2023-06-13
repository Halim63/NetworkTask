package com.example.networktask.cache

import androidx.appcompat.app.AppCompatActivity
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dagger.Component
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
@EntryPoint
@InstallIn(SingletonComponent::class)
@Dao
interface ImageDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertByReplacement(image: List<Image>)

    @Query("SELECT * FROM image")
    fun getAllImage(): List<Image>

//    @Query("SELECT * FROM image WHERE id")
//
//    fun findById(id:Int): Image

    @Delete
    fun delete(image: Image)
}