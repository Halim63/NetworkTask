package com.example.networktask.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.networktask.cache.ImagesCacheRepository
import com.example.networktask.cache.ImageDbEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val imagesCacheRepository: ImagesCacheRepository,
) : ViewModel() {
    val imageDbEntityLiveData = MutableLiveData<List<ImageDbEntity>>()

    fun getImages() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val images = imagesCacheRepository.getAllImage()
                imageDbEntityLiveData.postValue(images)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteImage(imageDbEntity: ImageDbEntity) = viewModelScope.launch(Dispatchers.IO) {
        imagesCacheRepository.delete(imageDbEntity)
    }

}