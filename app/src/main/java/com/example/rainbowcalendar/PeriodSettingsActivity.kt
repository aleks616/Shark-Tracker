package com.example.rainbowcalendar

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PeriodSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_period_settings)
        val sharedPrefs=applicationContext.getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)
        val gender=sharedPrefs.getString("gender","")
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
        lastStartPicker.value=20
        lastStartPicker.maxValue=600
        val buttonNext=findViewById<Button>(R.id.buttonNext)
        val periodLengthText=findViewById<TextView>(R.id.periodLengthText)
        val cycleLengthText=findViewById<TextView>(R.id.cycleLengthText)

        if(gender=="m"){
            periodTitle.text=getString(R.string.period_settings_m)
            periodRegularCB.text=getString(R.string.period_regular_cb_m)
            periodLengthText.text=getString(R.string.period_length_question_m)
            cycleLengthText.text=getString(R.string.cycle_length_question_m)
        }

        periodRegularCB.setOnCheckedChangeListener{_, isChecked->
            regular=isChecked
            if(regular){
                periodRegularL.visibility=View.VISIBLE
                lastStartPicker.maxValue=cycleDaysPicker.value
            }
            else{
                periodRegularL.visibility=View.GONE
                lastStartPicker.maxValue=600
            }
        }

        // lastStartPicker.maxValue=cycleDaysPicker.value-1
        cycleDaysPicker.setOnValueChangedListener{ _, _, newVal->
            if(regular)
                lastStartPicker.maxValue=newVal-1
        }


        //doc:
        // cycleLength: Int
        // periodLength: Int
        // periodRegular: Boolean
        // lastPeriod


        buttonNext.setOnClickListener {
            sharedPrefs.edit().putBoolean("periodRegular",regular).apply()
            val cycleLength=cycleDaysPicker.value
            val periodLength=periodDaysPicker.value
            if(regular){
                sharedPrefs.edit().putInt("cycleLength",cycleLength).apply()
                sharedPrefs.edit().putInt("periodLength",periodLength).apply()
            }
            else{
                sharedPrefs.edit().putInt("cycleLength",0).apply()
                sharedPrefs.edit().putInt("periodLength",0).apply()

            }
            //todo: last start
            val formatter=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
            val calendar=Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR,-1*lastStartPicker.value)
            val date=formatter.format(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR, cycleLength)
            val nextDate=formatter.format(calendar.time)

            Log.i("shit", date)
            sharedPrefs.edit().putString("lastPeriod",date).apply()
            sharedPrefs.edit().putString("nextPeriod",nextDate).apply()

            startActivity(Intent(applicationContext, MainActivity::class.java))
        }


    }
}