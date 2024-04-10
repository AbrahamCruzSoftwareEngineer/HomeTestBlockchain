package com.example.hometestblockchain.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.hometestblockchain.data.ImageRepository
import com.example.hometestblockchain.data.local.entity.ImageEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    application: Application,
    private val repository: ImageRepository
) : AndroidViewModel(application) {
    private val _images = MutableStateFlow<List<ImageEntity>>(emptyList())
    val images: LiveData<List<ImageEntity>> = _images.asLiveData()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: LiveData<Boolean> = _isLoading.asLiveData()

    init {
        loadImagesFromDb()
    }

    private fun loadImagesFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.allImages.collect { imageList ->
                _images.value = imageList
                if (imageList.isEmpty()) {
                    _isLoading.value = true
                }
            }
        }
    }

    fun searchByNameLimited(query: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.fetchImagesFromApiLimited("$query/images")
                _isLoading.value = false
                Log.d(TAG, "searchByName: ${repository.allImages}")
            } catch (e: Exception) {
                Log.e(TAG, "searchByName: ${e.message}")
                _isLoading.value = false
            }
        }
    }

    fun searchByName(query: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.fetchImagesFromApi("$query/images")
                _isLoading.value = false
                Log.d(TAG, "searchByName: ${repository.allImages}")
            } catch (e: Exception) {
                Log.e(TAG, "searchByName: ${e.message}")
                _isLoading.value = false
            }
        }
    }

    companion object {
        const val TAG = "MyViewModel"
        const val DEFAULT_VALUE = "husky"
    }
}