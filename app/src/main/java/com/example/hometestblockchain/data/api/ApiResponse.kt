package com.example.hometestblockchain.data.api

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("status")
    var status: String,
    @SerializedName("message")
    var images: List<String>
)