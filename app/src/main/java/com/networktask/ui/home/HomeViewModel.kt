package com.networktask.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.networktask.repos.imagesRepo.ImageDbEntity
import com.networktask.repos.imagesRepo.ImagesCacheRepository
import com.networktask.ui.capturePhoto.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val imagesCacheRepository: ImagesCacheRepository,
) : ViewModel() {
    val imageDbEntityLiveData = MutableLiveData<List<ImageDbEntity>>()
    private val compositeDisposable = CompositeDisposable()

    fun getImages() {
        compositeDisposable.add(
            imagesCacheRepository.getAllImages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { images ->
                        imageDbEntityLiveData.postValue(images)
                    },
                    { throwable ->
                        Log.d(TAG, "onError ${throwable.message}")
                    })
        )

    }

    fun deleteImage(imageDbEntity: ImageDbEntity) =
        compositeDisposable.add(
            imagesCacheRepository.delete(imageDbEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}