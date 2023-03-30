package com.example.akrapapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.akrapapp.databinding.ActivityHomeBinding
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.homeNavigation -> replaceFragment(HomeFragment())
                R.id.scheduleNavigation -> replaceFragment(ScheduleFragment())
                R.id.infoNavigation -> replaceFragment(InformationFragment())
                R.id.settingsNavigation -> replaceFragment(SettingFragment())
            }
            true
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