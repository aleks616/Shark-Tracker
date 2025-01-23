package com.example.rainbowcalendar

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.LocaleList
import android.os.Looper
import android.widget.TextView
import java.util.Locale

class SplashScreenActivity:AppCompatActivity(){

    override fun attachBaseContext(newBase:Context){
        //val sharedPreferences=newBase.getSharedPreferences(newBase.packageName,MODE_PRIVATE)
        val sharedPrefs=newBase.getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)
        var locale=Locale(sharedPrefs.getString("lang","en")!!)
        if(locale.toString()=="pt-br")
            locale=Locale("pt","BR")
        Locale.setDefault(locale)
        val context=languageChange(newBase,locale)
        super.attachBaseContext(context)
    }
    private fun languageChange (context: Context, locale: Locale): Context {
        var tempContext=context
        val res=tempContext.resources
        val configuration=res.configuration
        configuration.setLocale(locale)
        val localList=LocaleList(locale)
        LocaleList.setDefault(localList)
        configuration.setLocales(localList)
        tempContext=tempContext.createConfigurationContext(configuration)
        return tempContext
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val welcomeTextView=findViewById<TextView>(R.id.welcome)
        val sharedPrefs=applicationContext.getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)
        val setupDone=sharedPrefs.getBoolean("setup",false)
        val welcomeText: String=getString(R.string.welcome_text)
        val welcomeBackText: String=getString(R.string.welcome_back)
        val delay: Long

        if (!setupDone){
            createNotificationChannel(this)
            welcomeTextView.text=welcomeText
            delay=2000
            val intent=Intent(this, LanguageSettingsActivity::class.java)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(intent)
                finish()
            }, delay)
        }
        else {
            if(!sharedPrefs.getString("passwordValue","").isNullOrEmpty()){ //there is password
                startActivity(Intent(this,PasswordActivity::class.java))
            }
            welcomeTextView.text=welcomeBackText
            // rest of the Not-FirstTime Logic here
            delay=0
            val intent=Intent(this, MainActivity::class.java)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(intent)
                finish()
            }, delay)
        }
    }
    private fun createNotificationChannel(context: Context){
        val channel=NotificationChannel("hrt","Hrt reminders", NotificationManager.IMPORTANCE_HIGH).apply{
            description="This channel is for hrt reminders"
        }

        val notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}