package com.example.rainbowcalendar

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import java.util.Calendar

class AlarmReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent){
        Log.v("rainbowcalendartime","reached receiver")

        val title=intent.getStringExtra("title")?:"no"
        val text=intent.getStringExtra("text")?:"nope"
        val icon=intent.getIntExtra("icon",R.drawable.flag_br)

        val notification=NotificationCompat.Builder(context,"HRT")
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(icon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()
        showPushNotification(context,notification)

        val sharedPrefs=context.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
        if(title=="hrt"){ //todo: or stealth thing
            Log.i("rainbowcalendartime_notificationWasAt",System.currentTimeMillis().toString())
            sharedPrefs.edit().putLong(Constants.key_lastTNotification,System.currentTimeMillis()).apply()
        }




    }
    private fun showPushNotification(context:Context,notification:Notification){
        val notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify((System.currentTimeMillis()/1000).toInt(), notification)
    }
}

class BootReceiver:BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent){
        if(intent.action=="android.intent.action.BOOT_COMPLETED"){
            val alarm=Alarm(context)
            val sharedPrefs=context.getSharedPreferences(Constants.key_package, Context.MODE_PRIVATE)
            val tNotificationsOn=sharedPrefs.getBoolean(Constants.key_tRemindersOn,false)
            val periodNotificationsOn=sharedPrefs.getBoolean(Constants.key_tRemindersOn,false)
            val birthControlNotificationsOn=sharedPrefs.getBoolean(Constants.key_tRemindersOn,false)

            if(tNotificationsOn){
                val lastNotif=sharedPrefs.getLong(Constants.key_lastTNotification,0L)
                val reminderI=sharedPrefs.getInt(Constants.key_currentTestosteroneInterval,1)

                val time=Calendar.getInstance().apply{timeInMillis=lastNotif}
                val days=((Calendar.getInstance().timeInMillis-time.timeInMillis)/(1000*60*60*24)).toInt()
                if(time.timeInMillis<System.currentTimeMillis())
                    alarm.schedulePushNotifications(time.get(Calendar.HOUR_OF_DAY),time.get(Calendar.MINUTE),reminderI,reminderI-days,Constants.key_lastTNotification)

            }
            if(periodNotificationsOn){
                //todo period
            }
            if(birthControlNotificationsOn){
                //todo birth control
            }
        }
    }
}

