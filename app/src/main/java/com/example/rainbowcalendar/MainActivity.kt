package com.example.rainbowcalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.rainbowcalendar.fragments.AddFragment
import com.example.rainbowcalendar.fragments.CalendarFragment
import com.example.rainbowcalendar.fragments.HomeFragment
import com.example.rainbowcalendar.fragments.SettingsFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment=HomeFragment()
        val settingsFragment=SettingsFragment()
        val calendarFragment=CalendarFragment()
        val addFragment=AddFragment()

        makeCurrentFragment(homeFragment)

        val bottomNav = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener{
            when(it.itemId){
                R.id.navMenu_home->makeCurrentFragment(homeFragment)
                R.id.navMenu_acc->makeCurrentFragment(settingsFragment)
                R.id.navMenu_calendar->makeCurrentFragment(calendarFragment)
                R.id.navMenu_add->makeCurrentFragment(addFragment)
            }
            true
        }


    }
    private fun makeCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}