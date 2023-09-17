package com.networktask.ui.capturePhoto

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.networktask.base.Resource
import com.networktask.base.State
import com.networktask.repos.imagesRepo.ImageDbEntity
import com.networktask.repos.imagesRepo.ImagesCacheRepository
import com.networktask.repos.weatherRepo.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

const val TAG = "MainActivity"

@HiltViewModel
class CapturePhotoViewModel
@Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val imagesCacheRepository: ImagesCacheRepository,

    ) : ViewModel() {
    val temperatureLiveData = MutableLiveData<Resource<Double>>()
    val saveImageInDbLiveData = MutableLiveData<Boolean>()
    private val compositeDisposable = CompositeDisposable()


    fun getCurrentWeather() {

        compositeDisposable.add(
            weatherRepository.getCurrentWeather()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        if (response.isSuccessful && response.body() != null) {
                            val temperature = response.body()?.main?.temp
                            temperatureLiveData.postValue(Resource.success(temperature!!))

                        }
                    },
                    {
                        temperatureLiveData.postValue(Resource.error(message = State.ERROR.toString()))

                    })
        )

    }

    fun saveImageInDb(imageDbEntity: ImageDbEntity) {
        compositeDisposable.add(
            imagesCacheRepository.insertImage(imageDbEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        saveImageInDbLiveData.postValue(true)

                    },
                    { throwable ->
                        Log.d(TAG, "onError ${throwable.message}")
                        saveImageInDbLiveData.postValue(false)


                    })
        )


    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()

    }

}

