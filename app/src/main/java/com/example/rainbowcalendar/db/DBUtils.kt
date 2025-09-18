package com.example.rainbowcalendar.db

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.rainbowcalendar.TimeUtils
import com.example.rainbowcalendar.cycleDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

object DBUtils{

    //region select
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
    fun doesDateExistForCycleId(context:Context,date:String,cycleId:Int):Boolean{
        val result=AtomicBoolean(false)
        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()

        val thread=Thread{
            //result.set(cycleDao.doesDateExistForCycleId(date,cycleId))
            result.set(cycleDao.dataForCycleIdDate(date,cycleId)!=0)
        }
        thread.start()
        thread.join()

        return result.get()
    }
    fun getCycleDayByIdAndDate(context:Context,cycleId:Int,date:String):Int{
        if(!TimeUtils.isValidDate(date)) return -1
        val exists=doesDateExistForCycleId(context,date,cycleId)
        if(!exists) return -1
        var day:Int=-1
        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        val thread=Thread{
            day=cycleDao.getCycleDayByIdAndDate(cycleId,date)?:-1
        }
        thread.start()
        thread.join()
        Log.i("day",day.toString())

        return day
    }

    fun getActiveCycles(context:Context):List<Cycles>{
        var data=listOf(Cycles(0,"",0,false,-1))
        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        val thread=Thread{
            data=cycleDao.getActiveCycleTypes()
        }
        thread.start()
        thread.join()
        return data
    }
    fun getInactiveCyclesOfType(context:Context,cycleType:Int):List<OldCycleData>{
        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        val data=mutableListOf(OldCycleData(0,cycleType,"","",""))
        var inactiveCycles:List<Cycles>
        val thread=Thread{
            inactiveCycles=cycleDao.getInactiveCycleTypes()
            inactiveCycles.forEach{
                Log.v("archive cycles",it.cycleName)
                data+=OldCycleData(it.cycleId,it.cycleType,it.cycleName,cycleDao.geFirstCycleDay(it.cycleId).date,cycleDao.getLastCycleDaySync(it.cycleId).date) //todo: test, upgrade
            }
        }
        thread.start()
        thread.join()

        if(data[0].firstDate=="") data.removeAt(0)
        return data //todo: FIX, NOW IF THERE IS NO DATE IT CRASHES (which shouldn't be possible, but edge cases)
    }


    fun lastCycleDay(context:Context,cycleId:Int?=2):DateCycle{
        try{
            val newCycleId=cycleId?:2
            cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
            var cycle=DateCycle("",0,-1)
            var cycles=listOf<DateCycle>()
            val thread=Thread{
                cycles=cycleDao.getAllCycleDatesDays(newCycleId)
            }
            thread.start()
            thread.join()

            runBlocking{
                withContext(Dispatchers.IO){
                    cycle=cycleDao.getLastCycleDaySync(newCycleId)
                }
            }

            cycles.forEach{
                Log.v("cycles",it.cycleId.toString()+" "+it.date+" "+it.cycleDay)
            }
            Log.w("last cycle day",cycleId.toString()+" "+cycle.cycleId+" "+cycle.date)
            return cycle
        }
        catch(e:Exception){
            Log.e("ERROR",e.message.toString())
            return DateCycle("",0,0)
        }

    }

    fun getCyclesDataDate(context:Context,date:String):List<CyclesDateCycle>{
        var data=listOf<CyclesDateCyclePartial>()
        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        val thread=Thread{
            data=cycleDao.getAllActiveCyclesData1()

        }
        thread.start()
        thread.join()

        val finalData=mutableListOf<CyclesDateCycle>()
        val thread1=Thread{
            data.forEach{
                val day:Int=cycleDao.getCycleDayByIdAndDate(cycleDao.getCycleIdByName(it.cycleName),date)?:-1
                finalData+=CyclesDateCycle(it.cycleName,it.isActive,day,it.correctLength,it.cycleType)
            }
        }
        thread1.start()
        thread1.join()

        return finalData
    }
    fun getCycleStartDays(context:Context,cycleId:Int):ArrayList<Pair<String,Int?>>{
        var data=mutableListOf(DateCycle("",0,0))
        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        val thread=Thread{
            data=cycleDao.getCycleStartDates(cycleId).toMutableList()
        }
        thread.start()
        thread.join()
        if(data[0].date=="") data.removeAt(0)
        try{
            val startDates=ArrayList<Pair<String, Int?>>()
            data.reversed().forEachIndexed{i,it->
                startDates+=Pair(it.date,0)
                val length:Int?=if(i>0) TimeUtils.dateDiffString(startDates[i-1].first,it.date) else null
                /*if(i>0) Log.i("dates420",startDates[i-1].first+" "+startDates[i-1].second)
                if(i>0) Log.i("length420",length.toString())*/
                if(i>0) startDates[i-1]=Pair(startDates[i-1].first,length)
                //if(i>0) Log.i("dates4201",startDates[i-1].first+" "+startDates[i-1].second)
            }
            return startDates
        }
        catch(e:Exception){
            Log.v("list",e.toString())
            return arrayListOf(Pair("",0))
        }
    }

    fun getCycleLengthData(context:Context,cycleId:Int):List<Int?>{
        var data=mutableListOf(DateCycle("",0,0))
        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        val thread=Thread{
            data=cycleDao.getCycleStartDates(cycleId).toMutableList()
        }
        thread.start()
        thread.join()

        val lengthData=mutableListOf<Int?>()
        val startDates=ArrayList<Pair<String, Int?>>()
        data.reversed().forEachIndexed{i,it->
            startDates+=Pair(it.date,0)
            val length:Int?=if(i>0) TimeUtils.dateDiffString(startDates[i-1].first,it.date) else null
            if(i>0){
                startDates[i-1]=Pair(startDates[i-1].first,length)
                lengthData.add(length)
            }
        }

        return lengthData
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

    fun hasDysphoria(context:Context):Boolean{
        val data=getAllMoodData(context)
        var hasDysphoria=false
        data.forEach{day->
            if(day.dysphoria!=null)
                hasDysphoria=true
        }
        return hasDysphoria
    }

    fun getFirstMetricDate(context:Context):String?{
        var date:String?=""
        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        val thread=Thread{
            date=cycleDao.getFirstDate()
        }
        thread.start()
        thread.join()
        return date
    }
    //endregion

    //region insert
    fun autoAddCycleDayData(context:Context/*,date:String*/){
        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        val activeCycles=DBUtils.getActiveCycles(context)
        val lastCycle=mutableListOf<DateCycle>()
        val today=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(Calendar.getInstance().time)

        activeCycles.forEachIndexed{i,it->
            //var temp=DateCycle("",-1,-1)
            Log.v("cycle2137",it.cycleId.toString()+" "+it.cycleName)
            /*runBlocking{
                withContext(Dispatchers.IO){
                    //Log.i("data result",cycleDao.getLastCycleDay(it.cycleId).date)
                    temp=cycleDao.getLastCycleDay(it.cycleId)
                    //Log.i("nowData2137",it.cycleId.toString()+" "+temp.date)
                }
            }*/
            /*val thread=Thread{
                Log.i("data result",cycleDao.getLastCycleDay(it.cycleId).date)
                temp=cycleDao.getLastCycleDay(it.cycleId)
                Log.i("nowData2137",it.cycleId.toString()+" "+temp.date)
            }
            thread.start()
            thread.join()*/
            //Log.v("data last","cycleId: "+it.cycleId.toString()+" cycleName: "+it.cycleName+" lastDate: "+lastCycle.last().date+"  lastDay: "+lastCycle.last().cycleDay)

            Log.e("data cycle id last",it.cycleId.toString())
            lastCycle+=DBUtils.lastCycleDay(context,it.cycleId)
            var date=lastCycle[lastCycle.size-1].date
            var cycleDay=lastCycle[lastCycle.size-1].cycleDay
            try{
                while(TimeUtils.isDate1AfterOrSameAsDate2(today,date)){
                    if(!DBUtils.doesDateExistForCycleId(context,date,it.cycleId))
                        DBUtils.addNewDateCycleNew(context,DateCycle(date,it.cycleId,cycleDay))

                    cycleDay++
                    date=TimeUtils.iterateDateString(date)
                }
            }
            catch(e:Exception){
                Log.e("ERROR",e.toString())
            }
        }
    }

    fun addNewCycleType(context:Context,cycleName:String,correctInterval:Int,active:Boolean=true,cycleType:Int=-1):String{
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
                cycleDao.addNewCycle(Cycles(0,cycleName,correctInterval,active,cycleType))
            else
                Log.e("cycleData","cycle with name is already there")

        }.start()
        return if(canInsert) "" else "fail"
    }
    fun addNewDateCycleNew(context:Context,dateCycle:DateCycle):Boolean{
        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        val result=AtomicBoolean(false)

        runBlocking{
            withContext(Dispatchers.IO){
                val cycleTypeExists:Boolean=cycleDao.doesCycleExist(dateCycle.cycleId)
                if(cycleTypeExists){
                    Log.v("newDateCycle","id: "+dateCycle.cycleId.toString()+" date:"+dateCycle.date+" day: "+dateCycle.cycleDay.toString())
                    cycleDao.addNewDateCycle(DateCycle(dateCycle.date,dateCycle.cycleId,dateCycle.cycleDay))
                    result.set(true)
                }
            }
        }
        return result.get()
    }

    fun debug(context:Context){
        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        var data:List<DateCycle>
        val query=SimpleSQLiteQuery("SELECT date, cycleId, cycleDay FROM datecycle ORDER BY cycleId, date ASC")
        val thread1=Thread{
            data=cycleDao.rawDateCycle(query)
            data.forEach{
                Log.v("raw data",it.cycleId.toString()+" "+it.date+" "+it.cycleDay)
            }
        }
        thread1.start()
        thread1.join()
        //rawDateCycle
    }

    //endregion

    //region update
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
    //endregion

    //region delete
    fun removeFaultyData(context:Context){
        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        Thread{
            cycleDao.removeFaultyData()
            //cycleDao.fix()
        }.start()
    }

    fun deleteAllCycleTypes(context:Context){
        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        Thread{
                cycleDao.deleteAllCycles()
        }.start()
    }
    //endregion

}