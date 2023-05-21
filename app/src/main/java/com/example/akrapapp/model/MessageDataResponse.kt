package com.example.akrapapp.model

import com.google.gson.annotations.SerializedName

data class MessageDataResponse(
    @SerializedName("message") val msg:String,
    val data: String
)
