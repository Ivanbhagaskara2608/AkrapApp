package com.example.akrapapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.akrapapp.R
import com.example.akrapapp.api.RetrofitClient
import com.example.akrapapp.model.LoginResponse
import com.example.akrapapp.shared_preferences.PrefManager
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefManager = PrefManager(this)

        registerTextView.setOnClickListener {
            val intent = Intent(this, Register1Activity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            login()
//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)
        }
    }

    private fun login() {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

        val requestBody = JSONObject()
        requestBody.put("username", username)
        requestBody.put("password", password)

        if (username.isEmpty()) {
            usernameEditText.error = "Username is required"
            usernameEditText.requestFocus()
        } else if (password.isEmpty()) {
            passwordEditText.error = "Password is required"
            passwordEditText.requestFocus()
        } else {
            RetrofitClient.instance.login(requestBody)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful) {
                            prefManager.setToken(response.body()!!.token)
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // Respon gagal
                            val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                            val messageError = jsonObj.getString("message")

                            Toast.makeText(this@LoginActivity, messageError, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, t.message.toString(), Toast.LENGTH_LONG).show()
                        Log.e("API Error", t.message.toString())
                    }

                })
        }
    }
}