package com.example.akrapapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.akrapapp.R
import com.example.akrapapp.adapter.CheckBoxMemberAdapter
import com.example.akrapapp.api.RetrofitClient
import com.example.akrapapp.model.GetAllUsersResponse
import com.example.akrapapp.model.GetScheduleDataResponse
import com.example.akrapapp.model.ItemViewSchedule
import com.example.akrapapp.model.UserData
import com.example.akrapapp.shared_preferences.PrefManager
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_check_box_list_member.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class CheckBoxListMemberActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    private lateinit var checkBoxMemberAdapter: CheckBoxMemberAdapter
    private var memberList = ArrayList<UserData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_box_list_member)

        prefManager = PrefManager(this)

        backCheckBoxMemberImageButton.setOnClickListener {
            finish()
        }

        showCheckBoxMember()

        checkBoxMemberSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                searchList(text)
                return true
            }

        })

        selectAllCheckBox.setOnCheckedChangeListener { _, isChecked ->
            for (i in 0 until checkBoxMemberRecyclerView.childCount) {
                val view = checkBoxMemberRecyclerView.getChildAt(i)
                val checkBox = view.findViewById<CheckBox>(R.id.selectCBMemberCheckBox)
                checkBox.isChecked = isChecked
            }
        }

        selectMemberButton.setOnClickListener {
            addSchedule()
        }
    }

    private fun addSchedule() {
        val schedule = prefManager.getAddScheduleData()
        val activityName = schedule["activityName"]
        val date = schedule["date"]
        val location = schedule["location"]
        val startTime = schedule["startTime"]
        val endTime = schedule["endTime"]
        val token = "Bearer ${prefManager.getToken()}"
        val checkedItems = checkBoxMemberAdapter.getCheckedItems()
        val jobj = JsonObject()
        jobj.addProperty("activity_name", activityName)
        jobj.addProperty("date", date)
        jobj.addProperty("location", location)
        jobj.addProperty("start_time", startTime)
        jobj.addProperty("end_time", endTime)
        jobj.add("users", checkedItems)

        RetrofitClient.instance.addSchedule(token, jobj).enqueue(object : Callback<GetScheduleDataResponse>{
            override fun onResponse(
                call: Call<GetScheduleDataResponse>,
                response: Response<GetScheduleDataResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()!!.data
                    val date = data.date
                    val activityName = data.activityName
                    val attdCode = data.attendanceCode
                    val startTime = data.startTime
                    val endTime = data.endTime
                    val location = data.location
                    val id = data.scheduleId
                    val status = data.status

                    val formatSchedule = ItemViewSchedule(date, activityName, startTime, attdCode, endTime, location, id, status)
                    prefManager.setScheduleData(date, activityName, formatSchedule.formattedStartTime, attdCode, formatSchedule.formattedEndTime, location, id, status)

                    Toast.makeText(this@CheckBoxListMemberActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@CheckBoxListMemberActivity, ScheduleDetailActivity::class.java)
                    startActivity(intent)
                } else {
                    // Respon gagal
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                    val messageError = jsonObj.getString("message")

                    Toast.makeText(this@CheckBoxListMemberActivity, messageError, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<GetScheduleDataResponse>, t: Throwable) {
                Log.e("API Error", t.message.toString())
            }

        })
    }

    private fun searchList(query: String?) {
        if (query!!.isNotEmpty()) {
            val filteredList = ArrayList<UserData>()
            for (i in memberList) {
                if (i.username.toLowerCase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                checkBoxMemberRecyclerView.visibility = View.GONE
                checkBoxMemberResponseTextView.visibility = View.VISIBLE
            } else {
                checkBoxMemberRecyclerView.visibility = View.VISIBLE
                checkBoxMemberResponseTextView.visibility = View.GONE
                checkBoxMemberAdapter.setFilteredList(filteredList)
            }
        } else {
            checkBoxMemberRecyclerView.visibility = View.VISIBLE
            checkBoxMemberResponseTextView.visibility = View.GONE
            checkBoxMemberAdapter.setFilteredList(memberList)
        }
    }

    private fun showCheckBoxMember() {
        val token = "Bearer ${prefManager.getToken()}"

        RetrofitClient.instance.getUsers(token).enqueue(object : Callback<GetAllUsersResponse> {
            override fun onResponse(
                call: Call<GetAllUsersResponse>,
                response: Response<GetAllUsersResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()!!.data
                    for (i in 0 until data.size){
                        val userId = data[i].userId
                        val fullName = data[i].fullName
                        val phoneNumber = data[i].phoneNumber
                        val birthdate = data[i].birthdate
                        val gender = data[i].gender
                        val username = data[i].username
                        val role = data[i].role
                        val status = data[i].status
                        val privacyCode = data[i].privacyCode


                        val member = UserData(userId, fullName, phoneNumber, birthdate, gender, username, role, status, privacyCode)
                        memberList.add(member)
                    }

                    checkBoxMemberAdapter = CheckBoxMemberAdapter(this@CheckBoxListMemberActivity, memberList)
                    checkBoxMemberRecyclerView.adapter = checkBoxMemberAdapter
                    val layoutManager = LinearLayoutManager(this@CheckBoxListMemberActivity)
                    checkBoxMemberRecyclerView.layoutManager = layoutManager
                }
            }

            override fun onFailure(call: Call<GetAllUsersResponse>, t: Throwable) {
                Log.e("API Error", t.message.toString())
            }

        })
    }
}