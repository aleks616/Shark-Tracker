package com.example.rainbowcalendar.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName="metrics")
data class Cycle(
    @PrimaryKey
    @ColumnInfo(name="date")
    val date:String,

/*    @ColumnInfo(name="cycleDay")
    val cycleDay:Int?=null,*/

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

@Entity(tableName="Cycles")
data class Cycles(
    @PrimaryKey(autoGenerate=true)
    @ColumnInfo(name="cycleId")
    val cycleId:Int,
    @ColumnInfo(name="cycleName")
    val cycleName:String,
    @ColumnInfo(name="correctLength")
    val correctLength:Int,
    @ColumnInfo(name="isActive")
    val isActive:Boolean
)

@Entity(
    primaryKeys=["date","cycleId"],
    foreignKeys=[
        ForeignKey(
            entity=Cycles::class,
            parentColumns=["cycleId"],
            childColumns=["cycleId"],
            onDelete=ForeignKey.CASCADE
        ),
    ],
    indices=[Index("cycleId")]
)
data class DateCycle(
    val date:String,
    val cycleId:Int,
    val cycleDay:Int
)