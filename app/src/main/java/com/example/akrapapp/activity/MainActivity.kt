package com.example.akrapapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.akrapapp.R
import com.example.akrapapp.shared_preferences.PrefManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefManager = PrefManager(this)
        // Set a delay for the splash screen
        val splashDelay: Long = 3000 // 3 seconds

        if (prefManager.getToken()!!.isNotEmpty()) {
            Handler().postDelayed({
                // Launch the main activity
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("fragmentId", "home")
                startActivity(intent)
                // Close the splash screen activity
                finish()
            }, splashDelay)
        } else {
            Handler().postDelayed({
                // Launch the main activity
                startActivity(Intent(this, LoginActivity::class.java))
                // Close the splash screen activity
                finish()
            }, splashDelay)
        }
    }
}