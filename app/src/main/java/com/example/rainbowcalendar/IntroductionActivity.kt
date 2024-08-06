package com.example.rainbowcalendar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast

class IntroductionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)


        val spinner = findViewById<Spinner>(R.id.themeSpinner)
        ArrayAdapter.createFromResource(this,R.array.themes_array,android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val value: String = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //("Not yet implemented")
            }
        }
        //TODO: do themes


        val sharedpref: SharedPreferences =applicationContext.getSharedPreferences("com.example.rainbowcalendar", MODE_PRIVATE)
        val token: String?=sharedpref.getString("token", null)

        val sharedPrefGender = applicationContext.getSharedPreferences("com.example.rainbowcalendar_gender", Context.MODE_PRIVATE) ?: return
        var gender: String? =sharedPrefGender.getString("com.example.rainbowcalendar_gender","")
        val genderM=findViewById<RadioButton>(R.id.genderMale)
        val genderF=findViewById<RadioButton>(R.id.genderFemale)
        val genderN=findViewById<RadioButton>(R.id.genderNeutral)

        val button=findViewById<Button>(R.id.buttonNext)
        button.setOnClickListener{
            startActivity(Intent(this, IntroductionActivity2::class.java))

            if(genderM.isChecked) gender="m"
            else if (genderF.isChecked) gender="f"
            else if (genderN.isChecked) gender="n"

            if(gender!=null){
                val sharedPrefGender: SharedPreferences =applicationContext.getSharedPreferences("com.example.rainbowcalendar_gender", MODE_PRIVATE)
                with (sharedPrefGender.edit()) {
                    putString("com.example.rainbowcalendar_gender", gender)
                    //Toast.makeText(this@IntroductionActivity, "tried saving sharedpref", Toast.LENGTH_SHORT).show()
                    apply()
                }
                val genderRead: String? =sharedPrefGender.getString("com.example.rainbowcalendar_gender","")
                Toast.makeText(this@IntroductionActivity, "g1: $genderRead", Toast.LENGTH_SHORT).show()
            }
        }


    }
}

