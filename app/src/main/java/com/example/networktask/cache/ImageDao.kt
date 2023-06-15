package com.example.networktask.cache

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

//@EntryPoint
//@InstallIn(SingletonComponent::class)
@Dao
interface ImageDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertImage(image: Image)

    @Query("SELECT * FROM image")
    fun getAllImage(): List<Image>


    @Delete
    fun delete(image: Image)
}