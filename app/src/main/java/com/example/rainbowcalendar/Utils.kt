package com.example.rainbowcalendar

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.text.BoringLayout
import android.util.Log
import com.google.gson.Gson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
    fun timeSinceDate(start:String):MilestoneDate{
        val today=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(Calendar.getInstance().time)
        val dateFormat=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        dateFormat.isLenient=false

        try{
            val startDate=dateFormat.parse(start)
            val todayDate=dateFormat.parse(today)
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
                cycleDao.addNewCycle(Cycles(0,cycleName,correctInterval,active));
            else
                Log.e("cycleData","cycle with name is already there")

        }.start()
        return if(canInsert) "" else "fail"
    }

    fun deleteAllCycleTypes(context:Context,areYouSure:Boolean){
        if(areYouSure){
            Thread{
                cycleDao.deleteAllCycles()
            }
        }
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
    fun langToCode(lang: String): String{
        return when(lang){
            "English" -> "en"
            "Polski" -> "pl"
            "Francais" -> "fr"
            "Italiano" -> "it"
            "Espanol" -> "es"
            "Português (Brasil)" -> "pt-BR"
            "Русский" -> "ru"
            "українська" -> "uk"
            else ->{
                "en"
            }
        }
    }
    fun langToCodeNew(lang: String): String{
        return when(lang.lowercase()){
            "english" -> "en"
            "polski" -> "pl"
            "français" -> "fr"
            "italiano" -> "it"
            "español" -> "es"
            "português" -> "pt-BR"
            "русский" -> "ru"
            "українська" -> "uk"
            else ->{
                "en"
            }
        }
    }
    fun codeToLanguage(code:String):String{
        return when(code.lowercase()){
            "en" -> "English"
            "pl" -> "Polski"
            "fr" -> "Français"
            "it" -> "italiano"
            "es" -> "Español"
            "pt" -> "Português"
            "pt-br" -> "Português"
            "ru" -> "Русский"
            "uk" -> "Українська"
            else->{
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

        val sharedPrefs=context.getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)
        sharedPrefs.edit().putString("lang",lang).apply()
        if (context is Activity){
            context.recreate()
        }
    }
    fun smth(context:Context){
        val sharedPrefs=context.getSharedPreferences("com.example.rainbowcalendar_pref",Context.MODE_PRIVATE)
        val gson=Gson()
        val customName1=sharedPrefs.getString("customMetric1","custom1-missing")!!
        val customName2=sharedPrefs.getString("customMetric2","custom2-missing")!!
        val customName3=sharedPrefs.getString("customMetric3","custom-missing")!!


        val metricRowsList=listOf(
            MetricPersistence(context.getString(R.string.metrics_crampLevelTitle),8,false),
            MetricPersistence(context.getString(R.string.metrics_headacheTitle),0,true),
            MetricPersistence(context.getString(R.string.metrics_energyLevelTitle),5,true),
            MetricPersistence(context.getString(R.string.metrics_SleepQualityTitle),4,true),
            MetricPersistence(context.getString(R.string.metrics_CravingsTitle),9,true),
            MetricPersistence(context.getString(R.string.metrics_SkinConditionTitle),2,true),
            MetricPersistence(context.getString(R.string.metrics_DigestiveIssuesTitle),3,true),
            MetricPersistence(context.getString(R.string.metrics_MoodSwingsTitle),6,true),
            MetricPersistence(context.getString(R.string.metrics_OverallMoodTitle),11,true),
            MetricPersistence(context.getString(R.string.metrics_DysphoriaTitle),10,true),
            MetricPersistence(context.getString(R.string.metrics_BleedingTitle),7,true),
            MetricPersistence(context.getString(R.string.metrics_MusclePainTitle),1,true),
            MetricPersistence(customName1,12,false),
            MetricPersistence(customName2,13,false),
            MetricPersistence(customName3,14,false)
        )
        //TODO: SWITCH ORDERS BASED ON SETTINGS, GENDER ETC, SWITCH 0-11 ORDER

        checkOrder(metricRowsList)

        val metricsJson=gson.toJson(metricRowsList)
        sharedPrefs.edit().putString("metricsOrder",metricsJson).apply()
    }

    private fun checkOrder(metricList:List<MetricPersistence>){
        for(i in 0..11){
            for(j in 0..11){
                if(i!=j&&metricList[i].order==metricList[j].order)
                    Log.e("metric error",metricList[i].metricName+" and "+metricList[j].metricName+" have same order value!!!")
            }
        }
    }

    private fun swapOrder(metricList:List<MetricPersistence>,pos1:Int,pos2:Int):MutableList<MetricPersistence>{
        val metricList1=metricList.toMutableList()
        val temp=pos1
        metricList1[pos1]=metricList1[pos2]
        metricList1[pos2]=metricList1[temp]
        return metricList1
    }
    private fun setOrder(metricList:List<MetricPersistence>){
        val orderList=listOf<Int>(8,0,5,4,9,2,3,6,11,10,7,1)
        val maxIndex=11
        /*if(period woman){
            val orderList=listOf<Int>(8,0,5,4,9,2,3,6,11,10,7,1)
            val maxIndex=10
            for(i in 0..maxIndex){
                //switch items around so there are no holes
            }
        }*/
    }
}