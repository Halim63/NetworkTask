package com.example.networktask.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.networktask.cache.CacheRepository
import com.example.networktask.cache.Image
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CacheViewModel @Inject constructor(
    private val cacheRepository: CacheRepository
    ) : ViewModel() {
     val imageLiveData = MutableLiveData<List<Image>>()

     fun getImages() {
        viewModelScope.launch(Dispatchers.IO){
            try {
                val images = cacheRepository.getAllImage()
                imageLiveData.postValue(images)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteImage(image: Image) = viewModelScope.launch(Dispatchers.IO) {
        cacheRepository.delete(image)
    }

    fun insertImage(image: Image) = viewModelScope.launch(Dispatchers.IO) {
        cacheRepository.insertImage(image)
    }


}