package com.example.akrapapp.shared_preferences

import android.content.Context
import android.content.SharedPreferences
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

    fun setUserData(fullName: String, phoneNumber: String, birthdate: String, gender: String, username: String, job: String) {
        editor.putString("fullName", fullName)
        editor.putString("phoneNumber", phoneNumber)
        editor.putString("birthdate", birthdate)
        editor.putString("gender", gender)
        editor.putString("username", username)
        editor.putString("job", job)
    }

    fun getToken(): String? {
        return pref.getString("token", "")
    }

    fun getUserData(): UserData {
//        to call sessionManager.getUserData().fullName
        val fullName = pref.getString("fullName", "").toString()
        val phoneNumber = pref.getString("phoneNumber", "").toString()
        val birthdate = pref.getString("birthdate", "").toString()
        val gender = pref.getString("gender", "").toString()
        val username = pref.getString("username", "").toString()
        val job = pref.getString("job", "").toString()
        return UserData(fullName, phoneNumber, birthdate, gender, username, job)
    }

    fun clearToken() {
        editor.clear()
        editor.commit()
    }
}