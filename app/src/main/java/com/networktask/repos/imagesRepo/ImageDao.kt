package com.networktask.repos.imagesRepo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

//@EntryPoint
//@InstallIn(SingletonComponent::class)
@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertImage(imageDbEntity: ImageDbEntity):Completable

    @Query("SELECT * FROM imagedbentity")
    fun getAllImage(): Observable<List<ImageDbEntity>>


    @Delete
    fun delete(imageDbEntity: ImageDbEntity):Completable
}