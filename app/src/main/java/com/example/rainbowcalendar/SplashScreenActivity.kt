package com.example.rainbowcalendar

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import kotlinx.coroutines.runBlocking

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val welcomeTextView=findViewById<TextView>(R.id.welcome)
        val sharedpref: SharedPreferences=applicationContext.getSharedPreferences("com.example.android.your_application", MODE_PRIVATE)
        val token: String?=sharedpref.getString("token", null)
        val welcomeText: String=getString(R.string.welcome_text)
        val welcomeBackText: String=getString(R.string.welcome_back)
        var delay: Long=0
        if (token=="False"||token==null){
            //first time logic
            welcomeTextView.text=welcomeText
            sharedpref.edit().putString("token", "true").apply()
            delay=4000
        }
        else {
            welcomeTextView.text=welcomeBackText
            // rest of the Not-FirstTime Logic here
            delay=1000
        }

        //switch to next screen
        Handler(Looper.getMainLooper()).postDelayed({
            val intent=Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, delay)

    }
}