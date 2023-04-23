package com.example.akrapapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.akrapapp.fragment.SchedulePastFragment
import com.example.akrapapp.fragment.ScheduleRecentFragment

class FragmentScheduleAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2 // Jumlah fragmen yang ingin ditampilkan
    }

    override fun createFragment(position: Int): Fragment {
        // Kembalikan fragmen yang ingin ditampilkan pada posisi tertentu
        return when (position) {
            0 -> ScheduleRecentFragment()
            1 -> SchedulePastFragment()
            else -> ScheduleRecentFragment()// default fragmen
        }
    }
}

