package com.example.rainbowcalendar

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

class IntroductionActivity2 : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction2)

        /* todo: get birthday
        val dpBirthday=findViewById<DatePicker>(R.id.datePickerBirthday)
        val sdf = SimpleDateFormat("dd/M/yyyy", Locale.US)
        val currentDate = sdf.format(Date())*/

        val spinner = findViewById<Spinner>(R.id.ageSpinner)
        ArrayAdapter.createFromResource(this,R.array.age_array,android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }

        var ageValue:String=""
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                ageValue = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //("Not yet implemented")
            }
        }

        val tDatePickerMonth=findViewById<NumberPicker>(R.id.TDatePickerMonth)
        tDatePickerMonth.minValue=1
        tDatePickerMonth.maxValue=12
        val tDatePickerYear=findViewById<NumberPicker>(R.id.TDatePickerYear)
        val year=Calendar.getInstance().get(Calendar.YEAR)
        tDatePickerYear.minValue=year
        tDatePickerYear.maxValue=year+4
        //Toast.makeText(this@IntroductionActivity2, year, Toast.LENGTH_LONG).show()

        val m1_young=findViewById<RadioButton>(R.id.m1_young)
        val m2_gay=findViewById<RadioButton>(R.id.m2_gay)
        val m3_ftm=findViewById<RadioButton>(R.id.m3_ftm)
        val m4_ftm_t=findViewById<RadioButton>(R.id.m4_ftm_t)
        val m5_gnc=findViewById<RadioButton>(R.id.m5_gnc)
        val m6_nb=findViewById<RadioButton>(R.id.m6_nb)
        val m7_ace=findViewById<RadioButton>(R.id.m7_ace)

        val sharedPrefGender = applicationContext.getSharedPreferences("com.example.rainbowcalendar_gender", Context.MODE_PRIVATE) ?: return
        val gender: String? =sharedPrefGender.getString("com.example.rainbowcalendar_gender","")
        //Toast.makeText(this@IntroductionActivity2, "g$gender", Toast.LENGTH_SHORT).show()
        when (gender) {
            "f" -> {
                m1_young.visibility= View.VISIBLE
                m2_gay.visibility=View.VISIBLE
                m5_gnc.visibility=View.VISIBLE
                m6_nb.visibility=View.VISIBLE
                m7_ace.visibility=View.VISIBLE
            }
            "m" -> {
                m1_young.visibility= View.VISIBLE
                m2_gay.visibility=View.VISIBLE
                m3_ftm.visibility=View.VISIBLE
                m4_ftm_t.visibility=View.VISIBLE
                m7_ace.visibility=View.VISIBLE
            }
            "n" -> {
                m1_young.visibility= View.VISIBLE
                m2_gay.visibility=View.VISIBLE
                m3_ftm.visibility=View.VISIBLE
                m4_ftm_t.visibility=View.VISIBLE
                m5_gnc.visibility=View.VISIBLE
                m6_nb.visibility=View.VISIBLE
                m7_ace.visibility=View.VISIBLE
            }
        }

        var mode:Int=0
        val button=findViewById<Button>(R.id.buttonNext)
        button.setOnClickListener{
                if(m1_young.isChecked)
                    mode=1
                else if(m2_gay.isChecked)
                    mode=2
                else if(m3_ftm.isChecked)
                    mode=3
                else if(m4_ftm_t.isChecked)
                    mode=4
                else if(m5_gnc.isChecked)
                    mode=5
                else if(m6_nb.isChecked)
                    mode=6
                else if(m7_ace.isChecked)
                    mode=7
            val introHeader=findViewById<TextView>(R.id.intro2_header)
            introHeader.text="Additional options" //todo: add resources
            m1_young.visibility= View.GONE
            m2_gay.visibility=View.GONE
            m3_ftm.visibility=View.GONE
            m4_ftm_t.visibility=View.GONE
            m5_gnc.visibility=View.GONE
            m6_nb.visibility=View.GONE
            m7_ace.visibility=View.GONE

            //ONLY IF T OPTION
            if(m3_ftm.isChecked){
                val startDatePicker=findViewById<LinearLayout>(R.id.startDatePicker)
                startDatePicker.visibility=View.VISIBLE

                /*val stringDate=tDatePickerYear.toString()+"-"+tDatePickerMonth.toString().padStart(2,'0')+"-01"
                val startDate=LocalDate.parse(stringDate, DateTimeFormatter.ISO_LOCAL_DATE)
                val sharedPrefPossibleTDate=applicationContext.getSharedPreferences("com.example.rainbowcalendar_PossibleTDate", Context.MODE_PRIVATE)
                with (sharedPrefPossibleTDate.edit()) {
                    putString("com.example.rainbowcalendar_PossibleTDate", startDate.toString())
                    apply()
                }*/
                //Toast.makeText(this@IntroductionActivity2, startDate.toString(), Toast.LENGTH_SHORT).show()
                //TODO: IMPORTANT!!! HIDE THE NEXT BUTTON AND SHOW ANOTHER NEXT BUTTON!!!!
            }
        }
        val sharedPrefAge = applicationContext.getSharedPreferences("com.example.rainbowcalendar_minor", Context.MODE_PRIVATE)
        var age: Int? =sharedPrefAge.getInt("com.example.rainbowcalendar_minor",0)
        val sharedPrefSex = applicationContext.getSharedPreferences("com.example.rainbowcalendar_sex", Context.MODE_PRIVATE)
        var sex: Int? =sharedPrefSex.getInt("com.example.rainbowcalendar_sex",0)
        val sharedPrefTestosterone = applicationContext.getSharedPreferences("com.example.rainbowcalendar_testosterone", Context.MODE_PRIVATE)
        var testosterone: Int? =sharedPrefTestosterone.getInt("com.example.rainbowcalendar_testosterone",0)

        //ALWAYS:  0: none
        //AGE 1: 16- 2: 16-17 3: 18+
        //SEX 1: yes, 2: no, 3: ask
        //T 1: yes 2: no 3: ask about start
        with (sharedPrefAge.edit()) {
            putInt("com.example.rainbowcalendar_gender", ageToCode(ageValue))
            apply()

        when(mode){
            1->{
                with (sharedPrefSex.edit()) {
                    putInt("com.example.rainbowcalendar_sex", 2)
                    apply()
                }
                with (sharedPrefTestosterone.edit()) {
                    putInt("com.example.rainbowcalendar_sex", 2)
                    apply()
                }

            }
            2->{
                with (sharedPrefSex.edit()) {
                    putInt("com.example.rainbowcalendar_sex", 2)
                    apply()
                }
                with (sharedPrefTestosterone.edit()) {
                    putInt("com.example.rainbowcalendar_sex", 2)
                    apply()
                }
            }
            3->{
                with (sharedPrefSex.edit()) {
                    putInt("com.example.rainbowcalendar_sex", 3)
                    apply()
                }
                with (sharedPrefTestosterone.edit()) {
                    putInt("com.example.rainbowcalendar_sex", 3)
                    //TODO: add the "are you close to starting it?, choose approx month" and if so, display on main screen
                    apply()
                }
            }
            4->{
                with (sharedPrefSex.edit()) {
                    putInt("com.example.rainbowcalendar_sex", 3)
                    apply()
                }
                with (sharedPrefTestosterone.edit()) {
                    putInt("com.example.rainbowcalendar_sex", 1)
                    apply()
                }
            }
            5->{
                with (sharedPrefSex.edit()) {
                    putInt("com.example.rainbowcalendar_sex", 3)
                    apply()
                }
                with (sharedPrefTestosterone.edit()) {
                    putInt("com.example.rainbowcalendar_sex", 2)
                    apply()
                }
            }
            6->{
                with (sharedPrefSex.edit()) {
                    putInt("com.example.rainbowcalendar_sex", 3)
                    apply()
                }
                with (sharedPrefTestosterone.edit()) {
                    putInt("com.example.rainbowcalendar_sex", 2)
                    apply()
                }
            }
            7->{
                with (sharedPrefSex.edit()) {
                    putInt("com.example.rainbowcalendar_sex", 2)
                    apply()
                }
                with (sharedPrefTestosterone.edit()) {
                    putInt("com.example.rainbowcalendar_sex", 2)
                    apply()
                }
            }
        }
    }
}


    private fun ageToCode(age: String): Int{
        return when(age){
            "under 16" -> 1
            "17-18" -> 2
            "over 18" -> 3
            else -> 0
        }
    }
}