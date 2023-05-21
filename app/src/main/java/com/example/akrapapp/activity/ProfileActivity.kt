package com.example.akrapapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.akrapapp.R
import com.example.akrapapp.api.RetrofitClient
import com.example.akrapapp.model.GetUserDataResponse
import com.example.akrapapp.shared_preferences.PrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog_profile.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        prefManager = PrefManager(this)

        usernameTextViewProfile.text = prefManager.getUserData().username
        roleTextViewProfile.text = prefManager.getUserData().role
        fullnameProfileTextView.text = prefManager.getUserData().fullName
        phoneNumberProfileTextView.text = prefManager.getUserData().phoneNumber
        birthdateProfileTextView.text = prefManager.getUserData().birthdate
        genderProfileTextView.text = prefManager.getUserData().gender

        backProfileImageButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("fragmentId", "setting")
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        editUsernameImageButton.setOnClickListener {
            // on below line we are creating a new bottom sheet dialog.
            val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)

            // on below line we are inflating a layout file which we have created.
            val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_profile, null)

            // for call API updateUsername and dismissing the dialog button.
            view.confirmEditUsernameButton.setOnClickListener {
                val username = view.updateUsernameEditText.text.toString()
                if (username.isEmpty()) {
                    view.updateUsernameEditText.error = "Harap isi Username"
                    view.updateUsernameEditText.requestFocus()
                } else {
                    updateUsername(username)
                    dialog.dismiss()
                    recreate()
                }
            }

            // closing of dialog box when clicking on the screen.
            dialog.setCancelable(true)

            // content view to our view.
            dialog.setContentView(view)

            // a show method to display a dialog.
            dialog.show()
        }
    }

    private fun updateUsername(username: String) {
        val token = "Bearer ${prefManager.getToken()}"
        val jobj = JsonObject()

        jobj.addProperty("username", username)

        RetrofitClient.instance.updateUsername(token, jobj).enqueue(object : Callback<GetUserDataResponse>{
            override fun onResponse(call: Call<GetUserDataResponse>, response: Response<GetUserDataResponse>) {
                val userId = response.body()!!.data.userId
                val fullName = response.body()!!.data.fullName
                val phoneNumber = response.body()!!.data.phoneNumber
                val birthdate = response.body()!!.data.birthdate
                val gender = response.body()!!.data.gender
                val username = response.body()!!.data.username
                val role = response.body()!!.data.role
                val status = response.body()!!.data.status
                val privacyCode = response.body()!!.data.privacyCode ?: ""

                prefManager.setUserData(userId, fullName, phoneNumber, birthdate, gender, username, role, status, privacyCode)
                usernameTextViewProfile.text = username
                Toast.makeText(this@ProfileActivity, response.body()!!.message , Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<GetUserDataResponse>, t: Throwable) {
                Log.e("API Error", t.message.toString())
            }

        })
    }
}