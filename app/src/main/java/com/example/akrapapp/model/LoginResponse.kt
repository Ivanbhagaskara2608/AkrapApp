package com.example.akrapapp.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("message") val msg:String,
    val token:String,
)
