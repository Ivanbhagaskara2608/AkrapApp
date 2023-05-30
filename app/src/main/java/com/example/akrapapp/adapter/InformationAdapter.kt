package com.example.akrapapp.adapter

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
import com.example.akrapapp.activity.InformationDetailActivity
import com.example.akrapapp.model.DataItemInformation
import com.example.akrapapp.shared_preferences.PrefManager
import kotlinx.android.synthetic.main.card_view_information.view.*

class InformationAdapter(val context: Context, private var mList: ArrayList<DataItemInformation>, val fragmentId: String) : RecyclerView.Adapter<InformationAdapter.ViewHolder>() {

    private lateinit var prefManager: PrefManager

    class ViewHolder (ItemView: View): RecyclerView.ViewHolder(ItemView) {
        val informationLayout: LinearLayout = ItemView.informationLayout
        val informationImageView: ImageView = ItemView.informationImageView
        val titleInfo: TextView = ItemView.titleInfoTextView
        val dateInfo: TextView = ItemView.dateInfoTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_information, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = mList[position]
        prefManager = PrefManager(context)

        holder.informationImageView.setImageResource(itemsViewModel.getImage!!)
        holder.titleInfo.text = itemsViewModel.title
        if (itemsViewModel.created_at != itemsViewModel.updated_at) {
            holder.dateInfo.text = "Diedit : ${itemsViewModel.formattedUpdatedDate}"
        } else {
            holder.dateInfo.text = itemsViewModel.formattedDate
        }

        holder.informationLayout.setOnClickListener {
            val id = itemsViewModel.informationId
            val title = itemsViewModel.title
            val content = itemsViewModel.content
            val category = itemsViewModel.category
            val date = itemsViewModel.created_at
            val updated_at = itemsViewModel.updated_at

            prefManager.setInformationData(id, title, content, category, date, updated_at)

            val intent = Intent(context, InformationDetailActivity::class.java)
            intent.putExtra("fragmentId", fragmentId)
            context.startActivity(intent)
        }
    }
}