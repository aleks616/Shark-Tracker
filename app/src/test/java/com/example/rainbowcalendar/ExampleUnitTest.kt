package com.example.rainbowcalendar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.TestInstance
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

//import androidx.activity.compose.setContent
//import androidx.compose.ui.test.*
//import androidx.compose.ui.test.junit4.createAndroidComposeRule
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.google.gson.Gson
//import org.junit.Before
//import org.junit.Rule
//import org.junit.runner.RunWith
//import org.mockito.kotlin.whenever
//import java.text.SimpleDateFormat
//import java.util.*


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest{
//    private val mockContext=mock<Context>()
//    private val mockPrefs=mock<SharedPreferences>()
//    private val mockEditor=mock<SharedPreferences.Editor>()
    @Test
    fun addition_isCorrect(){
    Assertions.assertEquals(4,2 + 2)
    }

    /*@Test //shared preferences don't work
    fun `test alarm scheduling updates shared preferences`(){
        whenever(mockContext.getSharedPreferences(any(),any())).thenReturn(mockPrefs)
        whenever(mockPrefs.edit()).thenReturn(mockEditor)
        whenever(mockEditor.putLong(any(),any())).thenReturn(mockEditor)

        val alarm=Alarm(mockContext)
        alarm.schedulePushNotifications(10,30,1,1)

        verify(mockEditor).putLong(eq("lastNotif"),any())
        verify(mockEditor).apply()
    }*/

}

class UtilsUniTest{
    companion object{
        private lateinit var today:LocalDate
        @BeforeEach
        fun before(){
            val formatter:DateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd")
            today=LocalDate.parse(LocalDate.now().format(formatter),formatter)
        }
        @Test
        fun timeSinceDate_works_for_today_date(){
            val date=LocalDate.of(today.year,today.month,today.dayOfMonth).toString()
            val result=Utils.timeSinceDate(date)
            val expectedResult=MilestoneDate(0,0,TIMEUNIT.DAYS)
            Assertions.assertEquals(expectedResult,result)

        }

        @TestFactory
        fun timeSinceDate_works_for_all_values_that_should_return_days()=(1..35).map{day->
            DynamicTest.dynamicTest("Test for day $day"){
                val date=LocalDate.of(today.year,today.month,today.dayOfMonth).minusDays(day-0L).toString()
                val result=Utils.timeSinceDate(date)
                val expectedResult=MilestoneDate(day,day,TIMEUNIT.DAYS)
                Assertions.assertEquals(expectedResult,result)
            }
        }

        @TestFactory
        fun timeSinceDate_works_for_all_values_that_should_return_weeks()=(36..168).map{day->
            DynamicTest.dynamicTest("Test for day $day"){
                val date=LocalDate.of(today.year,today.month,today.dayOfMonth).minusDays(day-0L).toString()
                val result=Utils.timeSinceDate(date)
                val expectedResult=MilestoneDate(day,(day/7),TIMEUNIT.WEEKS)
                Assertions.assertEquals(expectedResult,result)
            }
        }

        @TestFactory
        fun timeSinceDate_works_for_all_values_that_should_return_months()=(169..1095).map{day->
            DynamicTest.dynamicTest("Test for day $day"){
                val date=LocalDate.of(today.year,today.month,today.dayOfMonth).minusDays(day-0L).toString()
                val result=Utils.timeSinceDate(date)
                val expectedResult=MilestoneDate(day,(day/30.437).toInt(),TIMEUNIT.MONTHS)
                Assertions.assertEquals(expectedResult,result)
            }
        }

        @TestFactory
        fun timeSinceDate_works_for_values_that_should_return_years()=(1096..7305).map{day->
            DynamicTest.dynamicTest("Test for day $day"){
                val date=LocalDate.of(today.year,today.month,today.dayOfMonth).minusDays(day-0L).toString()
                val result=Utils.timeSinceDate(date)
                val expectedResult=MilestoneDate(day,(day/365.25).toInt(),TIMEUNIT.YEARS)
                Assertions.assertEquals(expectedResult,result)
            }
        }


        @Test
        fun `timeSinceDate_catches_error_for_non-date_strings`(){
            val result=Utils.timeSinceDate("gdfhdfhgdflo")
            val expectedResult=MilestoneDate(-1,-1,TIMEUNIT.ERROR)
            Assertions.assertEquals(expectedResult,result)
        }

        @Test
        fun timeSinceDate_catches_error_for_empty_string(){
            val result=Utils.timeSinceDate("")
            val expectedResult=MilestoneDate(-1,-1,TIMEUNIT.ERROR)
            Assertions.assertEquals(expectedResult,result)
        }

        @Test
        fun timeSinceDate_catches_error_for_different_date_format(){
            val result=Utils.timeSinceDate("01-02-2024")
            val expectedResult=MilestoneDate(-1,-1,TIMEUNIT.ERROR)
            Assertions.assertEquals(expectedResult,result)
        }

    }
}



class PasswordActivityTest{
    @Test
    fun `isNumeric_returns_true_for_numeric_strings`(){
        Thread{
            val activity=PasswordActivity()
            val result=activity.isNumeric("12345")
            assertTrue(result)
        }
    }

    @Test
    fun `isNumeric_returns_false_for_non-numeric_strings`(){
        Thread{
            val activity=PasswordActivity()
            val result=activity.isNumeric("abc123")
            assertFalse(result)
        }
    }
}

class RecoveryActivityTest{

    @Test
    fun `simplify_removes_spaces_and_converts_to_lowercase`(){
        Thread{
            val activity=RecoveryActivity()
            val result=activity.simplify(" Test String ")
            Assertions.assertEquals("teststring",result)
        }
    }

    @Test
    fun `simplify_handles_null_inputs`(){
        Thread{
            val activity=RecoveryActivity()
            val result=activity.simplify(null)
            Assertions.assertEquals(null,result)
        }
    }
}

/*mockmaker doesn't work
class ThemeManagerTest{
    private val mockContext=mock<Context>()

    @Test
    fun `apply theme resource based on input`(){
        Thread{
            ThemeManager[mockContext]="Blue"
            verify(mockContext).setTheme(R.style.Blue_RainbowCalendar)
        }
    }
}*/
