package com.example.akrapapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.akrapapp.R
import com.example.akrapapp.model.BannerItem
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.android.synthetic.main.banner_item_container.view.*

class BannerAdapter (private var mList: ArrayList<BannerItem>, private var viewPager: ViewPager2) : RecyclerView.Adapter<BannerAdapter.ViewHolder>(){
    class ViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView) {
        val banner: RoundedImageView = itemView.imageBanner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.banner_item_container, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = mList[position]

        holder.banner.setImageResource(itemsViewModel.image)
        if (position == mList.size - 2) {
            viewPager.post(runnable)
        }
    }

    private val runnable: Runnable = Runnable {
        mList.addAll(mList)
        notifyDataSetChanged()
    }
}