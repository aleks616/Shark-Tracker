package com.example.rainbowcalendar.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface CycleDao {
    @Insert
    fun insert(cycle:Cycle)

    @Update
    fun update(cycle:Cycle)

    @Delete
    fun delete(cycle:Cycle)

    @Query("DELETE FROM metrics")
    fun deleteAllMetrics()

    @Query("SELECT * FROM metrics ORDER BY date ASC")
    fun getAllMetricData():LiveData<List<Cycle>>

    @Query("SELECT * FROM metrics ORDER BY date DESC")
    fun getAllMetricsSync():List<Cycle>

    @Query("SELECT date FROM metrics ORDER BY date(date) ASC LIMIT 1")
    fun getFirstDate():String

    @Query("SELECT * FROM metrics WHERE date=:date LIMIT 1")
    fun getCycleByDate(date:String):Cycle?

/*    @Query("SELECT * FROM metrics WHERE cycleDay=:cycleDay")
    fun getCyclesByCycleDay(cycleDay:Int):LiveData<List<Cycle>>

    @Query("UPDATE metrics SET cycleDay=:cycleDay WHERE date=:date")
    fun updateCycleDay(date:String,cycleDay:Int?)*/

    @Insert
    fun addNewCycle(cycle:Cycles)

    @Query("SELECT * FROM Cycles")
    fun getAllCyclesTypes():List<Cycles>?

    @Query("DELETE FROM Cycles")
    fun deleteAllCycles()

    @Query("SELECT * FROM cycles WHERE cycleName==:cycleName")
    fun getCycleDataByName(cycleName:String):Cycles

    @Query("UPDATE cycles SET cycleName=:newCycleName WHERE cycleId=:cycleId")
    fun changeCycleTypeName(newCycleName:String,cycleId:Int)

    @Query("UPDATE cycles SET correctLength=:newCorrectInterval WHERE cycleId=:cycleId")
    fun changeCycleTypeCorrectInterval(newCorrectInterval:Int,cycleId:Int)

    @Query("SELECT cycleId FROM cycles WHERE cycleName=:name")
    fun getCycleIdByName(name:String):Int

    @Insert
    fun addNewDateCycle(dateCycle:DateCycle)


    @Query("SELECT EXISTS(SELECT 1 FROM datecycle WHERE date=:date)")
    fun doesDateExist(date:String):Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM Cycles WHERE cycleId=:cycleId)")
    fun doesCycleExist(cycleId:Int):Boolean

    @Query("UPDATE datecycle SET cycleDay=:cycleDay AND cycleId=:cycleId WHERE date=:date")
    fun updateDateCycle(cycleId:Int,cycleDay:Int,date:String)

    @Query("SELECT date, overallMood FROM metrics")
    fun getMoodData():List<Cycle>

    @Query(
        """UPDATE metrics SET crampLevel=:crampLevel,headache=:headache,
            energyLevel=:energyLevel,sleepQuality=:sleepQuality,cravings=:cravings,
            skinCondition=:skinCondition,digestiveIssues=:digestiveIssues,moodSwings=:moodSwings,
            overallMood=:overallMood,kcalBalance=:kcalBalance,dysphoria=:dysphoria, musclePain=:musclePain, bleeding=:bleeding, weight=:weight,
            customColumn1=:customColumn1,customColumn2=:customColumn2,customColumn3=:customColumn3,notes=:notes WHERE date=:date"""
    )
    fun updateAllMetrics(date:String,crampLevel:Int?,headache:Int?,energyLevel:Int?,sleepQuality:Int?,
                         cravings:Int?,skinCondition:Int?,digestiveIssues:Int?,moodSwings:Int?,overallMood:Int?,kcalBalance:Int?,
                         dysphoria:Int?,musclePain:Int?,bleeding:Int?,weight:Int?,
                         customColumn1:Int?,customColumn2:Int?,customColumn3:Int?,notes:String?)
}