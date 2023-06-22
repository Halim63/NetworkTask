package com.networktask.repos.imagesRepo

import javax.inject.Inject

class ImagesCacheRepository @Inject constructor(
    private val imageDao: ImageDao,
) {

    fun getAllImage() = imageDao.getAllImage()

    fun insertImage(imageDbEntity: ImageDbEntity) {
        imageDao.insertImage(imageDbEntity)
    }

    fun delete(imageDbEntity: ImageDbEntity) {
        imageDao.delete(imageDbEntity)
    }

}