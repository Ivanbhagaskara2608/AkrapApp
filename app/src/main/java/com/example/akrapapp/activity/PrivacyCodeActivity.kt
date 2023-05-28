package com.example.akrapapp.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.akrapapp.R
import com.example.akrapapp.api.RetrofitClient
import com.example.akrapapp.model.MessageDataResponse
import com.example.akrapapp.shared_preferences.PrefManager
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_privacy_code.*
import kotlinx.android.synthetic.main.delete_privacy_code_dialog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

class PrivacyCodeActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_code)

        prefManager = PrefManager(this)
        val task = intent.getStringExtra("task")
        val biometric = prefManager.getBiometric()

        backPrivacyCodeImageButton.setOnClickListener {
            finish()
        }

        moveEditText(code1, code2, code1)
        moveEditText(code2, code3, code1)
        moveEditText(code3, code4, code2)
        moveEditText(code4, code5, code3)
        moveEditText(code5, code6, code4)
        moveEditText(code6, code6, code5)

        clearEditText()

        when (task) {
            "openApp" -> {
                passCodeTaskTextView.text = "Masukkan Kode Privasi Akun Anda untuk masuk aplikasi"
                if (biometric) {
                    code1.clearFocus()
                    showBiometricAuthentication()
                }
            }
            "changePassCode" -> {
                passCodeTaskTextView.text = "Masukkan Kode Privasi Akun anda saat ini, untuk mengganti Kode Privasi yang baru"
            }
            "nonActive" -> {
                passCodeTaskTextView.text = "Masukkan Kode Privasi Akun anda saat ini, untuk menonaktfkan Kode Kunci Privasi"
            }
        }

        rootLayoutPrivacyCode.setOnClickListener {
            // Dapatkan instance InputMethodManager dari sistem
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            // Dapatkan token window yang sedang aktif
            val windowToken = currentFocus?.windowToken

            // Sembunyikan keyboard menggunakan token window
            imm.hideSoftInputFromWindow(windowToken, 0)
        }
    }

    private fun showBiometricAuthentication() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(applicationContext, errString, Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                val intent = Intent(this@PrivacyCodeActivity, HomeActivity::class.java)
                intent.putExtra("fragmentId", "home")
                startActivity(intent)
                finish()
            }

//            override fun onAuthenticationFailed() { super.onAuthenticationFailed()
//                Toast.makeText(applicationContext, "Autentikasi Kunci Biometrik gagal", Toast.LENGTH_SHORT).show()
//            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Kunci Biometrik")
            .setSubtitle("AKRAP")
            .setDescription("Pindai Sidik Jari untuk masuk kedalam aplikasi")
            .setNegativeButtonText("Batal")
            .build()

        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                biometricPrompt.authenticate(promptInfo)
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Toast.makeText(this, "Tidak ada Fitur Biometrik pada perangkat ini", Toast.LENGTH_LONG).show()
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Toast.makeText(this, "Fitur Biometrik tidak tersedia saat ini", Toast.LENGTH_LONG).show()
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                Toast.makeText(this, "Tidak ada biometrik yang terdaftar, Periksa pada pengaturan perangkat anda", Toast.LENGTH_LONG).show()
        }
    }

    private fun moveEditText(mainEditText: EditText, editTextAfter: EditText, editTextBefore: EditText) {
        mainEditText.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                mainEditText.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@PrivacyCodeActivity, R.color.black))
            } else {
                // Set background tint ke warna asli (jika ingin mengembalikan ke warna default)
                if (mainEditText.text.isEmpty()) {
                    mainEditText.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@PrivacyCodeActivity, R.color.white))
                } else {
                    mainEditText.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@PrivacyCodeActivity, R.color.brown))
                }
            }
        }

        mainEditText.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                if (mainEditText.text.isEmpty()) {
                    if (mainEditText == code1) {
                        mainEditText.requestFocus()
                    } else {
                        mainEditText.isFocusable = false
                        mainEditText.isFocusableInTouchMode = false
                        editTextBefore.isFocusable = true
                        editTextBefore.isFocusableInTouchMode = true
                        editTextBefore.text.clear()
                        if (editTextBefore == code1) {
                            editTextBefore.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@PrivacyCodeActivity, R.color.black))
                        } else {
                            editTextBefore.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@PrivacyCodeActivity, R.color.white))
                        }
                        editTextBefore.requestFocus()
                    }
                } else {
                    mainEditText.text.clear()
                    mainEditText.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@PrivacyCodeActivity, R.color.white))
                    mainEditText.requestFocus()
                }
            }
            false
        }

        mainEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                do nothing
            }

            override fun afterTextChanged(p0: Editable?) {
                if (mainEditText.text.isEmpty()) {
                    if (mainEditText == code1) {
                        mainEditText.requestFocus()
                    } else {
                        editTextBefore.isFocusable = true
                        editTextBefore.isFocusableInTouchMode = true
                        editTextBefore.requestFocus()
                    }
                } else {
                    if (mainEditText == code6) {
                        mainEditText.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@PrivacyCodeActivity, R.color.brown))

                        val code1Text = code1.text.toString()
                        val code2Text = code2.text.toString()
                        val code3Text = code3.text.toString()
                        val code4Text = code4.text.toString()
                        val code5Text = code5.text.toString()
                        val code6Text = code6.text.toString()
                        val inputPassCode = code1Text + code2Text + code3Text + code4Text + code5Text + code6Text

                        checkPassCode(inputPassCode)
                    } else {
                        mainEditText.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@PrivacyCodeActivity, R.color.brown))
                        mainEditText.isFocusable = false
                        mainEditText.isFocusableInTouchMode = false
                        editTextAfter.isFocusable = true
                        editTextAfter.isFocusableInTouchMode = true
                        editTextAfter.requestFocus()
                    }
                }
            }

        })
    }

    private fun checkPassCode(inputPassCode: String) {
        val task = intent.getStringExtra("task")
        val privacyCode = prefManager.getPrivacyCode()
        val delayInMillis: Long = 1000

        gifImageView.visibility = View.VISIBLE
        Glide.with(this).load(R.drawable.loading).into(gifImageView)
        Handler().postDelayed({
            when (task) {
                "openApp" -> {
                    if (inputPassCode == privacyCode) {
                        val intent = Intent(this@PrivacyCodeActivity, HomeActivity::class.java)
                        intent.putExtra("fragmentId", "home")
                        startActivity(intent)
                        finish()
                    } else {
                        clearEditText()
                        Toast.makeText(this@PrivacyCodeActivity, "Kode Privasi salah, harap masukkan kembali", Toast.LENGTH_LONG).show()
                    }
                }
                "changePassCode" -> {
                    if (inputPassCode == privacyCode) {
                        val intent = Intent(this, SetPrivacyCodeActivity::class.java)
                        intent.putExtra("task", task)
                        intent.putExtra("currentPrivacyCode", inputPassCode)
                        startActivity(intent)
                        finish()
                    } else {
                        clearEditText()
                        Toast.makeText(this@PrivacyCodeActivity, "Kode Privasi salah, harap masukkan kembali", Toast.LENGTH_LONG).show()
                    }
                }
                "nonActive" -> {
                    if (inputPassCode == privacyCode) {
                        showDialog(inputPassCode)
                    } else {
                        clearEditText()
                        Toast.makeText(this@PrivacyCodeActivity, "Kode Privasi salah, harap masukkan kembali", Toast.LENGTH_LONG).show()
                    }
                }
            }

            // Hapus GIF setelah waktu tertentu
            gifImageView.setImageResource(0)
        }, delayInMillis)
    }

    private fun showDialog(passCode: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.delete_privacy_code_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val yesBtn: Button = dialog.yesDeletePassCodeButton
        val noBtn: Button = dialog.noDeletePassCodeButton

        yesBtn.setOnClickListener {
            deletePrivacyCode(passCode)
        }

        noBtn.setOnClickListener {
            dialog.dismiss()
            clearEditText()
        }

        dialog.show()
    }

    private fun deletePrivacyCode(passCode: String) {
        val token = "Bearer ${prefManager.getToken()}"
        val jobj = JsonObject()

        jobj.addProperty("currentPrivacyCode", passCode)

        RetrofitClient.instance.deletePrivacyCode(token, jobj).enqueue(object : Callback<MessageDataResponse> {
            override fun onResponse(
                call: Call<MessageDataResponse>,
                response: Response<MessageDataResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@PrivacyCodeActivity, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                    prefManager.clearPrivacyCode()
                    val intent = Intent(this@PrivacyCodeActivity, PrivacyActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<MessageDataResponse>, t: Throwable) {
                Log.e("API Error", t.message.toString())
            }

        })
    }

    private fun clearEditText() {
        code1.text.clear()
        code2.text.clear()
        code3.text.clear()
        code4.text.clear()
        code5.text.clear()
        code6.text.clear()

        code1.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@PrivacyCodeActivity, R.color.white))
        code2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@PrivacyCodeActivity, R.color.white))
        code3.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@PrivacyCodeActivity, R.color.white))
        code4.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@PrivacyCodeActivity, R.color.white))
        code5.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@PrivacyCodeActivity, R.color.white))
        code6.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@PrivacyCodeActivity, R.color.white))

        code1.isFocusable = true
        code2.isFocusable = false
        code3.isFocusable = false
        code4.isFocusable = false
        code5.isFocusable = false
        code6.isFocusable = false

        code1.isFocusableInTouchMode = true
        code2.isFocusableInTouchMode = false
        code3.isFocusableInTouchMode = false
        code4.isFocusableInTouchMode = false
        code5.isFocusableInTouchMode = false
        code6.isFocusableInTouchMode = false

        code1.requestFocus()
    }
}