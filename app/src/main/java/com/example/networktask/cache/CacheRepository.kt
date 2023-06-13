package com.example.networktask.cache

import javax.inject.Inject

class CacheRepository @Inject constructor(
    private val imageDao: ImageDao
    ) {

    fun getAllImage() = imageDao.getAllImage()

    suspend fun upsertByReplacement(image: List<Image>){
        imageDao.upsertByReplacement(image)
    }
//    suspend fun findById(id: Int){
//        imageDao.findById(id)
//    }
    suspend fun delete(image: Image){
        imageDao.delete(image)
    }

}