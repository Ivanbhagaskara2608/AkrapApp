package com.example.akrapapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.akrapapp.R
import kotlinx.android.synthetic.main.activity_presence2.*

class Presence2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presence2)

        backPresence2.setOnClickListener {
            val intent = Intent(this, PresenceActivity::class.java)
            startActivity(intent)
        }

        presenceButton.setOnClickListener {
            presence()
        }
    }

    private fun presence() {
        TODO("Not yet implemented")
    }
}