package com.example.akrapapp.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.akrapapp.R
import com.example.akrapapp.shared_preferences.PrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_privacy.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog_privacy_code.view.*
import kotlinx.android.synthetic.main.set_biometric_dialog.*
import java.util.concurrent.Executor

class PrivacyActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)

        prefManager = PrefManager(this)
        val biometric = prefManager.getBiometric()

        backPrivacyImageButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("fragmentId", "setting")
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

//        PRIVACY CODE
        privacyCodeLayout.setOnClickListener {
            checkPrivacyCodeAction()
        }

//        BIOMETRIC
        biometricLayout.setOnClickListener {
            showDialogSetBiometric(biometric)
        }
    }

    private fun showDialogSetBiometric(biometric: Boolean) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.set_biometric_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val title: TextView = dialog.titleBiometricTextView
        val subtitle: TextView = dialog.subtitleBiometricTextView
        val yesBtn: Button = dialog.yesBiometricButton
        val noBtn: Button = dialog.noBiometricButton

        if (biometric) {
            title.text = "Nonaktifkan Kunci Biometrik"
            subtitle.text = "Apakah anda yakin ingin menonaktifkan Kunci Biometrik?"
        } else {
            title.text = "Aktifkan Kunci Biometrik"
            subtitle.text = "Aktifkan Kunci Biometrik untuk menjaga keamanan akun anda"
        }

        yesBtn.setOnClickListener {
            showBiometricAuthentication(biometric)
            dialog.dismiss()
        }

        noBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showBiometricAuthentication(biometric: Boolean) {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(applicationContext, errString, Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                if (biometric) {
                    prefManager.setBiometric(false)
                    recreate()
                } else {
                    prefManager.setBiometric(true)
                    recreate()
                }
            }

//            override fun onAuthenticationFailed() { super.onAuthenticationFailed()
//                Toast.makeText(applicationContext, "Autentikasi Kunci Biometrik gagal", Toast.LENGTH_SHORT).show()
//            }
        })

        var description = if (biometric) {
            "Pindai Sidik Jari untuk menonaktifkan Kunci Biometrik"
        } else {
            "Pindai Sidik Jari untuk mengaktifkan Kunci Biometrik"
        }

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Kunci Biometrik")
            .setSubtitle("AKRAP")
            .setDescription(description)
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

    private fun checkPrivacyCodeAction() {
        val privacyCode = prefManager.getPrivacyCode()

        if (privacyCode!!.isEmpty() || privacyCode == "") {
            val intent = Intent(this, SetPrivacyCodeActivity::class.java)
            intent.putExtra("task", "setPassCode")
            startActivity(intent)
        } else {
            // on below line we are creating a new bottom sheet dialog.
            val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)

            // on below line we are inflating a layout file which we have created.
            val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_privacy_code, null)

            view.changePrivacyCodeLayout.setOnClickListener {
                dialog.dismiss()
                val intent = Intent(this, PrivacyCodeActivity::class.java)
                intent.putExtra("task", "changePassCode")
                startActivity(intent)
            }

            view.deletePrivacyCodeLayout.setOnClickListener {
                dialog.dismiss()
                val intent = Intent(this, PrivacyCodeActivity::class.java)
                intent.putExtra("task", "nonActive")
                startActivity(intent)
            }

            // closing of dialog box when clicking on the screen.
            dialog.setCancelable(true)

            // content view to our view.
            dialog.setContentView(view)

            // a show method to display a dialog.
            dialog.show()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("fragmentId", "setting")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}