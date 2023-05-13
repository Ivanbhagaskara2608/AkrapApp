package com.example.akrapapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.akrapapp.R
import com.example.akrapapp.adapter.AdapterSchedule
import com.example.akrapapp.api.RetrofitClient
import com.example.akrapapp.model.ItemViewSchedule
import com.example.akrapapp.model.GetAllScheduleResponse
import com.example.akrapapp.shared_preferences.PrefManager
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefManager = PrefManager(requireActivity())
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

        scheduleToday()
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

                        val schedule = ItemViewSchedule(date, activityName, startTime, attdCode, endTime, location, id)
                        scheduleList.add(schedule)
                    }
                    scheduleTodayRecyclerView.adapter = AdapterSchedule(requireContext(), "home", scheduleList)
                    val layoutManager = LinearLayoutManager(activity)
                    scheduleTodayRecyclerView.layoutManager = layoutManager
//                    val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//                    scheduleTodayRecyclerView.layoutManager = layoutManager
                } else {
                    // Respon gagal
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                    val messageError = jsonObj.getString("message")

                    scheduleTodayRecyclerView.visibility = View.GONE
                    scheduleTodayTextView.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<GetAllScheduleResponse>, t: Throwable) {
//                get and show error response if there's error on API or server
                Toast.makeText(requireContext(), t.message.toString() , Toast.LENGTH_LONG).show()
                Log.e("API Error", t.message.toString())
            }

        })
    }
}