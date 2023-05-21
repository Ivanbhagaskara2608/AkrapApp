package com.example.akrapapp.adapter

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.akrapapp.R
import com.example.akrapapp.activity.ScheduleDetailActivity
import com.example.akrapapp.model.ItemViewSchedule
import com.example.akrapapp.shared_preferences.PrefManager
import com.google.gson.Gson
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.activity_register1.*
import kotlinx.android.synthetic.main.card_view_schedule.view.*

class AdapterSchedule (val context: Context, val fragmentId: String, private var mList: ArrayList<ItemViewSchedule>) : RecyclerView.Adapter<AdapterSchedule.ViewHolder>(){

    private lateinit var prefManager: PrefManager
    private val checkedItems = ArrayList<Int>()
    private var selected = false

    class ViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView) {
        val scheduleCard: LinearLayout = ItemView.layoutScheduleCard
        val optionSchedule: ImageButton = ItemView.optionScheduleImageButton
        val subjectScheduleLayout: LinearLayout = ItemView.subjectScheduleLayout
        val subjectSchedule: TextView = ItemView.subjectScheduleTextView
        val dateSchedule: TextView = ItemView.dateScheduleTextView
        val yearSchedule: TextView = ItemView.yearScheduleTextView
        val locationSchedule: TextView = ItemView.locationScheduleTextView
        val startTimeSchedule: TextView = ItemView.startTimeScheduleTextView
        val endTimeSchedule: TextView = ItemView.endTimeScheduleTextView
        val checkSchedule: ImageView = ItemView.scheduleCheckImageView
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

        if (itemsViewModel.status == "unavailable") {
            holder.scheduleCard.background = ContextCompat.getDrawable(context, R.drawable.bg_schedule_past)
            holder.subjectScheduleLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.darkGray))
            holder.optionSchedule.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black))
        } else {
            holder.scheduleCard.background = ContextCompat.getDrawable(context, R.drawable.bg_schedule)
            holder.subjectScheduleLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.brown))
            holder.optionSchedule.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.brown))
        }

        // Mengatur margin bottom jika posisi saat ini adalah posisi terakhir
        val marginBottom = if (position == mList.size - 1) {
            val density = context.resources.displayMetrics.density
            (70 * density).toInt()
        } else {
            0
        }
        val layoutParams = holder.scheduleCard.layoutParams as RelativeLayout.LayoutParams
        layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.rightMargin, marginBottom)
        holder.scheduleCard.layoutParams = layoutParams

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
                val status = itemsViewModel.status

                prefManager.setScheduleData(date, activityName,startTime, attendanceCode, endTime, location, scheduleId, status)

                val intent = Intent(context, ScheduleDetailActivity::class.java)
                context.startActivity(intent)
            }
        } else {
            val height = 100
            val marginTop = 10
            val density = context.resources.displayMetrics.density
            val heightDp = (height * density).toInt()
            val marginTopDp = (marginTop * density).toInt()
            val layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                heightDp
            )
            layoutParams.topMargin = marginTopDp
            holder.scheduleCard.layoutParams = layoutParams
            holder.optionSchedule.visibility = View.GONE

            if (fragmentId == "deleteActivity") {
                if (selected) {
                    holder.checkSchedule.visibility = View.VISIBLE
                    if (itemsViewModel.status == "unavailable") {
                        holder.checkSchedule.setImageResource(R.drawable.icon_check_past)
                    } else {
                        holder.checkSchedule.setImageResource(R.drawable.icon_check_recent)
                    }
                } else {
                    holder.checkSchedule.visibility = View.GONE
                }

                holder.scheduleCard.setOnClickListener {
                    if (!selected) {
                        holder.checkSchedule.visibility = View.VISIBLE
                        if (itemsViewModel.status == "unavailable") {
                            holder.checkSchedule.setImageResource(R.drawable.icon_check_past)
                        } else {
                            holder.checkSchedule.setImageResource(R.drawable.icon_check_recent)
                        }
                        selected = true
                        checkedItems.add(itemsViewModel.scheduleId)
                    } else {
                        holder.checkSchedule.visibility = View.GONE
                        selected = false
                        checkedItems.remove(itemsViewModel.scheduleId)
                    }
                }
            }
        }
    }

    fun setFilteredList(mList: ArrayList<ItemViewSchedule>) {
        this.mList = mList
        notifyDataSetChanged()
    }

    fun getCheckedItems(): JsonArray {
        val gson = Gson()
        val jsonArray = gson.toJsonTree(checkedItems).asJsonArray

        return jsonArray
    }

    fun clearCheckedItems() {
        selected = false
        checkedItems.clear()
    }
}