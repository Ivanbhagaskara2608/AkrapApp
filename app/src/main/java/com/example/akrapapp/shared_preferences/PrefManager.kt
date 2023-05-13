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

    fun setUserData(userId: Int, fullName: String, phoneNumber: String, birthdate: String, gender: String, username: String, role: String, status: Int) {
        editor.putInt("userId", userId)
        editor.putString("fullName", fullName)
        editor.putString("phoneNumber", phoneNumber)
        editor.putString("birthdate", birthdate)
        editor.putString("gender", gender)
        editor.putString("username", username)
        editor.putString("role", role)
        editor.putInt("status", status)
        editor.commit()
    }

    fun setScheduleData(date: String, activityName: String, startTime: String, attendanceCode: String, endTime: String, location: String, scheduleId: Int) {
        editor.putString("date", date)
        editor.putString("activityName", activityName)
        editor.putString("startTime", startTime)
        editor.putString("attendanceCode", attendanceCode)
        editor.putString("endTime", endTime)
        editor.putString("location", location)
        editor.putInt("scheduleId", scheduleId)
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

    fun getToken(): String? {
        return pref.getString("token", "")
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
        val status = pref.getInt("status", -1)

        return UserData(userId, fullName, phoneNumber, birthdate, gender, username, role, status)
    }

    fun getScheduleData(): DataItemSchedule {
        val date = pref.getString("date", "").toString()
        val activityName = pref.getString("activityName", "").toString()
        val startTime = pref.getString("startTime", "").toString()
        val attendanceCode = pref.getString("attendanceCode", "").toString()
        val endTime = pref.getString("endTime", "").toString()
        val location = pref.getString("location", "").toString()
        val scheduleId = pref.getInt("scheduleId", -1)

        return DataItemSchedule(date, activityName, startTime, attendanceCode, endTime, location, scheduleId)
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

    fun clearScheduleData() {
        editor.remove("date")
        editor.remove("activityName")
        editor.remove("startTime")
        editor.remove("endTime")
        editor.remove("location")
        editor.remove("scheduleId")
        editor.commit()
    }


    fun clearData() {
        editor.clear()
        editor.commit()
    }
}