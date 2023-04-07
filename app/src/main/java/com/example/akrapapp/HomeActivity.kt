package com.example.akrapapp

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
        val window: Window = this.window
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            if (it.itemId == R.id.homeNavigation) {
                replaceFragment(HomeFragment())
                window.statusBarColor = ContextCompat.getColor(this, R.color.white)
            } else if (it.itemId == R.id.scheduleNavigation) {
                replaceFragment(ScheduleFragment())
                window.statusBarColor = ContextCompat.getColor(this, R.color.orange)
            } else if (it.itemId == R.id.infoNavigation) {
                replaceFragment(InformationFragment())
                window.statusBarColor = ContextCompat.getColor(this, R.color.orange)
            } else if (it.itemId == R.id.settingsNavigation) {
                replaceFragment(SettingFragment())
                window.statusBarColor = ContextCompat.getColor(this, R.color.orange)
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