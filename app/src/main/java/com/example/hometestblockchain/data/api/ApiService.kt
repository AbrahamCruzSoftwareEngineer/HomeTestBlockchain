package com.example.hometestblockchain.data.api


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun getImageList(@Url url: String): Response<ApiResponse>
}