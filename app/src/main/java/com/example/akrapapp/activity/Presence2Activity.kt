package com.example.akrapapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.akrapapp.R
import com.example.akrapapp.api.RetrofitClient
import com.example.akrapapp.model.PresenceResponse
import com.example.akrapapp.shared_preferences.PrefManager
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_presence2.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Presence2Activity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presence2)

        prefManager = PrefManager(this)

        backPresence2.setOnClickListener {
            val intent = Intent(this, PresenceActivity::class.java)
            startActivity(intent)
        }

        presenceButton.setOnClickListener {
            presence()
        }
    }

    private fun presence() {
        //        get token from prefManager
        val token = "Bearer ${prefManager.getToken()}"
        val jobj = JsonObject()
        jobj.addProperty("attendance_code", inputCodeEditText.text.toString())

        RetrofitClient.instance.presence(token, jobj).enqueue(object : Callback<PresenceResponse> {
            override fun onResponse(
                call: Call<PresenceResponse>,
                response: Response<PresenceResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Presence2Activity, response.body()!!.msg, Toast.LENGTH_LONG).show()
                } else {
                    // Respon gagal
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                    val messageError = jsonObj.getString("message")

                    Toast.makeText(this@Presence2Activity, messageError, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<PresenceResponse>, t: Throwable) {
                Log.e("API Error", t.message.toString())
            }

        })
    }
}