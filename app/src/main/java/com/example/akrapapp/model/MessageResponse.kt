package com.example.akrapapp.model

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("message") val msg:String
)
