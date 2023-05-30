package com.example.akrapapp.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.akrapapp.R
import com.example.akrapapp.activity.StatisticActivity
import com.example.akrapapp.adapter.AdapterSchedule
import com.example.akrapapp.adapter.BannerAdapter
import com.example.akrapapp.adapter.InformationAdapter
import com.example.akrapapp.api.RetrofitClient
import com.example.akrapapp.model.*
import com.example.akrapapp.shared_preferences.PrefManager
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.math.abs


class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private lateinit var prefManager: PrefManager
    private var scheduleList = ArrayList<ItemViewSchedule>()
    private var informationList = ArrayList<DataItemInformation>()
    private val bannerList = ArrayList<BannerItem>()
    private val slideHandler = Handler()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefManager = PrefManager(requireActivity())

        homeRefreshLayout.setOnRefreshListener {
            informationList.clear()
            scheduleList.clear()
            scheduleToday()
            latestInfo()
            homeRefreshLayout.isRefreshing = false
        }

//        val scheduleListIntent = requireActivity().intent?.getSerializableExtra("scheduleList") as? ArrayList<ItemViewSchedule>
        val dataLogin = requireActivity().intent.getStringExtra("username")
        val username = prefManager.getUserData().username
        val cal = Calendar.getInstance()
        val timeOfDay = cal.get(Calendar.HOUR_OF_DAY)

        if (dataLogin.isNullOrEmpty()) {
            usernameTextView.text = username
        } else {
            usernameTextView.text = dataLogin
        }

        if (timeOfDay >= 0 && timeOfDay < 12) {
            greetingTextView.text = "Selamat Pagi"
        } else if (timeOfDay >= 12 && timeOfDay < 18) {
            greetingTextView.text = "Selamat Siang"
        } else {
            greetingTextView.text = "Selamat Malam"
        }

        statisticButton.setOnClickListener {
            val intent = Intent(requireActivity(), StatisticActivity::class.java)
            startActivity(intent)
        }

        scheduleToday()
        latestInfo()

        bannerList.add(BannerItem(R.drawable.banner1))
        bannerList.add(BannerItem(R.drawable.banner2))
        bannerList.add(BannerItem(R.drawable.banner3))
        bannerList.add(BannerItem(R.drawable.banner4))

        bannerViewPager.adapter = BannerAdapter(bannerList, bannerViewPager)
        bannerViewPager.clipToPadding = false
        bannerViewPager.clipChildren = false
        bannerViewPager.offscreenPageLimit = 3
        bannerViewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositionTransform = CompositePageTransformer()
        compositionTransform.addTransformer(MarginPageTransformer(40))
        compositionTransform.addTransformer(ViewPager2.PageTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        })

        bannerViewPager.setPageTransformer(compositionTransform)

        bannerViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                slideHandler.removeCallbacks(sliderRunnable)
                slideHandler.postDelayed(sliderRunnable, 4000)
            }
        })
    }

    private fun latestInfo() {
        val token = "Bearer ${prefManager.getToken()}"

        RetrofitClient.instance.latestInformationAll(token).enqueue(object : Callback<GetAllInformationResponse> {
            override fun onResponse(
                call: Call<GetAllInformationResponse>,
                response: Response<GetAllInformationResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()!!.data
                    if (data.isNullOrEmpty()) {
                        noLatestInfoTextView.visibility = View.VISIBLE
                        latestInfoRecyclerView.visibility = View.GONE
                    } else {
                        for (i in 0 until data.size){
                            val id = data[i].informationId
                            val title = data[i].title
                            val content = data[i].content
                            val category = data[i].category
                            val date = data[i].created_at
                            val updatedDate = data[i].updated_at

                            val information = DataItemInformation(id, title, content, category, date, updatedDate)
                            informationList.add(information)
                        }

                        latestInfoRecyclerView.adapter = InformationAdapter(requireContext(), informationList, "home")
                        val layoutManager = LinearLayoutManager(activity)
                        latestInfoRecyclerView.layoutManager = layoutManager
                    }
                }
            }

            override fun onFailure(call: Call<GetAllInformationResponse>, t: Throwable) {
                Log.e("API Error", t.message.toString())
            }

        })
    }

    private val sliderRunnable: Runnable = Runnable {
        bannerViewPager.currentItem = bannerViewPager.currentItem + 1
    }

    override fun onDestroyView() {
        super.onDestroyView()
        slideHandler.removeCallbacksAndMessages(null)
    }

    private fun scheduleToday() {
        val token = "Bearer ${prefManager.getToken()}"

        RetrofitClient.instance.scheduleToday(token).enqueue(object : Callback<GetAllScheduleResponse> {
            override fun onResponse(
                call: Call<GetAllScheduleResponse>,
                response: Response<GetAllScheduleResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()!!.data
                    for (i in 0 until data.size){
                        val date = data[i].date
                        val activityName = data[i].activityName
                        val attdCode = data[i].attendanceCode
                        val startTime = data[i].startTime
                        val endTime = data[i].endTime
                        val location = data[i].location
                        val id = data[i].scheduleId
                        val status = data[i].status

                        val schedule = ItemViewSchedule(date, activityName, startTime, attdCode, endTime, location, id, status)
                        scheduleList.add(schedule)
                    }
                    scheduleTodayTextView.visibility = View.VISIBLE
                    scheduleTodayRecyclerView.adapter = AdapterSchedule(requireContext(), "home", scheduleList)
                    val layoutManager = LinearLayoutManager(activity)
                    scheduleTodayRecyclerView.layoutManager = layoutManager
//                    val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//                    scheduleTodayRecyclerView.layoutManager = layoutManager
                }
            }

            override fun onFailure(call: Call<GetAllScheduleResponse>, t: Throwable) {
//                get and show error response if there's error on API or server
                Toast.makeText(requireContext(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show()
                Log.e("API Error", t.message.toString())
            }

        })
    }
}