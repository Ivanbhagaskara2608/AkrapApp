package com.example.akrapapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.akrapapp.R
import com.example.akrapapp.model.UserData
import com.example.akrapapp.shared_preferences.PrefManager
import com.google.gson.Gson
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.card_view_checkbox_member.view.*

class CheckBoxMemberAdapter (val context: Context, private var mList: ArrayList<UserData>) : RecyclerView.Adapter<CheckBoxMemberAdapter.ViewHolder>() {

    private val checkedItems = ArrayList<Int>()
    private lateinit var prefManager: PrefManager

    class ViewHolder (ItemView: View): RecyclerView.ViewHolder(ItemView) {
        val imageMember: ImageView = ItemView.imageCBMemberImageView
        val usernameMember: TextView = ItemView.usernameCBMemberTextView
        val checkBox: CheckBox = ItemView.selectCBMemberCheckBox
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_checkbox_member, parent, false)

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
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkedItems.add(itemsViewModel.userId)
            } else {
                checkedItems.remove(itemsViewModel.userId)
            }
        }
    }

    fun setFilteredList(mList: ArrayList<UserData>) {
        this.mList = mList
        notifyDataSetChanged()
    }

    fun getCheckedItems(): JsonArray {
        val gson = Gson()
        val jsonArray = gson.toJsonTree(checkedItems).asJsonArray

        return jsonArray
    }
}