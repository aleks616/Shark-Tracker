package com.example.rainbowcalendar

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

    @Query("DELETE FROM cycle")
    fun deleteAll()

    @Query("SELECT * FROM cycle ORDER BY date ASC")
    fun getAllCycles():LiveData<List<Cycle>>

    @Query("SELECT * FROM cycle WHERE date = :date LIMIT 1")
    fun getCycleByDate(date:String):LiveData<Cycle?>

    @Query("SELECT * FROM cycle WHERE cycleDay = :cycleDay")
    fun getCyclesByCycleDay(cycleDay:Int):LiveData<List<Cycle>>

    @Query("UPDATE cycle SET cycleDay = :cycleDay WHERE date = :date")
    fun updateCycleDay(date:String,cycleDay:Int?)

    @Query(
        """UPDATE cycle SET crampLevel = :crampLevel, headache = :headache,
            energyLevel = :energyLevel,sleepQuality = :sleepQuality,cravings = :cravings,
            skinCondition = :skinCondition, digestiveIssues = :digestiveIssues,moodSwings = :moodSwings,
            overallMood = :overallMood, kcalBalance=:kcalBalance, dysphoria = :dysphoria,customColumn1 = :customColumn1, 
            customColumn2 = :customColumn2,customColumn3 = :customColumn3, notes = :notes WHERE date = :date"""
    )
    fun updateAllFields(date:String, crampLevel:Int?,headache:Int?,energyLevel:Int?,sleepQuality:Int?,
                        cravings:Int?,skinCondition:Int?,digestiveIssues:Int?,moodSwings:Int?,overallMood:Int?,kcalBalance:Int?,
                        dysphoria:Int?,customColumn1:Int?,customColumn2:Int?,customColumn3:Int?,notes:String?)
}