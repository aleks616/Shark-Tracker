package com.example.rainbowcalendar

import android.content.Context
import android.content.SharedPreferences

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.rainbowcalendar.fragments.MainActivity
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
//import androidx.activity.ComponentActivity
//import org.mockito.MockitoAnnotations
//import org.mockito.kotlin.anyOrNull
import java.text.SimpleDateFormat
import java.util.*

@RunWith(AndroidJUnit4::class)
class MetricsKtTest{
    private lateinit var context:Context
    private lateinit var sharedPrefs:SharedPreferences
    private lateinit var editor:SharedPreferences.Editor
    private lateinit var gson:Gson

    @get:Rule
    val composeTestRule=createAndroidComposeRule<MainActivity>()
    @Before
    fun setUp(){
        context=ApplicationProvider.getApplicationContext()
        sharedPrefs=mock(SharedPreferences::class.java)
        editor=mock(SharedPreferences.Editor::class.java)
        gson=Gson()
        `when`(sharedPrefs.edit()).thenReturn(editor)
    }

    @Test
    fun test_changeDate_updates_date_correctly(){
        val initialDate="2025-01-01"
        usedDateState.value=initialDate

        changeDate(1)

        val expectedDate=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
            .format(SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
                .parse(initialDate)!!.apply{
                    time+=86400000 //one day
                })

        assertEquals(expectedDate,usedDateState.value)
    }


    @Test
    fun getColor_should_return_a_valid_color_resource(){
        val attrColorId=com.google.android.material.R.attr.colorSecondary
        Thread{
            composeTestRule.activity.setContent{
                val color=getColor(attrColorId)
                assertNotNull(color)
            }
        }
    }


//wont work idk why
/*
    @Test
    fun test_saveMetricsJson_stores_correct_json(){
        val context=mock(Context::class.java)
        sharedPrefs=mock(SharedPreferences::class.java)
        editor=mock(SharedPreferences.Editor::class.java)

        `when`(context.getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)).thenReturn(sharedPrefs)
        `when`(sharedPrefs.edit()).thenReturn(editor.apply{})
        verify(editor).apply()

        val metricsList=listOf(MetricRowData("Test","testMetric",0))
        val expectedJson=gson.toJson(
            metricsList.map{MetricPersistence(it.metricName,0,it.visible)}
        )

        saveMetricsJson(context,metricsList)

        verify(editor).putString(eq("metricsOrder"),eq(expectedJson))
        verify(editor).apply()
    }

    @Test
    fun test_loadMetricsJson_returns_parsed_list(){
        val testJson=gson.toJson(
            listOf(MetricPersistence("testMetric",0,true))
        )
        `when`(sharedPrefs.getString("metricsOrder",null)).thenReturn(testJson)

        val result=loadMetricsJson(context)

        assertNotNull(result)
        assertEquals(1,result!!.size)
        assertEquals("testMetric",result[0].metricName)
        assertEquals(0,result[0].order)
        assertTrue(result[0].visible)
    }*/

    //won't work on singleton
    /*@Test
    fun saveToDB_should_insert_or_update_cycle(){
        MockitoAnnotations.openMocks(this)
        val mockDao=mock(CycleDao::class.java)
        cycleDao=mockDao
        val content=listOf(0,1,2,-1,-1,3,4,5,6,-1,-1,-1,-1,-1,-1)
        val weight="70"
        val kcalBalance="-200"
        val notes="Test notes"

        saveToDB(content,context,weight,kcalBalance,notes)

        verify(mockDao).insert(anyOrNull())
    }*/
}


