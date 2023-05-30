package com.example.akrapapp.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.akrapapp.R
import com.example.akrapapp.api.RetrofitClient
import com.example.akrapapp.model.MessageResponse
import com.example.akrapapp.shared_preferences.PrefManager
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_information_detail.*
import kotlinx.android.synthetic.main.delete_information_dialog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InformationDetailActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information_detail)

        prefManager = PrefManager(this)

        val fragmentId = intent.getStringExtra("fragmentId")
        val role = prefManager.getUserData().role
        val category = prefManager.getInformationData().category
        val title = prefManager.getInformationData().title
        val createdDate = prefManager.getInformationData().created_at
        val updatedDate = prefManager.getInformationData().updated_at
        val formattedDate = prefManager.getInformationData().formattedDate
        val formattedUpdatedDate = prefManager.getInformationData().formattedUpdatedDate
        val content = prefManager.getInformationData().content

        categoryInfoTextView.text = category
        titleInfoTextView.text = title
        if (createdDate != updatedDate) {
            dateInfoDetailTextView.text = "Diedit : ${formattedUpdatedDate}"
        } else {
            dateInfoDetailTextView.text = formattedDate
        }
        contentInfoTextView.text = content

        if (role != "admin") {
            editInformationButton.visibility = View.GONE
            deleteInformationButton.visibility = View.GONE
        }

        backAddInfoImageButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("fragmentId", fragmentId)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        editInformationButton.setOnClickListener {
            val intent = Intent(this, EditInformationActivity::class.java)
            startActivity(intent)
        }

        deleteInformationButton.setOnClickListener {
            showDeleteDialog()
        }
    }

    private fun showDeleteDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.delete_information_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val yesBtn: Button = dialog.yesDeleteInfoButton
        val noBtn: Button = dialog.noDeleteInfoButton

        yesBtn.setOnClickListener {
            deleteInformation()
            dialog.dismiss()
        }

        noBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun deleteInformation() {
        val token = "Bearer ${prefManager.getToken()}"
        val informationId = prefManager.getInformationData().informationId
        val jobj = JsonObject()

        jobj.addProperty("informationId", informationId)

        RetrofitClient.instance.deleteInformation(token, jobj).enqueue(object : Callback<MessageResponse> {
            override fun onResponse(
                call: Call<MessageResponse>,
                response: Response<MessageResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@InformationDetailActivity, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@InformationDetailActivity, HomeActivity::class.java)
                    intent.putExtra("fragmentId", "information")
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Log.e("API Error", t.message.toString())
            }

        })
    }

    override fun onBackPressed() {
        val fragmentId = intent.getStringExtra("fragmentId")
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("fragmentId", fragmentId)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}