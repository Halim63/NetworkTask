package com.networktask.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.networktask.cache.ImageDbEntity
import com.networktask.cache.ImagesCacheRepository
import com.networktask.remote.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CapturePhotoViewModel
@Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val imagesCacheRepository: ImagesCacheRepository,

    ) : ViewModel() {
    val tempLiveData = MutableLiveData<Double>()
    val saveImageInDbLiveData = MutableLiveData<Boolean>()


    fun getCurrentWeather() {

        viewModelScope.launch(Dispatchers.IO) {
            val response = try {
                weatherRepository.getCurrentWeather()
            } catch (e: IOException) {
                return@launch
            } catch (e: HttpException) {
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                val temperature=response.body()?.main?.temp
                withContext(Dispatchers.Main) {
                    tempLiveData.postValue(temperature!!)
                }
            }
        }


    }

    fun saveImageInDb(imageDbEntity: ImageDbEntity) {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                imagesCacheRepository.insertImage(imageDbEntity)
                saveImageInDbLiveData.postValue(true)
            } catch (e: Exception) {
                e.printStackTrace()
                saveImageInDbLiveData.postValue(false)

            }
        }
    }


}

