package com.example.akrapapp.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.akrapapp.R
import com.example.akrapapp.shared_preferences.PrefManager
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.activity_schedule_detail.*
import java.util.*

class ScheduleDetailActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_detail)

        prefManager = PrefManager(this)
        val bundle = intent.extras
        val attendanceCode = bundle?.getString("attendanceCode")
        val date = bundle?.getString("date")
        val location = bundle?.getString("location")
        val startTime = bundle?.getString("startTime")
        val endTime = bundle?.getString("endTime")

//        generate qrcode image
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

        codePresenceEditText.setText(attendanceCode)
        dateScheduleEditText.setText(date)
        locationScheduleEditText.setText(location)
        startTimeScheduleEditText.setText(startTime)
        endTimeScheduleEditText.setText(endTime)

        backScheduleImageButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("fragmentId", "schedule")
            startActivity(intent)
            finish()
        }

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        dateScheduleEditText.setOnClickListener {
            dateScheduleInputLayout.isHintEnabled = false
            val dpd = DatePickerDialog(this, android.R.style.Theme_Material_Dialog,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    val mMonth1 = mMonth + 1
                    dateScheduleEditText.setText("" + mYear + "/" + mMonth1 + "/" + mDay)
                }, year, month, day)
            dpd.show()
        }
    }
}