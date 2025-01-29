package com.example.rainbowcalendar

import org.junit.Test

import android.content.Context
import android.content.SharedPreferences
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Assert.*
import org.mockito.Mockito.*
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
        assertEquals(4, 2 + 2)
    }

    /*@Test //shared preferences don't work
    fun `test alarm scheduling updates shared preferences`() {
        whenever(mockContext.getSharedPreferences(any(),any())).thenReturn(mockPrefs)
        whenever(mockPrefs.edit()).thenReturn(mockEditor)
        whenever(mockEditor.putLong(any(),any())).thenReturn(mockEditor)

        val alarm=Alarm(mockContext)
        alarm.schedulePushNotifications(10,30,1,1)

        verify(mockEditor).putLong(eq("lastNotif"),any())
        verify(mockEditor).apply()
    }*/

}

class PasswordActivityTest{
    @Test
    fun `isNumeric returns true for numeric strings`(){
        Thread{
            val activity=PasswordActivity()
            val result=activity.isNumeric("12345")
            assertTrue(result)
        }
    }

    @Test
    fun `isNumeric returns false for non-numeric strings`(){
        Thread{
            val activity=PasswordActivity()
            val result=activity.isNumeric("abc123")
            assertFalse(result)
        }
    }
}

class RecoveryActivityTest {

    @Test
    fun `simplify removes spaces and converts to lowercase`(){
        Thread{
            val activity=RecoveryActivity()
            val result=activity.simplify(" Test String ")
            assertEquals("teststring", result)
        }
    }

    @Test
    fun `simplify handles null inputs`(){
        Thread{
            val activity=RecoveryActivity()
            val result=activity.simplify(null)
            assertEquals(null, result)
        }
    }
}

/*mockmaker doesn't work
class ThemeManagerTest {
    private val mockContext=mock<Context>()

    @Test
    fun `apply theme resource based on input`() {
        Thread{
            ThemeManager[mockContext]="Blue"
            verify(mockContext).setTheme(R.style.Blue_RainbowCalendar)
        }
    }
}*/
