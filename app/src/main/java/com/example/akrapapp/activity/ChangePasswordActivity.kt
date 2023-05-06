package com.example.akrapapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.akrapapp.R
import com.example.akrapapp.api.RetrofitClient
import com.example.akrapapp.model.TokenResponse
import com.example.akrapapp.shared_preferences.PrefManager
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_change_password.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        prefManager = PrefManager(this)

        backChangePasswordImageButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("fragmentId", "setting")
            startActivity(intent)
        }

        confirmChangePasswordButton.setOnClickListener {
            changePassword()
        }
    }

    private fun changePassword() {
        val token = "Bearer ${prefManager.getToken()}"
        val currentPassword = currentPasswordEditText.text.toString()
        val newPassword = newPasswordEditText.text.toString()
        val newPasswordConfirmation = confirmNewPasswordEditText.text.toString()

        if (currentPassword.isEmpty()) {
            currentPasswordEditText.error = "Harap isi Password Saat Ini"
            currentPasswordEditText.requestFocus()
        } else if (newPassword.isEmpty()) {
            newPasswordEditText.error = "Harap isi Password Baru"
            newPasswordEditText.requestFocus()
        } else if (newPasswordConfirmation.isEmpty()) {
            confirmNewPasswordEditText.error = "Harap isi Konfirmasi Password Baru"
            confirmNewPasswordEditText.requestFocus()
        } else {
            val jobj = JsonObject()
            jobj.addProperty("currentPassword", currentPassword)
            jobj.addProperty("newPassword", newPassword)
            jobj.addProperty("newPassword_confirmation", newPasswordConfirmation)

            RetrofitClient.instance.changePassword(token, jobj).enqueue(object : Callback<TokenResponse>{
                override fun onResponse(
                    call: Call<TokenResponse>,
                    response: Response<TokenResponse>
                ) {
                    if (response.isSuccessful) {
//                    clear all data on prefManager(token, userData)
                        prefManager.clearData()
//                    Toast response logout success
                        Toast.makeText(this@ChangePasswordActivity, response.body()!!.msg, Toast.LENGTH_LONG).show()
//                    go to LoginActivity
                        val intent = Intent(this@ChangePasswordActivity, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    } else {
                        // Respon gagal
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        val messageError = jsonObj.getString("message")

                        Toast.makeText(this@ChangePasswordActivity, messageError, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                    Log.e("API Error", t.message.toString())
                }

            })
        }
    }
}