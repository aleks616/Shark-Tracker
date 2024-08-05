package com.example.rainbowcalendar

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.example.rainbowcalendar.Alarm
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("here","notification should work")
        showPushNotification(context)
        val alarm=Alarm(context)
    }
    private fun showPushNotification(context: Context){
        Log.d("notification","should work")
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, "hrt")
            .setContentTitle("Reminder")
            .setContentText("It's T time innit")
            .setSmallIcon(R.drawable.icon_vaccine)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1, notification)
    }
}

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val alarm=Alarm(context)

            val sharedPrefNotif=context.getSharedPreferences("com.example.rainbowcalendar_notif",Context.MODE_PRIVATE)
            val lastNotif=sharedPrefNotif.getLong("com.example.rainbowcalendar_notif",0L)
            val sharedPrefTReminderI=context.getSharedPreferences("com.example.rainbowcalendar_TReminderI", Context.MODE_PRIVATE)
            val reminderI=sharedPrefTReminderI.getInt("com.example.rainbowcalendar_TReminderI",1)

            val time=Calendar.getInstance().apply{timeInMillis=lastNotif}
            val days=((Calendar.getInstance().timeInMillis - time.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()
            if(time.timeInMillis<System.currentTimeMillis())
                alarm.schedulePushNotifications(time.get(Calendar.HOUR_OF_DAY),time.get(Calendar.MINUTE),reminderI,reminderI-days)

        }
    }
}

