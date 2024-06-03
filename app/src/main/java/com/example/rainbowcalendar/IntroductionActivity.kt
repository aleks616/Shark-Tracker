package com.example.rainbowcalendar

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner

class IntroductionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)

        val sharedpref: SharedPreferences =applicationContext.getSharedPreferences("com.example.android.rainbowcalendar", MODE_PRIVATE)
        val token: String?=sharedpref.getString("token", null)

        val spinner = findViewById<Spinner>(R.id.themeSpinner)
        ArrayAdapter.createFromResource(this,R.array.themes_array,android.R.layout.simple_spinner_item)
            .also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        if (token=="False"||token==null){
            TODO("first time settings have to show when they're not saved, not only 1st launch")
        }

    }
}