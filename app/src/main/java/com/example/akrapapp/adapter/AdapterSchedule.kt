package com.example.akrapapp.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.akrapapp.R
import com.example.akrapapp.activity.ScheduleDetailActivity
import com.example.akrapapp.model.ItemViewSchedule
import com.example.akrapapp.shared_preferences.PrefManager
import kotlinx.android.synthetic.main.activity_register1.*

class AdapterSchedule (val context: Context, val fragmentId: String, private val mList: ArrayList<ItemViewSchedule>) : RecyclerView.Adapter<AdapterSchedule.ViewHolder>(){

    private lateinit var prefManager: PrefManager
    class ViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView) {
        val scheduleCard: LinearLayout = ItemView.findViewById(R.id.layoutScheduleCard)
        val optionSchedule: ImageButton = ItemView.findViewById(R.id.optionScheduleImageButton)
        val subjectSchedule: TextView = ItemView.findViewById(R.id.subjectScheduleTextView)
        val dateSchedule: TextView = ItemView.findViewById(R.id.dateScheduleTextView)
        val yearSchedule: TextView = ItemView.findViewById(R.id.yearScheduleTextView)
        val locationSchedule: TextView = ItemView.findViewById(R.id.locationScheduleTextView)
        val startTimeSchedule: TextView = ItemView.findViewById(R.id.startTimeScheduleTextView)
        val endTimeSchedule: TextView = ItemView.findViewById(R.id.endTimeScheduleTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_schedule, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = mList[position]
        prefManager = PrefManager(context)
        val role = prefManager.getUserData().role
        val fragmentId = fragmentId

        holder.subjectSchedule.text = itemsViewModel.activityName
        holder.dateSchedule.text = itemsViewModel.formattedDate
        holder.yearSchedule.text = itemsViewModel.formattedYear
        holder.locationSchedule.text = itemsViewModel.location
        holder.startTimeSchedule.text = itemsViewModel.formattedStartTime
        holder.endTimeSchedule.text = itemsViewModel.formattedEndTime

        if (role == "admin" && fragmentId == "schedule") {
            holder.optionSchedule.setOnClickListener {
                val date = itemsViewModel.date
                val activityName = itemsViewModel.activityName
                val startTime = itemsViewModel.formattedStartTime
                val attendanceCode = itemsViewModel.attendanceCode
                val endTime = itemsViewModel.formattedEndTime
                val location = itemsViewModel.location
                val scheduleId = itemsViewModel.scheduleId

                prefManager.setScheduleData(date, activityName,startTime, attendanceCode, endTime, location, scheduleId)

                val intent = Intent(context, ScheduleDetailActivity::class.java)
                context.startActivity(intent)
            }
        } else {
            val height = 100
            val marginBottom = 20
            val density = context.resources.displayMetrics.density
            val heightDp = (height * density).toInt()
            val marginBottomDp = (marginBottom * density).toInt()
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                heightDp
            )
            layoutParams.bottomMargin = marginBottomDp
            holder.scheduleCard.layoutParams = layoutParams
            holder.optionSchedule.visibility = View.GONE
        }
    }
}