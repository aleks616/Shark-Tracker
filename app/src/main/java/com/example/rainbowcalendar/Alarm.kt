package com.example.rainbowcalendar
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import java.util.Calendar
import java.util.GregorianCalendar
import android.content.Context
import android.util.Log
import android.widget.Toast

class Alarm(private val context: Context){
    private val alarmManager=context.getSystemService(ALARM_SERVICE) as AlarmManager
    private val alarmPendingIntent by lazy {
        val intent=Intent(context, AlarmReceiver::class.java)
        PendingIntent.getBroadcast(context, 0,intent,PendingIntent.FLAG_IMMUTABLE)
    }

        /*fun schedulePushNotifications(hour: Int, minute: Int, intervalD: Int, startFrom: Int){
        val calendar=GregorianCalendar.getInstance().apply{
            if (before(GregorianCalendar.getInstance())) add(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY,hour)
            set(Calendar.MINUTE,minute)
            set(Calendar.SECOND,0)
            set(Calendar.MILLISECOND,0)

            add(Calendar.DAY_OF_MONTH, startFrom)
            Log.v("notification", "notification set to$hour $minute")
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,AlarmManager.INTERVAL_DAY*intervalD,alarmPendingIntent)
    }*/

    fun schedulePushNotifications(hour:Int,minute:Int,intervalD:Int,startFrom:Int){
        val calendar=GregorianCalendar.getInstance().apply{
            set(Calendar.HOUR_OF_DAY,hour)
            set(Calendar.MINUTE,minute)
            set(Calendar.SECOND,0)
            set(Calendar.MILLISECOND,0)

            val sharedPrefNotif=context.getSharedPreferences("com.example.rainbowcalendar_notif",Context.MODE_PRIVATE)
            val lastNotif=sharedPrefNotif.getLong("com.example.rainbowcalendar_notif",0L)

            if(lastNotif>0) timeInMillis=lastNotif+intervalD*AlarmManager.INTERVAL_DAY
            else add(Calendar.DAY_OF_MONTH,startFrom)

            if(time<Calendar.getInstance().time) add(Calendar.DAY_OF_MONTH,intervalD)
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.time.time,AlarmManager.INTERVAL_DAY*intervalD,alarmPendingIntent)
        val sharedPref=context.getSharedPreferences("com.example.rainbowcalendar_notif",Context.MODE_PRIVATE)
        with(sharedPref.edit()){
            putLong("com.example.rainbowcalendar_notif",calendar.time.time)
            apply()
        }
    }

}