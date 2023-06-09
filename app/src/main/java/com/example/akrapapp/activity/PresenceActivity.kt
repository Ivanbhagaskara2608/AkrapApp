package com.example.akrapapp.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.example.akrapapp.R
import com.example.akrapapp.api.RetrofitClient
import com.example.akrapapp.model.MessageResponse
import com.example.akrapapp.shared_preferences.PrefManager
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_presence.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PresenceActivity : AppCompatActivity() {
    private lateinit var codescanner: CodeScanner
    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presence)

        prefManager = PrefManager(this)
        codescanner = CodeScanner(this, scanner_view)

        qrCodeButton.setOnClickListener {
            val intent = Intent(this, Presence2Activity::class.java)
            startActivity(intent)
        }

        backPresence.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("fragmentId", "home")
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 123)
        } else {
            startScanning()
        }
    }

    private fun startScanning() {
        //        get token from prefManager
        val token = "Bearer ${prefManager.getToken()}"

        // Parameters (default values)
        codescanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codescanner.formats= CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codescanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codescanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codescanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codescanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codescanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                val jobj = JsonObject()
                jobj.addProperty("attendance_code", it.text)

                RetrofitClient.instance.presence(token, jobj).enqueue(object : Callback<MessageResponse>{
                    override fun onResponse(
                        call: Call<MessageResponse>,
                        response: Response<MessageResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@PresenceActivity, response.body()!!.msg, Toast.LENGTH_LONG).show()
                        } else {
                            // Respon gagal
                            val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                            val messageError = jsonObj.getString("message")

                            Toast.makeText(this@PresenceActivity, messageError, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                        Log.e("API Error", t.message.toString())
                    }

                })
            }
        }
        codescanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scanner_view.setOnClickListener {
            codescanner.startPreview()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("fragmentId", "home")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        codescanner.startPreview()
    }

    override fun onPause() {
        codescanner.releaseResources()
        super.onPause()
    }
}