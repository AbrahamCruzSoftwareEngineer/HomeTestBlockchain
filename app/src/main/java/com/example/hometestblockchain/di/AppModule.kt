package com.example.hometestblockchain.di

import android.content.Context
import com.example.hometestblockchain.data.api.ApiService
import com.example.hometestblockchain.data.local.AppDatabase
import com.example.hometestblockchain.data.local.dao.ImageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getDatabase(appContext)
    }

    @Provides
    fun provideImageDao(appDatabase: AppDatabase): ImageDao {
        return appDatabase.imageDao()
    }

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
