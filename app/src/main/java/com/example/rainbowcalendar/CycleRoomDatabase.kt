package com.example.rainbowcalendar

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities=[Cycle::class],version=1,exportSchema=false)
abstract class CycleRoomDatabase:RoomDatabase() {
    abstract fun cycleDao():CycleDao

    companion object{
        @Volatile
        private var INSTANCE:CycleRoomDatabase?=null

        fun getDatabase(context:Context):CycleRoomDatabase {
            return INSTANCE?: synchronized(this){
                val instance=Room.databaseBuilder(context.applicationContext,CycleRoomDatabase::class.java,"RainbowCalendar.db").fallbackToDestructiveMigration().build()
                INSTANCE=instance
                instance
            }
        }
    }


}
