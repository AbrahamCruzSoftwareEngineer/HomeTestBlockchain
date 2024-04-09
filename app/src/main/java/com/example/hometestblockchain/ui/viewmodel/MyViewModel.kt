package com.example.hometestblockchain.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.hometestblockchain.data.ImageRepository
import com.example.hometestblockchain.data.api.ApiService
import com.example.hometestblockchain.data.local.AppDatabase
import com.example.hometestblockchain.data.local.entity.ImageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ImageRepository
    private val _images = MutableStateFlow<List<ImageEntity>>(emptyList())
    val images: LiveData<List<ImageEntity>> = _images.asLiveData()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: LiveData<Boolean> = _isLoading.asLiveData()

    init {
        val imageDao = AppDatabase.getDatabase(application).imageDao()
        repository = ImageRepository(imageDao)
        loadImagesFromDb()
    }

    private fun loadImagesFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.allImages.collect { imageList ->
                _images.value = imageList
                if (imageList.isEmpty()) {
                    // Indicate that we are loading images
                    _isLoading.value = true
                    searchByNameLimited(DEFAULT_VALUE)
                }
            }
        }
    }

    fun searchByNameLimited(query: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = getRetrofit().create(ApiService::class.java).getImageList("$query/images")
                if (response.isSuccessful) {
                    val limitedImages = response.body()?.images?.take(5) ?: emptyList()
                    val imageEntities = limitedImages.map { imageUrl -> ImageEntity(url = imageUrl) }
                    repository.insertAll(imageEntities)
                    _isLoading.value = false
                } else {
                    Log.e(TAG, "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "searchByName: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }


    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(DEFAULT_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    companion object {
        const val TAG = "MyViewModel"
        const val DEFAULT_VALUE = "husky"
        const val DEFAULT_BASE_URL = "https://dog.ceo/api/breed/"
    }
}
