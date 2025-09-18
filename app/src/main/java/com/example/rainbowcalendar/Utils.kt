package com.example.rainbowcalendar
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.graphics.PathParser
import com.example.rainbowcalendar.db.Cycle
import com.example.rainbowcalendar.db.DBUtils
import com.google.gson.Gson
import java.lang.Math.abs
import java.lang.Math.sqrt
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

    /**
     * doesn't take into account years 2400 2800 and so on
     * @param month 1-12
     * @param year by default 2025
     * @return how many days month has
     * **/
    private fun maxMonthDay(year:Int=2025,month:Int):Int{
        if(!(1..12).contains(month)) return 0

        val days31=listOf(1,3,5,7,8,10,12)
        val days30=listOf(4,6,8,11)
        return if(days31.contains(month)) 31 else if(days30.contains(month)) 30 else if(year%4==0) 29 else 28
    }

    fun showLogChanges(firstTDate:String):Boolean{
        val today=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(Calendar.getInstance().time)
        val todayInt=TimeUtils.intDateFromStringDate(today)
        val firstTInt=TimeUtils.intDateFromStringDate(firstTDate)
        val tDay=TimeUtils.dateDiffString(firstTDate,today)

        val fullMonths=(todayInt[2]==firstTInt[2]) /**same month day, eg 2025-02-04 and 2025-03-04**/
                ||((maxMonthDay(todayInt[0],todayInt[1])<maxMonthDay(firstTInt[0],firstTInt[1]))&&todayInt[2]==maxMonthDay(todayInt[0],todayInt[1]))

        val halfYears=fullMonths&&(kotlin.math.abs(todayInt[1]-firstTInt[1])==6)
        val fullYears=fullMonths&&(todayInt[1]==firstTInt[1])


        return ((tDay<=35&&tDay%7==0) /**every week till day 35/week 5**/
                ||(tDay<366&&fullMonths) /**every month till year**/
                ||tDay<1300&&halfYears /**1300 is between 3.5 and 4 years, last half year will be 3.5 **/
                ||fullYears) /**every year**/
    }



    fun calcAverageCycleLength(context:Context,cycleId:Int,storedValue:Int):Int{
        val data=DBUtils.getCycleLengthData(context,cycleId).reversed()
        val correctData=mutableListOf<Int?>()
        data.forEach{
            if((18..42).contains(it)) correctData+=it
        }
        val size=(correctData.size).coerceAtMost(20)

        //Log.v("calculating","size: "+size.toString())

        var result=0
        correctData.forEachIndexed{i,it->
            if(it!=null){
              //Log.v("calculating","data[$i]: "+it.toString())
                //((2*size)-i)
              //Log.v("calculating","weight for data[$i]: "+((2*size)-i).toString())
              result+=it*((2*size)-i)
              //Log.v("calculating","multipled for [$i]: "+(it*((2*size)-i)).toString())
            }
        }
        //Log.v("calculating","partial result: "+result.toString())

        val dSize=size.toDouble()

        val partialWeightSum:Double=((3*dSize*dSize)+dSize)/2
        //Log.d("calculating","partial weight sum: "+partialWeightSum.toString())
        val avgWeight:Double=partialWeightSum/dSize
        //Log.v("calculating","avg weight: "+avgWeight.toString())

        val storedWeight:Double=avgWeight*(3/kotlin.math.sqrt(dSize))
        //Log.d("calculating","stored weight: "+storedWeight.toString())
        val weightSum=partialWeightSum+storedWeight
        //Log.v("calculating","full weight sum: "+weightSum.toString())

        val storedMultiplied=storedValue.toDouble()*storedWeight
        //Log.d("calculating","multiplied valuie for stored value: "+storedMultiplied.toString())
        result+=storedMultiplied.toInt()
        //Log.v("calculating","final sum: "+result.toString())

        return (result.toDouble()/weightSum).toInt()
    }

    fun getPeriodPhase(cycleDay:Int,expectedLength:Double,bleedingDays:Int):String{
        Log.i("phase","bleeding days $bleedingDays cycle day $cycleDay expected length $expectedLength")
        return if(bleedingDays>cycleDay) "MENstruation" else if(cycleDay-1<expectedLength/2) "Follicular"
        else if(cycleDay-1<expectedLength*0.6f) "Ovulation" else "Luteal"
    }

    //region localization logic
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

    //endregion

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

    fun arePathsTheSame(path:Path,pathData:String):Boolean{
        val targetPath=PathParser.createPathFromPathData(pathData)
        val bounds=RectF()
        path.computeBounds(bounds,true)
        val targetBounds=RectF()
        targetPath.computeBounds(targetBounds,true)
        if(bounds!=targetBounds)return false
        val region=Region()
        region.setPath(path,Region(
            bounds.left.toInt(),
            bounds.top.toInt(),
            bounds.right.toInt(),
            bounds.bottom.toInt()
        ))
        val targetRegion=Region()
        targetRegion.setPath(targetPath,Region(
            targetBounds.left.toInt(),
            targetBounds.top.toInt(),
            targetBounds.right.toInt(),
            targetBounds.bottom.toInt()
        ))
        return region.bounds==targetRegion.bounds
    }
    //endregion

    //region general logic
    fun capitalize(text:String):String{
        return text.lowercase().replaceFirstChar{it.uppercase()}
    }
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

    //region shot site management

    /**
     * @param red, which shot spot is red
     * @param yellow, which shot spot is yellow
     * ids: 0-left abdomen 1-right abdomen, 2-left thigh, 3-right thigh, 4-left buttock, 5-right buttock
     * -1 means none are red/yellow
     * @return drawable ids of FRONT of body and BACK of body, or -1 if params are incorrect
     *
     * red=0, yellow=-1, function returns (R.drawable.body_left_abdomen_red,R.drawable.body_back green)
     * **/
    fun musclesStateToImages(red:Int,yellow:Int):Array<Int>{
        if(!(-1..5).contains(red)||!(-1..5).contains(yellow)) return arrayOf(-1)

        var front:Int=-1
        var back:Int=-1
        if((4..5).contains(red)||(4..5).contains(yellow)){
            if(red==4&&yellow==5)return arrayOf(R.drawable.body_front_green,R.drawable.body_left_butt_red_right_butt_yellow)
            else if(red==5&&yellow==4)return arrayOf(R.drawable.body_front_green,R.drawable.body_right_butt_red_left_butt_yellow)

            else if(red==4) back=R.drawable.body_left_butt_red /**at least one front part is non-green**/
            else if(yellow==4) back=R.drawable.body_left_butt_yellow
            else if(red==5) back=R.drawable.body_right_butt_red
            else if(yellow==5) back=R.drawable.body_right_butt_yellow

            /**back isn't all green so FRONT HAS ONE non green element**/
            if(-1==red) front=R.drawable.body_front_green
            else if(-1==yellow) front=R.drawable.body_front_green
            if(0==red) front=R.drawable.body_left_abdomen_red
            else if(0==yellow) front=R.drawable.body_left_abdomen_yellow
            if(1==red) front=R.drawable.body_right_abdomen_red
            else if(1==yellow) front=R.drawable.body_right_abdomen_yellow
            if(2==red) front=R.drawable.body_left_thigh_red
            else if(2==yellow) front=R.drawable.body_left_thigh_yellow
            if(3==red) front=R.drawable.body_right_thigh_red
            else if(3==yellow) front=R.drawable.body_right_thigh_yellow

            if(front!=-1&&back!=-1)return arrayOf(front,back)
        }
        else{
            back=R.drawable.body_back_green
        }
        /**2 FRONT ITEMS ARE RED/YELLOW, 0-3**/
        if(red==0){
            when(yellow){
                1->front=R.drawable.body_left_abdomen_red_right_abdomen_yellow
                2->front=R.drawable.body_left_abdomen_red_left_thigh_yellow
                3->front=R.drawable.body_left_abdomen_red_right_thigh_yellow
                -1->front=R.drawable.body_left_abdomen_red
            }
        }
        else if(red==1){
            when(yellow){
                0->front=R.drawable.body_right_abdomen_red_left_abdomen_yellow
                2->front=R.drawable.body_right_abdomen_red_left_thigh_yellow
                3->front=R.drawable.body_right_abdomen_red_right_thigh_yellow
                -1->front=R.drawable.body_right_abdomen_red
            }
        }
        else if(red==2){
            when(yellow){
                0->front=R.drawable.body_left_thigh_red_left_abdomen_yellow
                1->front=R.drawable.body_left_thigh_red_right_abdomen_yellow
                3->front=R.drawable.body_left_thigh_red_right_thigh_yellow
                -1->front=R.drawable.body_left_thigh_red
            }
        }
        else if(red==3){
            when(yellow){
                0->front=R.drawable.body_right_thigh_red_left_abdomen_yellow
                1->front=R.drawable.body_right_thigh_red_right_abdomen_yellow
                2->front=R.drawable.body_right_thigh_red_left_thigh_yellow
                -1->front=R.drawable.body_left_thigh_red
            }
        }
        else if(red==-1){
            when(yellow){
                0->front=R.drawable.body_left_abdomen_yellow
                1->front=R.drawable.body_right_abdomen_yellow
                2->front=R.drawable.body_left_thigh_yellow
                3->front=R.drawable.body_right_thigh_yellow
                -1->front=R.drawable.body_front_green
            }
        }
        /*if(front!=-1)*/ return arrayOf(front,back)




        //return arrayOf(R.drawable.body_front_green,R.drawable.body_back_green)
    }

    /**
     * @param shotIn 0 left abdomen 1 right abdomen, 2 left thigh 3 right thigh, 4 left buttock, 5 right buttock
     * **/
    fun saveShotOrder(context:Context,shotIn:Int){
        if((0..5).contains(shotIn)){
            val sharedPrefs=context.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
            val shotHistory=sharedPrefs.getString("shotOrderHistory","")!!
            val toSave=if(shotHistory.isNotEmpty()) "$shotHistory;$shotIn" else shotIn.toString()
            //Log.v("saving muscle",toSave)
            sharedPrefs.edit().putString("shotOrderHistory",toSave).apply()
        }
    }

    fun readShotOrder(context:Context):List<String>{
        val sharedPrefs=context.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
        val shotHistory=sharedPrefs.getString("shotOrderHistory","")!!
        //Log.v("muscle shot history",shotHistory)
        return shotHistory.split(";")
    }

    /**
     * @return pair, first is muscle id for red, second is muscle id for yellow
     * **/
    fun getMuscleStates(context:Context):Pair<Int,Int>{
        val shotOrder=readShotOrder(context)
        //Log.v("muscle",shotOrder)
        return if(shotOrder.isEmpty()||shotOrder[0]=="") Pair(-1,-1)
        else if(shotOrder.size==1) Pair(shotOrder[0].toInt(),-1)
        else Pair(shotOrder[shotOrder.size-1].toInt(),shotOrder[shotOrder.size-2].toInt())
    }


    //endregion
}
