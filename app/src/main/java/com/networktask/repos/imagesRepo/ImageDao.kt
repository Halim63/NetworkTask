package com.networktask.repos.imagesRepo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

//@EntryPoint
//@InstallIn(SingletonComponent::class)
@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertImage(imageDbEntity: ImageDbEntity)

    @Query("SELECT * FROM imagedbentity")
    fun getAllImage(): List<ImageDbEntity>


    @Delete
    fun delete(imageDbEntity: ImageDbEntity)
}