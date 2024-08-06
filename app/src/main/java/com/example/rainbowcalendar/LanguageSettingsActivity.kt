package com.example.rainbowcalendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.util.Locale

class LanguageSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_settings)

        val buttonPl = findViewById<Button>(R.id.polishBtn)
        buttonPl.setOnClickListener{
            changeLanguage("pl")
            toMainScreen()
        }

        val buttonEn = findViewById<Button>(R.id.engBtn)
        buttonEn.setOnClickListener{
            changeLanguage("eng")
            toMainScreen()
        }
    }
    private fun changeLanguage(lang: String){
        val config = resources.configuration
        val locale = Locale(lang)
        Locale.setDefault(locale)
        config.setLocale(locale)
        createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)

        val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("code",lang)
        editor.apply()
    }
    private fun toMainScreen(){
        val intent=Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}