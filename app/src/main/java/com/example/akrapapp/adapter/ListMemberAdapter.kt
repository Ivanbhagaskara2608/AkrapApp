package com.example.akrapapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.akrapapp.R
import com.example.akrapapp.activity.EditScheduleActivity
import com.example.akrapapp.model.UserData
import com.example.akrapapp.shared_preferences.PrefManager
import kotlinx.android.synthetic.main.card_view_member.view.*

class ListMemberAdapter (val context: Context, private var mList: ArrayList<UserData>, val activity: String) : RecyclerView.Adapter<ListMemberAdapter.ViewHolder>() {

    private lateinit var prefManager: PrefManager

    class ViewHolder (ItemView: View): RecyclerView.ViewHolder(ItemView) {
        val imageMember: ImageView = ItemView.imageMemberImageView
        val usernameMember: TextView = ItemView.usernameMemberTextView
        val cardMember: LinearLayout = ItemView.layoutCardMember
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_member, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = mList[position]
        prefManager = PrefManager(context)


        holder.imageMember.setImageResource(R.drawable.person)
        holder.usernameMember.text = itemsViewModel.username
        holder.cardMember.setOnClickListener {
            when (activity) {
                "editSchedule" -> {
                    val intent = Intent(context, EditScheduleActivity::class.java)
                    intent.putExtra("location", itemsViewModel.username)
                    context.startActivity(intent)
                }
                "addSchedule" -> {
                    val schedule = prefManager.getAddScheduleData()
                    val activityName = schedule["activityName"]
                    val date = schedule["date"]
                    val startTime = schedule["startTime"]
                    val endTime = schedule["endTime"]

                    prefManager.setAddScheduleData(activityName!!, date!!, itemsViewModel.username, startTime!!, endTime!!)
                    (context as Activity).finish()
                }
            }
        }
    }

    fun setFilteredList(mList: ArrayList<UserData>) {
        this.mList = mList
        notifyDataSetChanged()
    }
}