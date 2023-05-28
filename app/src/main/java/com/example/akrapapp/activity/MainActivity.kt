package com.example.akrapapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.akrapapp.R
import com.example.akrapapp.shared_preferences.PrefManager

class MainActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefManager = PrefManager(this)
        val privacyCode = prefManager.getPrivacyCode()
//        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val network = connectivityManager.activeNetwork
//        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        // Set a delay for the splash screen
        val splashDelay: Long = 1000 // 3 seconds

        if (prefManager.getToken()!!.isNotEmpty()) {
            Handler().postDelayed({
                // Launch the main activity
                if (privacyCode!!.isEmpty() || privacyCode == "") {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("fragmentId", "home")
                    startActivity(intent)
                    // Close the splash screen activity
                    finish()
                } else {
                    val intent = Intent(this, PrivacyCodeActivity::class.java)
                    intent.putExtra("task", "openApp")
                    startActivity(intent)
                    finish()
                }
            }, splashDelay)
        } else {
            Handler().postDelayed({
                // Launch the main activity
                startActivity(Intent(this, LoginActivity::class.java))
                // Close the splash screen activity
                finish()
            }, splashDelay)
        }

//        if (networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
//            // Koneksi internet tersedia
//            if (prefManager.getToken()!!.isNotEmpty()) {
//                Handler().postDelayed({
//                    // Launch the main activity
//                    if (privacyCode!!.isNotEmpty()) {
//                        startActivity(Intent(this, PassCodeActivity::class.java))
//                        finish()
//                    } else {
//                        val intent = Intent(this, HomeActivity::class.java)
//                        intent.putExtra("fragmentId", "home")
//                        startActivity(intent)
//                        // Close the splash screen activity
//                        finish()
//                    }
//                }, splashDelay)
//            } else {
//                Handler().postDelayed({
//                    // Launch the main activity
//                    startActivity(Intent(this, LoginActivity::class.java))
//                    // Close the splash screen activity
//                    finish()
//                }, splashDelay)
//            }
//        } else {
//            // Tidak ada koneksi internet
//            if (prefManager.getToken()!!.isNotEmpty()) {
//                Handler().postDelayed({
//                    if (privacyCode!!.isNotEmpty()) {
//                        startActivity(Intent(this, PassCodeActivity::class.java))
//                        finish()
//                    } else {
//                        val intent = Intent(this, HomeActivity::class.java)
//                        intent.putExtra("fragmentId", "home")
//                        startActivity(intent)
//                        // Close the splash screen activity
//                        finish()
//                    }
//                }, splashDelay)
//            } else {
//                Handler().postDelayed({
//                    // Launch the main activity
//                    startActivity(Intent(this, LoginActivity::class.java))
//                    // Close the splash screen activity
//                    finish()
//                }, splashDelay)
//            }
//        }
    }
}