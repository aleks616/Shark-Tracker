package com.example.rainbowcalendar
/*

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView

class RecoveryActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPrefs=applicationContext.getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)
        val theme=sharedPrefs.getString("theme","Light")
        ThemeManager[this]=theme
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recovery)


        val recoverySetupLayout=findViewById<LinearLayout>(R.id.recoverySetupL)

        val recoverySpinner1=findViewById<Spinner>(R.id.recoverySpinner1)
        val recoverySpinner2=findViewById<Spinner>(R.id.recoverySpinner2)
        val recoverySpinner3=findViewById<Spinner>(R.id.recoverySpinner3)
        val recoverySetupEditText1=findViewById<EditText>(R.id.recoverySetupET1)
        val recoverySetupEditText2=findViewById<EditText>(R.id.recoverySetupET2)
        val recoverySetupEditText3=findViewById<EditText>(R.id.recoverySetupET3)

        val recoverySetupButton=findViewById<Button>(R.id.recoverySetupButton)


        val recoveryLayout=findViewById<LinearLayout>(R.id.recoveryL)

        val recoveryQuestion1=findViewById<TextView>(R.id.recoveryQuestion1)
        val recoveryQuestion2=findViewById<TextView>(R.id.recoveryQuestion2)
        val recoveryQuestion3=findViewById<TextView>(R.id.recoveryQuestion3)
        val recoveryEditText1=findViewById<EditText>(R.id.recoveryET1)
        val recoveryEditText2=findViewById<EditText>(R.id.recoveryET2)
        val recoveryEditText3=findViewById<EditText>(R.id.recoveryET3)

        val recoveryButton=findViewById<Button>(R.id.recoveryButton)

        val errorText=findViewById<TextView>(R.id.errorText)
        //todo: wrong recovery question answers system, attempts!

        val sharedPrefRecovery=applicationContext.getSharedPreferences("com.example.rainbowcalendar_recovery", Context.MODE_PRIVATE)
        val setupDone=sharedPrefRecovery.getBoolean("done",false)

        if(!setupDone){
            recoverySetupLayout.visibility=View.VISIBLE
            recoveryLayout.visibility=View.GONE
        }
        else{
            recoverySetupLayout.visibility=View.GONE
            recoveryLayout.visibility=View.VISIBLE

            recoveryQuestion1.text=sharedPrefRecovery.getString("question1","")
            recoveryQuestion2.text=sharedPrefRecovery.getString("question2","")
            recoveryQuestion3.text=sharedPrefRecovery.getString("question3","")

        }

        //region creation
        ArrayAdapter.createFromResource(this,R.array.recoveryQuestions,R.layout.spinner_item)
            .also{adapter->
                adapter.setDropDownViewResource(R.layout.simple_text)
                recoverySpinner1.adapter=adapter
                recoverySpinner2.adapter=adapter
                recoverySpinner3.adapter=adapter
            }
        var recoveryValue1=""
        var recoveryValue2=""
        var recoveryValue3=""
        recoverySpinner1.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>?,view:View?,position:Int,id:Long){
                recoveryValue1=parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent:AdapterView<*>?){}
        }
        recoverySpinner2.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>?,view:View?,position:Int,id:Long){
                recoveryValue2=parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent:AdapterView<*>?){}
        }
        recoverySpinner3.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>?,view:View?,position:Int,id:Long){
                recoveryValue3=parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent:AdapterView<*>?){}
        }

        recoverySetupButton.setOnClickListener {
            if(recoveryValue1!=recoveryValue2&&recoveryValue2!=recoveryValue3){
                errorText.text=""
                val default=resources.getStringArray(R.array.recoveryQuestions).first()
                if(listOf(recoveryValue1,recoveryValue2,recoveryValue3).all{it!=default}){
                    errorText.text=""
                    if(!recoverySetupEditText1.text.isNullOrEmpty()&&!recoverySetupEditText2.text.isNullOrEmpty()&&!recoverySetupEditText3.text.isNullOrEmpty()){
                        errorText.text=""
                        with(sharedPrefRecovery.edit()){
                            putString("question1",recoveryValue1)
                            putString("question2",recoveryValue2)
                            putString("question3",recoveryValue3)
                            putString("answer1",recoverySetupEditText1.text.toString())
                            putString("answer2",recoverySetupEditText2.text.toString())
                            putString("answer3",recoverySetupEditText3.text.toString())
                            putBoolean("done",true)
                            apply()
                        }
                        Log.v("g-q","everything ok")
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    else errorText.text= getString(R.string.recovery_answer_all)
                }
                else errorText.text= getString(R.string.recovery_choose_all)
            }
            else{
                errorText.text= getString(R.string.recovery_questions_repeat)
                Log.v("g_q1",recoveryValue1)
                Log.e("g_q2",recoveryValue2)
                Log.w("g_q3",recoveryValue3)
            }
        }
        //endregion

        recoveryButton.setOnClickListener {
            if(!recoveryEditText1.text.isNullOrEmpty()&&!recoveryEditText2.text.isNullOrEmpty()&&!recoveryEditText3.text.isNullOrEmpty()){
                errorText.text=""
                val correctAnswer1=simplify(sharedPrefRecovery.getString("answer1",""))
                val correctAnswer2=simplify(sharedPrefRecovery.getString("answer2",""))
                val correctAnswer3=simplify( sharedPrefRecovery.getString("answer3",""))

                if(simplify(recoveryEditText1.text.toString())==correctAnswer1){
                    errorText.text=""
                    if(simplify(recoveryEditText2.text.toString())==correctAnswer2){
                        errorText.text=""
                        if(simplify(recoveryEditText3.text.toString())==correctAnswer3){
                            errorText.text=""
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                        else errorText.text=getString(R.string.recovery_wrong_answer)+3
                    }
                    else{
                        errorText.text=getString(R.string.recovery_wrong_answer)+2
                        Log.e("g-q4","Answer: "+simplify(recoveryEditText2.text.toString())+" Correct answer: "+correctAnswer2)
                    }
                }
                else errorText.text=getString(R.string.recovery_wrong_answer)+1
            }
            else errorText.text= getString(R.string.recovery_answer_empty)
        }

    }
    fun simplify(string: String?):String?{
        return string?.lowercase()?.replace(" ","")
    }

}*/
