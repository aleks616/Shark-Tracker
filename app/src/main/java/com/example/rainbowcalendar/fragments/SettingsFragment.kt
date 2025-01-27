package com.example.rainbowcalendar.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.example.rainbowcalendar.LanguageSettingsActivity
import com.example.rainbowcalendar.MainActivity
import com.example.rainbowcalendar.PeriodSettingsActivity
import com.example.rainbowcalendar.R

class SettingsFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val testButton2=view.findViewById<Button>(R.id.testButton2)
        val testButton=view.findViewById<Button>(R.id.testButton)

        testButton.setOnClickListener {
            startActivity(Intent(requireContext(), PeriodSettingsActivity::class.java))
        }

        val goToLanguageActivityButton=view.findViewById<Button>(R.id.goToLanguageActivityButton)
        goToLanguageActivityButton.setOnClickListener {
            startActivity(Intent(requireContext(), LanguageSettingsActivity::class.java))
        }

        val stealthModeToggle=view.findViewById<Button>(R.id.stealthModeToggle)

        stealthModeToggle.setOnClickListener{
            val packageManager=requireContext().packageManager
            val stealth=ComponentName(requireContext(),"com.example.rainbowcalendar.SplashScreenActivityStealth")
            val default=ComponentName(requireContext(),"com.example.rainbowcalendar.SplashScreenActivity")

            val stealthMode=packageManager.getComponentEnabledSetting(stealth)==PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            packageManager.setComponentEnabledSetting((if(stealthMode) default else stealth),PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP)
            packageManager.setComponentEnabledSetting((if(stealthMode) stealth else default),PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP)
        }


        val sharedPrefs=requireActivity().getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)
        testButton2?.setOnClickListener {
            sharedPrefs.edit().putString("passwordValue","").apply()
            sharedPrefs.edit().putInt("passwordType",0).apply()

            val sharedPrefTemp=requireActivity().getSharedPreferences("temp", Context.MODE_PRIVATE)
            sharedPrefTemp.edit().putInt("temp",0).apply()
            sharedPrefTemp.edit().putString("temp1","").apply()

            val sharedPrefRecovery=requireActivity().getSharedPreferences("com.example.rainbowcalendar_recovery", Context.MODE_PRIVATE)
            with(sharedPrefRecovery.edit()){
                putString("question1","")
                putString("question2","")
                putString("question3","")
                putString("answer1","")
                putString("answer2","")
                putString("answer3","")
                putBoolean("done",false)
                apply()
            }
        }



        val spinner=view.findViewById<Spinner>(R.id.themeSpinner)
        ArrayAdapter.createFromResource(requireContext(),R.array.themes_array,R.layout.spinner_item)
            .also {adapter->
                adapter.setDropDownViewResource(R.layout.simple_text)
                spinner.adapter=adapter
            }
        var themeValue="Dark"
        spinner.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>?,view:View?,position:Int,id:Long){
                themeValue=parent?.getItemAtPosition(position).toString()

            }
            override fun onNothingSelected(parent:AdapterView<*>?){}
        }

        val buttonRefresh:Button=view.findViewById(R.id.buttonRefresh)
        buttonRefresh.setOnClickListener {
            sharedPrefs.edit().putString("theme",themeValue).apply()
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

}