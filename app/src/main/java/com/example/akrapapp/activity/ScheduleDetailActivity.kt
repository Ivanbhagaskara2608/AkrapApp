package com.example.akrapapp.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.akrapapp.R
import com.example.akrapapp.shared_preferences.PrefManager
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.activity_schedule_detail.*
import java.text.SimpleDateFormat
import java.util.*

class ScheduleDetailActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    private lateinit var activity: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_detail)

//        init prefmanager
        prefManager = PrefManager(this)
//        get data schedule from prefmanager
        val activityName = prefManager.getScheduleData().activityName
        val attendanceCode = prefManager.getScheduleData().attendanceCode
        val date = prefManager.getScheduleData().date
        val location = prefManager.getScheduleData().location
        val startTime = prefManager.getScheduleData().startTime
        val endTime = prefManager.getScheduleData().endTime

//        change format of date
        val sdfDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val parseDate = sdfDate.parse(date)
        val formattedDate = SimpleDateFormat("yyyy/M/dd").format(parseDate)

//        set activity name text
        activityNameTextView.setText(activityName)
//        set image and generate qrcode
        generateQrCode(attendanceCode)
//        set code presence text
        codePresenceEditText.setText(attendanceCode)
//        set date text
        dateScheduleEditText.setText(formattedDate)
//        set location text, if admin change the location then set variable getLocation for location text
        locationScheduleEditText.setText(location)
//        set start time text
        startTimeScheduleEditText.setText(startTime)
//        set end time text
        endTimeScheduleEditText.setText(endTime)

//        set event when button back is clicked go to HomeActivity fragment schedule
        backScheduleImageButton.setOnClickListener {
            prefManager.clearScheduleData()
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("fragmentId", "schedule")
            startActivity(intent)
            finish()
        }

//        set event when copy button is clicked then copy code attendance code to clipboard
        copyImageButton.setOnClickListener {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("attendanceCode", codePresenceEditText.text.toString())

            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Kode Presensi disalin", Toast.LENGTH_SHORT).show()
        }

//        set event when edit button is clicked, run editSchedule function
        editScheduleButton.setOnClickListener {
            val intent = Intent(this, EditScheduleActivity::class.java)
            startActivity(intent)
        }
    }
    private fun generateQrCode(attendanceCode: String) {
        val writer = QRCodeWriter()
        try {
            val bitMatrix = writer.encode(attendanceCode, BarcodeFormat.QR_CODE, 612, 612)
            val width = bitMatrix.width
            val heigth = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, heigth, Bitmap.Config.RGB_565)
            for (x in 0 until width){
                for (y in 0 until heigth) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            qrCodeImageView.setImageBitmap(bmp)
        } catch (e: WriterException){
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        prefManager.clearScheduleData()
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("fragmentId", "schedule")
        startActivity(intent)
        finish()
    }
}