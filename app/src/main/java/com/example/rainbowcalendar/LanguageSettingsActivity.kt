package com.example.rainbowcalendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import java.util.Locale

class LanguageSettingsActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_settings)

        val langArray by lazy { resources.getStringArray(R.array.lang_array) }
        val spinner = findViewById<Spinner>(R.id.lang_spinner)
        ArrayAdapter.createFromResource(this,R.array.lang_array,android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                    val lang: String = parent?.getItemAtPosition(position).toString()
                    changeLanguage(langToCode(lang))
                    Toast.makeText(this@LanguageSettingsActivity, langToCode(lang).toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //("Not yet implemented")
                }
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
    private fun langToCode(lang: String): String{
        return when(lang){
            "English" -> "en"
            "Polski" -> "pl"
            else ->{
                "en"
            }
        }
    }
}