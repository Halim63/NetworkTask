package com.networktask.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ImageDbEntity::class], version = 1)
abstract class ImageDatabase : RoomDatabase() {
    abstract fun getImage(): ImageDao

}