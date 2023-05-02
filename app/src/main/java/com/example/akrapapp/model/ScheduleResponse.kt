package com.example.akrapapp.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class ScheduleResponse(
	val data: ArrayList<DataItemSchedule>,
	val message: String
)

data class DataItemSchedule(
	val date: String,
	@SerializedName("activity_name")
	val activityName: String,
	@SerializedName("start_time")
	val startTime: String,
	@SerializedName("attendance_code")
	val attendanceCode: String,
	@SerializedName("end_time")
	val endTime: String,
	val location: String,
	val scheduleId: Int
)

