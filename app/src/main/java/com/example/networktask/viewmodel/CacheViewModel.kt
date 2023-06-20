package com.example.networktask.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.networktask.cache.CacheRepository
import com.example.networktask.cache.ImageDbEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CacheViewModel @Inject constructor(
    private val cacheRepository: CacheRepository,
) : ViewModel() {
    val imageDbEntityLiveData = MutableLiveData<List<ImageDbEntity>>()

    fun getImages() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val images = cacheRepository.getAllImage()
                imageDbEntityLiveData.postValue(images)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteImage(imageDbEntity: ImageDbEntity) = viewModelScope.launch(Dispatchers.IO) {
        cacheRepository.delete(imageDbEntity)
    }

    fun insertImage(imageDbEntity: ImageDbEntity) = viewModelScope.launch(Dispatchers.IO) {
        cacheRepository.insertImage(imageDbEntity)
    }


}