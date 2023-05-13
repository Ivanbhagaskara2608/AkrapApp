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
    ): Call<MessageResponse>

    @POST("logout")
    fun logout(
        @Header("Authorization") token: String
    ): Call<TokenResponse>

    @GET("user")
    fun userData(
        @Header("Authorization") token: String
    ): Call<GetUserDataResponse>

    @GET("schedule")
    fun scheduleRecent(
        @Header("Authorization") token: String
    ): Call<GetAllScheduleResponse>

    @GET("schedule/past")
    fun schedulePast(
        @Header("Authorization") token: String
    ): Call<GetAllScheduleResponse>

    @GET("schedule/today")
    fun scheduleToday(
        @Header("Authorization") token: String
    ): Call<GetAllScheduleResponse>

    @POST("presence")
    fun presence(
        @Header("Authorization") token: String, @Body attendanceCode: JsonObject
    ): Call<MessageResponse>

    @POST("user/updateUsername")
    fun updateUsername(
        @Header("Authorization") token: String, @Body username: JsonObject
    ): Call<GetUserDataResponse>

    @POST("user/changePassword")
    fun changePassword(
        @Header("Authorization") token: String, @Body password: JsonObject
    ): Call<TokenResponse>

    @GET("admin/getUsers")
    fun getUsers(
        @Header("Authorization") token: String
    ): Call<GetAllUsersResponse>

    @POST("admin/schedule/update")
    fun updateSchedule(
        @Header("Authorization") token: String, @Body schedule: JsonObject
    ): Call<GetScheduleDataResponse>

    @POST("admin/schedule/store")
    fun storeSchedule(
        @Header("Authorization") token: String, @Body schedule: JsonObject
    ): Call<MessageResponse>

    @POST("admin/schedule/add")
    fun addSchedule(
        @Header("Authorization") token: String, @Body schedule: JsonObject
    ): Call<GetScheduleDataResponse>
}