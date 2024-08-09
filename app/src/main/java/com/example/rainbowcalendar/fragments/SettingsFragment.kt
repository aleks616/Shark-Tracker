package com.example.rainbowcalendar.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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