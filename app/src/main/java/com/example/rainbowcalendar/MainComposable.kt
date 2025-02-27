package com.example.rainbowcalendar

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rainbowcalendar.Utils.localDateString
import java.time.LocalDate
import kotlin.math.floor
import kotlin.math.roundToInt


@Suppress("UsingMaterialAndMaterial3Libraries")
object Screens{
    const val sWelcome="WelcomeScreen"
    const val sLanguage="LanguageScreen"
    const val sTheme="ThemeScreen"
    const val sGenderOptions="GenderOptionsScreen"
    const val sStealthOptions="StealthOptionsScreen"

    const val sTOptions="TOptionsScreen"
    const val sAgeConsentOptions="AgeConsentOptions"
    const val sPeriodOptions="PeriodOptionsScreen"
    const val sContraceptiveOptions="ContraceptiveOptionsScreen"
    const val sNameBirthDayOptions="NameBirthDayOptionsScreen"

    const val sPassword="PasswordScreen"
    const val sRecovery="RecoveryScreen"
    const val sMain="MainScreen"
}
@Composable
fun MainComposable(){
    Utils.setLanguage(LocalContext.current)
    var currentScreen by remember{mutableStateOf(Screens.sWelcome)}
    //Log.v("currentScreen",currentScreen)

    when(currentScreen){
        Screens.sWelcome->WelcomeScreen{screen->currentScreen=screen}
        Screens.sLanguage->LanguageMenu()
        Screens.sTheme->ThemesSettings(onNavigate={screen->currentScreen=screen},thisScreen=currentScreen)
        Screens.sAgeConsentOptions->AgeConsentOptions(onNavigate={screen->currentScreen=screen},thisScreen=currentScreen)
        Screens.sGenderOptions->GenderOptionsScreen(onNavigate={screen->currentScreen=screen},thisScreen=currentScreen)
        Screens.sNameBirthDayOptions->NameBirthDayOptionsScreen(onNavigate={screen->currentScreen=screen},thisScreen=currentScreen)

        Screens.sStealthOptions->StealthOptionsScreen(onNavigate={screen->currentScreen=screen},thisScreen=currentScreen)
        Screens.sTOptions->TOptionsScreen(onNavigate={screen->currentScreen=screen},thisScreen=currentScreen)
        Screens.sPeriodOptions->PeriodOptionsScreen(onNavigate={screen->currentScreen=screen},thisScreen=currentScreen)
        Screens.sContraceptiveOptions->ContraceptiveOptionsScreen(onNavigate={screen->currentScreen=screen},thisScreen=currentScreen)

        Screens.sPassword->PasswordScreen{screen->currentScreen=screen}
        Screens.sRecovery->RecoveryScreen{screen->currentScreen=screen}


        Screens.sMain->MainScreen{screen->currentScreen=screen}
    }
}


@Composable
fun MainScreen(onNavigate:(String)->Unit){
    Utils.setLanguage(LocalContext.current)
    //val context=LocalContext.current
    data class BottomNavItem(
        val icon:Int,
        val text:String,
        val fragmentName:String
    )

    val bottomNavItems=listOf(
        BottomNavItem(R.drawable.home_icon,stringResource(id=R.string.home_button_name),"Home"),
        BottomNavItem(R.drawable.calendar_icon,stringResource(id=R.string.calendar_button_text),"Calendar"),
        BottomNavItem(R.drawable.add_icon,stringResource(id=R.string.add_button_text),"Add"),
        BottomNavItem(R.drawable.account_icon,stringResource(id=R.string.account_button_name),"Settings")
    )

    var currentFragment by remember{mutableStateOf("Home")}

    Column(modifier=Modifier.fillMaxSize()){
        Column(modifier=Modifier
            .weight(1f)
            .fillMaxHeight(0f)){
            Scaffold(
                content={padding->
                    Box(modifier=Modifier.padding(padding)){
                        when(currentFragment){
                            "Home"->HomeScreen()
                            "Calendar"->CalendarScreen()
                            "Add"->AddScreen()
                            "Settings"->SettingsScreen(onButtonClick={targetScreen->onNavigate(targetScreen)})
                        }
                    }
                },
                bottomBar={
                    Row(
                        modifier=Modifier.fillMaxWidth().background(colorTertiary()).heightIn(min=50.dp),
                        horizontalArrangement=Arrangement.SpaceAround,
                        Alignment.CenterVertically
                    ){
                        bottomNavItems.forEach{item->
                            IconButton(onClick={currentFragment=item.fragmentName},Modifier.weight(1f)){
                                Column(horizontalAlignment=Alignment.CenterHorizontally){
                                    Icon(painterResource(id=item.icon),contentDescription=null, tint=colorSecondary())
                                    AnimatedVisibility(
                                        visible=currentFragment==item.fragmentName,
                                        enter=fadeIn(animationSpec=tween(durationMillis=300))
                                    ){
                                        Box(contentAlignment=Alignment.Center){
                                            Text(
                                                text=item.text,
                                                color=colorSecondary(),
                                                textAlign=TextAlign.Center,
                                                modifier=Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            )
        }
    }
}

@Composable
fun HomeScreen(){
    Utils.setLanguage(LocalContext.current)
    Text("Home")
}
data class DayColor(
    val date:LocalDate,
    val color:Color
)
@Composable
fun CalendarScreen(){
    val context=LocalContext.current
    Utils.setLanguage(context)
    val colors5=Utils.calculateIntermediateColors(colorMin(),colorMax(),5)
    val colors10=Utils.calculateIntermediateColors(colorMin(),colorMax(),10)
    val colors4=Utils.calculateIntermediateColors(colorMin(),colorMax(),4)
    val colors3=Utils.calculateIntermediateColors(colorMin(),colorMax(),3)


    //Log.v("mood data2",temp[0].date+" "+temp[0].overallMood)




    val firstDate=Utils.getFirstMetricDate(context)
    val months=if(firstDate.isNotEmpty()) Utils.monthsSinceFirstDate(firstDate) else 12

    var expanded by remember{mutableStateOf(false)}
    var menuText by remember{mutableStateOf("Average")}
    val questions=mutableListOf("Average","Overall Mood","Headache","Muscle/back pain",
        "Skin condition", "Digestive issues","Sleep quality","Energy level","Mood swings",
        "Bleeding","Cramps","Cravings")
    if(Utils.hasDysphoria(context)) questions.add(1,"Dysphoria")

    val data=Utils.getAllMoodData(context)
    val dates=mutableListOf<DayColor>()
    data.forEach{
        val avg=Utils.avgFeel(context,it)
        Log.v("avg for date: "+it.date,avg.toString())
        val color=if(menuText=="Average") if(avg.toInt()==1) colors10[9] else colors10[floor((avg*10)).toInt()]
        else if(menuText=="Overall Mood")if(it.overallMood!=null&&it.overallMood!=-1) colors5[it.overallMood] else colorPrimary()
        else if(menuText=="Headache")if(it.headache!=null&&it.headache!=-1) colors5[4-it.headache] else colorPrimary()
        else if(menuText=="Dysphoria")if(it.dysphoria!=null&&it.dysphoria!=-1) colors5[4-it.dysphoria] else colorPrimary()
        else if(menuText=="Muscle/back pain")if(it.musclePain!=null&&it.musclePain!=-1) colors3[2-it.musclePain] else colorPrimary()

        else if(menuText=="Skin condition")if(it.skinCondition!=null&&it.skinCondition!=-1) colors3[2-it.skinCondition] else colorPrimary()
        else if(menuText=="Digestive issues")if(it.digestiveIssues!=null&&it.digestiveIssues!=-1) colors4[3-it.digestiveIssues] else colorPrimary()
        else if(menuText=="Sleep quality")if(it.sleepQuality!=null&&it.sleepQuality!=-1) colors3[it.sleepQuality] else colorPrimary()
        else if(menuText=="Energy level")if(it.energyLevel!=null&&it.energyLevel!=-1) colors5[it.energyLevel] else colorPrimary()

        else if(menuText=="Mood swings")if(it.moodSwings!=null&&it.moodSwings!=-1) colors3[2-it.moodSwings] else colorPrimary()
        else if(menuText=="Bleeding")if(it.bleeding!=null&&it.bleeding!=-1) colors5[4-it.bleeding] else colorPrimary()
        else if(menuText=="Cramps")if(it.crampLevel!=null&&it.crampLevel!=-1) colors3[2-it.crampLevel] else colorPrimary()
        else if(menuText=="Cravings")if(it.cravings!=null&&it.cravings!=-1) colors3[2-it.cravings] else colorPrimary()
        else colorPrimary()

        dates+=DayColor(localDateString(it.date),color)
    }


    val theme=context.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE).getString("theme","Black")
    Column(
        modifier=if(theme=="Pride")
            Modifier.paint(painterResource(id=R.drawable.pride50),contentScale=ContentScale.FillBounds)
        else Modifier.background(colorPrimary())
            .fillMaxSize()
    ){
        BetterButton(
            modifier=Modifier.fillMaxWidth().padding(horizontal=10.dp,vertical=15.dp).height(50.dp).border(width=2.dp,color=colorTertiary()),
            onClick={expanded=true},){
            BetterText(text=menuText)
            DropdownMenu(
                expanded=expanded,
                onDismissRequest={expanded=false},
                modifier=Modifier.background(colorPrimary()).fillMaxWidth().padding(horizontal=10.dp).heightIn(max=250.dp)){
                questions.forEach{question->
                    DropdownMenuItem(
                        text={BetterText(text=question)},
                        onClick={
                            expanded=false
                            menuText=question
                        }
                    )
                    Divider(color=colorTertiary(),thickness=1.dp)
                }
            }
        }
        BetterHeader(text=menuText,fontSize="L",modifier=Modifier.fillMaxWidth().padding(top=16.dp,bottom=10.dp))
        VerticalCalendar(
            dayContent={date->
                val backgroundColor=dates.find{it.date==date}?.color?:colorPrimary()
                val dateInFuture=date.isAfter(LocalDate.now())
                Box(modifier=Modifier.background(backgroundColor,CircleShape).aspectRatio(1f)
                ){
                    Column(verticalArrangement=Arrangement.Center,modifier=Modifier.fillMaxSize()){
                        BetterText(text=date.dayOfMonth.toString(),fontSize=30.sp,modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Center,color=if(dateInFuture)colorTertiary() else colorSecondary())
                    }
                }
            },
            monthsQuantity=months
        )
    }
}

@Composable
fun AddScreen(){
    Utils.setLanguage(LocalContext.current)
    MetricsScreen()
}

@Composable
fun SettingsScreen(onButtonClick:(String)->Unit){
    val context=LocalContext.current
    Utils.setLanguage(context)
    Column(modifier=Modifier.background(colorPrimary()).fillMaxSize()){
        BetterButton(
            onClick={onButtonClick(Screens.sTheme)},
            modifier=Modifier.height(100.dp).width(150.dp).align(Alignment.CenterHorizontally).padding(vertical=15.dp)
        ){
            BetterText(text="CHANGE THEME",textAlign=TextAlign.Center)
        }
        BetterButton(
            onClick={onButtonClick(Screens.sLanguage)},
            modifier=Modifier.height(100.dp).width(150.dp) .align(Alignment.CenterHorizontally).padding(vertical=15.dp)
        ){
            BetterText(text="CHANGE LANGUAGE",textAlign=TextAlign.Center)
        }
        BetterButton(
            onClick={Utils.toggleStealthMode(context)},
            modifier=Modifier.height(100.dp).width(150.dp).align(Alignment.CenterHorizontally).padding(vertical=15.dp)
        ){
            BetterText(text="TOGGLE STEALTH MODE",textAlign=TextAlign.Center)
        }
    }


}


//TODO BIG:
// 1 FIX PASSWORD ERRORS NOT SHOWING!
// 2. ADD RECOVERY WHEN FORGOT PASSWORD
// 3. BRING BACK CREATE PASSWORD/RECOVERY POPUP
// 4. SOCIALS/CREDITS TAB
//: TODO FROM OLD ACTIVITIES: home_fragment


@Composable
fun WelcomeScreen(onNavigate:(String)->Unit){
    val context=LocalContext.current
    Utils.setLanguage(context)
    val sharedPrefs=context.getSharedPreferences(Constants.key_package, Context.MODE_PRIVATE)
    val setupDone=sharedPrefs.getBoolean(Constants.key_isSetupDone,false)

    val metricsOrderSet=sharedPrefs.getBoolean(Constants.metricsSetUp,false)
    if(!metricsOrderSet){
        Utils.setStartMetricsOrder(context,sharedPrefs.getInt(Constants.key_gender,-1))
    }

    SideEffect{
        if(setupDone){
            if(!sharedPrefs.getString(Constants.key_passwordValue,"").isNullOrEmpty())//there is password
                onNavigate(Screens.sPassword)
        }
        else{
            Handler(Looper.getMainLooper()).postDelayed({
                if(sharedPrefs.getBoolean(Constants.key_isSetupDone,false))
                    onNavigate(Screens.sMain)
                else if(!sharedPrefs.getBoolean(Constants.key_isLanguageSetUp,false))
                    onNavigate(Screens.sLanguage)
                else if(!sharedPrefs.getBoolean(Constants.key_isThemeSetUp,false))
                    onNavigate(Screens.sTheme)
                else if(!sharedPrefs.getBoolean(Constants.key_isConsentDone,false))
                    onNavigate(Screens.sAgeConsentOptions)
                else if(sharedPrefs.getInt(Constants.key_gender,-1)==-1)
                    onNavigate(Screens.sGenderOptions)
                else if(!sharedPrefs.getBoolean(Constants.key_isNameBirthDayMenuComplete,false))
                    onNavigate(Screens.sNameBirthDayOptions)
                else if(!sharedPrefs.getBoolean(Constants.key_isStealthDone,false))
                    onNavigate(Screens.sStealthOptions)
                else if(!sharedPrefs.getBoolean(Constants.key_testosteroneMenuComplete,false))
                    onNavigate(Screens.sTOptions)
                else if(!sharedPrefs.getBoolean(Constants.key_isPeriodMenuComplete,false))
                    onNavigate(Screens.sPeriodOptions)
                else if(!sharedPrefs.getBoolean(Constants.key_BCMenuComplete,false))
                    onNavigate(Screens.sContraceptiveOptions)
                else if(!sharedPrefs.getString(Constants.key_passwordValue,"").isNullOrEmpty())
                    onNavigate(Screens.sPassword) //only if setup isn't complete, if it is, allow to continue without password
                else{
                    sharedPrefs.edit().putBoolean(Constants.key_isSetupDone,true).apply()
                    onNavigate(Screens.sMain)
                }
            },500)
        }
    }
//    sharedPrefs.edit().putBoolean("setup",false).apply()

    val theme=sharedPrefs.getString("theme","Gray")
    Column(
        modifier=if(theme=="Pride")
            Modifier.fillMaxSize().paint(painterResource(id=R.drawable.pride50),contentScale=ContentScale.FillBounds)
        else Modifier.fillMaxSize().background(colorPrimary())
    ){
        Image(
            painter=painterResource(id=R.drawable.icon_shark_normal),
            contentDescription=null,
            modifier=Modifier
                .fillMaxWidth()
                .padding(top=50.dp)
        )
        BetterText(
            color=colorSecondary(),
            text=stringResource(id=R.string.welcome_text),
            textAlign=TextAlign.Center,
            modifier=Modifier.fillMaxWidth().padding(vertical=20.dp,horizontal=10.dp),
            fontSize=50.sp
        )
    }
}
