package com.example.akrapapp.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.akrapapp.R
import com.example.akrapapp.api.RetrofitClient
import com.example.akrapapp.model.GetScheduleDataResponse
import com.example.akrapapp.model.MessageResponse
import com.example.akrapapp.shared_preferences.PrefManager
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_edit_schedule.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class EditScheduleActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_schedule)

        prefManager = PrefManager(this)
        //        get data location form intent
        val getLocation = intent.getStringExtra("location")
//        get data schedule from prefmanager
        val scheduleId = prefManager.getScheduleData().scheduleId
        val date = prefManager.getScheduleData().date
        val location = prefManager.getScheduleData().location
        val startTime = prefManager.getScheduleData().startTime
        val endTime = prefManager.getScheduleData().endTime

//        change format of date
        val sdfDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val parseDate = sdfDate.parse(date)
        val formattedDate = SimpleDateFormat("yyyy/M/dd").format(parseDate)

        //        set date text
        dateEditScheduleEditText.setText(formattedDate)
        //        set location text, if admin change the location then set variable getLocation for location text
        if (getLocation.isNullOrEmpty()) {
            locationEditScheduleEditText.setText(location)
        } else {
            locationEditScheduleEditText.setText(getLocation)
        }
        //        set start time text
        startTimeEditScheduleEditText.setText(startTime)
//        set end time text
        endTimeEditScheduleEditText.setText(endTime)

        backEditScheduleImageButton.setOnClickListener {
            val intent = Intent(this, ScheduleDetailActivity::class.java)
            startActivity(intent)
            finish()
        }

        dateEditScheduleEditText.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                val mMonth1 = mMonth + 1
                dateEditScheduleEditText.setText("" + mYear + "/" + mMonth1 + "/" + mDay)
            }, year, month, day).show()
        }

        locationEditScheduleEditText.setOnClickListener {
            val intent = Intent(this, ListMemberActivity::class.java)
            intent.putExtra("activityId", "editSchedule")
            startActivity(intent)
        }

        startTimeEditScheduleEditText.setOnClickListener {
            showTimePicker(startTimeEditScheduleEditText)
        }

        endTimeEditScheduleEditText.setOnClickListener {
            showTimePicker(endTimeEditScheduleEditText)
        }

        editScheduleButton.setOnClickListener {
            editSchedule(scheduleId)
        }

        storeScheduleButton.setOnClickListener {
            storeSchedule(scheduleId)
        }
    }

    private fun storeSchedule(scheduleId: Int) {
        val token = "Bearer ${prefManager.getToken()}"
        val jobj = JsonObject()
        jobj.addProperty("scheduleId", scheduleId.toString())

        RetrofitClient.instance.storeSchedule(token, jobj).enqueue(object : Callback<MessageResponse>{
            override fun onResponse(
                call: Call<MessageResponse>,
                response: Response<MessageResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditScheduleActivity, response.body()!!.msg, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Log.e("API Error", t.message.toString())
            }

        })
    }

    private fun editSchedule(scheduleId: Int) {
        val date = dateEditScheduleEditText.text.toString()
        val location = locationEditScheduleEditText.text.toString()
        val startTime = startTimeEditScheduleEditText.text.toString()
        val endTime = endTimeEditScheduleEditText.text.toString()
        val token = "Bearer ${prefManager.getToken()}"
        val jobj = JsonObject()
        jobj.addProperty("scheduleId", scheduleId)
        jobj.addProperty("date", date)
        jobj.addProperty("location", location)
        jobj.addProperty("start_time", startTime)
        jobj.addProperty("end_time", endTime)

        RetrofitClient.instance.updateSchedule(token, jobj).enqueue(object :
            Callback<GetScheduleDataResponse> {
            override fun onResponse(
                call: Call<GetScheduleDataResponse>,
                response: Response<GetScheduleDataResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()!!.data
                    val date = data.date
                    val activityName = data.activityName
                    val attdCode = data.attendanceCode
                    val startTime = data.startTime
                    val endTime = data.endTime
                    val location = data.location
                    val id = data.scheduleId

                    prefManager.setScheduleData(date, activityName, startTime, attdCode, endTime, location, id)

                    Toast.makeText(this@EditScheduleActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@EditScheduleActivity, ScheduleDetailActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<GetScheduleDataResponse>, t: Throwable) {
                Log.e("API Error", t.message.toString())
            }

        })
    }

    private fun showTimePicker(timeEditText: TextInputEditText) {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        TimePickerDialog(this@EditScheduleActivity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            timeEditText.setText(String.format("%02d:%02d", hourOfDay, minute))
        }, hour, minute, false).show()
    }
}