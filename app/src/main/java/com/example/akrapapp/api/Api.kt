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

    @GET("admin/schedule/all")
    fun scheduleAll(
        @Header("Authorization") token: String
    ): Call<GetAllScheduleResponse>

    @POST("admin/schedule/delete")
    fun deleteSchedule(
        @Header("Authorization") token: String, @Body schedule: JsonObject
    ): Call<MessageDataResponse>

    @POST("user/setPrivacyCode")
    fun setPrivacyCode(
        @Header("Authorization") token: String, @Body privacyCode: JsonObject
    ): Call<MessageDataResponse>

    @POST("user/updatePrivacyCode")
    fun updatePrivacyCode(
        @Header("Authorization") token: String, @Body privacyCode: JsonObject
    ): Call<MessageDataResponse>

    @POST("user/deletePrivacyCode")
    fun deletePrivacyCode(
        @Header("Authorization") token: String, @Body privacyCode: JsonObject
    ): Call<MessageDataResponse>

    @GET("information")
    fun informationAll(
        @Header("Authorization") token: String
    ): Call<GetAllInformationResponse>

    @GET("information/latest")
    fun latestInformationAll(
        @Header("Authorization") token: String
    ): Call<GetAllInformationResponse>

    @POST("admin/information/add")
    fun addInformation(
        @Header("Authorization") token: String, @Body information: JsonObject
    ): Call<GetInformationData>

    @POST("admin/information/delete")
    fun deleteInformation(
        @Header("Authorization") token: String, @Body information: JsonObject
    ): Call<MessageResponse>

    @POST("admin/information/update")
    fun updateInformation(
        @Header("Authorization") token: String, @Body information: JsonObject
    ): Call<GetInformationData>
}