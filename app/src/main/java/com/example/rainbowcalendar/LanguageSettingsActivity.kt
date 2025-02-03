package com.example.rainbowcalendar

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import java.util.Locale

class LanguageSettingsActivity:AppCompatActivity(){

    override fun onCreate(savedInstanceState:Bundle?){
        val sharedPrefs=applicationContext.getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)
        val theme=sharedPrefs.getString("theme","Light")
        ThemeManager[this]=theme
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_settings)

        val spinner=findViewById<Spinner>(R.id.lang_spinner)
        ArrayAdapter.createFromResource(this,R.array.lang_array,R.layout.spinner_item)
            .also{adapter->
                adapter.setDropDownViewResource(R.layout.simple_text)
                spinner.adapter=adapter
            }

        val button=findViewById<Button>(R.id.buttonNext)
        button.setOnClickListener{
            startActivity(Intent(this,IntroductionActivity::class.java))
        }

        spinner.onItemSelectedListener=object:
            AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?,view:View?,position:Int,id:Long){
                    val lang:String=parent?.getItemAtPosition(position).toString()
                    changeLanguage(Utils.langToCode(lang))
                    //Toast.makeText(this@LanguageSettingsActivity,langToCode(lang),Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //("Not yet implemented")
                }
            }
    }

    private fun changeLanguage(lang: String){
        val config=resources.configuration
        val locale=Locale(lang)
        Locale.setDefault(locale)
        config.setLocale(locale)
        createConfigurationContext(config)
        resources.updateConfiguration(config,resources.displayMetrics)

        val sharedPrefs=applicationContext.getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)
        sharedPrefs.edit().putString("lang",lang).apply()
    }
   /* private fun toMainScreen(){
        val intent=Intent(this, MainActivity::class.java)
        startActivity(intent)
    }*/
}