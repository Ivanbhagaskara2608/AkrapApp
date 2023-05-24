package com.example.akrapapp.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.akrapapp.R
import com.example.akrapapp.adapter.ListMemberAdapter
import com.example.akrapapp.api.RetrofitClient
import com.example.akrapapp.model.GetAllUsersResponse
import com.example.akrapapp.model.UserData
import com.example.akrapapp.shared_preferences.PrefManager
import kotlinx.android.synthetic.main.activity_list_member.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ListMemberActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    private lateinit var listMemberAdapter: ListMemberAdapter
    private var memberList = ArrayList<UserData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_member)

        prefManager = PrefManager(this)
        val activityId = intent.getStringExtra("activityId")

        backListMemberImageButton.setOnClickListener {
            finish()
        }

        showListMember(activityId.toString())

        listMemberSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                searchList(text)
                return true
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
                listMemberRecyclerView.visibility = View.GONE
                listMemberResponseTextView.visibility = View.VISIBLE
            } else {
                listMemberRecyclerView.visibility = View.VISIBLE
                listMemberResponseTextView.visibility = View.GONE
                listMemberAdapter.setFilteredList(filteredList)
            }
        } else {
            listMemberRecyclerView.visibility = View.VISIBLE
            listMemberResponseTextView.visibility = View.GONE
            listMemberAdapter.setFilteredList(memberList)
        }
    }

    private fun showListMember(activityId: String) {
        val token = "Bearer ${prefManager.getToken()}"

        RetrofitClient.instance.getUsers(token).enqueue(object : Callback<GetAllUsersResponse>{
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
                    shrimmerMember.stopShimmer()
                    shrimmerMember.visibility = View.GONE

                    listMemberAdapter = ListMemberAdapter(this@ListMemberActivity, memberList, activityId)
                    listMemberRecyclerView.adapter = listMemberAdapter
                    val layoutManager = LinearLayoutManager(this@ListMemberActivity)
                    listMemberRecyclerView.layoutManager = layoutManager
                }
            }

            override fun onFailure(call: Call<GetAllUsersResponse>, t: Throwable) {
                Log.e("API Error", t.message.toString())
            }

        })
    }
}