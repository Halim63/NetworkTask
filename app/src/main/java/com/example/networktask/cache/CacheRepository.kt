package com.example.networktask.cache

import javax.inject.Inject

class CacheRepository @Inject constructor(
    private val imageDao: ImageDao
    ) {

    fun getAllImage() = imageDao.getAllImage()

    fun insertImage(image: Image){
        imageDao.insertImage(image)
    }

    fun delete(image: Image){
        imageDao.delete(image)
    }

}