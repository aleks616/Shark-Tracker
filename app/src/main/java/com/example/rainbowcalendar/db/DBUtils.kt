package com.example.rainbowcalendar.db

import android.content.Context
import android.util.Log
import com.example.rainbowcalendar.cycleDao
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

object DBUtils{
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
            result.set(cycleDao.doesDateExistForCycleId(date,cycleId))
        }
        thread.start()
        thread.join()

        return result.get()
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
    fun addNewDateCycle(context:Context,dateCycle:DateCycle):Boolean{
        val result=AtomicBoolean(false)

        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        val thread=Thread{
            val cycleTypeExists:Boolean=cycleDao.doesCycleExist(dateCycle.cycleId)
            if(cycleTypeExists){
                val canInsert=!cycleDao.doesDateExistForCycleId(dateCycle.date,dateCycle.cycleId)
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

    fun getActiveCycles(context:Context):List<Cycles>{
        var data=listOf(Cycles(0,"",0,false))
        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        val thread=Thread{
            data=cycleDao.getActiveCycleTypes()
        }
        thread.start()
        thread.join()
        return data
    }


    fun getCyclesDataDate(context:Context,date:String):List<CyclesDateCycle>{
        var data=listOf<CyclesDateCycle>()
        cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
        val thread=Thread{
            data=cycleDao.getAllCyclesDataDate(date)
        }
        thread.start()
        thread.join()

        return data
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

}