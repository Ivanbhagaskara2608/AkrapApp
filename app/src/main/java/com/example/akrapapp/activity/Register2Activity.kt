package com.example.akrapapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.akrapapp.R
import kotlinx.android.synthetic.main.activity_register2.*

class Register2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)

        register2Button.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
//            register()
        }
    }

//    private fun register() {
//        TODO("Not yet implemented")
//    }
}