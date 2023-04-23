package com.example.akrapapp.api

import com.example.akrapapp.model.LoginResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface Api {
    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(
        @Body requestBody: JSONObject
    ): Call<LoginResponse>
}