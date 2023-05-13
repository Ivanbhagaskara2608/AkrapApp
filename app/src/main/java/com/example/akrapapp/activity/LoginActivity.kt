package com.example.akrapapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.akrapapp.R
import com.example.akrapapp.api.RetrofitClient
import com.example.akrapapp.model.GetUserDataResponse
import com.example.akrapapp.model.TokenResponse
import com.example.akrapapp.shared_preferences.PrefManager
import com.google.gson.JsonObject
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
            usernameEditText.error = "Harap isi Username"
            usernameEditText.requestFocus()
        } else if (password.isEmpty()) {
            passwordEditText.error = "Harap isi Password"
            passwordEditText.requestFocus()
        } else {
            val jobj = JsonObject()
            jobj.addProperty("username", username)
            jobj.addProperty("password", password)

            RetrofitClient.instance.login(jobj).enqueue(object : Callback<TokenResponse>{
                    override fun onResponse(
                        call: Call<TokenResponse>,
                        response: Response<TokenResponse>
                    ) {
                        if (response.isSuccessful) {
                            prefManager.setToken(response.body()!!.token)
                            saveUserData()
                            Toast.makeText(this@LoginActivity, response.body()!!.token, Toast.LENGTH_LONG).show()
                        } else {
                            // Respon gagal
                            val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                            val messageError = jsonObj.getString("message")

                            Toast.makeText(this@LoginActivity, messageError, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "Username or Password is wrong" , Toast.LENGTH_LONG).show()
                        Log.e("API Error", t.message.toString())
                    }
                })
        }
    }

    private fun saveUserData() {
        val token = "Bearer ${prefManager.getToken()}"

        RetrofitClient.instance.userData(token).enqueue(object : Callback<GetUserDataResponse>{
            override fun onResponse(call: Call<GetUserDataResponse>, response: Response<GetUserDataResponse>) {
                val userId = response.body()!!.data.userId
                val fullName = response.body()!!.data.fullName
                val phoneNumber = response.body()!!.data.phoneNumber
                val birthdate = response.body()!!.data.birthdate
                val gender = response.body()!!.data.gender
                val username = response.body()!!.data.username
                val role = response.body()!!.data.role
                val status = response.body()!!.data.status

                prefManager.setUserData(userId, fullName, phoneNumber, birthdate, gender, username, role, status)
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                intent.putExtra("username", username)
                intent.putExtra("fragmentId", "home")
                startActivity(intent)
                finish()
            }

            override fun onFailure(call: Call<GetUserDataResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, t.message.toString() , Toast.LENGTH_LONG).show()
                Log.e("API Error", t.message.toString())
            }

        })
    }
}