package com.example.rainbowcalendar.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery


@Dao
interface CycleDao {
    @Insert(onConflict=OnConflictStrategy.REPLACE)
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

    @Query("SELECT * FROM cycles WHERE isActive=1")
    fun getActiveCycleTypes():List<Cycles>

    @Query("SELECT * FROM cycles WHERE isActive=0")
    fun getInactiveCycleTypes():List<Cycles>

    @Query("SELECT c.cycleName,c.isActive,d.cycleDay,c.correctLength,c.cycleType FROM cycles c JOIN datecycle d ON c.cycleId=d.cycleId WHERE d.date=:date")
    fun getAllCyclesDataDate(date:String):List<CyclesDateCycle>

    @Query("SELECT c.cycleName,c.isActive,d.cycleDay,c.correctLength,c.cycleType FROM cycles c JOIN datecycle d ON c.cycleId=d.cycleId WHERE d.date=:date AND c.isActive=1")
    fun getAllActiveCyclesDataDate(date:String):List<CyclesDateCycle>

    @Query("SELECT c.cycleName,c.isActive,c.correctLength,c.cycleType FROM cycles c JOIN datecycle d ON c.cycleId=d.cycleId WHERE c.isActive=1")
    fun getAllActiveCyclesData1():List<CyclesDateCyclePartial>

    @Query("SELECT cycleId,MAX(date) AS 'date',cycleDay FROM datecycle WHERE cycleId=:cycleId")
    suspend fun getLastCycleDay(cycleId:Int):DateCycle

    @Query("SELECT cycleId,date,cycleDay FROM datecycle WHERE cycleId=:cycleId ORDER BY date DESC LIMIT 1")
    fun getLastCycleDaySync(cycleId:Int):DateCycle

  /*  @RawQuery
    fun rawDateCycle(query:Query):DateCycle*/

    @Query("SELECT cycleId,date,cycleDay FROM datecycle WHERE cycleId=:cycleId ORDER BY date ASC LIMIT 1")
    fun geFirstCycleDay(cycleId:Int):DateCycle


    /*@Query("SELECT ")
    fun getInactiveCycleData():List<OldCycleData>*/

    @Query("SELECT date FROM metrics ORDER BY date ASC LIMIT 1")
    fun getFirstDate():String?

    @Query("SELECT * FROM metrics WHERE date=:date LIMIT 1")
    fun getCycleByDate(date:String):Cycle?

    @Query("SELECT * FROM datecycle WHERE cycleDay=0 AND cycleId=:cycleId ORDER BY date DESC")
    fun getCycleStartDates(cycleId:Int):List<DateCycle>


    @Query("SELECT * FROM datecycle WHERE cycleId=:cycleId ORDER BY date ASC")
    fun getAllCycleDatesDays(cycleId:Int):List<DateCycle>

    @Insert(onConflict=OnConflictStrategy.REPLACE)
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

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun addNewDateCycle(dateCycle:DateCycle)

    @Query("INSERT INTO datecycle VALUES (:date,:id,:day)")
    fun insertDateCycle(id:Int, date:String, day:Int)


    @Query("SELECT EXISTS(SELECT 1 FROM datecycle WHERE date=:date)")
    fun doesDateExist(date:String):Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM datecycle WHERE date=:date AND cycleId=:cycleId)")
    fun doesDateExistForCycleId(date:String,cycleId:Int):Boolean

    @Query("SELECT COUNT() FROM datecycle WHERE date=:date AND cycleId=:cycleId")
    fun dataForCycleIdDate(date:String,cycleId:Int):Int?

    @RawQuery
    fun rawDateCycle(query:SupportSQLiteQuery):List<DateCycle>

    @Query("SELECT EXISTS(SELECT 1 FROM Cycles WHERE cycleId=:cycleId)")
    fun doesCycleExist(cycleId:Int):Boolean

    @Query("SELECT cycleDay FROM datecycle WHERE cycleId=:cycleId AND date=:date")
    fun getCycleDayByIdAndDate(cycleId:Int,date:String):Int?



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
//date='2005' OR date='2008' OR date='2009'
    @Query("DELETE FROM datecycle WHERE date='2023-04-20'")
    fun removeFaultyData()

    @Query("UPDATE datecycle SET date='2025-03-04' WHERE cycleId=0 AND cycleDay=0")
    fun fix()
}