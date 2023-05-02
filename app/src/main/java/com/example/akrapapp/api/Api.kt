package com.example.akrapapp.api

import com.example.akrapapp.model.RegisterResponse
import com.example.akrapapp.model.ScheduleResponse
import com.example.akrapapp.model.TokenResponse
import com.example.akrapapp.model.UserResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface Api {
    @POST("login")
    fun login(
        @Body loginRequest: JsonObject
    ): Call<TokenResponse>

    @POST("register")
    fun register(
        @Body registerRequest: JsonObject
    ): Call<RegisterResponse>

    @POST("logout")
    fun logout(
        @Header("Authorization") token: String
    ): Call<TokenResponse>

    @GET("user")
    fun userData(
        @Header("Authorization") token: String
    ): Call<UserResponse>

    @GET("schedule")
    fun scheduleRecent(
        @Header("Authorization") token: String
    ): Call<ScheduleResponse>

    @GET("schedule/past")
    fun schedulePast(
        @Header("Authorization") token: String
    ): Call<ScheduleResponse>
}