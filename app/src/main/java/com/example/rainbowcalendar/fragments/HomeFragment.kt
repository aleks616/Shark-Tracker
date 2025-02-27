package com.example.rainbowcalendar.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.rainbowcalendar.db.CycleDao
import com.example.rainbowcalendar.db.CycleRoomDatabase
import com.example.rainbowcalendar.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale


class HomeFragment:Fragment() {
    private lateinit var cycleDao:CycleDao
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view:View,savedInstanceState:Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        //Log.v("theme",requireContext().theme.toString())

        cycleDao=CycleRoomDatabase.getDatabase(requireContext()).cycleDao()

        //requireActivity().deleteDatabase("RainbowCalendar.db")
       /* Thread {
            cycleDao.insert(Cycle(date="2025-01-25",overallMood=3, crampLevel=2, digestiveIssues=1))
        }.start()*/

        cycleDao.getAllMetricData().observe(viewLifecycleOwner){cycles->
            cycles.forEach {
                Log.v("all ROOM data","${it.date} ${it.crampLevel} ${it.headache} ${it.energyLevel}")
            }
        }

        /*Thread{
            cycleDao.update(Cycle(date="2025-01-14",cycleDay=1,overallMood=1))
        }.start()*/


        val homeTitle=view.findViewById<TextView>(R.id.homeTitle)
        val sharedPrefs=requireActivity().getSharedPreferences("com.example.rainbowcalendar_pref",Context.MODE_PRIVATE)
        val name=sharedPrefs.getString("name","")
        homeTitle.text=name

        val testTextV=view.findViewById<TextView>(R.id.test)
        val popUpText=requireActivity().findViewById<TextView>(R.id.popUpText)
        val popUpL=requireActivity().findViewById<LinearLayout>(R.id.popUp)
        //val popUpSpace=requireActivity().findViewById<Space>(R.id.popUpSpace)

        val passwordSet=sharedPrefs.getString("passwordValue","")!=""
        val sharedPrefRecovery=requireActivity().getSharedPreferences( "com.example.rainbowcalendar_recovery",Context.MODE_PRIVATE)
        val recoverySet=sharedPrefRecovery.getBoolean("done",false)
        val goToActivity=requireActivity().findViewById<Button>(R.id.goToActivityButton)
        val buttonClosePopup=requireActivity().findViewById<Button>(R.id.buttonClosePopup)

        //region popup
        if(!passwordSet) {
            popUpL.visibility=View.VISIBLE
            popUpText.text="Set up a password!"
            //popUpSpace.visibility=View.VISIBLE
        }
        else if(!recoverySet) {
            popUpL.visibility=View.VISIBLE
            popUpText.text="Make recovery questions!"
            //popUpSpace.visibility=View.VISIBLE
        }
        else {
            popUpL.visibility=View.GONE
            //popUpSpace.visibility=View.GONE
        }

        /*goToActivity.setOnClickListener {
            if(!passwordSet)
                startActivity(Intent(requireContext(),PasswordActivity::class.java))
            else if(!recoverySet)
                startActivity(Intent(requireContext(),RecoveryActivity::class.java))
        }*/

        buttonClosePopup.setOnClickListener {
            popUpL.visibility=View.GONE
            //popUpSpace.visibility=View.GONE
        }
        //endregion


        val last=sharedPrefs.getString("lastPeriod","")
        val next=sharedPrefs.getString("nextPeriod","")
        val length=sharedPrefs.getInt("cycleLength",28)

        val progressBarL=requireActivity().findViewById<ConstraintLayout>(R.id.progressBarL)
        val progressBar=requireActivity().findViewById<ProgressBar>(R.id.progress)

        if(length!=0&&!last.isNullOrEmpty()&&!next.isNullOrEmpty()) {
            progressBarL.visibility=View.VISIBLE
            val formatter=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
            val calendar=Calendar.getInstance()
            val today=formatter.format(calendar.time)
            val startToNow=daysBetween(last,today)
            //Log.v("idk_days", startToNow.toString())
            val progress:Double=(startToNow.toDouble()/length.toDouble())*100.toDouble()
            //Log.v("idk_calc", (startToNow/length).toString())
            //Log.v("idk_progress", progress.toString())
            val daysLeft=daysBetween(today,next)
            val progressText=requireActivity().findViewById<TextView>(R.id.progressText)

            if(progress>=100) {
                progressText.text="It should start today"
                progressBar.progress=100
            }
            else{
                progressBar.progress=progress.toInt()
                //THIS WON'T BE USED I'M JUST TESTING
                progressText.text=sharedPrefs.getString("possTDate","none")

                progressText.text="$daysLeft days left"
            }

        }

        testTextV.text=last+"\n"+next


        val periodStartButton=view.findViewById<Button>(R.id.periodStartButton)
        periodStartButton.setOnClickListener {
            val formatter=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
            val calendar=Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR,-1*0)
            val date=formatter.format(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR,length)
            val nextDate=formatter.format(calendar.time)

            sharedPrefs.edit().putString("lastPeriod",date).apply()
            sharedPrefs.edit().putString("nextPeriod",nextDate).apply()

            startActivity(Intent(requireContext(),MainActivity::class.java))
        }


    }

    /**
     * calculates difference in days between two string dates
     * @param date1 start date
     * @param date2 end date
     * @return difference in number of days
     * **/
    private fun daysBetween(date1:String,date2:String):Short {
        val start=LocalDate.parse(date1)
        val end=LocalDate.parse(date2)
        return ChronoUnit.DAYS.between(start,end).toShort()
    }

    override fun onCreateView(
        inflater:LayoutInflater,container:ViewGroup?,savedInstanceState:Bundle?
    ):View? {
        return inflater.inflate(R.layout.fragment_home,container,false)
    }

    //endregion
}