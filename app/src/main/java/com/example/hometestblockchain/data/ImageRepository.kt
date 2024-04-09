package com.example.hometestblockchain.data

import com.example.hometestblockchain.data.local.dao.ImageDao
import com.example.hometestblockchain.data.local.entity.ImageEntity
import kotlinx.coroutines.flow.Flow

class ImageRepository(private val imageDao: ImageDao) {
    val allImages: Flow<List<ImageEntity>> = imageDao.getAllImages()

    suspend fun insertAll(images: List<ImageEntity>) {
        imageDao.insertAllImages(images)
    }

}