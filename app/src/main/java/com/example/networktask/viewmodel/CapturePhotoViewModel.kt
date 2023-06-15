package com.example.networktask.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.networktask.cache.CacheRepository
import com.example.networktask.cache.Image
import com.example.networktask.remote.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CapturePhotoViewModel
    @Inject constructor(
        private val weatherRepository: WeatherRepository,
        private val cacheRepository: CacheRepository,

        ) : ViewModel() {
    val tempLiveData = MutableLiveData<Double>()
    val saveImageInDbLiveData=MutableLiveData<Boolean>()


    fun getCurrentWeather() {

        viewModelScope.launch(Dispatchers.IO) {
            val response = try {
                weatherRepository.getCurrentWeather()
            } catch (e: IOException) {
//                Toast.makeText(applicationContext, "app error ${e.message}", Toast.LENGTH_LONG)
//                    .show()
                return@launch
            } catch (e: HttpException) {

//                Toast.makeText(applicationContext, "app error ${e.message}", Toast.LENGTH_LONG)
//                    .show()
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    tempLiveData.postValue(response.body()!!.main.temp)
                }
            }
        }


    }
    fun saveImageInDb(image: Image){

        CoroutineScope(Dispatchers.IO).launch {
            try {
                cacheRepository.insertImage(image)
                saveImageInDbLiveData.postValue(true)
            }catch (e:Exception){
                e.printStackTrace()
                saveImageInDbLiveData.postValue(false)

            }
        }
    }


}

