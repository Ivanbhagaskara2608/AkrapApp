package com.example.akrapapp.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.akrapapp.R
import com.example.akrapapp.shared_preferences.PrefManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_add_schedule.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddScheduleActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule)

        prefManager = PrefManager(this)

        val getSchedule = prefManager.getAddScheduleData()
        val getActivityName = getSchedule["activityName"]
        val getDate = getSchedule["date"]
        val getStartTime = getSchedule["startTime"]
        val getEndTime = getSchedule["endTime"]

        setText(getActivityName!!, addScheduleNameInputLayout, addScheduleNameEditText)
        setText(getDate!!, dateAddScheduleInputLayout, dateAddScheduleEditText)
        setText(getStartTime!!, startTimeAddScheduleInputLayout, startTimeAddScheduleEditText)
        setText(getEndTime!!, endTimeAddScheduleInputLayout, endTimeAddScheduleEditText)

        backAddScheduleImageButton.setOnClickListener {
            prefManager.clearScheduleData()
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("fragmentId", "schedule")
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        dateAddScheduleEditText.setOnClickListener {
            dateAddScheduleInputLayout.isHintEnabled = false

            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                val mMonth1 = mMonth + 1
                dateAddScheduleEditText.setText("" + mYear + "/" + mMonth1 + "/" + mDay)
            }, year, month, day).show()
        }

        locationAddScheduleEditText.setOnClickListener {
            val activityName = addScheduleNameEditText.text.toString()
            val location = locationAddScheduleEditText.text.toString()
            val date = dateAddScheduleEditText.text.toString()
            val startTime = startTimeAddScheduleEditText.text.toString()
            val endTime = endTimeAddScheduleEditText.text.toString()

            locationAddScheduleInputLayout.isHintEnabled = false
            prefManager.setAddScheduleData(activityName, date, location, startTime, endTime)
            val intent = Intent(this, ListMemberActivity::class.java)
            intent.putExtra("activityId", "addSchedule")
            startActivity(intent)
        }

        startTimeAddScheduleEditText.setOnClickListener {
            showTimePicker(startTimeAddScheduleEditText, startTimeAddScheduleInputLayout)
        }

        endTimeAddScheduleEditText.setOnClickListener {
            showTimePicker(endTimeAddScheduleEditText, endTimeAddScheduleInputLayout)
        }

        addScheduleButton.setOnClickListener {
            val activityName = addScheduleNameEditText.text.toString()
            val location = locationAddScheduleEditText.text.toString()
            val date = dateAddScheduleEditText.text.toString()
            val startTime = startTimeAddScheduleEditText.text.toString()
            val endTime = endTimeAddScheduleEditText.text.toString()

            if (activityName.isEmpty()) {
                addScheduleNameEditText.error = "Harap isi Nama Acara"
                addScheduleNameEditText.requestFocus()
                clearErrorEditText(addScheduleNameEditText)
            } else if (date.isEmpty()) {
                dateAddScheduleEditText.error = "Harap isi Tanggal"
                dateAddScheduleEditText.requestFocus()
//                clearErrorEditText(dateAddScheduleEditText)
            } else if (location.isEmpty()) {
                locationAddScheduleEditText.error = "Harap isi Lokasi"
                locationAddScheduleEditText.requestFocus()
                clearErrorEditText(locationAddScheduleEditText)
            } else if (startTime.isEmpty()) {
                startTimeAddScheduleEditText.error = "Harap isi Jam Mulai"
                startTimeAddScheduleEditText.requestFocus()
                clearErrorEditText(startTimeAddScheduleEditText)
            } else if (endTime.isEmpty()) {
                endTimeAddScheduleEditText.error = "Harap isi Jam Selesai"
                endTimeAddScheduleEditText.requestFocus()
                clearErrorEditText(endTimeAddScheduleEditText)
            } else {
                var isValidationPassed = true

                val formatterDate = DateTimeFormatter.ofPattern("yyyy/M/d")
                val formatDate = LocalDate.parse(date, formatterDate)
                val dateNow = LocalDate.now()

                if (formatDate.isBefore(dateNow)) {
                    Toast.makeText(this, "Tanggal tidak bisa hari kemarin", Toast.LENGTH_SHORT).show()
                    isValidationPassed = false
                }

                val formatterTime = DateTimeFormatter.ofPattern("HH:mm")
                val formatStartTime = LocalTime.parse(startTime, formatterTime)
                val formatEndTime = LocalTime.parse(endTime, formatterTime)

                if (formatEndTime.isBefore(formatStartTime)) {
                    Toast.makeText(this, "Jam Selesai tidak bisa sebelum Jam Mulai", Toast.LENGTH_SHORT).show()
                    isValidationPassed = false
                } else if (formatEndTime.equals(formatStartTime)){
                    Toast.makeText(this, "Jam Selesai tidak bisa sama dengan Jam Mulai", Toast.LENGTH_SHORT).show()
                    isValidationPassed = false
                }

                if (isValidationPassed) {
                    prefManager.setAddScheduleData(activityName, date, location, startTime, endTime)
                    val intent = Intent(this, CheckBoxListMemberActivity::class.java)
                    startActivity(intent)
                }

            }
        }

    }

    override fun onBackPressed() {
        prefManager.clearScheduleData()
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("fragmentId", "schedule")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
    override fun onResume() {
        super.onResume()
        val prefManager = PrefManager(this)
        val getSchedule = prefManager.getAddScheduleData()
        val location = getSchedule["location"]
        setText(location!!, locationAddScheduleInputLayout, locationAddScheduleEditText)
    }

    private fun setText(text: String, inputLayout: TextInputLayout, editText: TextInputEditText) {
        if (text.isNullOrEmpty()) {
            inputLayout.isHintEnabled = true
        } else {
            editText.setText(text)
        }
    }

    private fun showTimePicker(timeEditText: TextInputEditText, timeInputLayout: TextInputLayout) {
        timeInputLayout.isHintEnabled = false

        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        TimePickerDialog(this@AddScheduleActivity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            timeEditText.setText(String.format("%02d:%02d", hourOfDay, minute))
        }, hour, minute, false).show()
    }

    private fun clearErrorEditText(editText: TextInputEditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                        do nothing
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                        do nothing
            }

            override fun afterTextChanged(p0: Editable?) {
                editText.error = null
            }

        })
    }
}