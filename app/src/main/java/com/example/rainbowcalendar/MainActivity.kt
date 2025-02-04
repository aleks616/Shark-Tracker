package com.example.rainbowcalendar

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.example.rainbowcalendar.fragments.AddFragment
import com.example.rainbowcalendar.fragments.CalendarFragment
import com.example.rainbowcalendar.fragments.HomeFragment
import com.example.rainbowcalendar.fragments.SettingsFragment
import java.util.Locale
import android.view.View
import androidx.navigation.NavController
import com.example.rainbowcalendar.fragments.MainComposable

class MainActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState:Bundle?){
        val sharedPrefs=applicationContext.getSharedPreferences(
            "com.example.rainbowcalendar_pref",
            Context.MODE_PRIVATE
        )
        val theme=sharedPrefs.getString("theme","Light")
        ThemeManager[this]=theme
        val lang=sharedPrefs.getString("lang","en")!!
        if(lang=="pt-br")
            Locale("pt","BR")
        else
            Locale(lang)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val composeView:ComposeView=findViewById(R.id.compose_view)
        composeView.setContent{
            MainComposable()
        }



        val homeFragment=HomeFragment()
        val settingsFragment=SettingsFragment()
        val calendarFragment=CalendarFragment()
        val addFragment=AddFragment()

        makeCurrentFragment(homeFragment)

        val bottomNav=findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener{
            when(it.itemId){
                R.id.navMenu_home->composeView.setContent{HomeScreen{}}
                R.id.navMenu_calendar->composeView.setContent{CalendarScreen{}}
                R.id.navMenu_add->composeView.setContent{AddScreen{}}
                R.id.navMenu_acc->composeView.setContent{SettingsScreen{}}
            }
            true
        }

    }

    private fun makeCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.fl_wrapper,fragment)
            commit()
        }
}




@Composable
fun ScreenController(){
    var currentScreen by remember{mutableStateOf(Screen.Home)}
    val context=LocalContext.current
    val bottomNav=(context as MainActivity).findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_nav)

    LaunchedEffect(currentScreen){
        bottomNav.visibility=when(currentScreen) {
            Screen.Home,Screen.Calendar,Screen.Add,Screen.Settings->View.VISIBLE
            else->View.GONE
        }
    }

    when(currentScreen){
        Screen.Home->HomeScreen{currentScreen=Screen.Home}
        Screen.Calendar->CalendarScreen{currentScreen=Screen.Calendar}
        Screen.Add->AddScreen{currentScreen=Screen.Add}
        Screen.Settings->SettingsScreen{newScreen->currentScreen=newScreen}
    }
}
enum class Screen {
    Home,
    Calendar,
    Add,
    Settings
}

@Composable
fun HomeScreen(currentScreen:(Screen)->Unit){
    Text(text="home")
}

@Composable
fun CalendarScreen(currentScreen:(Screen)->Unit){
    Text(text="calendar")
}

@Composable
fun AddScreen(currentScreen: (Screen) -> Unit){
    ScrollableMetricsView()
}

@Composable
fun SettingsScreen(currentScreen:(Screen)->Unit){
    val context=LocalContext.current

    Box(modifier=Modifier.sizeIn(maxWidth=200.dp, maxHeight=50.dp)){
        Button(onClick={}) {
        }
    }
}

