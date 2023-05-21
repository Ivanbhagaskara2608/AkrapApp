package com.example.akrapapp.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.akrapapp.R
import kotlinx.android.synthetic.main.activity_privacy_code.code1
import kotlinx.android.synthetic.main.activity_privacy_code.code2
import kotlinx.android.synthetic.main.activity_privacy_code.code3
import kotlinx.android.synthetic.main.activity_privacy_code.code4
import kotlinx.android.synthetic.main.activity_privacy_code.code5
import kotlinx.android.synthetic.main.activity_privacy_code.code6
import kotlinx.android.synthetic.main.activity_set_privacy_code.*

class SetPrivacyCodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_privacy_code)

        backSetPrivacyCodeImageButton.setOnClickListener {
            finish()
        }

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(code1, InputMethodManager.SHOW_IMPLICIT)

        moveEditText(code1, code2, code1)
        moveEditText(code2, code3, code1)
        moveEditText(code3, code4, code2)
        moveEditText(code4, code5, code3)
        moveEditText(code5, code6, code4)
        moveEditText(code6, code6, code5)

        clearEditText()

        rootLayoutSetPrivacyCode.setOnClickListener {
            // Dapatkan instance InputMethodManager dari sistem
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            // Dapatkan token window yang sedang aktif
            val windowToken = currentFocus?.windowToken

            // Sembunyikan keyboard menggunakan token window
            imm.hideSoftInputFromWindow(windowToken, 0)
        }
    }

    private fun moveEditText(mainEditText: EditText, editTextAfter: EditText, editTextBefore: EditText) {
        mainEditText.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                mainEditText.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black))
            } else {
                // Set background tint ke warna asli (jika ingin mengembalikan ke warna default)
                if (mainEditText.text.isEmpty()) {
                    mainEditText.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
                } else {
                    mainEditText.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.brown))
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
                            editTextBefore.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black))
                        } else {
                            editTextBefore.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
                        }
                        editTextBefore.requestFocus()
                    }
                } else {
                    mainEditText.text.clear()
                    mainEditText.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
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
                        mainEditText.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@SetPrivacyCodeActivity, R.color.brown))

                        val code1Text = code1.text.toString()
                        val code2Text = code2.text.toString()
                        val code3Text = code3.text.toString()
                        val code4Text = code4.text.toString()
                        val code5Text = code5.text.toString()
                        val code6Text = code6.text.toString()
                        val inputPassCode = code1Text + code2Text + code3Text + code4Text + code5Text + code6Text

                        checkTask(inputPassCode)
                    } else {
                        mainEditText.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@SetPrivacyCodeActivity, R.color.brown))
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

    private fun checkTask(inputPassCode: String) {
        val task = intent.getStringExtra("task")
        val currentPrivacyCode = intent.getStringExtra("currentPrivacyCode")

        when (task) {
            "setPassCode" -> {
                clearEditText()
                val intent = Intent(this, SetConfirmationPrivacyCodeActivity::class.java)
                intent.putExtra("task", task)
                intent.putExtra("newPrivacyCode", inputPassCode)
                startActivity(intent)
            }
            "changePassCode" -> {
                if (currentPrivacyCode == inputPassCode) {
                    clearEditText()
                    Toast.makeText(this, "Harap buat Kode Privasi yang berbeda dari sebelumnya", Toast.LENGTH_LONG).show()
                } else {
                    clearEditText()
                    val intent = Intent(this, SetConfirmationPrivacyCodeActivity::class.java)
                    intent.putExtra("task", task)
                    intent.putExtra("currentPrivacyCode", currentPrivacyCode)
                    intent.putExtra("newPrivacyCode", inputPassCode)
                    startActivity(intent)
                }
            }
        }
    }

    private fun clearEditText() {
        code1.text.clear()
        code2.text.clear()
        code3.text.clear()
        code4.text.clear()
        code5.text.clear()
        code6.text.clear()

        code1.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        code2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        code3.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        code4.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        code5.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        code6.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))

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