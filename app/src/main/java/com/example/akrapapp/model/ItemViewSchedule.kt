package com.example.akrapapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class ItemViewSchedule(val date: String, val activityName: String, val startTime: String, val attendanceCode: String, val endTime: String, val location: String, val scheduleId: Int, var status: String) :
    Parcelable {
    val sdfDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val parseDate = sdfDate.parse(date)
    val formattedDate = SimpleDateFormat("dd MMM").format(parseDate)
    val formattedYear = SimpleDateFormat("yyyy").format(parseDate)

    val sdfTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val parseStartTime = sdfTime.parse(startTime)
    val parseEndTime = sdfTime.parse(endTime)
    val formattedStartTime = SimpleDateFormat("HH:mm").format(parseStartTime)
    val formattedEndTime = SimpleDateFormat("HH:mm").format(parseEndTime)

}
