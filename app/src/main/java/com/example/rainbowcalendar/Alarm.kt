package com.example.rainbowcalendar
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import java.util.Calendar
import java.util.GregorianCalendar
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

class Alarm(private val context: Context){
    private val alarmManager=context.getSystemService(ALARM_SERVICE) as AlarmManager
    //private var alarmPendingIntent: PendingIntent?=null

    private val sharedPrefs: SharedPreferences=context.getSharedPreferences(Constants.key_package, Context.MODE_PRIVATE)
    /*
    fun schedulePushNotifications(hour:Int,minute:Int,intervalD:Int,startFrom:Int){
        val calendar=GregorianCalendar.getInstance().apply{
            set(Calendar.HOUR_OF_DAY,hour)
            set(Calendar.MINUTE,minute)
            set(Calendar.SECOND,0)
            set(Calendar.MILLISECOND,0)

            val lastNotif=sharedPrefs.getLong("lastNotif",0L)

            if(lastNotif>0) timeInMillis=lastNotif+intervalD*AlarmManager.INTERVAL_DAY
            else add(Calendar.DAY_OF_MONTH,startFrom)

            if(time<Calendar.getInstance().time) add(Calendar.DAY_OF_MONTH,intervalD)
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.time.time,AlarmManager.INTERVAL_DAY*intervalD,alarmPendingIntent)
        sharedPrefs.edit().putLong("lastNotif",calendar.time.time).apply()
    }*/

    @SuppressLint("ScheduleExactAlarm")
    fun schedulePushNotifications(hour:Int,minute:Int,intervalDays:Int,startFrom:Int,key:String){

        val calendar=GregorianCalendar.getInstance().apply{
            set(Calendar.HOUR_OF_DAY,hour)
            set(Calendar.MINUTE,minute)
            set(Calendar.SECOND,0)
            set(Calendar.MILLISECOND,0)

            val lastNotificationTime=sharedPrefs.getLong(key,0L)

            if(lastNotificationTime>0) timeInMillis=lastNotificationTime+intervalDays*AlarmManager.INTERVAL_DAY
            else add(Calendar.DAY_OF_MONTH,startFrom)

            if(time<Calendar.getInstance().time) add(Calendar.DAY_OF_MONTH,intervalDays)
        }
        Log.d("rainbowcalendartime",(calendar.time.time).toString())

        if(key==Constants.key_lastTNotification){
            val notification=NotificationCompat.Builder(context, "HRT")
                .setContentTitle("hrt")
                .setContentText(context.getString(R.string.tReminderText))
                .setSmallIcon(R.drawable.icon_vaccine)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

         /*   val intent=Intent(context,AlarmReceiver::class.java).apply{
                putExtra("notification",notification)
            }*/
            val intent=Intent(context,AlarmReceiver::class.java).apply{
                action="com.example.ALARM_TRIGGERED"
                putExtra("title","Reminder intent")
                putExtra("text",context.getString(R.string.tReminderText))
                putExtra("icon",R.drawable.icon_vaccine)
            }
            val alarmPendingIntent=PendingIntent.getBroadcast(context,key.hashCode(),intent,PendingIntent.FLAG_IMMUTABLE)
            /*alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis()+20000,
                alarmPendingIntent
            )*/
            Log.v("rainbowcalendartime1",(System.currentTimeMillis()+20000).toString())
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.time.time,AlarmManager.INTERVAL_DAY*intervalDays,alarmPendingIntent!!)


        }
       else if(key==Constants.key_lastPeriodNotification){
            val notification=NotificationCompat.Builder(context, "period")
                .setContentTitle("Reminder")
                .setContentText("period reminder")
                .setSmallIcon(R.drawable.blood_4)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            val intent=Intent(context,AlarmReceiver::class.java).apply{
                putExtra("notification",notification)
            }
            val alarmPendingIntent=PendingIntent.getBroadcast(context,1,intent,PendingIntent.FLAG_MUTABLE)
       }
        else{
            val notification=NotificationCompat.Builder(context, "bc")
                .setContentTitle("Reminder")
                .setContentText("birth control reminder")
                .setSmallIcon(R.drawable.alarm_icon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            val intent=Intent(context,AlarmReceiver::class.java).apply{
                putExtra("notification",notification)
            }
            val alarmPendingIntent=PendingIntent.getBroadcast(context,2,intent,PendingIntent.FLAG_MUTABLE)
        }


        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.time.time,AlarmManager.INTERVAL_DAY*intervalDays,alarmPendingIntent!!)
        //sharedPrefs.edit().putLong(key,calendar.time.time).apply()
    }

}