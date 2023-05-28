package com.example.akrapapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.akrapapp.R
import com.example.akrapapp.activity.*
import com.example.akrapapp.api.RetrofitClient
import com.example.akrapapp.model.TokenResponse
import com.example.akrapapp.shared_preferences.PrefManager
import kotlinx.android.synthetic.main.fragment_setting.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }
    private lateinit var prefManager: PrefManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        init prefManager
        prefManager = PrefManager(requireActivity())
//        Set card user text
        usernameTextViewSetting.text = prefManager.getUserData().username
        roleTextViewSetting.text = prefManager.getUserData().role
        if (prefManager.getUserData().status == "active") {
            statusTextViewSetting.text = "Aktif"
        } else {
            statusTextViewSetting.text = "Tidak Aktif"
            statusTextViewSetting.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        }

        profileImageButton.setOnClickListener {
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            startActivity(intent)
        }

//        CHANGE PASSWORD
        changePasswordLayout.setOnClickListener {
            toChangePassword()
        }

//        PRIVACY & SECURITY
        privacyLayout.setOnClickListener {
            val intent = Intent(requireActivity(), PrivacyActivity::class.java)
            startActivity(intent)
        }
//        ABOUT
        aboutLayout.setOnClickListener {
            val intent = Intent(requireActivity(), AboutAppActivity::class.java)
            startActivity(intent)
        }
//        RATING
//        REPORT BUG
        reportLayout.setOnClickListener {
            val recipient = "ivanbhagas2003@gmail.com"
            val subject = "AKRAP BUG REPORT"

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)

            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(Intent.createChooser(intent, "Send Email"))
            } else {
                Toast.makeText(requireContext(), "Tidak ada aplikasi untuk mengirim e-mail", Toast.LENGTH_SHORT).show()
            }
        }
//        LOGOUT
        logoutLayout.setOnClickListener {
            logout()
        }

    }

    private fun toChangePassword() {
        val intent = Intent(requireActivity(), ChangePasswordActivity::class.java)
        startActivity(intent)
    }

    private fun logout() {
//        get token from prefManager
        val token = "Bearer ${prefManager.getToken()}"

//        call Retrofit logout API using val token as header auth, and get response from LogoutResponse
        RetrofitClient.instance.logout(token).enqueue(object : Callback<TokenResponse>{
            override fun onResponse(
                call: Call<TokenResponse>,
                response: Response<TokenResponse>
            ) {
                if (response.isSuccessful) {
//                    clear all data on prefManager(token, userData)
                    prefManager.clearData()
//                    Toast response logout success
                    Toast.makeText(requireContext(), response.body()!!.msg, Toast.LENGTH_LONG).show()
//                    go to LoginActivity
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
//                get and show error response if there's error on API or server
                Toast.makeText(requireContext(), t.message.toString() , Toast.LENGTH_LONG).show()
                Log.e("API Error", t.message.toString())
            }

        })
    }
}