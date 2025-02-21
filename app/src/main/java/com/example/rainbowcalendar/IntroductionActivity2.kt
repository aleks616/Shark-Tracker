package com.example.rainbowcalendar
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import java.util.Calendar

class IntroductionActivity2 : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        val sharedPrefs=applicationContext.getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)
        val theme=sharedPrefs.getString("theme","Light")
        ThemeManager[this]=theme
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction2)

        //TODO:
        // 1. if year is same as now skip year but still send birthday notifications

        val adultCb=findViewById<CheckBox>(R.id.adultCb)
        var adult=false
        adultCb.setOnCheckedChangeListener{_, isChecked->
            adult = isChecked
        }

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
        val modesRadioGroup=findViewById<RadioGroup>(R.id.modes_radioGroup)
        modesRadioGroup.visibility=View.VISIBLE

        val m1Young=findViewById<RadioButton>(R.id.m1_young)
        val m2Gay=findViewById<RadioButton>(R.id.m2_gay)
        val m3Ftm=findViewById<RadioButton>(R.id.m3_ftm)
        val m4FtmT=findViewById<RadioButton>(R.id.m4_ftm_t)
        val m5Gnc=findViewById<RadioButton>(R.id.m5_gnc)
        val m6Nb=findViewById<RadioButton>(R.id.m6_nb)
        val m7Ace=findViewById<RadioButton>(R.id.m7_ace)
        val m8FtmtL=findViewById<RadioButton>(R.id.m8_ftm_tL)

        val sexCb=findViewById<CheckBox>(R.id.sexCb)

        val gender: String?=sharedPrefs.getString("gender","n")
        val tM:Boolean=sharedPrefs.getBoolean("tm", false) //todo: transmed
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
                sexCb.text=getString(R.string.sexually_active_question_female)
                tooYoungError=getString(R.string.too_young_f)
                m1Young.text=getString(R.string.intro2_young_f)
            }
            "m" -> {
                m1Young.visibility= View.VISIBLE
                m3Ftm.visibility=View.VISIBLE
                m4FtmT.visibility=View.VISIBLE
                m7Ace.visibility=View.VISIBLE
                m8FtmtL.visibility=View.VISIBLE
                sexCb.text=getString(R.string.sexually_active_question_male)
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
        val introHeader=findViewById<TextView>(R.id.intro2_header)
        val startDatePicker=findViewById<LinearLayout>(R.id.startDatePicker)
        val layoutBirthday=findViewById<LinearLayout>(R.id.layoutBirthday)
        val dpBirthday=findViewById<DatePicker>(R.id.datePickerBirthday)
        val tStartDateLayout=findViewById<LinearLayout>(R.id.tStartDateLayout)
        val tStartDate=findViewById<DatePicker>(R.id.tStartDate)
        val errorText=findViewById<TextView>(R.id.errorText)

        dpBirthday.maxDate=System.currentTimeMillis()



        var sex:Boolean?=null
        //document
        // gender:String        m/f/n
        // tm:Boolean           (is transmed)
        // name:String          name to refer user
        // possTDate:String     possible T starting date month and year
        // (T)tStartDate        T start Date dd-mm-yyy
        // birthday:String      dd-mm-yyyy
        // adult:Boolean        is legal adult, ask about sex or no
        // sex:Boolean          is sexually active
        // onT:Boolean          is taking T
        // fertile:Boolean      can physically get pregnant
        // period:Int?          0: unknown 1: regular 2: irregular 3: no
        // tInterval:Int        how many days between T shots/gel 1->everyday
        // lastNotif:Long       time from last notification
        // setup:Boolean        is setup done: show settings after splash screen or not
        // lang:String          language code
        // passwordType:Int     0->unknown 1->text 2->pin
        // passwordValue:String
        // failedAttemptsCount  password/pin


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


        //gel switch
        gelCb.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked){
                tIntervalInput.visibility=View.GONE
                daysSinceTInput.visibility=View.GONE
                timeText.visibility=View.VISIBLE
                lastDoseText.visibility=View.GONE
                showCalendarCB.visibility=View.GONE //calendar isn't needed for daily dose
            }
            else{
                tIntervalInput.visibility=View.VISIBLE
                daysSinceTInput.visibility=View.VISIBLE
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
                    sex=false
                    sharedPrefs.edit().putBoolean("onT",false).apply()
                    sharedPrefs.edit().putBoolean("fertile",false).apply()
                    //todo: ask like is it regular already
                    sharedPrefs.edit().putInt("period",0).apply()
                }
                2->{
                    sharedPrefs.edit().putBoolean("onT",false).apply()
                    sharedPrefs.edit().putBoolean("fertile",false).apply()
                    sharedPrefs.edit().putInt("period",0).apply()
                }
                3->{
                    sharedPrefs.edit().putBoolean("onT",false).apply()
                    sharedPrefs.edit().putBoolean("fertile",true).apply()
                    sharedPrefs.edit().putInt("period",0).apply()
                }
                4->{
                    sharedPrefs.edit().putBoolean("onT",true).apply()
                    sharedPrefs.edit().putBoolean("fertile",true).apply()
                    sharedPrefs.edit().putInt("period",2).apply()
                }
                5->{
                    sharedPrefs.edit().putBoolean("onT",false).apply()
                    sharedPrefs.edit().putBoolean("fertile",true).apply()
                    sharedPrefs.edit().putInt("period",0).apply()
                }
                6->{
                    sharedPrefs.edit().putBoolean("onT",false).apply()
                    sharedPrefs.edit().putBoolean("fertile",true).apply()
                    sharedPrefs.edit().putInt("period",0).apply()
                }
                7->{
                    sex=false
                    sharedPrefs.edit().putBoolean("onT",false).apply()
                    sharedPrefs.edit().putBoolean("fertile",false).apply()
                    sharedPrefs.edit().putInt("period",0).apply()
                }
                8->{
                    sharedPrefs.edit().putBoolean("onT",true).apply()
                    sharedPrefs.edit().putBoolean("fertile",false).apply()
                    sharedPrefs.edit().putInt("period",3).apply()
                }
            }

            //change header text and hide options
            introHeader.text= getString(R.string.additional_options)
            modesRadioGroup.visibility=View.GONE
            button.visibility=View.GONE
            button1.visibility=View.VISIBLE
            adultCb.visibility=View.GONE
            layoutBirthday.visibility=View.VISIBLE
            //show sex active choice if it's not obvious and if adult

            if(sex==null&&adult){
                sexCb.visibility=View.VISIBLE
            }
            sharedPrefs.edit().putBoolean("adult",adult).apply()


            //if is planning to start T, enter approx date
            if(m3Ftm.isChecked) startDatePicker.visibility=View.VISIBLE
            //if on T, show start date
            if(m4FtmT.isChecked||m8FtmtL.isChecked){
                tStartDateLayout.visibility=View.VISIBLE
            }

            helperCalendar.visibility=View.GONE
        }
        //endregion handling

        //region BUTTON2
        button1.setOnClickListener{
            //to do: possible t start, show on main screen when it's close
            if(mode==3){
                val stringDate:String=tDatePickerYear.value.toString()+"-"+tDatePickerMonth.value.toString().padStart(2,'0')+"-01"
                sharedPrefs.edit().putString("possTDate",stringDate).apply()
            }
            //MEN ON T
            //actual t day if started T, remind of anniversary and ask about last shot and shot frequency later
            if(mode==4||mode==8){
                val tDate:String=tStartDate.dayOfMonth.toString()+"/"+tStartDate.month.toString()+"/"+tStartDate.year
                tStartDate.maxDate=System.currentTimeMillis()
                sharedPrefs.edit().putString("tStartDate",tDate).apply()
                if(!gelCb.isChecked){ //normal T
                    val daysSinceT: Int=daysSinceTInput.value
                    val tInterval: Int=tIntervalInput.value
                    val daysTillShot=tInterval-daysSinceT

                    val alarm=Alarm(this)
                    //alarm.schedulePushNotifications(tTime.hour, tTime.minute, tInterval, daysTillShot)
                    Toast.makeText(this@IntroductionActivity2, "notification set to"+tTime.hour.toString()+":"+tTime.minute.toString()+"in "+daysTillShot+" days",Toast.LENGTH_SHORT).show()

                    sharedPrefs.edit().putInt("tInterval",tInterval).apply()
                }
                else{ //gel
                    val alarm=Alarm(this)
                    //alarm.schedulePushNotifications(tTime.hour,tTime.minute,1,0)

                    sharedPrefs.edit().putInt("tInterval",1).apply()
                    Toast.makeText(this@IntroductionActivity2, "notification set to"+tTime.hour.toString()+":"+tTime.minute.toString(),Toast.LENGTH_SHORT).show()
                }

                //val nextTDate=
            }

            //BIRTHDAY
            if(dpBirthday.visibility==View.VISIBLE){
                //birthday dd-mm-yyyy
                val birthday: String=dpBirthday.dayOfMonth.toString()+"/"+dpBirthday.month.toString()+"/"+dpBirthday.year
                if(year-dpBirthday.year > 9){
                    errorText.text=""
                    sharedPrefs.edit().putString("birthday",birthday).apply()
                }
                //todo: show too young if younger than 10, and error if date is today, AND STOP USER FROM GOING FURTHER
                else if(year-dpBirthday.year==0 && month-dpBirthday.month==0)
                    errorText.text=getString(R.string.date_error)
                else if(year-dpBirthday.year in 0..9){
                    errorText.text=tooYoungError
                }
            }
            //todo: if adult and it's unknown if sexually active or not, hide sex options for inactive, show the tick for active somewhere close
            sexCb.setOnCheckedChangeListener{_, isChecked->
                if(sex==false)
                    sharedPrefs.edit().putBoolean("sex",false).apply()
                else
                    sharedPrefs.edit().putBoolean("sex",isChecked).apply()
            }

            //reg: saving "settings done" + notifications FIX  THIS SHIT
            if(errorText.text!="Enter correct date"){
                sharedPrefs.edit().putBoolean("setup",true).apply()

                if(!sharedPrefs.getString("passwordValue","").isNullOrEmpty())
                    startActivity(Intent(this, MainActivity::class.java))
                else
                    startActivity(Intent(this,PasswordActivity::class.java))
            }
        }
        //endregion

    }
    //region helper functions
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