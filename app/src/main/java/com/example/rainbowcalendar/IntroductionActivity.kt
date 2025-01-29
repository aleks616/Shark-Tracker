package com.example.rainbowcalendar

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView

class IntroductionActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        val sharedPrefs=applicationContext.getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)
        val theme=sharedPrefs.getString("theme","Light")
        ThemeManager[this]=theme
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)

        //themes
        val spinner=findViewById<Spinner>(R.id.themeSpinner)
        ArrayAdapter.createFromResource(this,R.array.themes_array,R.layout.spinner_item)
            .also{adapter->
                adapter.setDropDownViewResource(R.layout.simple_text)
                spinner.adapter=adapter
            }
        var themeValue="Dark"
        spinner.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>?,view:View?,position:Int,id:Long){
                themeValue=parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent:AdapterView<*>?){}
        }


        // gender:String m/f/n
        // tm:Boolean (is transmed)
        // name:String

        var gender: String?=sharedPrefs.getString("gender","n")
        var tM=false
        //var name: String?=sharedPrefName.getString("com.example.rainbowcalendar_name", "")
        val genderM=findViewById<RadioButton>(R.id.genderMale)
        val genderF=findViewById<RadioButton>(R.id.genderFemale)
        val genderN=findViewById<RadioButton>(R.id.genderNeutral)

        val nameText=findViewById<TextView>(R.id.nameET)
        nameText.setOnEditorActionListener{v,actionId,_ ->
            if(actionId==EditorInfo.IME_ACTION_DONE){
                if(v.text.contains("tRvEt",ignoreCase=false)){
                    tM=true
                    genderM.text=getString(R.string.gender_mode_m)
                }
                else{
                    genderM.text=getString(R.string.gender_mode_tm)
                }
                true
            }
            else
                false
        }

        val button=findViewById<Button>(R.id.buttonNext)
        button.setOnClickListener{
            sharedPrefs.edit().putString("theme",themeValue).apply()
            val errorText=findViewById<TextView>(R.id.errorText)
            //val nameText=findViewById<TextView>(R.id.nameET)
            var name=nameText.text.toString()
            if(name.isEmpty()){
                errorText.text=getString(R.string.fill_name)
            }
            else{
                if(name.contains("tRvEt",ignoreCase=false)){
                    name=name.replace("tRvEt","")
                }
                sharedPrefs.edit().putBoolean("tm",tM).apply()
                sharedPrefs.edit().putString("name",name).apply()
                errorText.text=""
            }

            if(genderM.isChecked) gender="m"
            else if (genderF.isChecked) gender="f"
            else if (genderN.isChecked) gender="n"
            else errorText.text=getString(R.string.select_one_option)
            if(gender!=null){
                sharedPrefs.edit().putString("gender",gender).apply()
            }
            if(name.isNotEmpty()&&(!gender.isNullOrEmpty())){
                errorText.text=""
                startActivity(Intent(this, IntroductionActivity2::class.java))
            }

        }


    }
}

