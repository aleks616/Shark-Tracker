package com.example.rainbowcalendar

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.util.Log
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.core.app.NotificationCompat
import com.example.rainbowcalendar.db.Cycle
import com.example.rainbowcalendar.db.CycleRoomDatabase
import com.example.rainbowcalendar.db.Cycles
import com.example.rainbowcalendar.db.DateCycle
import com.google.gson.Gson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

enum class TIMEUNIT{
    ERROR,
    DAYS,
    WEEKS,
    MONTHS,
    YEARS
}
data class MilestoneDate(
    var days:Int,
    var amount:Int,
    var timeUnit:TIMEUNIT
)
object Utils{
    fun canBeIntParsed(text:String):Boolean{
        if(text.isNotEmpty()){
            return text.all{it.isDigit()}
        }
        return false
    }

    fun isStringANumber(text:String):Boolean{
        return text.all{it.isDigit()}
    }

    @OptIn(ExperimentalMaterial3Api::class)
    object PastOrPresentSelectableDates:SelectableDates{
        override fun isSelectableDate(utcTimeMillis:Long):Boolean{
            return utcTimeMillis<=System.currentTimeMillis()
        }
        override fun isSelectableYear(year:Int):Boolean{
            return year<=LocalDate.now().year
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    object OldEnoughSelectableDates:SelectableDates{
        override fun isSelectableDate(utcTimeMillis:Long):Boolean{
            return (System.currentTimeMillis()-22090320000000..System.currentTimeMillis()-410240038000).contains(utcTimeMillis)
            //return utcTimeMillis<=System.currentTimeMillis()-410240038000
        }
        override fun isSelectableYear(year:Int):Boolean{
            return (LocalDate.now().year-70..LocalDate.now().year-13).contains(year)
            //return year<=LocalDate.now().year-13
        }
    }

    fun hasDysphoria(context:Context):Boolean{
        val data=getAllMoodData(context)
        var hasDysphoria=false
        data.forEach{day->
            if(day.dysphoria!=null)
                hasDysphoria=true
        }
        return hasDysphoria
    }
    fun avgFeel(context:Context,date:Cycle):Float{
        val values=arrayOf(
            //max value, importance
            floatArrayOf(4f,4f), //reverse
            floatArrayOf(4f,3f),
            floatArrayOf(4f,2f),
            floatArrayOf(2f,2f),
            floatArrayOf(2f,1f),
            floatArrayOf(3f,2f),
            floatArrayOf(2f,2f),//reverse
            floatArrayOf(4f,3f),//reverse
            floatArrayOf(2f,2f),
            floatArrayOf(4f,0f),
            floatArrayOf(2f,3f),
            floatArrayOf(2f,1f),
        )
        val data=intArrayOf(
            if(date.overallMood==null||date.overallMood==-1) 2 else date.overallMood,
            if(date.dysphoria==null||date.dysphoria==-1) 0 else date.dysphoria,
            if(date.headache==null||date.headache==-1) 0 else date.headache,
            if(date.musclePain==null||date.musclePain==-1) 0 else date.musclePain,
            if(date.skinCondition==null||date.skinCondition==-1) 0 else date.skinCondition,
            if(date.digestiveIssues==null||date.digestiveIssues==-1) 0 else date.digestiveIssues,
            if(date.sleepQuality==null||date.sleepQuality==-1) 1 else date.sleepQuality,
            if(date.energyLevel==null||date.energyLevel==-1) 2 else date.energyLevel,
            if(date.moodSwings==null||date.moodSwings==-1) 0 else date.moodSwings,
            if(date.bleeding==null||date.bleeding==-1) 0 else date.bleeding,
            if(date.crampLevel==null||date.crampLevel==-1) 0 else date.crampLevel,
            if(date.cravings==null||date.cravings==-1) 0 else date.cravings,
        )
        //Log.i("data",data.toString())


        var score=
            data[0]*(values[0][1])/(values[0][0])+
            (values[2][0]-data[2])*(values[2][1])/(values[2][0])+
            (values[3][0]-data[3])*(values[3][1])/(values[3][0])+
            (values[4][0]-data[4])*(values[4][1])/(values[4][0])+
            (values[5][0]-data[5])*(values[5][1])/(values[5][0])+
            data[6]*(values[6][1])/(values[6][0])+
            data[7]*(values[7][1])/(values[7][0])+
            (values[8][0]-data[8])*(values[8][1])/(values[8][0])+
            (values[10][0]-data[10])*(values[10][1])/(values[10][0])+
            (values[11][0]-data[11])*(values[11][1])/(values[11][0])
            //not included: bleeding!!!

        val max=if(hasDysphoria(context)) 25 else 22

        //Log.v("has dysphoria",hasDysphoria(context).toString())
        //Log.v("score without dysphoria",(score.toFloat()/22).toString())

        if(hasDysphoria(context)){
            score+=(values[1][0]-data[1])*((values[1][1])/(values[1][0]))
            val tempVal=values[1][0]-data[1]
            val tempMulti=(values[1][1])/(values[1][0])
            //Log.v("dysphoria value",(values[1][0]-data[1]).toString())
            //Log.v("dysphoria multiply",((values[1][1])/(values[1][0])).toString())
            //Log.v("temp dysphoria multiply",tempMulti.toString())
            Log.v("dysphoria result",((values[1][0]-data[1])*(values[1][1])/(values[1][0])).toString())
            //Log.v("temp dysphoria result",(tempVal*tempMulti).toString())
        }


        //Log.v("score with dysphoria",(score.toFloat()/25).toString())

        return score/max.toFloat()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    object MetricsSelectableDates:SelectableDates{
        override fun isSelectableDate(utcTimeMillis:Long):Boolean{
            return (System.currentTimeMillis()-315569510000..System.currentTimeMillis()).contains(utcTimeMillis)
        }
        override fun isSelectableYear(year:Int):Boolean{
            return (LocalDate.now().year-10..LocalDate.now().year).contains(year)
        }
    }
    fun convertMillisToDate(millis:Long):String{
        val formatter=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        return formatter.format(Date(millis))
    }
    fun isValidPastOrPresentYear(year:Int):Boolean{
        val currentYear=Calendar.getInstance().get(Calendar.YEAR)
        return (2000..currentYear).contains(year)
    }
    fun isValidMonth(month:Int):Boolean{
        return (1..12).contains(month)
    }
    fun isValidDay(day:Int):Boolean{
        return (1..31).contains(day)
    }

    fun isValidDate(year:Int,month:Int,day:Int):Boolean{
        return (isValidDay(day)&&isValidMonth(month)&&isValidPastOrPresentYear(year))
    }

    fun isValidPastDate(year:Int,month:Int,day:Int):Boolean{
        val formatter=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        val today=formatter.format(Calendar.getInstance().time)
        val calendar=Calendar.getInstance()
        calendar.set(year,month-1,day)
        val date=formatter.format(calendar.time)
        return formatter.parse(today)!!>=formatter.parse(date)
    }

    fun createDateFromIntegers(year:Int,month:Int,day:Int):String{
        return "%04d-%02d-%02d".format(year, month, day)
    }

    fun localDateString(date:String):LocalDate{
        val year=date.split("-")[0].toInt()
        val month=date.split("-")[1].toInt()
        val day=date.split("-")[2].toInt()
        return LocalDate.of(year,month,day)
    }

    fun isDate1AfterDate2(date1:String,date2:String):Boolean{
        val format=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        return format.parse(date1)!!.after(format.parse(date2))
    }
    fun isDate(date:String):Boolean{
        val pattern="\\d{4}-\\d{2}-\\d{2}"
        return date.matches(Regex(pattern))
    }
    fun smartLastPeriodDate(date:String):String{
        if(!isDate(date)) return "1970-01-01"

        val dateFormat=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        val lastDate=dateFormat.parse(date)!!
        val beforeDate=lastDate.time-28*24*3600*1000L

        return dateFormat.format(Date(beforeDate))
        //return "0000-00-00"
    }
    fun timeSinceDate(start:String):MilestoneDate{
        val today=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(Calendar.getInstance().time)
        val dateFormat=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        dateFormat.isLenient=false

        try{
            val startDate=dateFormat.parse(start)!!
            val todayDate=dateFormat.parse(today)!!
            val timeSinceStart=todayDate.time-startDate.time
            val daysSinceStart=timeSinceStart/(24*60*60*1000)

            if(daysSinceStart>1095){//>36 months -> years
                return MilestoneDate(daysSinceStart.toInt(),(daysSinceStart/365.25).toInt(),TIMEUNIT.YEARS)
                //todo: next date? in another function?
            }
            if(daysSinceStart>168){ //>24 weeks -> months
                return MilestoneDate(daysSinceStart.toInt(),(daysSinceStart/30.437).toInt(),TIMEUNIT.MONTHS)
            }
            if(daysSinceStart>35){//>5 weeks -> in weeks
                return MilestoneDate(daysSinceStart.toInt(),(daysSinceStart/7).toInt(),TIMEUNIT.WEEKS)
            }
            return MilestoneDate(daysSinceStart.toInt(),daysSinceStart.toInt(),TIMEUNIT.DAYS)
        }
        catch(e:ParseException){
            return MilestoneDate(-1,-1,TIMEUNIT.ERROR)
        }
    }

    /**
     * @param lastDoseDate yyyy-MM-dd of last testosterone dose
     * @param interval correct interval between T doses, in days
     * @see getDaysTillNextShot
     * @return date, (type Date) of next dose, can be in the past!, check that later when using the result
     */
    fun getNextDoseDate(lastDoseDate:String,interval:Int):Date {
        val dateFormat=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        val lastDate=dateFormat.parse(lastDoseDate)!!
        return Date(lastDate.time+interval*3600*24*1000) //todo: if returned days is in the past show "overdue" and the old correct day and "today"
    }
    /**
     * @param lastDoseDate yyyy-MM-dd of last testosterone dose
     * @param interval correct interval between T doses, in days
     * @see getNextDoseDate
     * @return number of days left to next shot, can be a negative number!, check that later when using the result
     **/
    fun getDaysTillNextShot(lastDoseDate:String,interval:Int):Int {
        val dateFormat=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        val today=dateFormat.format(Calendar.getInstance().time)
        val lastDate=dateFormat.parse(lastDoseDate)!!
        val todayDate=dateFormat.parse(today)!!
        val nextDate=Date(lastDate.time+interval*3600*24*1000)
        return ((nextDate.time-todayDate.time)/(3600*24*1000)).toInt()
    }

    /**
     * @param type key for sharedPreferences: [Constants.key_lastTNotification],
     * [Constants.key_lastBCNotification] , [Constants.key_lastPeriodNotification]
     * @param interval in days
     * **/
    fun scheduleNotifications(notificationHour:Int=6,notificationMinute:Int=0,interval:Int,daysTillNext:Int,context:Context,type:String){
        val alarm=Alarm(context)
        alarm.schedulePushNotifications(notificationHour,notificationMinute,interval,daysTillNext,type)
    }
    fun createTestosteroneNotificationChannel(context: Context){
        val channel=NotificationChannel("HRT","T reminders", NotificationManager.IMPORTANCE_HIGH).apply{
            description="This channel is for hrt reminders"
        }

        val notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun createPeriodNotificationChannel(context: Context){
        val channel=NotificationChannel("PERIOD","Period reminders", NotificationManager.IMPORTANCE_HIGH).apply{
            description="Get reminders X days before period"
        }

        val notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun createContraceptiveNotificationChannel(context: Context){
        val channel=NotificationChannel("BC","Contraceptive reminders", NotificationManager.IMPORTANCE_HIGH).apply{
            description="Get reminders X days before contraceptive date"
        }

        val notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    /**
     * @param lang language code, e.g. "pl"
     * @param gender only these options are valid: m,f,n
     * @return empty if gender wasn't valid
     * **/
    fun getStringGender(context:Context,resId:Int,lang:String,gender:String):String{
        val config=Configuration(context.resources.configuration)
        return if(listOf("m","n","n").contains(gender)){
            config.setLocale(Locale(lang,gender))
            val genderedContext=context.createConfigurationContext(config)
            genderedContext.getString(resId)
        }
        else ""
    }

    fun censorPeriod(string:String){
        string.replace("period","shark week")
        string.replace("okres","wodospad")
        string.replace("okresie","wodospadzie")
        string.replace("okresu","wodospadu")
        //string.replace("","Semaine rouge")
        /*string.replace("","дни Красной армии")
        string.replace("","днi Червоноï армiï")*/
    }
    fun getTestosteroneVersions():List<String>{
        return listOf(
            "Agovirin Depot",
            "Andractim",
            "Andriol",
            "AndroGel",
            "AndroPatch",
            "Androderm",
            "Androject",
            "Andronaq",
            "Aveed",
            "Axiron",
            "Delatestryl",
            "Depo-Testosterone",
            "Dianabol",
            "Halotestin",
            "Jatenzo",
            "Metandren",
            "Nebido",
            "Omnadren",
            "Ora-Testryl",
            "Oreton Methyl",
            "Perandren",
            "Prolongatum",
            "Proviron",
            "Rektandron",
            "Sterotate",
            "Striant",
            "Sustanon 100",
            "Sustanon 250",
            "Testim",
            "TestoGel",
            "TestoPatch",
            "Testopel",
            "Testoral",
            "Testoviron",
            "Testred",
            "Ultandren",
            "Virosterone",
            "Xyosted"
        )
    }

    fun getBCVersions():List<String>{
        return listOf(
            "Algestone acetophenide",
            "Azagly-nafarelin",
            "Chlormadinone acetate",
            "Cymegesolate",
            "Desogestrel",
            "Dienogest",
            "Diosgenin",
            "Drospirenone",
            "Estetrol",
            "Estradiol benzoate",
            "Estradiol cypionate",
            "Estradiol enantate",
            "Estradiol valerate",
            "Ethinylestradiol",
            "Ethinylestradiol sulfonate",
            "Etonogestrel",
            "Gestodene",
            "Hydroxyprogesterone caproate",
            "Levonorgestrel",
            "Levonorgestrel butanoate",
            "Levonorgestrel-releasing implant",
            "Medroxyprogesterone acetate",
            "Megestrol acetate",
            "Mestranol",
            "Nomegestrol acetate",
            "Norelgestromin",
            "Norethisterone",
            "Norethisterone acetate",
            "Noretynodrel",
            "Norgesterone",
            "Norgestimate",
            "Norgestrel",
            "Norvinisterone",
            "Ormeloxifene",
            "Oxogestone",
            "Oxogestone phenpropionate",
            "Quingestanol acetate",
            "Quingestrone",
            "Segesterone acetate",
            "Tosagestin",
            "Trestolone",
            "Ulipristal acetate",
            "Yuzpe regimen",
            "Plan B",
            "Next Choice",
            "Depo-Provera",
            "NuvaRing",
            "Mirena",
            "Skyla",
            "Implanon",
            "Jadelle",
            "Norplant",
            "Seasonale",
            "Yasmin",
            "Marvelon"
        )
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun datePickerColors():DatePickerColors{
        return DatePickerDefaults.colors(
            titleContentColor=colorSecondary(),
            headlineContentColor=colorSecondary(),
            weekdayContentColor=colorSecondary(),
            dayContentColor=colorSecondary(),

            todayDateBorderColor=colorSecondary(),
            todayContentColor=colorSecondary(),
            selectedDayContainerColor=colorTertiary(),
            selectedDayContentColor=colorSecondary(),

            containerColor=colorPrimary(),
            subheadContentColor=Color.Red,
            yearContentColor=colorSecondary(),
            currentYearContentColor=colorQuaternary(),
            selectedYearContentColor=colorQuaternary(),
            selectedYearContainerColor=colorTertiary(),

            disabledDayContentColor=colorTertiary(),

            disabledSelectedYearContentColor=colorTertiary(),
            disabledSelectedYearContainerColor=colorTertiary(),
            disabledSelectedDayContentColor=colorTertiary(),
        )
    }

    fun getAllMoodData(context:Context):List<Cycle>{
        var data=listOf(Cycle(""))
        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        val thread=Thread{
            data=cycleDao.getAllMetricsSync()
        }
        thread.start()
        thread.join()
        return data
    }

    fun getFirstMetricDate(context:Context):String{
        var date:String=""
        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        val thread=Thread{
            date=cycleDao.getFirstDate()
        }
        thread.start()
        thread.join()
        return date
    }

    fun monthsSinceFirstDate(date:String):Int{
        val today=LocalDate.now()
        val date1=LocalDate.parse(date)
        return((today.year-date1.year)*12)+(today.monthValue-date1.monthValue)+1
    }

    fun addNewCycleType(context:Context,cycleName:String,correctInterval:Int,active:Boolean=true):String{
        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        var canInsert=true
        Thread{
            cycleDao.getAllCyclesTypes()?.forEach{cycle->
                Log.v("cycleData",cycle.cycleId.toString()+" "+cycle.cycleName+" "+cycle.correctLength+" "+cycle.isActive)
                if(cycle.cycleName==cycleName){
                    canInsert=false
                }
            }
            if(canInsert)
                cycleDao.addNewCycle(Cycles(0,cycleName,correctInterval,active))
            else
                Log.e("cycleData","cycle with name is already there")

        }.start()
        return if(canInsert) "" else "fail"
    }

    fun getCycleIdByName(context:Context,name:String):Int{
        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        val result=AtomicInteger(-1)
        val thread=Thread{
            result.set(cycleDao.getCycleIdByName(name))
        }
        thread.start()
        thread.join()
        return result.get()
    }

    fun previousScreenKey(previousScreen:String?):String{
        var prefs=""
        if(previousScreen!=null){
            //Log.v("previous screen in function",previousScreen)
            prefs=when(previousScreen){
                Screens.sLanguage->Constants.key_isLanguageSetUp
                Screens.sTheme->Constants.key_isThemeSetUp
                Screens.sAgeConsentOptions->Constants.key_isConsentDone
                Screens.sGenderOptions->Constants.key_gender //-1, NOT A BOOLEAN!
                Screens.sNameBirthDayOptions->Constants.key_isNameBirthDayMenuComplete
                Screens.sStealthOptions->Constants.key_isStealthDone
                Screens.sTOptions->Constants.key_testosteroneMenuComplete
                Screens.sPeriodOptions->Constants.key_isPeriodMenuComplete
                Screens.sContraceptiveOptions->Constants.key_BCMenuComplete
                else->""
            }
        }
        //Log.v("screen will be",prefs)
        return prefs
    }

    fun getPreviousScreen(currentScreen:String?,context:Context):String{
        var previousScreen=""
        if(currentScreen!=null){
            //Log.v("current screen in function",currentScreen)
            previousScreen=when(currentScreen){
                Screens.sLanguage->""
                Screens.sTheme->Screens.sLanguage
                Screens.sAgeConsentOptions->Screens.sTheme
                Screens.sGenderOptions->Screens.sAgeConsentOptions
                Screens.sNameBirthDayOptions->Screens.sGenderOptions
                Screens.sStealthOptions->Screens.sNameBirthDayOptions
                Screens.sTOptions->Screens.sStealthOptions
                Screens.sPeriodOptions->"unknown"
                Screens.sContraceptiveOptions->Screens.sPeriodOptions
                else->""
            }
            if(previousScreen=="unknown"){
                val sharedPrefs=context.getSharedPreferences(Constants.key_package, Context.MODE_PRIVATE)
                val gender=sharedPrefs.getInt(Constants.key_gender,-1)
                previousScreen=if(gender==2) Screens.sStealthOptions
                else Screens.sTOptions
            }
        }
        //Log.v("previous screen is",previousScreen)
        return previousScreen
    }

    fun addNewDateCycle(context:Context,dateCycle:DateCycle):Boolean{
        val result=AtomicBoolean(false)

        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        val thread=Thread{
            val cycleTypeExists:Boolean=cycleDao.doesCycleExist(dateCycle.cycleId)
            if(cycleTypeExists){
                val canInsert=!cycleDao.doesDateExist(dateCycle.date)
                if(canInsert){
                    Log.v("newDateCycle","id: "+dateCycle.cycleId.toString()+" date:"+dateCycle.date+" day: "+dateCycle.cycleDay.toString())
                    cycleDao.addNewDateCycle(dateCycle)
                    result.set(true)
                }
                else{
                    cycleDao.updateDateCycle(dateCycle.cycleId,dateCycle.cycleDay,dateCycle.date)
                    Log.v("newDateCycle","date "+dateCycle.date+" already exists, updating instead")
                    result.set(true)
                }
            }
            else{
                result.set(false)
            }
        }
        thread.start()
        thread.join()
        return result.get()
    }



    fun deleteAllCycleTypes(context:Context,areYouSure:Boolean){
        if(areYouSure){
            Thread{
                cycleDao.deleteAllCycles()
            }
        }
    }

    fun testNotif(context:Context){
        val notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel=NotificationChannel("testId","CHANNEL_NAME",NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)

        val notification:NotificationCompat.Builder=
            NotificationCompat.Builder(context,"testId")
                .setContentTitle("Powiadomienie")
                .setContentText("test")
                .setSmallIcon(R.drawable.alarm_icon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
        notificationManager.notify(1,notification.build())
    }

    fun changeCycleName(context:Context,oldCycleName:String,newCycleName:String){
        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        Thread{
            val cycleData=cycleDao.getCycleDataByName(oldCycleName)
            cycleDao.changeCycleTypeName(newCycleName,cycleData.cycleId)
        }
    }
    fun changeCycleCorrectInterval(context:Context,cycleName:String,newCorrectInterval:Int){
        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        Thread{
            val cycleData=cycleDao.getCycleDataByName(cycleName)
            cycleDao.changeCycleTypeCorrectInterval(newCorrectInterval,cycleData.cycleId)
        }
    }

    fun simplify(string: String?):String?{
        return string?.lowercase()?.replace(" ","")
    }
    fun langToCodeNew(lang: String): String{
        return when(lang.lowercase()){
            "english"->"en"
            "polski"->"pl"
            "français"->"fr"
            "português"->"pt-BR"
            "русский"->"ru"
            "українська"->"uk"
            else-> {
                "en"
            }
        }
    }
    fun codeToLanguage(code:String):String{
        return when(code.lowercase()){
            "en"->"English"
            "pl"->"Polski"
            "fr"->"Français"
            "pt"->"Português"
            "pt-br"->"Português"
            "ru"->"Русский"
            "uk"->"Українська"
            else-> {
                "English"
            }
        }
    }
    fun changeLanguage(lang: String,context:Context){
        val locale:Locale=if(lang=="pt-BR")
            Locale("pt","BR")
        else
            Locale(lang)
        Locale.setDefault(locale)

        val resources=context.resources
        val config=Configuration(resources.configuration)
        config.setLocale(locale)

        context.createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)

        val sharedPrefs=context.getSharedPreferences(Constants.key_package, Context.MODE_PRIVATE)
        sharedPrefs.edit().putString("lang",lang).apply()
        if (context is Activity){
            context.recreate()
        }
    }
    fun setLanguage(context:Context){
        val sharedPrefs=context.getSharedPreferences("com.example.rainbowcalendar_pref",Context.MODE_PRIVATE)
        val lang=sharedPrefs.getString("lang","en")!!
        val locale:Locale=if(lang=="pt-BR")
            Locale("pt","BR")
        else
            Locale(lang)
        Locale.setDefault(locale)

        val resources=context.resources
        val config=Configuration(resources.configuration)
        config.setLocale(locale)

        context.createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
    fun isStealthModeOn(context:Context):Boolean{
        val packageManager=context.packageManager
        val stealth=ComponentName(context,"com.example.rainbowcalendar.MainActivityStealth")

        return packageManager.getComponentEnabledSetting(stealth)==PackageManager.COMPONENT_ENABLED_STATE_ENABLED
    }

    fun isPeriodCensored(context:Context):Boolean{
        val sharedPrefs=context.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean(Constants.key_censorPeriod,false)
    }
    fun togglePeriodCensor(context: Context){
        val sharedPrefs=context.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
        val censorPeriod=sharedPrefs.getBoolean(Constants.key_censorPeriod,false)
        if(censorPeriod) sharedPrefs.edit().putBoolean(Constants.key_censorPeriod,false).apply()
        else sharedPrefs.edit().putBoolean(Constants.key_censorPeriod,true).apply()
    }


    fun toggleStealthMode(context:Context){
        val packageManager=context.packageManager
        val stealth=ComponentName(context,"com.example.rainbowcalendar.MainActivityStealth")
        val default=ComponentName(context,"com.example.rainbowcalendar.fragments.MainActivity")

        val stealthMode=packageManager.getComponentEnabledSetting(stealth)==PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        packageManager.setComponentEnabledSetting((if(stealthMode) default else stealth),PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP)
        packageManager.setComponentEnabledSetting((if(stealthMode) stealth else default),PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP)
    }

    fun setStartMetricsOrder(context:Context,gender:Int){
        val sharedPrefs=context.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
        val femaleMetrics=listOf(
            MetricRowData(context.getString(R.string.metrics_OverallMoodTitle),"overallMood",-1),
            MetricRowData(context.getString(R.string.metrics_headacheTitle),"headache",-1),
            MetricRowData(context.getString(R.string.metrics_MusclePainTitle),"musclePain",-1),
            MetricRowData(context.getString(R.string.metrics_SkinConditionTitle),"skinCondition",-1),
            MetricRowData(context.getString(R.string.metrics_DigestiveIssuesTitle),"digestiveIssues",-1),
            MetricRowData(context.getString(R.string.metrics_SleepQualityTitle),"sleepQuality",-1),
            MetricRowData(context.getString(R.string.metrics_energyLevelTitle),"energyLevel",-1),
            MetricRowData(context.getString(R.string.metrics_MoodSwingsTitle),"moodSwings",-1),
            MetricRowData(context.getString(R.string.metrics_BleedingTitle),"bleeding",-1),
            MetricRowData(context.getString(R.string.metrics_crampLevelTitle),"crampLevel",-1),
            MetricRowData(context.getString(R.string.metrics_CravingsTitle),"cravings",-1),
            MetricRowData(context.getString(R.string.metrics_DysphoriaTitle),"dysphoria",-1,visible=false),
            MetricRowData(sharedPrefs.getString("customMetric1","custom1-missing")!!,"customColumn1",-1),
            MetricRowData(sharedPrefs.getString("customMetric2","custom2-missing")!!,"customColumn2",-1),
            MetricRowData(sharedPrefs.getString("customMetric3","custom3-missing")!!,"customColumn3",-1),
        )
        val transMetrics=listOf(
            MetricRowData(context.getString(R.string.metrics_DysphoriaTitle),"dysphoria",-1),
            MetricRowData(context.getString(R.string.metrics_OverallMoodTitle),"overallMood",-1),
            MetricRowData(context.getString(R.string.metrics_headacheTitle),"headache",-1),
            MetricRowData(context.getString(R.string.metrics_MusclePainTitle),"musclePain",-1),
            MetricRowData(context.getString(R.string.metrics_SkinConditionTitle),"skinCondition",-1),
            MetricRowData(context.getString(R.string.metrics_DigestiveIssuesTitle),"digestiveIssues",-1),
            MetricRowData(context.getString(R.string.metrics_SleepQualityTitle),"sleepQuality",-1),
            MetricRowData(context.getString(R.string.metrics_energyLevelTitle),"energyLevel",-1),
            MetricRowData(context.getString(R.string.metrics_MoodSwingsTitle),"moodSwings",-1),
            MetricRowData(context.getString(R.string.metrics_BleedingTitle),"bleeding",-1),
            MetricRowData(context.getString(R.string.metrics_crampLevelTitle),"crampLevel",-1),
            MetricRowData(context.getString(R.string.metrics_CravingsTitle),"cravings",-1),
            MetricRowData(sharedPrefs.getString("customMetric1","custom1-missing")!!,"customColumn1",-1),
            MetricRowData(sharedPrefs.getString("customMetric2","custom2-missing")!!,"customColumn2",-1),
            MetricRowData(sharedPrefs.getString("customMetric3","custom3-missing")!!,"customColumn3",-1),
        )

        val metricRowsState=if(gender==0){mutableStateOf(femaleMetrics)} else{mutableStateOf(transMetrics)}
        val gson=Gson()

        val metricPersistence2List=metricRowsState.value.mapIndexed{index,metric->
            MetricPersistence2(metricName=metric.metricName,order=index,visible=metric.visible,title=metric.title,selectedIndex=metric.selectedIndex)
        }
        val metrics2Json=gson.toJson(metricPersistence2List)
        sharedPrefs.edit().putString("metricsOrder2", metrics2Json).putBoolean(Constants.metricsSetUp,true).apply()
    }


    fun calculateIntermediateColors(colorMin:Color,colorMax:Color,numberOfColors:Int):MutableList<Color>{
        val colors=mutableListOf<Color>()
        for(i in 0 until numberOfColors) {
            val fraction=i.toFloat()/(numberOfColors-1)
            val red=colorMin.red+(colorMax.red-colorMin.red)*fraction
            val green=colorMin.green+(colorMax.green-colorMin.green)*fraction
            val blue=colorMin.blue+(colorMax.blue-colorMin.blue)*fraction
            colors.add(Color(red,green,blue))
        }
        return colors
    }


}
