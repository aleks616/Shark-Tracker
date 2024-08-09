package com.example.rainbowcalendar

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
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
        cycleDaysPicker.minValue=10
        cycleDaysPicker.maxValue=50
        cycleDaysPicker.value=28
        val periodDaysPicker=findViewById<NumberPicker>(R.id.periodDaysPicker)
        periodDaysPicker.maxValue=10
        periodDaysPicker.minValue=1
        periodDaysPicker.value=5
        val lastStartPicker=findViewById<NumberPicker>(R.id.lastStartPicker)
        lastStartPicker.minValue=0
        lastStartPicker.maxValue=50
        val buttonNext=findViewById<Button>(R.id.buttonNext)

        periodRegularCB.setOnCheckedChangeListener{_, isChecked->
            regular=isChecked
            if(regular)
                periodRegularL.visibility=View.VISIBLE
            else
                periodRegularL.visibility=View.GONE
        }

        // lastStartPicker.maxValue=cycleDaysPicker.value-1
        cycleDaysPicker.setOnValueChangedListener{ _, _, newVal->
            if(regular)
                lastStartPicker.maxValue=newVal-1
        }


        val sharedPrefs=applicationContext.getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)
        //doc:
        // cycleLength: Int
        // periodLength: Int
        //


        buttonNext.setOnClickListener {
            if(regular){
                val cycleLength=cycleDaysPicker.value
                val periodLength=periodDaysPicker.value
            }


        }


    }
}