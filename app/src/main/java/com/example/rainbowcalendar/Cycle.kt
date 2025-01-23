package com.example.rainbowcalendar

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="cycle")
data class Cycle(
    @PrimaryKey
    @ColumnInfo(name="date")
    val date:String,

    @ColumnInfo(name="cycleDay")
    val cycleDay:Int?=null,

    @ColumnInfo(name="crampLevel")
    val crampLevel:Int?=null,

    @ColumnInfo(name="headache")
    val headache:Int?=null,

    @ColumnInfo(name="energyLevel")
    val energyLevel:Int?=null,

    @ColumnInfo(name="sleepQuality")
    val sleepQuality:Int?=null,

    @ColumnInfo(name="cravings")
    val cravings:Int?=null,

    @ColumnInfo(name="skinCondition")
    val skinCondition:Int?=null,

    @ColumnInfo(name="digestiveIssues")
    val digestiveIssues:Int?=null,

    @ColumnInfo(name="moodSwings")
    val moodSwings:Int?=null,

    @ColumnInfo(name="overallMood")
    val overallMood:Int?=null,

    @ColumnInfo(name="kcalBalance")
    val kcalBalance:Int?=null,

    @ColumnInfo(name="dysphoria")
    val dysphoria:Int?=null,

    @ColumnInfo(name="bleeding")
    val bleeding:Int?=null,

    @ColumnInfo(name="musclePain")
    val musclePain:Int?=null,

    @ColumnInfo(name="weight")
    val weight:Int?=null,

    @ColumnInfo(name="customColumn1")
    val customColumn1:Int?=null,

    @ColumnInfo(name="customColumn2")
    val customColumn2:Int?=null,

    @ColumnInfo(name="customColumn3")
    val customColumn3:Int?=null,

    @ColumnInfo(name="notes")
    val notes:String?=null
)