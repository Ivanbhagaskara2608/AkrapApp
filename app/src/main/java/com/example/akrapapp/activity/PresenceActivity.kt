package com.example.akrapapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.*
import com.example.akrapapp.R
import kotlinx.android.synthetic.main.activity_presence.*

class PresenceActivity : AppCompatActivity() {
    private lateinit var codescanner: CodeScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presence)

        qrCodeButton.setOnClickListener {
            val intent = Intent(this, Presence2Activity::class.java)
            startActivity(intent)
        }

        backPresence.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        codescanner = CodeScanner(this, scanner_view)

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
                Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
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

    override fun onResume() {
        super.onResume()
        codescanner.startPreview()
    }

    override fun onPause() {
        codescanner.releaseResources()
        super.onPause()
    }
}