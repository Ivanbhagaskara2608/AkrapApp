package com.example.akrapapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.akrapapp.R
import com.example.akrapapp.databinding.ActivityHomeBinding
import com.example.akrapapp.fragment.HomeFragment
import com.example.akrapapp.fragment.InformationFragment
import com.example.akrapapp.fragment.ScheduleFragment
import com.example.akrapapp.fragment.SettingFragment
import com.example.akrapapp.shared_preferences.PrefManager
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager(this)

        val window: Window = this.window
        val fragmentId = intent.getStringExtra("fragmentId")

//        to change fragment and bottomnav selected, if user back from activity to fragment before
        when (fragmentId) {
            "home" -> {
                replaceFragment(HomeFragment())
                window.statusBarColor = ContextCompat.getColor(this, R.color.white)
            }

            "schedule" -> {
                replaceFragment(ScheduleFragment())
                binding.bottomNavigationView.selectedItemId = R.id.scheduleNavigation
            }

            "information" -> {
                replaceFragment(InformationFragment())
                binding.bottomNavigationView.selectedItemId = R.id.infoNavigation
            }

            "setting" -> {
                replaceFragment(SettingFragment())
                binding.bottomNavigationView.selectedItemId = R.id.settingsNavigation
            }
        }
//        *******************************************************************************

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeNavigation -> {
                    replaceFragment(HomeFragment())
                    window.statusBarColor = ContextCompat.getColor(this, R.color.white)

                }

                R.id.scheduleNavigation -> {
                    replaceFragment(ScheduleFragment())
                    window.statusBarColor = ContextCompat.getColor(this, R.color.orange)
                }

                R.id.infoNavigation -> {
                    replaceFragment(InformationFragment())
                    window.statusBarColor = ContextCompat.getColor(this, R.color.orange)
                }

                R.id.settingsNavigation -> {
                    replaceFragment(SettingFragment())
                    window.statusBarColor = ContextCompat.getColor(this, R.color.orange)
                }
            }
            true
        }

        scanNavigation.setOnClickListener {
            val intent = Intent(this, PresenceActivity::class.java)
            startActivity(intent)
        }

        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(2).isEnabled = false
    }

    private fun  replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }
}