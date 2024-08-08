package com.example.rainbowcalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
class PeriodSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_period_settings)

        val periodRegularL=findViewById<LinearLayout>(R.id.periodRegularL)
        val periodTitle=findViewById<TextView>(R.id.periodTitle)
        val periodRegularCB=findViewById<CheckBox>(R.id.periodRegularCB)
        var regular=true
        val cycleDaysPicker=findViewById<NumberPicker>(R.id.cycleDaysPicker)
        val periodDaysPicker=findViewById<NumberPicker>(R.id.periodDaysPicker)
        val lastStartPicker=findViewById<NumberPicker>(R.id.lastStartPicker)


        periodRegularCB.setOnCheckedChangeListener{_, isChecked->
            regular=isChecked
            if(regular)
                periodRegularL.visibility=View.VISIBLE
            else
                periodRegularL.visibility=View.GONE
        }


    }
}