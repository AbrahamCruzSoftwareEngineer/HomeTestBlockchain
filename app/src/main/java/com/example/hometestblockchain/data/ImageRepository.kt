package com.example.hometestblockchain.data

import android.util.Log
import com.example.hometestblockchain.data.api.ApiService
import com.example.hometestblockchain.data.local.dao.ImageDao
import com.example.hometestblockchain.data.local.entity.ImageEntity
import com.example.hometestblockchain.ui.viewmodel.MyViewModel.Companion.TAG
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepository @Inject constructor(
    private val apiService: ApiService,
    private val imageDao: ImageDao
) {
    /**
     * Fetches and inserts all the images into the database.
     *
     * This method makes an API call to retrieve images related to the given query.
     * from the response are processed and stored.
     * If the API call is unsuccessful or the response is invalid, an error is logged.
     *
     * @param query The search query to fetch images for. This is used to construct the specific endpoint for the API call.
     * @throws Exception if there's an error during the API call or data processing. The exceptions are caught and logged within the method.
     */
    suspend fun fetchImagesFromApi(query: String) {
        val response = apiService.getImageList(query)
        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                val images = responseBody.images.map { ImageEntity(url = it) }
                insertAll(images)
            }
        }
    }

    /**
     * Fetches and inserts the first 5 images into the database.
     *
     * This method makes an API call to retrieve images related to the given query. Only the first 5 images
     * from the response are processed and stored. If the API call is unsuccessful or the response is invalid,
     * an error is logged.
     *
     * @param query The search query to fetch images for. This is used to construct the specific endpoint for the API call.
     * @throws Exception if there's an error during the API call or data processing. The exceptions are caught and logged within the method.
     */
    suspend fun fetchImagesFromApiLimited(query: String) {
        val response = apiService.getImageList(query)
        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                val images = responseBody.images.take(5).map { ImageEntity(url = it) }
                insertAll(images)
            }
        } else {
            Log.e(TAG, "Error fetching images: ${response.errorBody()?.string()}")
        }
    }

    private suspend fun insertAll(images: List<ImageEntity>) {
        imageDao.insertAllImages(images)
    }

    val allImages: Flow<List<ImageEntity>> = imageDao.getAllImages()
}