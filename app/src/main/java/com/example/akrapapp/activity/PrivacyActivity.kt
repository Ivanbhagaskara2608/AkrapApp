package com.example.akrapapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.akrapapp.R
import com.example.akrapapp.shared_preferences.PrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_privacy.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog_privacy_code.view.*

class PrivacyActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)

        prefManager = PrefManager(this)

        backPrivacyImageButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("fragmentId", "setting")
            startActivity(intent)
            finish()
        }

//        PRIVACY CODE
        privacyCodeLayout.setOnClickListener {
            checkPrivacyCodeAction()
        }

//        BIOMETRIC
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
        startActivity(intent)
        finish()
    }
}