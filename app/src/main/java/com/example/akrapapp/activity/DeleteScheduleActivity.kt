package com.example.akrapapp.activity

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.akrapapp.R
import com.example.akrapapp.adapter.AdapterSchedule
import com.example.akrapapp.api.RetrofitClient
import com.example.akrapapp.model.GetAllScheduleResponse
import com.example.akrapapp.model.ItemViewSchedule
import com.example.akrapapp.model.MessageDataResponse
import com.example.akrapapp.shared_preferences.PrefManager
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_delete_schedule.*
import kotlinx.android.synthetic.main.delete_schedule_dialog.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class DeleteScheduleActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    private lateinit var scheduleAdapter: AdapterSchedule
    private var scheduleList = ArrayList<ItemViewSchedule>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_schedule)

        prefManager = PrefManager(this)

        backDeleteScheduleImageButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("fragmentId", "schedule")
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        optionMenuDeleteScheduleImageButton.setOnClickListener {
            showPopupMenu()
        }

        deleteScheduleButton.setOnClickListener {
            showCustomDialog()
        }

        showAllSchedule()
    }

    private fun showPopupMenu() {
        val popupMenu = PopupMenu(this, optionMenuDeleteScheduleImageButton)
        popupMenu.menuInflater.inflate(R.menu.menu_item_schedule, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.recentMenu -> {
                    scheduleAdapter.clearCheckedItems()
                    filterList("available")
                    true
                }
                R.id.pastMenu -> {
                    // Aksi ketika opsi "Acara Selesai" dipilih
                    scheduleAdapter.clearCheckedItems()
                    filterList("unavailable")
                    true
                }
                R.id.datePickerMenu -> {
                    // Aksi ketika opsi "Atur Tanggal" dipilih
                    scheduleAdapter.clearCheckedItems()
                    showDatePickerDialog()
                    true
                }
                R.id.allScheduleMenu -> {
                    // Aksi ketika opsi "Acara Selesai" dipilih
                    scheduleAdapter.clearCheckedItems()
                    filterList("unavailable")
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun filterList(query: String) {
        val filteredList = ArrayList<ItemViewSchedule>()
        for (i in scheduleList) {
            if (i.status == query) {
                filteredList.add(i)
            }
        }
        if (filteredList.isEmpty()) {
            deleteScheduleRecyclerView.visibility = View.GONE
            deleteScheduleResponseTextView.visibility = View.VISIBLE
        } else {
            deleteScheduleRecyclerView.visibility = View.VISIBLE
            deleteScheduleResponseTextView.visibility = View.GONE
            scheduleAdapter.setFilteredList(filteredList)
        }
    }

    private fun filterListDate(date: String) {
        val filteredList = ArrayList<ItemViewSchedule>()
        for (i in scheduleList) {
            if (i.date == date) {
                filteredList.add(i)
            }
        }
        if (filteredList.isEmpty()) {
            deleteScheduleRecyclerView.visibility = View.GONE
            deleteScheduleResponseTextView.visibility = View.VISIBLE
        } else {
            deleteScheduleRecyclerView.visibility = View.VISIBLE
            deleteScheduleResponseTextView.visibility = View.GONE
            scheduleAdapter.setFilteredList(filteredList)
        }
    }

    private fun showDatePickerDialog() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
            val mMonth1 = mMonth + 1
            val date = ("" + mYear + "/" + mMonth1 + "/" + mDay)
            val sdfDate = SimpleDateFormat("yyyy/M/dd", Locale.getDefault())
            val parseDate = sdfDate.parse(date)
            val formattedDate = SimpleDateFormat("dd/MM/yyyy").format(parseDate)

            filterListDate(formattedDate)
        }, year, month, day).show()
    }

    private fun showAllSchedule() {
        val token = "Bearer ${prefManager.getToken()}"

        RetrofitClient.instance.scheduleAll(token).enqueue(object : Callback<GetAllScheduleResponse> {
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
                    shrimmerScheduleDelete.stopShimmer()
                    shrimmerScheduleDelete.visibility = View.GONE

                    scheduleAdapter = AdapterSchedule(this@DeleteScheduleActivity, "deleteActivity", scheduleList)
                    deleteScheduleRecyclerView.adapter = scheduleAdapter
                    val layoutManager = LinearLayoutManager(this@DeleteScheduleActivity)
                    deleteScheduleRecyclerView.layoutManager = layoutManager
                } else {
                    // Respon gagal
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                    val messageError = jsonObj.getString("message")

                    deleteScheduleResponseTextView.text = messageError
                    deleteScheduleResponseTextView.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<GetAllScheduleResponse>, t: Throwable) {
                Toast.makeText(this@DeleteScheduleActivity, t.message.toString() , Toast.LENGTH_LONG).show()
                Log.e("API Error", t.message.toString())
            }

        })
    }

    private fun showCustomDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.delete_schedule_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val yesBtn: Button = dialog.yesDeleteScheduleButton
        val noBtn: Button = dialog.noDeleteScheduleButton
        val checkedItems = scheduleAdapter.getCheckedItems()

        yesBtn.setOnClickListener {
            if (checkedItems.size() == 0) {
                Toast.makeText(this, "Harap pilih Acara yang ingin dihapus", Toast.LENGTH_SHORT).show()
            } else {
                deleteSchedule()
                dialog.dismiss()
                recreate()
            }
        }

        noBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun deleteSchedule() {
        val token = "Bearer ${prefManager.getToken()}"
        val jobj = JsonObject()
        val scheduleId = scheduleAdapter.getCheckedItems()

        jobj.add("scheduleId", scheduleId)

        RetrofitClient.instance.deleteSchedule(token, jobj).enqueue(object : Callback<MessageDataResponse> {
            override fun onResponse(
                call: Call<MessageDataResponse>,
                response: Response<MessageDataResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@DeleteScheduleActivity, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MessageDataResponse>, t: Throwable) {
                Log.e("API Error", t.message.toString())
            }

        })
    }
}