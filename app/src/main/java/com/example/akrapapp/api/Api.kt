package com.example.akrapapp.api

import com.example.akrapapp.model.*
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

    @GET("schedule/today")
    fun scheduleToday(
        @Header("Authorization") token: String
    ): Call<ScheduleResponse>

    @POST("presence")
    fun presence(
        @Header("Authorization") token: String, @Body attendanceCode: JsonObject
    ): Call<PresenceResponse>

    @POST("user/updateUsername")
    fun updateUsername(
        @Header("Authorization") token: String, @Body username: JsonObject
    ): Call<UserResponse>

    @POST("user/changePassword")
    fun changePassword(
        @Header("Authorization") token: String, @Body password: JsonObject
    ): Call<TokenResponse>
}