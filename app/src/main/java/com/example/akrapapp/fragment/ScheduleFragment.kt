package com.example.akrapapp.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.akrapapp.R
import com.example.akrapapp.activity.AddScheduleActivity
import com.example.akrapapp.activity.DeleteScheduleActivity
import com.example.akrapapp.adapter.FragmentScheduleAdapter
import com.example.akrapapp.shared_preferences.PrefManager
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_schedule.*

class ScheduleFragment : Fragment() {
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim) }

    private var clicked = false
    private lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FragmentScheduleAdapter(requireActivity())
        scheduleViewPager.adapter = adapter

        TabLayoutMediator(scheduleTabLayout, scheduleViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Terbaru"
                1 -> "Selesai"
                else -> ""
            }
        }.attach()

        prefManager = PrefManager(requireActivity())
        val role = prefManager.getUserData().role

        if (role == "member") {
            editFloatingButton.visibility = View.GONE
        }

//        change icon and background color
        editFloatingButton.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange))
        editFloatingButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))

        editFloatingButton.setOnClickListener {
            onAddButtonClicked()
        }

        addFloatingButton.setOnClickListener {
            prefManager.clearScheduleData()
            val intent = Intent(requireActivity(), AddScheduleActivity::class.java)
            startActivity(intent)
        }

        deleteFloatingButton.setOnClickListener {
            val intent = Intent(requireActivity(), DeleteScheduleActivity::class.java)
            startActivity(intent)
        }

    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            addFloatingButton.visibility = View.VISIBLE
            deleteFloatingButton.visibility = View.VISIBLE
        } else {
            addFloatingButton.visibility = View.INVISIBLE
            deleteFloatingButton.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            addFloatingButton.startAnimation(fromBottom)
            deleteFloatingButton.startAnimation(fromBottom)
            editFloatingButton.startAnimation(rotateOpen)
        } else {
            addFloatingButton.startAnimation(toBottom)
            deleteFloatingButton.startAnimation(toBottom)
            editFloatingButton.startAnimation(rotateClose)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if (!clicked) {
            addFloatingButton.isClickable = true
            deleteFloatingButton.isClickable = true
        } else {
            addFloatingButton.isClickable = false
            deleteFloatingButton.isClickable = false
        }
    }
}