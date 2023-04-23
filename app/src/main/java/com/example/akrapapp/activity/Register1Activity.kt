package com.example.akrapapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.akrapapp.R
import kotlinx.android.synthetic.main.activity_register1.*

class Register1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register1)

        register1Button.setOnClickListener {
            val intent = Intent(this, Register2Activity::class.java)
            startActivity(intent)
        }
    }
}