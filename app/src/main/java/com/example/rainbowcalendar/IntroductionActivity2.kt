package com.example.rainbowcalendar
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.NotificationCompat
import java.util.Calendar

class IntroductionActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction2)

        //TODO:
        // 1. after button1 do button2 and whole lockscreen mechanism after SplashScreenActivity
        // 2. change age categories just into legal and minor
        // 3. if year is same as now skip year but still send birthday notifications
        // 4. extract strings
        // 5. make notifications take text from strings
        // 6. UI modes
        // 7. option to reset settings/remove data and change them
        // 8. period options menu

        //region age categories
        //doc: age spinner with age group values
        val ageSpinner=findViewById<Spinner>(R.id.ageSpinner)
        val ageLayout=findViewById<LinearLayout>(R.id.ageGroup)
        ArrayAdapter.createFromResource(this,R.array.age_array,android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                ageSpinner.adapter = adapter
            }

        var ageValue=""
        val legalAgeProof=findViewById<CheckBox>(R.id.legal_minor_consent)
        ageSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                ageValue = parent?.getItemAtPosition(position).toString()
                if(ageToCode(ageValue)==2)
                    legalAgeProof.visibility=View.VISIBLE
                else
                    legalAgeProof.visibility=View.GONE
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        //endregion

        //region possible T date picker
        val tDatePickerMonth=findViewById<NumberPicker>(R.id.TDatePickerMonth)
        tDatePickerMonth.minValue=1
        tDatePickerMonth.maxValue=12
        val tDatePickerYear=findViewById<NumberPicker>(R.id.TDatePickerYear)
        val year=Calendar.getInstance().get(Calendar.YEAR)
        val month=Calendar.getInstance().get(Calendar.YEAR)
        tDatePickerYear.minValue=year
        tDatePickerYear.maxValue=year+5
        fixAvailableMonths(tDatePickerYear,tDatePickerMonth)

        tDatePickerYear.setOnValueChangedListener{ _, _, _ ->
            fixAvailableMonths(tDatePickerYear,tDatePickerMonth)
        }
        //endregion

        //region modes finding views and shared preferences
        val m1Young=findViewById<RadioButton>(R.id.m1_young)
        val m2Gay=findViewById<RadioButton>(R.id.m2_gay)
        val m3Ftm=findViewById<RadioButton>(R.id.m3_ftm)
        val m4FtmT=findViewById<RadioButton>(R.id.m4_ftm_t)
        val m5Gnc=findViewById<RadioButton>(R.id.m5_gnc)
        val m6Nb=findViewById<RadioButton>(R.id.m6_nb)
        val m7Ace=findViewById<RadioButton>(R.id.m7_ace)
        val m8FtmtL=findViewById<RadioButton>(R.id.m8_ftm_tL)

        val sexActiveText=findViewById<TextView>(R.id.sexActiveText)
        val sharedPrefGender = applicationContext.getSharedPreferences("com.example.rainbowcalendar_gender", Context.MODE_PRIVATE) ?: return
        val gender: String? =sharedPrefGender.getString("com.example.rainbowcalendar_gender","")
        val sharedPrefTM=applicationContext.getSharedPreferences("com.example.rainbowcalendar_tm", Context.MODE_PRIVATE) //todo: transmed
        val tM:Boolean=sharedPrefTM.getBoolean("com.example.rainbowcalendar_tm", false)
        var tooYoungError=""
        val birthdayPickerTextView=findViewById<TextView>(R.id.birthdayPicker_text)
        val lastDoseText=findViewById<TextView>(R.id.lastDoseText)

        //region settings by gender
        when (gender) {
            "f" -> {
                m1Young.visibility= View.VISIBLE
                m2Gay.visibility=View.VISIBLE
                m5Gnc.visibility=View.VISIBLE
                m6Nb.visibility=View.VISIBLE
                m7Ace.visibility=View.VISIBLE
                sexActiveText.text=getString(R.string.sexually_active_question_female)
                tooYoungError=getString(R.string.too_young_f)
                m1Young.text=getString(R.string.intro2_young_f)
            }
            "m" -> {
                m1Young.visibility= View.VISIBLE
                m3Ftm.visibility=View.VISIBLE
                m4FtmT.visibility=View.VISIBLE
                m7Ace.visibility=View.VISIBLE
                m8FtmtL.visibility=View.VISIBLE
                sexActiveText.text=getString(R.string.sexually_active_question_male)
                tooYoungError=getString(R.string.too_young_m)
                birthdayPickerTextView.text=getString(R.string.choose_birthday_m)
                m1Young.text=getString(R.string.intro2_young_m)
                lastDoseText.text=getString(R.string.last_t_dose_m)
            }
            "n" -> {
                m1Young.visibility= View.VISIBLE
                m2Gay.visibility=View.VISIBLE
                m3Ftm.visibility=View.VISIBLE
                m4FtmT.visibility=View.VISIBLE
                m5Gnc.visibility=View.VISIBLE
                m6Nb.visibility=View.VISIBLE
                m7Ace.visibility=View.VISIBLE
                m8FtmtL.visibility=View.VISIBLE
                tooYoungError=getString(R.string.too_young_n)
                birthdayPickerTextView.text=getString(R.string.choose_birthday_n)
                m1Young.text=getString(R.string.intro2_young_n)
                m3Ftm.text=getString(R.string.intro2_ftm_n)
                lastDoseText.text=getString(R.string.last_t_dose_n)
            }
        }
        //endregion

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
        val errorText=findViewById<TextView>(R.id.errorText)

        val sharedPrefT = applicationContext.getSharedPreferences("com.example.rainbowcalendar_tday", Context.MODE_PRIVATE)
        val sharedPrefBirthday = applicationContext.getSharedPreferences("com.example.rainbowcalendar_birthday", Context.MODE_PRIVATE)
        dpBirthday.maxDate=System.currentTimeMillis()

        val sharedPrefAge = applicationContext.getSharedPreferences("com.example.rainbowcalendar_minor", Context.MODE_PRIVATE)
        val sharedPrefSex = applicationContext.getSharedPreferences("com.example.rainbowcalendar_sex", Context.MODE_PRIVATE)
        val sex: Int =sharedPrefSex.getInt("com.example.rainbowcalendar_sex",0)
        val sharedPrefTestosterone = applicationContext.getSharedPreferences("com.example.rainbowcalendar_testosterone", Context.MODE_PRIVATE)
        val sharedPrefFertile= applicationContext.getSharedPreferences("com.example.rainbowcalendar_fert", Context.MODE_PRIVATE)
        val sharedPrefPeriod=applicationContext.getSharedPreferences("com.example.rainbowcalendar_pr", Context.MODE_PRIVATE)
        //doc
        // ALWAYS:  0: none
        // AGE (_minor) 1: 16- 2: 16-17 3: 18+
        // SEX (_sex) 1: yes, 2: no, 3: ask
        // T (_testosterone) 1: yes 2: no 3: ask about start
        // FERTILE (_fert) 1: yes
        // birthday: dd-mm-yyyy
        // ACTUAL T START (_tday) dd-mm-yyyy
        // PERIOD: 1: regular 2: irregular 3: no 4: ask
        //endregion

        //region T settings and notifications setup
        val helperCalendar=findViewById<CalendarView>(R.id.helperCalendar)
        val showCalendarCB=findViewById<CheckBox>(R.id.showCalendarCB)

        showCalendarCB.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked) helperCalendar.visibility=View.VISIBLE
            else helperCalendar.visibility=View.GONE
        }

        val daysSinceTInput=findViewById<NumberPicker>(R.id.daysSinceT)
        daysSinceTInput.minValue=0
        daysSinceTInput.maxValue=180

        val tIntervalInput=findViewById<NumberPicker>(R.id.TInterval)
        tIntervalInput.minValue=1
        tIntervalInput.maxValue=180

        val tTime=findViewById<TimePicker>(R.id.tTime)
        val timeText=findViewById<TextView>(R.id.timeText)
        val gelCb=findViewById<CheckBox>(R.id.gelCB)


        //doc: gel switch
        gelCb.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked){
                tIntervalInput.visibility=View.GONE
                daysSinceTInput.visibility=View.GONE
                //tTime.visibility=View.VISIBLE
                timeText.visibility=View.VISIBLE
                lastDoseText.visibility=View.GONE
                showCalendarCB.visibility=View.GONE //doc: calendar isn't needed for daily dose
            }
            else{
                tIntervalInput.visibility=View.VISIBLE
                daysSinceTInput.visibility=View.VISIBLE
                //tTime.visibility=View.GONE
                timeText.visibility=View.GONE
                lastDoseText.visibility=View.VISIBLE
                showCalendarCB.visibility=View.VISIBLE
            }
        }

        val notifCB=findViewById<CheckBox>(R.id.notifCB)
        var tReminders=false

        notifCB.setOnCheckedChangeListener{_, isChecked ->
            tReminders = isChecked
        }
        //endregion

        val skipBd=findViewById<CheckBox>(R.id.skipBd)
        skipBd.setOnCheckedChangeListener{_, isChecked->
            if(isChecked) dpBirthday.visibility=View.GONE
            else dpBirthday.visibility=View.VISIBLE
        }
        //region BUTTON 1
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
            else if(m8FtmtL.isChecked)
                mode=8
            when(mode){
                1->{
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
                    //todo: ask like is it regular already
                    with(sharedPrefPeriod.edit()){
                        putInt("com.example.rainbowcalendar_pr",4)
                    }

                }
                2->{
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
                    with(sharedPrefPeriod.edit()){
                        putInt("com.example.rainbowcalendar_pr",4)
                    }

                }
                3->{
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
                    with(sharedPrefPeriod.edit()){
                        putInt("com.example.rainbowcalendar_pr",4)
                    }
                }
                4->{
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
                    with(sharedPrefPeriod.edit()){
                        putInt("com.example.rainbowcalendar_pr",2)
                    }
                }
                5->{
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
                    with(sharedPrefPeriod.edit()){
                        putInt("com.example.rainbowcalendar_pr",4)
                    }
                }
                6->{
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
                    with(sharedPrefPeriod.edit()){
                        putInt("com.example.rainbowcalendar_pr",4)
                    }
                }
                7->{
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
                    with(sharedPrefPeriod.edit()){
                        putInt("com.example.rainbowcalendar_pr",4)
                    }
                }
                8->{
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
                    with(sharedPrefPeriod.edit()){
                        putInt("com.example.rainbowcalendar_pr",3)
                    }
                }
            }

            //doc: change header text and hide options
            introHeader.text= getString(R.string.additional_options)
            m1Young.visibility= View.GONE
            m2Gay.visibility=View.GONE
            m3Ftm.visibility=View.GONE
            m4FtmT.visibility=View.GONE
            m5Gnc.visibility=View.GONE
            m6Nb.visibility=View.GONE
            m7Ace.visibility=View.GONE
            m8FtmtL.visibility=View.GONE
            button.visibility=View.GONE
            button1.visibility=View.VISIBLE
            ageLayout.visibility=View.GONE
            layoutBirthday.visibility=View.VISIBLE
            legalAgeProof.visibility=View.GONE
            //doc: show sex active choice if it's not obvious and if over 16
            var age1=ageToCode(ageValue)
            if(age1==2&&(!legalAgeProof.isChecked)){
                age1=1
                errorText.text=getString(R.string.error_minor_consent)
            }

            with (sharedPrefAge.edit()) {
                putInt("com.example.rainbowcalendar_gender", age1)
                apply()
            }
            //todo: this shit here
            if(sex==3&&(age1==2||age1==3)){
                layoutSex.visibility=View.VISIBLE
            }


            //doc: if is planning to start T, enter approx date
            if(m3Ftm.isChecked) startDatePicker.visibility=View.VISIBLE
            //doc: if on T, show start date
            if(m4FtmT.isChecked||m8FtmtL.isChecked){
                tStartDateLayout.visibility=View.VISIBLE
            }

            helperCalendar.visibility=View.GONE
        }
        //endregion handling

        val sharedPrefTReminderI=applicationContext.getSharedPreferences("com.example.rainbowcalendar_TReminderI", Context.MODE_PRIVATE)
        //region BUTTON2
        button1.setOnClickListener{
            //doc: possible t start, show on main screen when it's close
            if(mode==3){
                val stringDate: String=tDatePickerYear.value.toString()+"-"+tDatePickerMonth.value.toString().padStart(2,'0')+"-01"
                with (sharedPrefPossTDate.edit()) {
                    putString("com.example.rainbowcalendar_posstday", stringDate)
                    apply()
                }
            }
            //reg: MEN ON T
            //doc: actual t day if started T, remind of anniversary and ask about last shot and shot frequency later
            if(mode==4||mode==8){
                val tDate:String=tStartDate.dayOfMonth.toString()+"/"+tStartDate.month.toString()+"/"+tStartDate.year
                tStartDate.maxDate=System.currentTimeMillis()
                with(sharedPrefT.edit()){
                    putString("com.example.rainbowcalendar_tday", tDate)
                }
                if(!gelCb.isChecked){ //doc: normal T
                    val daysSinceT: Int=daysSinceTInput.value
                    val tInterval: Int=tIntervalInput.value
                    val daysTillShot=tInterval-daysSinceT

                    val alarm=Alarm(this)
                    alarm.schedulePushNotifications(tTime.hour, tTime.minute, tInterval, daysTillShot)
                    Toast.makeText(this@IntroductionActivity2, "notification set to"+tTime.hour.toString()+":"+tTime.minute.toString()+"in "+daysTillShot+" days",Toast.LENGTH_SHORT).show()

                    with(sharedPrefTReminderI.edit()){
                        putInt("com.example.rainbowcalendar_TReminderI",tInterval)
                        apply()
                    }
                }
                else{ //doc: gel
                    val alarm=Alarm(this)
                    alarm.schedulePushNotifications(tTime.hour,tTime.minute,1,0)

                    with(sharedPrefTReminderI.edit()){
                        putInt("com.example.rainbowcalendar_TReminderI",1)
                        apply()
                    }
                    //Toast.makeText(this@IntroductionActivity2, "HERE", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this@IntroductionActivity2, "notification set to"+tTime.hour.toString()+":"+tTime.minute.toString(),Toast.LENGTH_SHORT).show()
                }

                //val nextTDate=
            }

            //reg: BIRTHDAY
            if(dpBirthday.visibility==View.VISIBLE){
                //doc: birthday dd-mm-yyyy
                val birthday: String=dpBirthday.dayOfMonth.toString()+"/"+dpBirthday.month.toString()+"/"+dpBirthday.year
                if(year-dpBirthday.year > 9){
                    errorText.text=""
                    with (sharedPrefBirthday.edit()) {
                        putString("com.example.rainbowcalendar_birthday", birthday)
                        apply()
                    }
                }
                //doc: show too young if younger than 10, and error if date is today
                else if(year-dpBirthday.year==0 && month-dpBirthday.month==0)
                    errorText.text=getString(R.string.date_error)
                else if(year-dpBirthday.year in 0..9){
                    errorText.text=tooYoungError
                }
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

            //reg: saving "settings done" + notifcations
            if(errorText.text!="Enter correct date"){
                if(tReminders){
                    val sharedPrefSetup: SharedPreferences =applicationContext.getSharedPreferences("com.example.rainbowcalendar_setup", MODE_PRIVATE)
                    with(sharedPrefSetup.edit()){
                        putBoolean("com.example.rainbowcalendar_setup",true)
                        apply()
                    }
                }
                val intent=Intent(this, MainActivity::class.java)
                startActivity(intent)

            }

        }
        //endregion

    }
    //region helper functions
    private fun ageToCode(age: String): Int{
        return when(age){
            "minor" -> 1
            "minor, but legal" -> 2
            "adult (18+)" -> 3
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

    //endregion
}