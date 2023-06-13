package com.example.networktask.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.networktask.cache.CacheRepository
import com.example.networktask.cache.DatabaseModule
import com.example.networktask.cache.Image
import com.example.networktask.cache.WeatherDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CacheViewModel @Inject constructor(
    application: Context,
    private var cacheRepository: CacheRepository
    ) : ViewModel() {
     val imageLiveData = MutableLiveData<List<Image>>()

    init {
        val dao = WeatherDatabase.invoke(application).getImage()
        cacheRepository = CacheRepository(dao)
        getImages()
    }

//        private fun getImage()=cacheRepository.getAllImage()
     fun getImages() {
        viewModelScope.launch {
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

    fun upsertByReplacement(image: List<Image>) = viewModelScope.launch(Dispatchers.IO) {
        cacheRepository.upsertByReplacement(image)
    }

//    fun findByIds(id: Int) = viewModelScope.launch(Dispatchers.IO) {
//        cacheRepository.findById(id)
//    }
}