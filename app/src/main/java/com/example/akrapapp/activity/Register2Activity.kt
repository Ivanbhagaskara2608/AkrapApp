package com.example.akrapapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.akrapapp.R
import com.example.akrapapp.api.RetrofitClient
import com.example.akrapapp.model.RegisterResponse
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_register2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)

        val bundle = intent.extras
        val fullName = bundle?.getString("fullName").toString()
        val phoneNumber = bundle?.getString("phoneNumber").toString()
        val gender = bundle?.getString("gender").toString()
        val birthdate = bundle?.getString("birthdate").toString()

        register2Button.setOnClickListener {
            register(fullName, phoneNumber, birthdate, gender)
        }
    }

    private fun register(fullName: String, phoneNumber: String, birthdate: String, gender: String) {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()
        val rePassword = repasswordEditText.text.toString()

        if (username.isEmpty()) {
            usernameEditText.error = "Harap isi Username"
            usernameEditText.requestFocus()
        } else if (password.isEmpty()) {
            passwordEditText.error = "Harap isi Password"
            passwordEditText.requestFocus()
        } else if (rePassword.isEmpty()) {
            repasswordEditText.error = "Harap isi Ulangi Password"
            repasswordEditText.requestFocus()
        } else {
            val jobj = JsonObject()
            jobj.addProperty("fullName", fullName)
            jobj.addProperty("phoneNumber", phoneNumber)
            jobj.addProperty("birthdate", birthdate)
            jobj.addProperty("gender", gender)
            jobj.addProperty("username", username)
            jobj.addProperty("password", password)
            jobj.addProperty("password_confirmation", rePassword)

            RetrofitClient.instance.register(jobj).enqueue(object : Callback<RegisterResponse>{
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@Register2Activity, response.body()!!.msg, Toast.LENGTH_LONG).show()
                        val intent = Intent(this@Register2Activity, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Log.e("API Error", t.message.toString())
                }

            })
        }
    }
}