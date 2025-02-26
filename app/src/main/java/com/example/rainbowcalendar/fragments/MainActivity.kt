package com.example.rainbowcalendar.fragments

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.rainbowcalendar.R
import com.example.rainbowcalendar.ThemeManager
import java.util.Locale
import com.example.rainbowcalendar.MainComposable

class MainActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState:Bundle?){
        val sharedPrefs=applicationContext.getSharedPreferences("com.example.rainbowcalendar_pref",Context.MODE_PRIVATE)
        val theme=sharedPrefs.getString("theme","Gray")
        ThemeManager[this]=theme
        val lang=sharedPrefs.getString("lang","en")!!
        if(lang=="pt-br")
            Locale("pt","BR")
        else
            Locale(lang)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val composeView:ComposeView=findViewById(R.id.compose_view)
        composeView.setContent{
            MainComposable()
        }



        val homeFragment=HomeFragment()
        makeCurrentFragment(homeFragment)
    }

    private fun makeCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.fl_wrapper,fragment)
            commit()
        }
}



