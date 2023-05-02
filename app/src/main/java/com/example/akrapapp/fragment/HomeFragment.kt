package com.example.akrapapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.akrapapp.R
import com.example.akrapapp.shared_preferences.PrefManager
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private lateinit var prefManager: PrefManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefManager = PrefManager(requireActivity())
        val dataLogin = requireActivity().intent.getStringExtra("fullName")
        val fullName = prefManager.getUserData().fullName

        if (dataLogin.isNullOrEmpty()) {
            usernameTextView.text = fullName
        } else {
            usernameTextView.text = dataLogin
        }

    }
}