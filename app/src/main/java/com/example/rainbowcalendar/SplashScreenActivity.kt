package com.example.rainbowcalendar

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.LocaleList
import android.os.Looper
import android.util.Log
import android.widget.TextView
import java.util.Locale

class SplashScreenActivity:AppCompatActivity(){

    override fun attachBaseContext(newBase:Context){
        val sharedPrefs=newBase.getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)
        var locale=Locale(sharedPrefs.getString("lang","en")!!)
        if(locale.toString()=="pt-br")
            locale=Locale("pt","BR")
        Locale.setDefault(locale)
        val context=languageChange(newBase,locale)
        super.attachBaseContext(context)
    }
    private fun languageChange(context: Context, locale: Locale): Context{
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
        val sharedPrefs=applicationContext.getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)
        val theme=sharedPrefs.getString("theme","Monochrome-Dark")!!
        ThemeManager[this]=theme
        Log.v("rainbowcalendar theme",theme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Thread{
            val shortcutManager=getSystemService(ShortcutManager::class.java)
            val shortcut=ShortcutInfo.Builder(this,"id_1").setShortLabel("Shark Tracker").setIcon(Icon.createWithResource(this,R.drawable.icon_shark_trans))
                .setIntent(Intent(Intent.ACTION_MAIN).setComponent(ComponentName(this, SplashScreenActivity::class.java))).build()
            shortcutManager?.dynamicShortcuts=listOf(shortcut)
        }.start()

        val welcomeTextView=findViewById<TextView>(R.id.welcome)
        val setupDone=sharedPrefs.getBoolean("setup",false)
        val welcomeText: String=getString(R.string.welcome_text)
        val welcomeBackText: String=getString(R.string.welcome_back)
        val delay: Long

        if(!setupDone){
            createNotificationChannel(this)
            welcomeTextView.text=welcomeText
            delay=2000
            val intent=Intent(this, LanguageSettingsActivity::class.java)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(intent)
                finish()
            }, delay)
        }
        else{
            if(!sharedPrefs.getString("passwordValue","").isNullOrEmpty()){ //there is password
                startActivity(Intent(this,PasswordActivity::class.java))
            }
            welcomeTextView.text=welcomeBackText
            // rest of the Not-FirstTime Logic here
            delay=500
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