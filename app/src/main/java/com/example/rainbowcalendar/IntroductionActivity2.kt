package com.example.rainbowcalendar
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.util.Calendar
import kotlin.math.log

class IntroductionActivity2 : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction2)

        //val dpBirthday=findViewById<DatePicker>(R.id.datePickerBirthday)
        //val birthday=dpBirthday.dayOfMonth.toString()+"/"+dpBirthday.month.toString()+"/"+dpBirthday.year
        //Toast.makeText(this@IntroductionActivity2,birthday, Toast.LENGTH_SHORT).show()

        val ageSpinner = findViewById<Spinner>(R.id.ageSpinner)
        val ageLayout=findViewById<LinearLayout>(R.id.ageGroup)
        ArrayAdapter.createFromResource(this,R.array.age_array,android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                ageSpinner.adapter = adapter
            }

        var ageValue=""
        ageSpinner.onItemSelectedListener = object :
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
        tDatePickerYear.maxValue=year+5
        fixAvailableMonths(tDatePickerYear,tDatePickerMonth)

        tDatePickerYear.setOnValueChangedListener{ _, _, _ ->
            fixAvailableMonths(tDatePickerYear,tDatePickerMonth)
        }
        //Toast.makeText(this@IntroductionActivity2, year, Toast.LENGTH_LONG).show()

        val m1Young=findViewById<RadioButton>(R.id.m1_young)
        val m2Gay=findViewById<RadioButton>(R.id.m2_gay)
        val m3Ftm=findViewById<RadioButton>(R.id.m3_ftm)
        val m4FtmT=findViewById<RadioButton>(R.id.m4_ftm_t)
        val m5Gnc=findViewById<RadioButton>(R.id.m5_gnc)
        val m6Nb=findViewById<RadioButton>(R.id.m6_nb)
        val m7Ace=findViewById<RadioButton>(R.id.m7_ace)

        val sharedPrefGender = applicationContext.getSharedPreferences("com.example.rainbowcalendar_gender", Context.MODE_PRIVATE) ?: return
        val gender: String? =sharedPrefGender.getString("com.example.rainbowcalendar_gender","")
        //Toast.makeText(this@IntroductionActivity2, "g$gender", Toast.LENGTH_SHORT).show()
        when (gender) {
            "f" -> {
                m1Young.visibility= View.VISIBLE
                m2Gay.visibility=View.VISIBLE
                m5Gnc.visibility=View.VISIBLE
                m6Nb.visibility=View.VISIBLE
                m7Ace.visibility=View.VISIBLE
            }
            "m" -> {
                m1Young.visibility= View.VISIBLE
                m2Gay.visibility=View.VISIBLE
                m3Ftm.visibility=View.VISIBLE
                m4FtmT.visibility=View.VISIBLE
                m7Ace.visibility=View.VISIBLE
            }
            "n" -> {
                m1Young.visibility= View.VISIBLE
                m2Gay.visibility=View.VISIBLE
                m3Ftm.visibility=View.VISIBLE
                m4FtmT.visibility=View.VISIBLE
                m5Gnc.visibility=View.VISIBLE
                m6Nb.visibility=View.VISIBLE
                m7Ace.visibility=View.VISIBLE
            }
        }

        var mode=0
        val button=findViewById<Button>(R.id.buttonNext)
        val button1=findViewById<Button>(R.id.buttonNext1)
        val layoutSex=findViewById<LinearLayout>(R.id.layoutSex)
        val introHeader=findViewById<TextView>(R.id.intro2_header)
        val sharedPrefPossTDate=applicationContext.getSharedPreferences("com.example.rainbowcalendar_posstday", Context.MODE_PRIVATE)
        val startDatePicker=findViewById<LinearLayout>(R.id.startDatePicker)
        val layoutBirthday=findViewById<LinearLayout>(R.id.layoutBirthday)
        val dpBirthday=findViewById<DatePicker>(R.id.datePickerBirthday)
        val tStartDateLayout=findViewById<LinearLayout>(R.id.tStartDateLayout)
        val tStartDate=findViewById<DatePicker>(R.id.tStartDate)

        val sharedPrefT = applicationContext.getSharedPreferences("com.example.rainbowcalendar_tday", Context.MODE_PRIVATE)
        val sharedPrefBirthday = applicationContext.getSharedPreferences("com.example.rainbowcalendar_birthday", Context.MODE_PRIVATE)
        dpBirthday.maxDate=System.currentTimeMillis()

        val sharedPrefAge = applicationContext.getSharedPreferences("com.example.rainbowcalendar_minor", Context.MODE_PRIVATE)
        var age: Int =sharedPrefAge.getInt("com.example.rainbowcalendar_minor",0)
        val sharedPrefSex = applicationContext.getSharedPreferences("com.example.rainbowcalendar_sex", Context.MODE_PRIVATE)
        var sex: Int =sharedPrefSex.getInt("com.example.rainbowcalendar_sex",0)
        val sharedPrefTestosterone = applicationContext.getSharedPreferences("com.example.rainbowcalendar_testosterone", Context.MODE_PRIVATE)
        //var testosterone: Int? =sharedPrefTestosterone.getInt("com.example.rainbowcalendar_testosterone",0)
        val sharedPrefFertile= applicationContext.getSharedPreferences("com.example.rainbowcalendar_fert", Context.MODE_PRIVATE)
        //var fertile=sharedPrefFertile.getInt("com.example.rainbowcalendar_fert", 0)
        age=ageToCode(ageValue)
        //doc
        // ALWAYS:  0: none
        // AGE (_minor) 1: 16- 2: 16-17 3: 18+
        // SEX (_sex) 1: yes, 2: no, 3: ask
        // T (_testosterone) 1: yes 2: no 3: ask about start
        // FERTILE (_fert) 1: yes
        // birthday: dd-mm-yyyy
        // ACTUAL T START (_tday) dd-mm-yyyy

        with (sharedPrefAge.edit()) {
            putInt("com.example.rainbowcalendar_gender", ageToCode(ageValue))
            apply()
        }
        //doc: BUTTON 1
        button.setOnClickListener{
            if(m1Young.isChecked)
                mode=1
            else if(m2Gay.isChecked)
                mode=2
            else if(m3Ftm.isChecked)
                mode=3
            else if(m4FtmT.isChecked)
                mode=4
            else if(m5Gnc.isChecked)
                mode=5
            else if(m6Nb.isChecked)
                mode=6
            else if(m7Ace.isChecked)
                mode=7

            when(mode){
                1->{
                    sex=2
                    with (sharedPrefSex.edit()) {
                        putInt("com.example.rainbowcalendar_sex", 2)
                        apply()
                    }
                    with (sharedPrefTestosterone.edit()) {
                        putInt("com.example.rainbowcalendar_testosterone", 2)
                        apply()
                    }
                    with (sharedPrefFertile.edit()) {
                        putInt("com.example.rainbowcalendar_fert", 0)
                        apply()
                    }
                }
                2->{
                    sex=2
                    with (sharedPrefSex.edit()) {
                        putInt("com.example.rainbowcalendar_fert", 2)
                        apply()
                    }
                    with (sharedPrefTestosterone.edit()) {
                        putInt("com.example.rainbowcalendar_testosterone", 2)
                        apply()
                    }
                    with (sharedPrefFertile.edit()) {
                        putInt("com.example.rainbowcalendar_fert", 0)
                        apply()
                    }
                }
                3->{
                    //TODO: SEE HOW IT WORKS WITHOUT EXPLICITLY SETTINGS SEX VALUE, SAME WITH FERT AND TESTOSTERONE
                    sex=3
                    with (sharedPrefSex.edit()) {
                        putInt("com.example.rainbowcalendar_sex", 3)
                        apply()
                    }
                    with (sharedPrefTestosterone.edit()) {
                        putInt("com.example.rainbowcalendar_testosterone", 3)
                        apply()
                    }
                    with (sharedPrefFertile.edit()) {
                        putInt("com.example.rainbowcalendar_fert", 1)
                        apply()
                    }
                }
                4->{
                    sex=3
                    with (sharedPrefSex.edit()) {
                        putInt("com.example.rainbowcalendar_sex", 3)
                        apply()
                    }
                    with (sharedPrefTestosterone.edit()) {
                        putInt("com.example.rainbowcalendar_testosterone", 1)
                        apply()
                    }
                    with (sharedPrefFertile.edit()) {
                        putInt("com.example.rainbowcalendar_fert", 1)
                        apply()
                    }
                }
                5->{
                    sex=3
                    with (sharedPrefSex.edit()) {
                        putInt("com.example.rainbowcalendar_sex", 3)
                        apply()
                    }
                    with (sharedPrefTestosterone.edit()) {
                        putInt("com.example.rainbowcalendar_testosterone", 2)
                        apply()
                    }
                    with (sharedPrefFertile.edit()) {
                        putInt("com.example.rainbowcalendar_fert", 1)
                        apply()
                    }
                }
                6->{
                    sex=3
                    with (sharedPrefSex.edit()) {
                        putInt("com.example.rainbowcalendar_sex", 3)
                        apply()
                    }
                    with (sharedPrefTestosterone.edit()) {
                        putInt("com.example.rainbowcalendar_testosterone", 2)
                        apply()
                    }
                    with (sharedPrefFertile.edit()) {
                        putInt("com.example.rainbowcalendar_fert", 1)
                        apply()
                    }
                }
                7->{
                    sex=2
                    with (sharedPrefSex.edit()) {
                        putInt("com.example.rainbowcalendar_sex", 2)
                        apply()
                    }
                    with (sharedPrefTestosterone.edit()) {
                        putInt("com.example.rainbowcalendar_testosterone", 2)
                        apply()
                    }
                    with (sharedPrefFertile.edit()) {
                        putInt("com.example.rainbowcalendar_fert", 0)
                        apply()
                    }
                }
            }


            introHeader.text= getString(R.string.additional_options)
            m1Young.visibility= View.GONE
            m2Gay.visibility=View.GONE
            m3Ftm.visibility=View.GONE
            m4FtmT.visibility=View.GONE
            m5Gnc.visibility=View.GONE
            m6Nb.visibility=View.GONE
            m7Ace.visibility=View.GONE
            button.visibility=View.GONE
            button1.visibility=View.VISIBLE
            val age1=ageToCode(ageValue)
            if(sex==3&&(age1==2||age1==3))
                layoutSex.visibility=View.VISIBLE
            Toast.makeText(this@IntroductionActivity2, ageToCode(ageValue).toString(), Toast.LENGTH_SHORT).show()
            if(m3Ftm.isChecked) startDatePicker.visibility=View.VISIBLE
            ageLayout.visibility=View.GONE
            layoutBirthday.visibility=View.VISIBLE
            //todo: IF BIRTHDAY IS TODAY, DON'T PUT IT INTO SETTINGS
            if(m4FtmT.isChecked){
                tStartDateLayout.visibility=View.VISIBLE
            }
        }
        //doc BUTTON2
        button1.setOnClickListener{
            //doc: possible t start, show on main screen when it's close
            if(m3Ftm.isChecked){
                val stringDate: String=tDatePickerYear.value.toString()+"-"+tDatePickerMonth.value.toString().padStart(2,'0')+"-01"
                with (sharedPrefPossTDate.edit()) {
                    putString("com.example.rainbowcalendar_posstday", stringDate)
                    apply()
                }
            }
            //doc: actual t day if started T, remind of anniversary and ask about last shot and shot frequency later
            if(m4FtmT.isChecked){
                val tDate:String=tStartDate.dayOfMonth.toString()+"/"+tStartDate.month.toString()+"/"+tStartDate.year
                with(sharedPrefT.edit()){
                    putString("com.example.rainbowcalendar_tday", tDate)
                }
            }
            //doc: show too young if younger than 10
            val errorText=findViewById<TextView>(R.id.errorText)
            if(year-dpBirthday.year in 1..9)
                errorText.text=getString(R.string.too_young)
            else
                errorText.text=""
            //doc: birthday dd-mm-yyyy
            val birthday: String=dpBirthday.dayOfMonth.toString()+"/"+dpBirthday.month.toString()+"/"+dpBirthday.year
            //Toast.makeText(this@IntroductionActivity2,birthday, Toast.LENGTH_SHORT).show()
            with (sharedPrefBirthday.edit()) {
                putString("com.example.rainbowcalendar_birthday", birthday)
                apply()
            }
            //doc: if 16-17 or 18+ and it's unknown if sexually active or not, hide sex options for inactive, show the tick for active somewhere close
            val age1=ageToCode(ageValue)
            if(sex==3&&(age1==2||age1==3)){
                val sexRbNo=findViewById<RadioButton>(R.id.sexNo)
                val sexRbYes=findViewById<RadioButton>(R.id.sexYes)
                if(sexRbNo.isChecked){
                    with (sharedPrefSex.edit()) {
                        putInt("com.example.rainbowcalendar_sex", 2)
                        apply()
                    }
                }
                if(sexRbYes.isChecked){
                    with (sharedPrefSex.edit()) {
                        putInt("com.example.rainbowcalendar_sex", 1)
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
    private fun fixAvailableMonths(tDatePickerYear:NumberPicker, tDatePickerMonth:NumberPicker){
        val year=Calendar.getInstance().get(Calendar.YEAR)
        if(tDatePickerYear.value==year){
            var month=Calendar.getInstance().get(Calendar.MONTH)
            //WATCH IT!!! MONTHS ARE 0-11, 12 IS UNDECIMBER
            month+=1

            if(month<12)
                tDatePickerMonth.minValue=month+1
            else if(month==12){
                tDatePickerMonth.minValue=1
                tDatePickerYear.minValue=year+1
            }

        }
    }
}