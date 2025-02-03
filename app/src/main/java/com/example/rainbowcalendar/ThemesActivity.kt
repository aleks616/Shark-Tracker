package com.example.rainbowcalendar

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.example.rainbowcalendar.fragments.LanguageMenu

class ThemesActivity:AppCompatActivity(){
    override fun onCreate(savedInstanceState:Bundle?){
        val sharedPrefs=applicationContext.getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)
        val theme=sharedPrefs.getString("theme","Light")
        ThemeManager[this]=theme
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_themes)
        val composeView=findViewById<ComposeView>(R.id.composeView)
        composeView.setContent{
           //ThemesSettings()
            LanguageMenu()
        }
    }

}
