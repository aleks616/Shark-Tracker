package com.example.rainbowcalendar

import android.content.Context
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Timehelper{
    fun now(context: Context): String {
        val formatter= SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        val calendar=Calendar.getInstance()
        return formatter.format(calendar.time)
    }
}