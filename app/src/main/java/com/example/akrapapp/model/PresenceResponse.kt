package com.example.akrapapp.model

import com.google.gson.annotations.SerializedName

data class PresenceResponse(
    @SerializedName("message") val msg:String
)
