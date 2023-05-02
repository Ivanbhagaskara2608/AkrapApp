package com.example.akrapapp.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.akrapapp.R
import com.example.akrapapp.shared_preferences.PrefManager
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