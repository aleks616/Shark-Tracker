package com.example.rainbowcalendar.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.example.rainbowcalendar.db.CycleDao
import com.example.rainbowcalendar.db.CycleRoomDatabase
import com.example.rainbowcalendar.R
import com.example.rainbowcalendar.ScrollableMetricsView

class AddFragment : Fragment() {
    private lateinit var cycleDao:CycleDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cycleDao=CycleRoomDatabase.getDatabase(requireContext()).cycleDao()


        showAll()
    }

    override fun onViewCreated(view:View,savedInstanceState:Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        val composeView=requireActivity().findViewById<ComposeView>(R.id.composeView)
        composeView.setContent{
            ScrollableMetricsView()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    private fun showAll(){
        cycleDao.getAllMetricData().observe(this) {cycles->
            cycles.forEach {
                Log.v("TAG","Cycle: ${it.date}, Mood: ${it.overallMood} Cramp level: ${it.crampLevel}, Headache: ${it.headache}")
            }
        }
    }
}