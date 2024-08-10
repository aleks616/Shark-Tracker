package com.example.rainbowcalendar.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Space
import android.widget.TextView
import com.example.rainbowcalendar.PasswordActivity
import com.example.rainbowcalendar.R
import com.example.rainbowcalendar.RecoveryActivity
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale
import kotlin.time.times

class HomeFragment : Fragment() {
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Log.v("gayt",requireContext().theme.toString())

        val homeTitle=view.findViewById<TextView>(R.id.homeTitle)
        val sharedPrefs=requireActivity().getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)
        val name=sharedPrefs.getString("name","")
        homeTitle.text=name

        val testTextV=view.findViewById<TextView>(R.id.test)
        val popUpText=requireActivity().findViewById<TextView>(R.id.popUpText)
        val popUpL=requireActivity().findViewById<LinearLayout>(R.id.popUp)
        val popUpSpace=requireActivity().findViewById<Space>(R.id.popUpSpace)

        val passwordSet=sharedPrefs.getString("passwordValue","")!=""
        val sharedPrefRecovery=requireActivity().getSharedPreferences("com.example.rainbowcalendar_recovery", Context.MODE_PRIVATE)
        val recoverySet=sharedPrefRecovery.getBoolean("done",false)
        val goToActivity=requireActivity().findViewById<Button>(R.id.goToActivity)
        val buttonClosePopup=requireActivity().findViewById<Button>(R.id.buttonClosePopup)

        //region popup
        if(!passwordSet){
            popUpL.visibility=View.VISIBLE
            popUpText.text="Set up a password!"
            popUpSpace.visibility=View.VISIBLE
        }
        else if(!recoverySet){
            popUpL.visibility=View.VISIBLE
            popUpText.text="Make recovery questions!"
            popUpSpace.visibility=View.VISIBLE
        }
        else{
            popUpL.visibility=View.GONE
            popUpSpace.visibility=View.GONE
        }

        goToActivity.setOnClickListener {
            if(!passwordSet)
                startActivity(Intent(requireContext(),PasswordActivity::class.java))
            else if(!recoverySet)
                startActivity(Intent(requireContext(),RecoveryActivity::class.java))
        }

        buttonClosePopup.setOnClickListener {
            popUpL.visibility=View.GONE
            popUpSpace.visibility=View.GONE
        }
        //endregion

        val last=sharedPrefs.getString("lastPeriod","")
        val next=sharedPrefs.getString("nextPeriod","")
        val length=sharedPrefs.getInt("cycleLength",0)

        if(length!=0&&!last.isNullOrEmpty()&&!next.isNullOrEmpty()){
            val formatter=SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val calendar=Calendar.getInstance()
            val today=formatter.format(calendar.time)

            val startToNow=daysBetween(last,today)
            Log.v("idk_days", startToNow.toString())
            val progress:Double=(startToNow.toDouble()/length.toDouble())*100.toDouble()

            Log.v("idk_calc", (startToNow/length).toString())
            Log.v("idk_progress", progress.toString())
            val progressBar=requireActivity().findViewById<ProgressBar>(R.id.progress)
            progressBar.progress=progress.toInt()

            val daysLeft=daysBetween(today,next)
            val progressText=requireActivity().findViewById<TextView>(R.id.progressText)
            progressText.text="$daysLeft days left"


        }


        testTextV.text=last+"\n"+next

    }

    private fun daysBetween(date1:String, date2:String): Short{
        val start = LocalDate.parse(date1)
        val end = LocalDate.parse(date2)
        return ChronoUnit.DAYS.between(start, end).toShort()
    }

    //region generated
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,container:ViewGroup?,savedInstanceState: Bundle?): View?{
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    //endregion
}