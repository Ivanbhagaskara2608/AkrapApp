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
import com.example.akrapapp.model.ScheduleResponse
import com.example.akrapapp.shared_preferences.PrefManager
import kotlinx.android.synthetic.main.fragment_schedule_past.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SchedulePastFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule_past, container, false)
    }

    private lateinit var prefManager: PrefManager
    private var scheduleList = ArrayList<ItemViewSchedule>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefManager = PrefManager(requireActivity())

        showSchedulePast()
    }

    private fun showSchedulePast() {
        val token = "Bearer ${prefManager.getToken()}"

        RetrofitClient.instance.schedulePast(token).enqueue(object : Callback<ScheduleResponse> {
            override fun onResponse(
                call: Call<ScheduleResponse>,
                response: Response<ScheduleResponse>
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
                    schedulePastRecyclerView.adapter = AdapterSchedule("schedule", scheduleList)
                    val layoutManager = LinearLayoutManager(activity)
                    schedulePastRecyclerView.layoutManager = layoutManager
                } else {
                    // Respon gagal
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                    val messageError = jsonObj.getString("message")

                    schedulePastResponseTextView.text = messageError
                    schedulePastResponseTextView.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
//                get and show error response if there's error on API or server
                Toast.makeText(requireContext(), t.message.toString() , Toast.LENGTH_LONG).show()
                Log.e("API Error", t.message.toString())
            }

        })
    }
}