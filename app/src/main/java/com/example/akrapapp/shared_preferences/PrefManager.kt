package com.example.akrapapp.shared_preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.akrapapp.model.DataItemSchedule
import com.example.akrapapp.model.UserData

class PrefManager(var context: Context) {
    private val IS_LOGIN = "is_login"

    var pref: SharedPreferences = context.getSharedPreferences("TOKEN_PREFS", Context.MODE_PRIVATE)
    var editor: SharedPreferences.Editor = pref.edit()

    fun setLogin(isLogin: Boolean) {
        editor.putBoolean(IS_LOGIN, isLogin)
        editor.commit()
    }

    fun setToken(token: String) {
        editor.putString("token", token)
        editor.commit()
    }

    fun setUserData(userId: Int, fullName: String, phoneNumber: String, birthdate: String, gender: String, username: String, role: String, status: String, privacyCode: String) {
        editor.putInt("userId", userId)
        editor.putString("fullName", fullName)
        editor.putString("phoneNumber", phoneNumber)
        editor.putString("birthdate", birthdate)
        editor.putString("gender", gender)
        editor.putString("username", username)
        editor.putString("role", role)
        editor.putString("status", status)
        editor.putString("privacyCode", privacyCode)
        editor.commit()
    }

    fun setScheduleData(date: String, activityName: String, startTime: String, attendanceCode: String, endTime: String, location: String, scheduleId: Int, scheduleStatus: String) {
        editor.putString("date", date)
        editor.putString("activityName", activityName)
        editor.putString("startTime", startTime)
        editor.putString("attendanceCode", attendanceCode)
        editor.putString("endTime", endTime)
        editor.putString("location", location)
        editor.putInt("scheduleId", scheduleId)
        editor.putString("scheduleStatus", scheduleStatus)
        editor.commit()
    }

    fun setAddScheduleData(activityName: String, date: String, location: String, startTime: String, endTime: String) {
        editor.putString("activityName", activityName)
        editor.putString("date", date)
        editor.putString("location", location)
        editor.putString("startTime", startTime)
        editor.putString("endTime", endTime)
        editor.commit()
    }

    fun setPrivacyCode(privacyCode: String) {
        editor.putString("privacyCode", privacyCode)
        editor.commit()
    }

    fun setBiometric(biometric: Boolean) {
        editor.putBoolean("biometric", biometric)
        editor.commit()
    }

    fun getToken(): String? {
        return pref.getString("token", "")
    }

    fun getBiometric(): Boolean {
        return pref.getBoolean("biometric", false)
    }

    fun getUserData(): UserData {
//        to call sessionManager.getUserData().fullName
        val userId = pref.getInt("userId", -1)
        val fullName = pref.getString("fullName", "").toString()
        val phoneNumber = pref.getString("phoneNumber", "").toString()
        val birthdate = pref.getString("birthdate", "").toString()
        val gender = pref.getString("gender", "").toString()
        val username = pref.getString("username", "").toString()
        val role = pref.getString("role", "").toString()
        val status = pref.getString("status", "").toString()
        val privacyCode = pref.getString("privacyCode", "").toString()

        return UserData(userId, fullName, phoneNumber, birthdate, gender, username, role, status, privacyCode)
    }

    fun getScheduleData(): DataItemSchedule {
        val date = pref.getString("date", "").toString()
        val activityName = pref.getString("activityName", "").toString()
        val startTime = pref.getString("startTime", "").toString()
        val attendanceCode = pref.getString("attendanceCode", "").toString()
        val endTime = pref.getString("endTime", "").toString()
        val location = pref.getString("location", "").toString()
        val scheduleId = pref.getInt("scheduleId", -1)
        val scheduleStatus = pref.getString("scheduleStatus", "").toString()

        return DataItemSchedule(date, activityName, startTime, attendanceCode, endTime, location, scheduleId, scheduleStatus)
    }

    fun getAddScheduleData(): HashMap<String, String?> {
        val schedule = HashMap<String, String?>()
        schedule["activityName"] = pref.getString("activityName", "")
        schedule["date"] = pref.getString("date", "")
        schedule["location"] = pref.getString("location", "")
        schedule["startTime"] = pref.getString("startTime", "")
        schedule["endTime"] = pref.getString("endTime", "")
        return schedule
    }

    fun getPrivacyCode(): String? {
        return pref.getString("privacyCode", "")
    }

    fun clearPrivacyCode() {
        editor.remove("privacyCode")
        editor.commit()
    }

    fun clearScheduleData() {
        editor.remove("date")
        editor.remove("activityName")
        editor.remove("attendanceCode")
        editor.remove("startTime")
        editor.remove("endTime")
        editor.remove("location")
        editor.remove("scheduleId")
        editor.remove("scheduleStatus")
        editor.commit()
    }


    fun clearData() {
        editor.clear()
        editor.commit()
    }
}