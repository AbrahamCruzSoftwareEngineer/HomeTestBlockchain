package com.example.hometestblockchain.data

import com.example.hometestblockchain.data.api.ApiService
import com.example.hometestblockchain.data.local.dao.ImageDao
import com.example.hometestblockchain.data.local.entity.ImageEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepository @Inject constructor(
    private val apiService: ApiService,
    private val imageDao: ImageDao
) {
    private suspend fun insertAll(images: List<ImageEntity>) {
        imageDao.insertAllImages(images)
    }

    suspend fun fetchImagesFromApi(query: String) {
        val response = apiService.getImageList(query)
        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                val images = responseBody.images.map { ImageEntity(url = it) }
                insertAll(images)
            }
        }
    }

    val allImages: Flow<List<ImageEntity>> = imageDao.getAllImages()
}