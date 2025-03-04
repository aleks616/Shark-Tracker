package com.example.rainbowcalendar

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.core.app.NotificationCompat
import com.example.rainbowcalendar.db.Cycle
import com.example.rainbowcalendar.db.CycleRoomDatabase
import com.example.rainbowcalendar.db.DBUtils
import com.example.rainbowcalendar.db.DateCycle
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object Utils{
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

        val max=if(DBUtils.hasDysphoria(context)) 25 else 22

        if(DBUtils.hasDysphoria(context)){
            score+=(values[1][0]-data[1])*((values[1][1])/(values[1][0]))
        }

        return score/max.toFloat()
    }
    fun autoAddCycleDayData(context:Context/*,date:String*/){
        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        val activeCycles=DBUtils.getActiveCycles(context)
        val lastCycle=mutableListOf<DateCycle>()
        val today=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(Calendar.getInstance().time)

        activeCycles.forEach{
            val thread=Thread{
                lastCycle+=cycleDao.getLastCycleDay(it.cycleId)
            }
            thread.start()
            thread.join()

            Log.v("data","cycleId: "+it.cycleId.toString()+" cycleName: "+it.cycleName+" lastDate: "+lastCycle[lastCycle.size-1].date+"  lastDay: "+lastCycle[lastCycle.size-1].cycleDay)

            var date=lastCycle[lastCycle.size-1].date
            var cycleDay=lastCycle[lastCycle.size-1].cycleDay
            while(TimeUtils.isDate1AfterDate2(today,date)){
                if(!DBUtils.doesDateExistForCycleId(context,date,it.cycleId))
                    DBUtils.addNewDateCycle(context,DateCycle(date,it.cycleId,cycleDay))

                cycleDay++
                date=TimeUtils.iterateDateString(date)
            }
        }
    }
    //todo: i started writing it and then forgot why
    /*fun lastCycleStart(context:Context,cycleName:String):DateCycle{
        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        var lastCycle=DateCycle("",0,0)
        var cycleId:Int
        val thread=Thread{
            cycleId=cycleDao.getCycleIdByName(cycleName)
            lastCycle=cycleDao.getLastCycleDay(cycleId)
        }
        thread.start()
        thread.join()
        Log.v("last cycle start","cycleName: "+cycleName+" lastDate: "+lastCycle.date+"  lastDay: "+lastCycle.cycleDay)
        return lastCycle
    }*/


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

    //region general android logic
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
    //endregion

    //region general logic
    fun canBeIntParsed(text:String):Boolean{
        if(text.isNotEmpty()){
            return text.all{it.isDigit()}
        }
        return false
    }
    fun isStringANumber(text:String):Boolean{
        return text.all{it.isDigit()}
    }
    fun simplify(string:String?):String?{
        return string?.lowercase()?.replace(" ","")
    }
    //endregion

    //region language
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
    //endregion

    //region stealth/censor
    fun censorPeriod(string:String){
        string.replace("period","shark week")
        string.replace("okres","wodospad")
        string.replace("okresie","wodospadzie")
        string.replace("okresu","wodospadu")
        //string.replace("","Semaine rouge")
        /*string.replace("","дни Красной армии")
        string.replace("","днi Червоноï армiï")*/
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

    fun isStealthModeOn(context:Context):Boolean{
        val packageManager=context.packageManager
        val stealth=ComponentName(context,"com.example.rainbowcalendar.MainActivityStealth")

        return packageManager.getComponentEnabledSetting(stealth)==PackageManager.COMPONENT_ENABLED_STATE_ENABLED
    }
    fun toggleStealthMode(context:Context){
        val packageManager=context.packageManager
        val stealth=ComponentName(context,"com.example.rainbowcalendar.MainActivityStealth")
        val default=ComponentName(context,"com.example.rainbowcalendar.fragments.MainActivity")

        val stealthMode=packageManager.getComponentEnabledSetting(stealth)==PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        packageManager.setComponentEnabledSetting((if(stealthMode) default else stealth),PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP)
        packageManager.setComponentEnabledSetting((if(stealthMode) stealth else default),PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP)
    }

    //endregion

    //region notifications

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

    fun binderNotif(context:Context){
        val notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel=NotificationChannel("testId","CHANNEL_NAME",NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)

        val notification:NotificationCompat.Builder=
            NotificationCompat.Builder(context,"testId")
                .setContentTitle("Binding")
                .setContentText("TAKE OFF YOUR BINDER")
                .setSmallIcon(R.drawable.alarm_icon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
        notificationManager.notify(1,notification.build())
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

    //endregion
}
