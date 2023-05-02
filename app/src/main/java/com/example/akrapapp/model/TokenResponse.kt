package com.example.akrapapp.model

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("message") val msg:String,
    val token:String,
)
