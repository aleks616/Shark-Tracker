package com.example.rainbowcalendar

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.rainbowcalendar.fragments.AddFragment
import com.example.rainbowcalendar.fragments.CalendarFragment
import com.example.rainbowcalendar.fragments.HomeFragment
import com.example.rainbowcalendar.fragments.SettingsFragment

class MainActivity : AppCompatActivity() {
    override fun getTheme(): Resources.Theme {
        val theme=createConfigurationContext(Configuration()).theme
        theme.applyStyle(R.style.Dark_RainbowCalendar, true)

        return theme
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment=HomeFragment()
        val settingsFragment=SettingsFragment()
        val calendarFragment=CalendarFragment()
        val addFragment=AddFragment()
        Log.v("gayt",theme.toString())

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

    /**
     * Checks if password and recovery questions are set, shows or hides popup message accordingly
     * @
     *
     * */
    /*private fun popup(){
        val sharedPrefs=getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)
        val popUpText=findViewById<TextView>(R.id.popUpText)
        val popUpL=findViewById<LinearLayout>(R.id.popUp)
        val popUpSpace=findViewById<Space>(R.id.popUpSpace)
        val passwordSet=sharedPrefs.getString("passwordValue","")!=""
        val sharedPrefRecovery=getSharedPreferences("com.example.rainbowcalendar_recovery", Context.MODE_PRIVATE)
        val recoverySet=sharedPrefRecovery.getBoolean("done",false)
        val goToActivity=findViewById<Button>(R.id.goToActivityButton)
        val buttonClosePopup=findViewById<Button>(R.id.buttonClosePopup)

        if(!passwordSet){
            popUpL.visibility= View.VISIBLE
            popUpText.text="Set up a password!"
            popUpSpace.visibility= View.VISIBLE
        }
        else if(!recoverySet){
            popUpL.visibility= View.VISIBLE
            popUpText.text="Make recovery questions!"
            popUpSpace.visibility= View.VISIBLE
        }
        else{
            popUpL.visibility= View.GONE
            popUpSpace.visibility= View.GONE
        }
        goToActivity.setOnClickListener {
            if(!passwordSet)
                startActivity(Intent(this,PasswordActivity::class.java))
            else if(!recoverySet)
                startActivity(Intent(this,RecoveryActivity::class.java))
        }
        buttonClosePopup.setOnClickListener {
            popUpL.visibility= View.GONE
            popUpSpace.visibility= View.GONE
        }
    }*/
}