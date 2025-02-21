package com.example.rainbowcalendar.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.example.rainbowcalendar.Constants
import com.example.rainbowcalendar.MainActivity
import com.example.rainbowcalendar.R
import com.example.rainbowcalendar.ScrollableMetricsView
import com.example.rainbowcalendar.Utils
import com.example.rainbowcalendar.getColor
import com.example.rainbowcalendar.getLocal
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

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

    const val sPassword="PasswordScreen"
    const val sRecovery="RecoveryScreen"
    const val sMain="MainScreen"

}
@Composable
fun MainComposable(){
    var currentScreen by remember{mutableStateOf(Screens.sWelcome)}

    when(currentScreen){
        Screens.sWelcome->WelcomeScreen{screen->currentScreen=screen}
        Screens.sLanguage->LanguageScreen()
        Screens.sTheme->ThemeScreen()
        Screens.sGenderOptions->GenderOptionsScreen{screen->currentScreen=screen}
        Screens.sStealthOptions->StealthOptionsScreen{screen->currentScreen=screen}
        Screens.sTOptions->TOptionsScreen{screen->currentScreen=screen}
        Screens.sAgeConsentOptions->AgeConsentOptions{screen->currentScreen=screen}
        Screens.sPeriodOptions->PeriodOptionsScreen{screen->currentScreen=screen}
        Screens.sContraceptiveOptions->ContraceptiveOptionsScreen{screen->currentScreen=screen}

        Screens.sPassword->PasswordScreen{screen->currentScreen=screen}
        Screens.sRecovery->RecoveryScreen{screen->currentScreen=screen}
        Screens.sMain->MainScreen{screen->currentScreen=screen}
    }
}

@Composable
fun AgeConsentOptions(onNavigate:(String) -> Unit){
    val context=LocalContext.current
    val sharedPrefs=context.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
    val consentDone=sharedPrefs.getBoolean(Constants.key_isConsentDone,false)
    if(consentDone){
        onNavigate(Screens.sGenderOptions)
    }
    var consentChecked by remember{mutableStateOf(arrayOf(false,false,false,false))}
    val consentTexts=listOf(
        "I confirm that I am legally adult where I reside, or I am over the age of 13 and have the consent of my parent or guardian to use this app",
        "I acknowledge that this app does not collect, store, or share any of my data externally and that all data remains on my device.",
        "I understand that this app is not a medical tool and should not replace professional health advice.",
        "I understand that I am responsible for the security and backup of my own data, and the app developer is not liable for any data loss or misuse."
    )
    val index=listOf(0,1,2,3)

    Column(
        modifier=Modifier.fillMaxSize()
    ){
        BetterHeader(
            text="Terms and conditions",
            fontSize="L",
            textAlign=TextAlign.Center,
            modifier=Modifier
                .padding(vertical=15.dp)
                .fillMaxWidth()
        )
        index.forEach{index->
            Row(
                horizontalArrangement=Arrangement.Start,
                verticalAlignment=Alignment.CenterVertically,
                modifier=Modifier
                    .fillMaxWidth()
                    .padding(vertical=14.dp,horizontal=10.dp)
            ){
                Checkbox(
                    checked=consentChecked[index],
                    onCheckedChange={
                        consentChecked=consentChecked.copyOf().apply{this[index]=!consentChecked[index]}
                    },
                    colors=CheckboxDefaults.colors(checkedColor=colorSecondary(),uncheckedColor=colorSecondary(), checkmarkColor=colorTertiary())
                )
                BetterText(
                    text=consentTexts[index],
                    modifier=Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                )
            }
        }
        CheckmarkButtonRow(
            enabled=(consentChecked.all{it}),
            onClick={
                if(consentChecked.all{it}){
                    sharedPrefs.edit().putBoolean(Constants.key_isConsentDone,true).apply()
                    onNavigate(Screens.sGenderOptions)
                }
            }
        )
    }
}

//TODO: MOVE THESE DOWN!
data class GenderOption(
    var id: Int,
    var name: String,
    var description: String
)

@Composable
fun GenderOptionsScreen(onNavigate:(String)->Unit){
    val context=LocalContext.current
    val sharedPrefs=context.getSharedPreferences(Constants.key_package, Context.MODE_PRIVATE)
    var selectedOption by remember{mutableStateOf(sharedPrefs.getInt(Constants.key_gender,-1))}
    val genderItems=listOf(
        GenderOption(0,stringResource(id=R.string.genderTransMan),stringResource(id=R.string.tmanDescription)),
        GenderOption(1,stringResource(id=R.string.genderNB),stringResource(id=R.string.nbDescription)),
        GenderOption(2,stringResource(id=R.string.genderWoman),"")
    )
    SideEffect{
        if(sharedPrefs.getInt(Constants.key_gender,-1)!=-1)
            onNavigate(Screens.sStealthOptions)
    }

    Column(modifier=Modifier.fillMaxWidth()){
        BetterText(
            text=stringResource(id=R.string.yourIdentityHeader),
            fontSize=42.sp,
            modifier=Modifier
                .padding(15.dp)
                .fillMaxWidth(),
            textAlign=TextAlign.Center
        )
        BetterText(
            text=stringResource(id=R.string.chooseMostFittingOption),
            fontSize=25.sp,
            modifier=Modifier
                .padding(start=15.dp,end=15.dp,top=5.dp,bottom=15.dp)
                .fillMaxWidth(),
            textAlign=TextAlign.Center
        )
        genderItems.forEach{item->
            Row(
                modifier=Modifier
                    .padding(vertical=10.dp,horizontal=12.dp)
                    .border(
                        width=1.dp,
                        color=if(selectedOption==item.id) colorQuaternary() else colorTertiary(),
                        shape=RoundedCornerShape(20)
                    )
                    .fillMaxWidth()
            ){
                RadioButton(
                    selected=(selectedOption==item.id),
                    onClick={selectedOption=item.id},
                    colors=RadioButtonDefaults.colors(
                        selectedColor=colorSecondary(),
                        unselectedColor=colorSecondary()
                    ),
                )
                Column{
                    BetterText(
                        text=item.name,
                        fontSize=32.sp,
                        modifier=Modifier.padding(horizontal=6.dp, vertical=6.dp)
                    )
                    if(item.description.isNotEmpty()){
                        BetterText(
                            text=item.description,
                            fontSize=18.sp,
                            modifier=Modifier
                                .padding(start=6.dp, bottom=10.dp, end=6.dp)
                        )
                    }
                }
            }
        }

        Row(
            modifier=Modifier.fillMaxWidth(),
            horizontalArrangement=Arrangement.End
        ){
            IconButton(
                onClick={
                    sharedPrefs.edit().putInt(Constants.key_gender,selectedOption).apply()
                    //0-tm 1-nb 2-female
                    onNavigate(Screens.sStealthOptions)
                },
                modifier=Modifier
                    .padding(top=30.dp,end=35.dp)
            ){
                Image(
                    painter=painterResource(id=R.drawable.icon_checkmark_circle),
                    contentDescription=null,
                    modifier=Modifier
                        .scale(3.5f)
                )
            }
        }
    }
}

@Composable
fun StealthOptionsScreen(onNavigate:(String) -> Unit){
    val context=LocalContext.current
    val sharedPrefs=context.getSharedPreferences(Constants.key_package, Context.MODE_PRIVATE)
    val gender=sharedPrefs.getInt(Constants.key_gender,-1)
    //0-tm 1-nb 2-female
    var censorPeriod by remember{mutableStateOf(isPeriodCensored(context))}
    val stealthDone=sharedPrefs.getBoolean(Constants.key_isStealthDone,false)
    val next=if(gender==0||gender==1)Screens.sTOptions else Screens.sAgeConsentOptions
    //todo: later use another screen, and T/contraceptive notifications will be together with this

    SideEffect{
        if(stealthDone)
            onNavigate(next)
    }

    LaunchedEffect(censorPeriod){} //doesn't work
    Column(
        modifier=Modifier.fillMaxWidth()
    ){
        BetterText(
            text=stringResource(id=R.string.useStealthModeQuestion),
            fontSize=40.sp,
            textAlign=TextAlign.Center,
            modifier=Modifier
                .padding(top=18.dp,bottom=25.dp)
                .fillMaxWidth()
        )
        BetterText(
            text=stringResource(id=R.string.stealthModeDescription),
            fontSize=25.sp,
            textAlign=TextAlign.Center,
            modifier=Modifier
                .fillMaxWidth()
                .padding(horizontal=15.dp),
        )
        Row(
            modifier=Modifier.fillMaxWidth(),
            horizontalArrangement=Arrangement.Center
        ){
            BetterButton(
                onClick={toggleStealthMode(context)},
                shape=RoundedCornerShape(15.dp),
                modifier=Modifier
                    .width(190.dp)
                    .height(100.dp)
                    .padding(vertical=20.dp)
            ){
                BetterText(
                    text=if(isStealthModeOn(context)) "NORMAL" else "STEALTH",
                    fontSize=24.sp
                )
            }
        }
        if(gender==0||gender==1){
            //0-tm 1-nb 2-female
            BetterText(
                text=stringResource(id=R.string.censorPeriodQuestion),
                fontSize=26.sp,
                modifier=Modifier
                    .fillMaxWidth()
                    .padding(vertical=5.dp),
                textAlign=TextAlign.Center
            )
        }
        Row( //turn on/off period censor
            modifier=Modifier.fillMaxWidth(),
            horizontalArrangement=Arrangement.Center
        ){
            BetterButton(
                onClick={togglePeriodCensor(context);censorPeriod=!censorPeriod},
                shape=RoundedCornerShape(15.dp),
                modifier=Modifier
                    .width(190.dp)
                    .height(100.dp)
                    .padding(vertical=20.dp)

            ){
                BetterText(
                    text=if(censorPeriod) stringResource(id=R.string.usePeriod) else stringResource(id=R.string.useCensorPeriod),
                    fontSize=24.sp
                )
            }
        }

        Row(//go to next button
            modifier=Modifier.fillMaxWidth(),
            horizontalArrangement=Arrangement.End
        ){
            IconButton(
                onClick={
                    sharedPrefs.edit().putBoolean(Constants.key_isStealthDone,true).apply()
                    //0-tm 1-nb 2-female
                    if(gender==0||gender==1)
                        onNavigate(Screens.sTOptions)
                    else
                        onNavigate(Screens.sAgeConsentOptions)
                },
                modifier=Modifier
                    .padding(top=30.dp,end=35.dp)
            ){
                Image(
                    painter=painterResource(id=R.drawable.icon_checkmark_circle),
                    contentDescription=null,
                    modifier=Modifier
                        .scale(3.2f)
                )
            }
        }
        
    }
}
@OptIn(ExperimentalMaterial3Api::class)
object PastOrPresentSelectableDates:SelectableDates{
    override fun isSelectableDate(utcTimeMillis:Long):Boolean{
        return utcTimeMillis<=System.currentTimeMillis()
    }
    override fun isSelectableYear(year:Int):Boolean{
        return year<=LocalDate.now().year
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TOptionsScreen(onNavigate:(String) -> Unit){
    val context=LocalContext.current
    val sharedPrefs=context.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
    /**0-trans man 1-non binary 2-female**/
    val gender=sharedPrefs.getInt(Constants.key_gender,-1)
 /*   if(gender==2||sharedPrefs.getBoolean(Constants.key_testosteroneMenuComplete,false)){
        onNavigate(Screens.sPeriodOptions)
    }
    */

    val temp=if(sharedPrefs.contains("isOnT"))sharedPrefs.getBoolean(Constants.key_isTakingTestosterone,false) else null
    /**nullable, null->ask if taking T if null, show the screens if true/false **/
    var isOnT:Boolean? by remember{mutableStateOf(temp)}
    LaunchedEffect(isOnT){}
    var testosteroneName by remember{mutableStateOf("")}
    var testosteroneInterval by remember{mutableStateOf("")} //yeah it gotta be a string

    ///region datepickers setup
    //later: testosteroneInterval.toIntOrNull()
    val lastDoseDatePickerState=rememberDatePickerState(
        selectableDates=PastOrPresentSelectableDates
    )
    val lastDoseSelectedDate=lastDoseDatePickerState.selectedDateMillis?.let{
        convertMillisToDate(it)
    }?:""

    val firstDoseDatePickerState=rememberDatePickerState(
        selectableDates=PastOrPresentSelectableDates
    )
    val firstDoseSelectedDate=firstDoseDatePickerState.selectedDateMillis?.let{
        convertMillisToDate(it)
    }?:""
    val datePickerColors=DatePickerDefaults.colors(
        titleContentColor=colorSecondary(),
        headlineContentColor=colorSecondary(),
        weekdayContentColor=colorSecondary(),
        dayContentColor=colorSecondary(),

        todayDateBorderColor=colorSecondary(),
        todayContentColor=colorSecondary(),
        selectedDayContainerColor=colorTertiary(),
        selectedDayContentColor=colorSecondary(),

        containerColor=colorPrimary(),
        subheadContentColor=Color.Red,
        yearContentColor=colorSecondary(),
        currentYearContentColor=colorQuaternary(),
        selectedYearContentColor=colorQuaternary(),
        selectedYearContainerColor=colorTertiary(),

        disabledDayContentColor=colorTertiary(),

        disabledSelectedYearContentColor=colorTertiary(),
        disabledSelectedYearContainerColor=colorTertiary(),
        disabledSelectedDayContentColor=colorTertiary(),
    )
    //endregion

    //val theme=sharedPrefs.getString("theme","Black")

    sharedPrefs.edit().putString(Constants.key_firstTestosteroneDate,firstDoseSelectedDate).apply()
    sharedPrefs.edit().putString(Constants.key_lastTestosteroneDate,lastDoseSelectedDate).apply()
    //todo IMPORTANT put testosterone name to db!
    if(testosteroneInterval.isDigitsOnly()){
        sharedPrefs.edit().putInt(Constants.key_testosteroneInterval,testosteroneInterval.toInt()).apply()
    }


    LazyColumn(
        modifier=//if(theme=="Pride") //theme
        Modifier
            .fillMaxSize()
            //.paint(painterResource(id=R.drawable.pride50),contentScale=ContentScale.FillBounds)
        //else Modifier.fillMaxSize()
    ){
        if(isOnT==null){
            item{
                YesNoRow(
                    onClickYes={
                        isOnT=true
                        sharedPrefs.edit().putBoolean(Constants.key_isTakingTestosterone,true).apply()
                    },
                    onClickNo={
                        isOnT=false
                        sharedPrefs.edit().putBoolean(Constants.key_isTakingTestosterone,false).apply()
                    },
                    question="Are you currently taking testosterone?",
                    questionFontSize="ML"
                )
            }
        }
        if(isOnT==true){
            item{GoBackButtonRow(onClick={isOnT=null})}
            item{
                BetterHeader(
                    text="Enter name of your testosterone",
                    fontSize="ML",
                    modifier=Modifier
                        .fillMaxWidth()
                        .padding(vertical=12.dp,horizontal=20.dp)
                )
            }
            item{
                Row(
                    modifier=Modifier
                        .padding(vertical=12.dp)
                        .fillMaxWidth()
                ){
                    BetterTextField(
                        value=testosteroneName,
                        onValueChange={testosteroneName=it},
                        placeholderText="it can be anything",
                    )
                }
            }
            item{
                BetterHeader(
                    text="How often do you take it?",
                    fontSize="M",
                    modifier=Modifier
                        .fillMaxWidth()
                        .padding(top=25.dp,bottom=10.dp)
                )
            }
            item{
                Row(
                    modifier=Modifier
                        .padding(vertical=12.dp)
                ){
                    BetterTextField(
                        value=testosteroneInterval,
                        onValueChange={testosteroneInterval=it},
                        placeholderText="in days, 1->everyday",
                        imeAction=ImeAction.Done,
                        keyboardType=KeyboardType.Number
                    )
                }
            }
            item{
                BetterHeader(
                    text="When was your last dose?",
                    fontSize="M",
                    modifier=Modifier
                        .fillMaxWidth()
                        .padding(top=30.dp,bottom=15.dp)
                )
            }
            item{
                BetterHeader(
                    text=lastDoseSelectedDate,
                    fontSize="MS"
                )
            }
            item{
                Box(modifier=Modifier.padding(horizontal=10.dp)){
                    DatePicker(
                        title=null,
                        headline=null,
                        state=lastDoseDatePickerState,
                        showModeToggle=false,
                        colors=datePickerColors
                    )
                }
            }

            item{
                BetterHeader(
                    text="When did you start taking testosterone?",
                    fontSize="M",
                    modifier=Modifier
                        .fillMaxWidth()
                        .padding(top=30.dp)
                )
            }
            item{
                BetterHeader(
                    text="This date helps track milestones.\nIf you've taken breaks from testosterone, enter the date you consider your starting point.",
                    fontSize="S",
                    fontStyle=FontStyle.Italic,
                    modifier=Modifier
                        .fillMaxWidth()
                        .padding(top=6.dp,start=20.dp,end=20.dp,bottom=20.dp),
                )
            }
            item{
                BetterHeader(
                    text=firstDoseSelectedDate,
                    fontSize="MS",
                )
            }
            item{
                Box(modifier=Modifier.padding(horizontal=10.dp)){
                    DatePicker(
                        title=null,
                        headline=null,
                        state=firstDoseDatePickerState,
                        showModeToggle=false,
                        colors=datePickerColors
                    )
                }
            }
            
            item{
                YesNoRow(
                    onClickYes={sharedPrefs.edit().putBoolean(Constants.key_isMicrodosing,true).apply()},
                    onClickNo={sharedPrefs.edit().putBoolean(Constants.key_isMicrodosing,false).apply()},
                    question="Are you microdosing?"
                )
            }
            
            item{
                CheckmarkButtonRow(
                    onClick={
                        onNavigate(Screens.sPeriodOptions)
                        sharedPrefs.edit().putBoolean(Constants.key_testosteroneMenuComplete,true).apply()
                    },
                    rowModifier=Modifier.padding(bottom=50.dp)
                )
            }
        }
        else if(isOnT==false){
            item{GoBackButtonRow(onClick={isOnT=null})}
            item{
                YesNoRow(
                    onClickYes={sharedPrefs.edit().putBoolean(Constants.key_isPlanningToTakeTestosterone,true).apply()},
                    onClickNo={sharedPrefs.edit().putBoolean(Constants.key_isPlanningToTakeTestosterone,false).apply()},
                    question="Are you planning to take testosterone in future?")
            }
            item{
                BetterHeader(
                    text="if you're not sure, select \"yes\" and you'll be asked about it again in a while",
                    fontSize="S",
                    fontStyle=FontStyle.Italic,
                    modifier=Modifier
                        .fillMaxWidth()
                        .padding(horizontal=20.dp,vertical=10.dp)
                )
            }
            item{
                CheckmarkButtonRow(onClick={
                    onNavigate(Screens.sPeriodOptions)
                    sharedPrefs.edit().putBoolean(Constants.key_testosteroneMenuComplete,true).apply()
                })
            }
        }
    }
}


@Composable
fun PeriodOptionsScreen(onNavigate:(String) -> Unit){
    val context=LocalContext.current
    val sharedPrefs=context.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
    /**0-trans man 1-non binary 2-female**/
    val gender=sharedPrefs.getInt(Constants.key_gender,-1)
    val takingT=sharedPrefs.getBoolean(Constants.key_isTakingTestosterone,false)

    val question1=if(takingT) "Do you still have periods?" else "Are your periods regular?"

    LazyColumn{
        item{
            YesNoRow(
                onClickYes={ /*TODO*/},
                onClickNo={ /*TODO*/},
                question="aaa")
        }
    }
}

@Composable
fun ContraceptiveOptionsScreen(onNavigate:(String) -> Unit){
    BetterText(text="contraceptive")
}

//region MainActivity
@Composable
fun MainScreen(onNavigate:(String)->Unit){
    onNavigate(Screens.sAgeConsentOptions)
    //val colorTertiary=getColor(color=com.google.android.material.R.attr.colorTertiary)
    //val colorSecondary=getColor(color=com.google.android.material.R.attr.colorSecondary)
    data class BottomNavItem(
        val icon:Int,
        val text:String,
        val fragmentName:String
    )

    val bottomNavItems=listOf(
        BottomNavItem(R.drawable.home_icon,getLocal(id=R.string.home_button_name),"Home"),
        BottomNavItem(R.drawable.calendar_icon,getLocal(id=R.string.calendar_button_text),"Calendar"),
        BottomNavItem(R.drawable.add_icon,getLocal(id=R.string.add_button_text),"Add"),
        BottomNavItem(R.drawable.account_icon,getLocal(id=R.string.account_button_name),"Settings")
    )


    var currentFragment by remember{mutableStateOf("Home")}

    Column(modifier=Modifier.fillMaxSize()){
        Column(modifier=Modifier
            .weight(1f)
            .fillMaxHeight(0f)){
            Scaffold(
                content={padding->
                    Box(modifier=Modifier.padding(padding)){
                        when(currentFragment) {
                            "Home"->HomeScreen()
                            "Calendar"->CalendarScreen()
                            "Add"->AddScreen()
                            "Settings"->SettingsScreen(onButtonClick={targetScreen->onNavigate(targetScreen)})
                        }

                    }
                },
                bottomBar={
                    Row(
                        modifier=Modifier
                            .fillMaxWidth()
                            .background(colorTertiary())
                            .heightIn(min=50.dp),
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
    Text("Home")
}

@Composable
fun CalendarScreen(){
    Text("Calendar")
}

@Composable
fun AddScreen(){
    ScrollableMetricsView()
}

@Composable
fun SettingsScreen(onButtonClick:(String)->Unit){
    val context=LocalContext.current
    Column(modifier=Modifier
        .background(colorPrimary())
        .fillMaxSize()
    ){
            BetterButton(
                onClick={onButtonClick(Screens.sTheme)},
                modifier=Modifier
                    .height(100.dp)
                    .width(150.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical=15.dp)
            ){BetterText(text="CHANGE THEME",textAlign=TextAlign.Center)}
        BetterButton(
            onClick={onButtonClick(Screens.sLanguage)},
            modifier=Modifier
                .height(100.dp)
                .width(150.dp)
                .align(Alignment.CenterHorizontally)
                .padding(vertical=15.dp)
        ){BetterText(text="CHANGE LANGUAGE",textAlign=TextAlign.Center)}
        BetterButton(
            onClick={toggleStealthMode(context)},
            modifier=Modifier
                .height(100.dp)
                .width(150.dp)
                .align(Alignment.CenterHorizontally)
                .padding(vertical=15.dp)
        ){
            BetterText(
                text="TOGGLE STEALTH MODE",
                textAlign=TextAlign.Center
            )
        }
    }


}
//endregion


@Composable
fun LanguageScreen(){
    LanguageMenu()
}

@Composable
fun ThemeScreen(){
    ThemesSettings()
}
//TODO BIG:
// 1 FIX PASSWORD ERRORS NOT SHOWING!
// 2. ADD RECOVERY WHEN FORGOT PASSWORD
// 3. REDO INTRODUCTIONS/SETTINGS ( AND make it better)
// 4. (don't forget setup complete)
// 5. BRING BACK CREATE PASSWORD/RECOVERY POPUP
// 6. SOCIALS/CREDITS TAB

@Composable
fun WelcomeScreen(onNavigate:(String)->Unit){
    val context=LocalContext.current

    val sharedPrefs=context.getSharedPreferences(Constants.key_package, Context.MODE_PRIVATE)
    val setupDone=sharedPrefs.getBoolean(Constants.key_isSetupDone,false)
    val languageSetupDone=sharedPrefs.getBoolean(Constants.key_isLanguageSetUp,false)
    val themeSetupDone=sharedPrefs.getBoolean(Constants.key_isThemeSetUp,false)
    //sharedPrefs.edit().putBoolean(Constants.key_isSetupDone,false).apply()
    if(setupDone){
        SideEffect{
            if(!sharedPrefs.getString(Constants.key_passwordValue,"").isNullOrEmpty()){ //there is password
                onNavigate(Screens.sPassword)
            }
            else{
                Handler(Looper.getMainLooper()).postDelayed({
                    onNavigate(Screens.sMain)
                },500)
            }
        }
    }
    else{
        createNotificationChannel(context) //todo: i think i need to check if it exists before
        Handler(Looper.getMainLooper()).postDelayed({
            if(!languageSetupDone)
                onNavigate(Screens.sLanguage)
            else if(!themeSetupDone)
                onNavigate(Screens.sTheme)
            else{
                onNavigate(Screens.sPassword) //TODO: CHANGE TO INTRO SCREEN
            }
        }, 1000)
    }
//    sharedPrefs.edit().putBoolean("setup",false).apply()


    Column(modifier=Modifier.fillMaxSize()){
        Image(
            painter=painterResource(id=R.drawable.icon_shark_normal),
            contentDescription=null,
            modifier=Modifier
                .fillMaxWidth()
                .padding(top=50.dp)
        )
        BetterText(
            text=getLocal(id=R.string.welcome_text),
            textAlign=TextAlign.Center,
            modifier=Modifier
                .fillMaxWidth()
                .padding(vertical=20.dp,horizontal=10.dp),
            fontSize=50.sp
        )
    }
}
@Composable
fun PasswordScreen(onNavigate:(String)->Unit){
    val context=LocalContext.current
    val sharedPrefs=context.getSharedPreferences(Constants.key_package, Context.MODE_PRIVATE)
    //passwordScreenType 0-select 1-text 2-pin
    var passwordScreenType by remember{mutableStateOf(sharedPrefs.getInt(Constants.key_passwordScreenType,0))}
    var selectedOption by remember{mutableStateOf(2)}
    //doc: pinScreenType 0-create 1-confirm 2-enter
    var pinScreenType by remember{mutableStateOf(sharedPrefs.getInt(Constants.key_pinScreenType,1))} //previously pinButtonType
    //passwordValue THE password
    val passwordValue=sharedPrefs.getString(Constants.key_passwordValue,"")

    var failedAttemptsCount=sharedPrefs.getInt(Constants.key_failedAttempts,0)
    val setupDone=sharedPrefs.getBoolean(Constants.key_isSetupDone,false)
    val recoverySet=sharedPrefs.getBoolean(Constants.key_isRecoverySetUp,false)

    LaunchedEffect(passwordScreenType){

    }
    var showPasswordPressed by remember{mutableStateOf(false)}
    var enteredPassword by remember{mutableStateOf("")}
    var enteredRepeatedPassword by remember{mutableStateOf("")}
    var showError by remember{mutableStateOf("")}

    val differentPasswordsError=stringResource(id=R.string.passwords_different_error)
    val tooShortPasswordError=stringResource(id=R.string.password_length_error)
    val wrongPasswordError=stringResource(id=R.string.wrong_password)
    val wrong3TimesError=stringResource(id=R.string.wrong_password_wait)
    val wrongPinLengthError=stringResource(id=R.string.pin_length_error)
    val differentPinsError=stringResource(id=R.string.pins_different_error)
    val unknownError=stringResource(id=R.string.errorUnknown)

    var canExecute=true
    var digitsEntered by remember{mutableStateOf(0)}
    var enteredPin by remember{mutableStateOf("")}


    if(passwordScreenType==0){
        //display choose type
        Column(modifier=Modifier.fillMaxWidth()){
            BetterText(
                text=getLocal(id=R.string.password_type_title),
                fontSize=38.sp,
                modifier=Modifier.padding(horizontal=15.dp, vertical=5.dp),
                textAlign=TextAlign.Center
            )
            Row(modifier=Modifier.padding(top=20.dp)){
                RadioButton(
                    selected=(selectedOption==0),
                    onClick={selectedOption=0},
                    colors=RadioButtonDefaults.colors(selectedColor=colorSecondary(),unselectedColor=colorSecondary()),
                )
                BetterText(
                    text=getLocal(id=R.string.password_text_password),
                    fontSize=30.sp
                )
            }
            Row(modifier=Modifier.padding(top=20.dp)){
                RadioButton(
                    selected=(selectedOption==1),
                    onClick={selectedOption=1},
                    colors=RadioButtonDefaults.colors(selectedColor=colorSecondary(),unselectedColor=colorSecondary()),
                )
                BetterText(
                    text=getLocal(id=R.string.password_pin_password),
                    fontSize=30.sp
                )
            }
            Row(modifier=Modifier.padding(top=20.dp)){
                RadioButton(
                    selected=(selectedOption==2),
                    onClick={selectedOption=2},
                    colors=RadioButtonDefaults.colors(selectedColor=colorSecondary(),unselectedColor=colorSecondary()),
                )
                BetterText(
                    text=getLocal(id=R.string.dont_want_password),
                    fontSize=30.sp
                )
            }
            Row(modifier=Modifier.fillMaxWidth()){
                IconButton(onClick={
                    when(selectedOption) {
                        0->{
                            passwordScreenType=1 //text

                        }
                        1->{
                            passwordScreenType=2 //pin
                            pinScreenType=1
                        }
                        2->{
                            passwordScreenType=0 //none
                            sharedPrefs.edit().putBoolean(Constants.key_isSetupDone,true).apply()
                            onNavigate(Screens.sMain)
                            sharedPrefs.edit().putInt(Constants.key_passwordScreenType,passwordScreenType).apply()
                        }
                    }
                    sharedPrefs.edit().putInt(Constants.key_passwordScreenType,passwordScreenType).apply()
                },
                    modifier=Modifier
                        .fillMaxWidth()
                        .padding(30.dp)){
                   Image(
                       painter=painterResource(id=R.drawable.icon_checkmark_circle),
                       contentDescription=null,
                       modifier=Modifier
                           .scale(3f)
                   )
                }
            }

        }
    }
    else if(passwordScreenType==1){
       val creatingPassword=passwordValue.isNullOrEmpty()
//        creatingPassword=!creatingPassword
        Column(modifier=Modifier.fillMaxWidth()){
            BetterText(
                text=if(creatingPassword) getLocal(id=R.string.create_password)
                    else getLocal(id=R.string.enter_password),
                fontSize=40.sp,
                textAlign=TextAlign.Center,
                modifier=Modifier
                    .fillMaxWidth()
                    .padding(vertical=30.dp)
            )
            if(creatingPassword){
                BetterText(
                    text=getLocal(id=R.string.password_warning),
                    fontSize=22.sp,
                    modifier=Modifier.padding(horizontal=20.dp)
                )
            }
            Row(modifier=Modifier
                .fillMaxWidth()
                .padding(top=15.dp)
            ){
                if(!creatingPassword){
                    IconButton(
                        onClick={},
                        modifier=Modifier
                            .pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while(true) {
                                        val event=awaitPointerEvent()
                                        if(event.type==PointerEventType.Press)
                                            showPasswordPressed=true
                                        else if(event.type==PointerEventType.Release)
                                            showPasswordPressed=false
                                    }
                                }
                            }
                            .align(Alignment.CenterVertically)

                    ){
                        Icon(
                            painter=if(showPasswordPressed)painterResource(id=R.drawable.icon_eye)
                            else painterResource(id=R.drawable.icon_eye_closed),
                            contentDescription=null,
                            tint=Color.Unspecified,
                            modifier=Modifier
                                .scale(2f)
                                .padding(start=10.dp)
                        )
                    }
                }
                TextField(
                    value=enteredPassword,
                    onValueChange={enteredPassword=it},
                    modifier=Modifier
                        .padding(start=15.dp,end=10.dp)
                        .weight(1f),
                    colors=TextFieldDefaults.textFieldColors(
                        placeholderColor=colorSecondary(),
                        textColor=colorSecondary(),
                        focusedIndicatorColor=colorSecondary(),
                        unfocusedIndicatorColor=colorSecondary(),
                        cursorColor=colorSecondary()
                    ),
                    textStyle=TextStyle(fontSize=24.sp),
                    placeholder={
                        BetterText(
                            text=getLocal(id=R.string.password),
                            fontSize=24.sp,
                            modifier=Modifier.fillMaxWidth(),
                            textAlign=TextAlign.Center
                        )},
                    visualTransformation=if(!showPasswordPressed)PasswordVisualTransformation() else VisualTransformation.None,
                    keyboardOptions=KeyboardOptions.Default.copy(keyboardType=KeyboardType.Password),
                    maxLines=1
                )
                if(!creatingPassword){
                    IconButton(
                        onClick={
                                if(enteredPassword==passwordValue&&canExecute){
                                    showError=""
                                    failedAttemptsCount=0
                                    sharedPrefs.edit().putInt(Constants.key_failedAttempts,failedAttemptsCount).apply()
                                    onNavigate(Screens.sMain)
                                }
                                else{
                                    showError=wrongPasswordError
                                    failedAttemptsCount++
                                    sharedPrefs.edit().putInt(Constants.key_failedAttempts,failedAttemptsCount).apply()
                                    if(failedAttemptsCount in 3..4){
                                        showError=wrong3TimesError
                                        canExecute=false
                                        Handler(Looper.getMainLooper()).postDelayed({
                                            canExecute=true
                                            showError=""
                                        },30*1000)
                                    }
                                    else if(failedAttemptsCount>4){
                                        if(recoverySet)
                                            onNavigate(Screens.sRecovery)
                                        //todo i don't know what else
                                    }
                                }

                        },
                        modifier=Modifier.padding(end=10.dp)
                    ){
                        Icon(
                            painter=painterResource(id=R.drawable.icon_login_door),
                            contentDescription=null,
                            tint=Color.Unspecified,
                            modifier=Modifier
                                .scale(2.5f)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }
            if(creatingPassword){
                BetterText(
                    text=getLocal(id=R.string.password_repeat),
                    textAlign=TextAlign.Center,
                    fontSize=30.sp,
                    modifier=Modifier
                        .padding(top=30.dp)
                        .fillMaxWidth()

                )
                TextField(
                    value=enteredRepeatedPassword,
                    onValueChange={enteredRepeatedPassword=it},
                    modifier=Modifier
                        .padding(start=15.dp,end=10.dp,top=10.dp)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    colors=TextFieldDefaults.textFieldColors(
                        placeholderColor=colorSecondary(),
                        textColor=colorSecondary(),
                        focusedIndicatorColor=colorSecondary(),
                        unfocusedIndicatorColor=colorSecondary(),
                        cursorColor=colorSecondary()
                    ),
                    textStyle=TextStyle(fontSize=24.sp),
                    placeholder={
                        BetterText(
                            text=getLocal(id=R.string.password),
                            fontSize=24.sp,
                            modifier=Modifier.fillMaxWidth(),
                            textAlign=TextAlign.Center
                        )},
                    visualTransformation=PasswordVisualTransformation(),
                    keyboardOptions=KeyboardOptions.Default.copy(keyboardType=KeyboardType.Password)
                )
                Row(modifier=Modifier.fillMaxWidth()){
                    Spacer(modifier=Modifier.weight(1f))
                    IconButton(
                        onClick={
                                //creating password
                                if(enteredPassword!=enteredRepeatedPassword){
                                    showError=differentPasswordsError
                                }
                                else{
                                    showError=""
                                    if(enteredPassword.length<6){
                                        showError=tooShortPasswordError
                                    }
                                    else{
                                        showError=""
                                        sharedPrefs.edit().putString(Constants.key_passwordValue,enteredPassword).apply()
                                        if(recoverySet)
                                            onNavigate(Screens.sMain)
                                        else
                                            onNavigate(Screens.sRecovery)
                                    }
                                }

                        },
                        modifier=Modifier.padding(top=30.dp, end=30.dp, bottom=20.dp)

                    ){
                        Icon(
                            painter=painterResource(id=R.drawable.icon_checkmark_circle),
                            contentDescription=null,
                            tint=Color.Unspecified,
                            modifier=Modifier
                                .scale(2.9f)
                        )
                    }
                }
                    Text(
                        text=showError,
                        modifier=Modifier.fillMaxWidth(),
                        fontSize=25.sp,
                        textAlign=TextAlign.Center,
                        color=Color.Red


                    )
            }
        }
    }
    else if(passwordScreenType==2){
        Column(
            modifier=Modifier.fillMaxSize(),
            horizontalAlignment=Alignment.CenterHorizontally
        ){
            BetterText(
                text=
                    if(pinScreenType==0) getLocal(id=R.string.enter_pin)
                    else if(pinScreenType==1) getLocal(id=R.string.create_pin)
                    else getLocal(id=R.string.enter_pin_again), //2
                fontSize=40.sp,
                textAlign=TextAlign.Center,
                modifier=Modifier
                    .padding(vertical=15.dp)
                    .fillMaxWidth()

            )
            Row(
                modifier=Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement=Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)

            ){
                //region small buttons
                Icon(
                    painter=if(digitsEntered>0) painterResource(id=R.drawable.rounded_button_filled_img)
                    else painterResource(id=R.drawable.rounded_button_img),
                    contentDescription=null,
                    modifier=Modifier
                        .padding(horizontal=8.dp)
                        .scale(1.6f),
                    tint=Color.Unspecified
                )
                Icon(
                    painter=if(digitsEntered>1) painterResource(id=R.drawable.rounded_button_filled_img)
                    else painterResource(id=R.drawable.rounded_button_img),
                    contentDescription=null,
                    modifier=Modifier
                        .padding(horizontal=8.dp)
                        .scale(1.6f),
                    tint=Color.Unspecified
                )
                Icon(
                    painter=if(digitsEntered>2) painterResource(id=R.drawable.rounded_button_filled_img)
                    else painterResource(id=R.drawable.rounded_button_img),
                    contentDescription=null,
                    modifier=Modifier
                        .padding(horizontal=8.dp)
                        .scale(1.6f),
                    tint=Color.Unspecified
                )
                Icon(
                    painter=if(digitsEntered>3) painterResource(id=R.drawable.rounded_button_filled_img)
                    else painterResource(id=R.drawable.rounded_button_img),
                    contentDescription=null,
                    modifier=Modifier
                        .padding(horizontal=8.dp)
                        .scale(1.6f),
                    tint=Color.Unspecified
                )
            }
            //endregion
            Column(
                modifier=Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start=20.dp,end=20.dp,top=25.dp)
            ){
                val buttonDigits=listOf("1","2","3","4","5","6","7","8","9")
                val buttonDigitRow=buttonDigits.chunked(3)
                buttonDigitRow.forEach{rowButtons->
                    Row(
                        modifier=Modifier.fillMaxWidth(),
                        horizontalArrangement=Arrangement.SpaceEvenly
                    ){
                        rowButtons.forEach{text->
                            Button(
                                shape=CircleShape,
                                onClick={
                                    if(canExecute&&digitsEntered<4){
                                        enteredPin+=text
                                        digitsEntered++
                                    }},
                                modifier=Modifier
                                    .padding(10.dp)
                                    .width(100.dp)
                                    .height(100.dp),
                                colors=ButtonDefaults.buttonColors(backgroundColor=colorSecondary()),
                            ){
                                Text(
                                    text=text,
                                    color=colorPrimary(),
                                    fontSize=25.sp
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier=Modifier.fillMaxWidth(),
                    horizontalArrangement=Arrangement.SpaceEvenly,
                    verticalAlignment=Alignment.CenterVertically
                ){
                    IconButton(
                        onClick={
                            if(digitsEntered>0) digitsEntered--
                            enteredPin=enteredPin.dropLast(1)
                                },
                        modifier=Modifier.padding(10.dp)
                    ){
                        Icon(
                            painter=painterResource(id=R.drawable.icon_backspace),
                            contentDescription=null,
                            tint=Color.Unspecified,
                            modifier=Modifier.scale(3.3f)
                        )
                    }
                    Button(
                        //0
                        shape=CircleShape,
                        onClick={
                            if(canExecute&&digitsEntered<4){
                                enteredPin+="0"
                                digitsEntered++
                            }},
                        modifier=Modifier
                            .padding(10.dp)
                            .width(100.dp)
                            .height(100.dp),
                        colors=ButtonDefaults.buttonColors(backgroundColor=colorSecondary()),
                    ){
                        Text(
                            text="0",
                            color=colorPrimary(),
                            fontSize=25.sp
                        )
                    }
                    IconButton(
                        onClick={
                            if(pinScreenType==0){//login
                                if(enteredPin==passwordValue&&canExecute){
                                    showError=""
                                    failedAttemptsCount=0
                                    sharedPrefs.edit().putInt(Constants.key_failedAttempts,failedAttemptsCount).apply()
                                    onNavigate(Screens.sMain)
                                }
                                else{
                                    failedAttemptsCount++
                                    sharedPrefs.edit().putInt(Constants.key_failedAttempts,failedAttemptsCount).apply()
                                    showError=wrongPasswordError
                                    digitsEntered=0
                                    enteredPin=""


                                    if(failedAttemptsCount in 3..4){
                                        showError=wrong3TimesError
                                        canExecute=false
                                        Handler(Looper.getMainLooper()).postDelayed({
                                            canExecute=true
                                            showError=""
                                        },30*1000)
                                    }
                                    else if(failedAttemptsCount>4){
                                        if(recoverySet)
                                            onNavigate(Screens.sRecovery)
                                        //todo i don't know what else i guess user is fucked
                                    }
                                }
                            }
                            else if(pinScreenType==1){
                                if(enteredPin.length!=4){
                                    showError=wrongPinLengthError
                                    digitsEntered=0
                                    enteredPin=""
                                }
                                else{
                                    showError=""
                                    if(enteredPin.length!=digitsEntered){
                                        showError=unknownError
                                        digitsEntered=0
                                        enteredPin=""
                                    }
                                    else{
                                        pinScreenType=2
                                        sharedPrefs.edit().putInt(Constants.key_pinScreenType,pinScreenType).apply()
                                        val pinToSave=enteredPin
                                        sharedPrefs.edit().putString(Constants.key_temporaryPin,pinToSave).apply()
                                        digitsEntered=0
                                        enteredPin=""
                                    }
                                }


                            }
                            else if(pinScreenType==2){
                                val tempPin=sharedPrefs.getString(Constants.key_temporaryPin,"")
                                if(enteredPin==tempPin){
                                    sharedPrefs.edit().putInt(Constants.key_pinScreenType,0).apply()
                                    sharedPrefs.edit().putString(Constants.key_temporaryPin,"").apply()
                                    sharedPrefs.edit().putString(Constants.key_passwordValue,enteredPin).apply()
                                    if(recoverySet)
                                        onNavigate(Screens.sMain)
                                    else
                                        onNavigate(Screens.sRecovery)
                                }
                                else{
                                    showError=differentPinsError
                                    pinScreenType=1
                                    sharedPrefs.edit().putInt(Constants.key_pinScreenType,pinScreenType).apply()
                                    enteredPin=""
                                    digitsEntered=0
                                }
                            }
                        },
                        modifier=Modifier.padding(10.dp)
                    ){
                        Icon(
                            painter=painterResource(id=R.drawable.icon_checkmark_circle),
                            contentDescription=null,
                            tint=Color.Unspecified,
                            modifier=Modifier.scale(4.1f)
                        )
                    }

                }


            }

        }
        //display pin enter/creation
    }


}
@Composable
fun RecoveryScreen(onNavigate:(String)->Unit){
    val context=LocalContext.current
    val sharedPrefs=context.getSharedPreferences(Constants.key_package, Context.MODE_PRIVATE)
    val recoverySet=sharedPrefs.getBoolean(Constants.key_isRecoverySetUp,false)
    val questionsOptions=stringArrayResource(id=R.array.recoveryQuestions)
    var menuExpanded by remember{mutableStateOf(arrayOf(false,false,false))}
    var menuText by remember{mutableStateOf(arrayOf(questionsOptions[0],questionsOptions[0],questionsOptions[0]))}
    
    val noQuestionError=stringResource(id=R.string.recovery_noquestion_error)
    val savedQuestions=arrayOf(
        sharedPrefs.getString(Constants.key_recoveryQuestion1,noQuestionError)!!,
        sharedPrefs.getString(Constants.key_recoveryQuestion2,noQuestionError)!!,
        sharedPrefs.getString(Constants.key_recoveryQuestion3,noQuestionError)!!
    )
    val correctAnswers=arrayOf(
        sharedPrefs.getString(Constants.key_recoveryAnswer1,""),
        sharedPrefs.getString(Constants.key_recoveryAnswer2,""),
        sharedPrefs.getString(Constants.key_recoveryAnswer3,"")
    )
    var showError by remember{mutableStateOf("")}
    var enteredAnswers by remember{mutableStateOf(arrayOf("","",""))}

    val emptyQuestionsError=stringResource(id=R.string.recovery_answer_all)
    val repeatingQuestionsError=stringResource(id=R.string.recovery_questions_repeat)
    val noQuestionChosenError=stringResource(id=R.string.recovery_choose_all)
    val emptyAnswerError=stringResource(id=R.string.recovery_answer_empty)
    val wrongAnswerError=stringResource(id=R.string.recovery_wrong_answer)
    val enterAnswer=stringResource(id=R.string.enter_answer)

    val questionFieldIndex=arrayOf(0,1,2)
    Column{
        BetterText(
            text=
                if(recoverySet) stringResource(id=R.string.answer_recovery_questions)
                else stringResource(id=R.string.recovery_setup_title),
            fontSize=34.sp,
            textAlign=TextAlign.Center,
            modifier=Modifier
                .padding(15.dp)
                .fillMaxWidth()
        )

        questionFieldIndex.forEach{field->
            if(!recoverySet){
                Button(
                    modifier=Modifier
                        .fillMaxWidth()
                        .padding(horizontal=10.dp,vertical=15.dp)
                        .height(50.dp)
                        .border(width=2.dp,color=colorTertiary()),
                    onClick={menuExpanded=menuExpanded.copyOf().apply{this[field]=true}},
                    colors=ButtonDefaults.buttonColors(backgroundColor=colorPrimary()),
                    ){
                    BetterText(text=menuText[field])
                    DropdownMenu(
                        expanded=menuExpanded[field],
                        onDismissRequest={menuExpanded[field]=false},
                        modifier=Modifier
                            .background(colorPrimary())
                            .fillMaxWidth()
                            .padding(horizontal=10.dp),

                            ){
                        questionsOptions.forEach{question->
                            DropdownMenuItem(
                                text={BetterText(text=question)},
                                onClick={
                                    menuExpanded=menuExpanded.copyOf().apply{this[field]=false}
                                    menuText=menuText.copyOf().apply{this[field]=question}
                                }
                            )
                            Divider(color=colorTertiary(),thickness=1.dp)
                        }
                    }
                }
            }
            else{
                BetterText(
                    text=savedQuestions[field],
                    fontSize=20.sp,
                    modifier=Modifier
                        .fillMaxWidth()
                        .padding(top=20.dp,start=10.dp,end=10.dp)


                    ) //read questionsOptions //idk what that means anymore
                }
            TextField(
                value=enteredAnswers[field],
                onValueChange={enteredAnswers=enteredAnswers.copyOf().apply{this[field]=it}},
                modifier=Modifier
                    .padding(horizontal=10.dp),
                colors=TextFieldDefaults.textFieldColors(
                    placeholderColor=colorSecondary(),
                    textColor=colorSecondary(),
                    focusedIndicatorColor=colorSecondary(),
                    unfocusedIndicatorColor=colorSecondary(),
                    cursorColor=colorSecondary()
                ),
                textStyle=TextStyle(fontSize=24.sp),
                placeholder={
                    BetterText(
                        text=
                            if(recoverySet) enterAnswer
                            else menuText[field] ,
                        modifier=Modifier.fillMaxWidth(),
                        )},
                maxLines=1,
                keyboardOptions=KeyboardOptions.Default.copy(imeAction=ImeAction.Done)
            )
        }
        Row(modifier=Modifier.fillMaxWidth()){
            IconButton(onClick={
                if(recoverySet){
                    if(enteredAnswers.all {it.isNotEmpty()}){
                        showError=""
                        if(Utils.simplify(enteredAnswers[0])==correctAnswers[0]){
                            showError=""
                            if(Utils.simplify(enteredAnswers[1])==correctAnswers[1]){
                                showError=""
                                if(Utils.simplify(enteredAnswers[2])==correctAnswers[2]){
                                    showError=""
                                    onNavigate(Screens.sMain)
                                }
                                else{
                                    showError="$wrongAnswerError 3"
                                }
                            }
                            else{
                                showError="$wrongAnswerError 2"
                            }
                        }
                        else{
                            showError="$wrongAnswerError 1"
                        }
                    }
                    else{
                        showError=emptyAnswerError
                    }
                }
                else{
                    //create
                    if(enteredAnswers[0]!=enteredAnswers[1]&&enteredAnswers[1]!=enteredAnswers[2]&&enteredAnswers[0]!=enteredAnswers[2]){
                        showError=""
                        if(menuText.all {it!=questionsOptions[0]}){
                            showError=""
                            if(enteredAnswers.all{it.isNotEmpty()}){
                                //good
                                showError=""
                                with(sharedPrefs.edit()){
                                    putString(Constants.key_recoveryQuestion1,menuText[0])
                                    putString(Constants.key_recoveryQuestion2,menuText[1])
                                    putString(Constants.key_recoveryQuestion3,menuText[2])
                                    putString(Constants.key_recoveryAnswer1,enteredAnswers[0])
                                    putString(Constants.key_recoveryAnswer2,enteredAnswers[1])
                                    putString(Constants.key_recoveryAnswer3,enteredAnswers[2])
                                    putBoolean(Constants.key_isRecoverySetUp,true)
                                    apply()
                                }
                                onNavigate(Screens.sMain)
                            }
                            else{
                                showError=emptyQuestionsError
                            }
                        }
                        else{
                            showError=noQuestionChosenError
                        }
                    }
                    else{
                        showError=repeatingQuestionsError
                    }
                }
            },
                modifier=Modifier
                    .fillMaxWidth()
                    .padding(30.dp)){
                Image(
                    painter=painterResource(id=R.drawable.icon_checkmark_circle),
                    contentDescription=null,
                    modifier=Modifier
                        .scale(3f)
                )
            }
        }
        Text(
            text=showError,
            modifier=Modifier.fillMaxWidth(),
            fontSize=25.sp,
            textAlign=TextAlign.Center,
            color=Color.Red
        )
    }
}

fun convertMillisToDate(millis:Long):String{
    val formatter=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
    return formatter.format(Date(millis))
}
private fun createNotificationChannel(context: Context){
    val channel=NotificationChannel("hrt","Hrt reminders", NotificationManager.IMPORTANCE_HIGH).apply{
        description="This channel is for hrt reminders" //todo: strings
    }

    val notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}

fun isPeriodCensored(context:Context):Boolean{
    val sharedPrefs=context.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
    return sharedPrefs.getBoolean(Constants.key_censorPeriod,false)
}
fun togglePeriodCensor(context: Context){
    val sharedPrefs=context.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
    val censorPeriod=sharedPrefs.getBoolean(Constants.key_censorPeriod,false)
    if(censorPeriod) sharedPrefs.edit().putBoolean(Constants.key_censorPeriod,false).apply()
    else sharedPrefs.edit().putBoolean(Constants.key_censorPeriod,true).apply()
}


fun toggleStealthMode(context:Context){
    val packageManager=context.packageManager
    val stealth=ComponentName(context,"com.example.rainbowcalendar.MainActivityStealth")
    val default=ComponentName(context,"com.example.rainbowcalendar.MainActivity")

    val stealthMode=packageManager.getComponentEnabledSetting(stealth)==PackageManager.COMPONENT_ENABLED_STATE_ENABLED
    packageManager.setComponentEnabledSetting((if(stealthMode) default else stealth),PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP)
    packageManager.setComponentEnabledSetting((if(stealthMode) stealth else default),PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP)
}

fun isStealthModeOn(context:Context):Boolean {
    val packageManager=context.packageManager
    val stealth=ComponentName(context,"com.example.rainbowcalendar.MainActivityStealth")

    return packageManager.getComponentEnabledSetting(stealth)==PackageManager.COMPONENT_ENABLED_STATE_ENABLED
}

//region custom composables

/**
 * @param questionFontSize fontSize for [BetterHeader]
 */
@Composable
fun YesNoRow(
    questionFontSize:String="M",
    onClickYes:()->Unit,
    onClickNo:()->Unit,
    question:String
    ){
    val context=LocalContext.current
    Column(modifier=Modifier.fillMaxWidth()){
        BetterHeader(
            text=question,
            fontSize=questionFontSize,
            modifier=Modifier
                .fillMaxWidth()
                .padding(vertical=15.dp,horizontal=10.dp)
        )
        Row(
            modifier=Modifier
                .padding(vertical=12.dp,horizontal=12.dp)
                .fillMaxWidth(),
            Arrangement.SpaceAround
        ) {
            BetterButton(
                onClick=onClickYes
            ) {
                BetterText(
                    text=context.getString(R.string.yes),
                    fontSize=32.sp,
                    modifier=Modifier.padding(vertical=6.dp)
                )
            }
            BetterButton(
                onClick=onClickNo
            ) {
                BetterText(
                    text=context.getString(R.string.no),
                    fontSize=32.sp,
                    modifier=Modifier.padding(vertical=6.dp)
                )
            }
        }
    }
}


@Composable
fun BetterButton(
    onClick:()->Unit,
    modifier:Modifier=Modifier,
    enabled:Boolean=true,
    shape:androidx.compose.ui.graphics.Shape=RectangleShape,
    content:@Composable RowScope.()->Unit,
){
    Button(
        onClick=onClick,
        modifier=modifier,
        enabled=enabled,
        colors=ButtonDefaults.buttonColors(backgroundColor=colorTertiary()),
        shape=shape,
        content=content,
        contentPadding=PaddingValues(0.dp),
        )
}
/** use with modifier weight(1f) when in row!**/
@Composable
fun BetterTextField(
    value:String,
    onValueChange:(String)->Unit,
    textFieldModifier:Modifier=Modifier,
    placeholderText:String="",
    placeholderTextModifier:Modifier=Modifier,
    visualTransformation:VisualTransformation=VisualTransformation.None,
    keyboardType:KeyboardType=KeyboardType.Text,
    imeAction:ImeAction=ImeAction.Default

){
    TextField(
        value=value,
        onValueChange=onValueChange,
        modifier=textFieldModifier
            .padding(start=15.dp,end=10.dp),
        colors=TextFieldDefaults.textFieldColors(
            placeholderColor=colorSecondary(),
            textColor=colorSecondary(),
            focusedIndicatorColor=colorSecondary(),
            unfocusedIndicatorColor=colorSecondary(),
            cursorColor=colorSecondary()
        ),
        textStyle=TextStyle(fontSize=24.sp),
        placeholder={
            BetterText(
                text=placeholderText,
                fontSize=24.sp,
                modifier=placeholderTextModifier.fillMaxWidth(),
                textAlign=TextAlign.Center
            )},
        maxLines=1,
        visualTransformation=visualTransformation,
        keyboardOptions=KeyboardOptions.Default.copy(keyboardType=keyboardType),
    )
}

@Composable
fun GoBackButtonRow(
    onClick:()->Unit,
    rowModifier:Modifier=Modifier,
    iconButtonModifier:Modifier=Modifier,
    imageScale:Float=2.5f,
    enabled:Boolean=true
){
    Row(
        modifier=rowModifier.fillMaxWidth(),
        horizontalArrangement=Arrangement.Start
    ){
        IconButton(
            onClick=onClick,
            modifier=iconButtonModifier.padding(8.dp),
            enabled=enabled
        ){
            Image(
                painter=painterResource(id=R.drawable.icon_arrow_left_triangle),
                contentDescription=null,
                modifier=Modifier
                    .scale(imageScale)
            )
        }
    }
}

@Composable
fun CheckmarkButtonRow(
    onClick:()->Unit,
    rowModifier:Modifier=Modifier,
    iconButtonModifier:Modifier=Modifier,
    imageScale:Float=3.5f,
    enabled:Boolean=true
){
    Row(
        modifier=rowModifier.fillMaxWidth(),
        horizontalArrangement=Arrangement.End
    ){
        IconButton(
            onClick=onClick,
            modifier=iconButtonModifier.padding(top=30.dp,end=35.dp),
            enabled=enabled
        ){
            Image(
                painter=painterResource(id=R.drawable.icon_checkmark_circle),
                contentDescription=null,
                modifier=Modifier
                    .scale(imageScale)
            )
        }
    }
}

/**
 * @param textAlign center by default
 * @param fontSize XL, L, ML, M, MS, S, XS from 40.sp to 16.sp, default - ML(32.sp)
 * **/
@Composable
fun BetterHeader(
    text:String,
    modifier:Modifier=Modifier.fillMaxWidth(),
    textAlign:TextAlign=TextAlign.Center,
    fontSize:String="ML",
    fontStyle:FontStyle=FontStyle.Normal
){
    val fs=when(fontSize){
        "XL"->40.sp
        "L"->36.sp
        "ML"->32.sp
        "M"->28.sp
        "MS"->34.sp
        "S"->20.sp
        "XS"->16.sp
        else->{32.sp}
    }
    BetterText(
        fontSize=fs,
        text=text,
        textAlign=textAlign,
        modifier=modifier,
        fontStyle=fontStyle
    )
}

@Composable
fun BetterText(
    text:String,
    modifier:Modifier=Modifier,
    textAlign:TextAlign=TextAlign.Start,
    fontSize:TextUnit=16.sp,
    fontStyle:FontStyle=FontStyle.Normal
){
    Text(
        fontSize=fontSize,
        text=text,
        textAlign=textAlign,
        modifier=modifier,
        letterSpacing=0.sp,
        color=colorSecondary(),
        fontStyle=fontStyle
    )
}
//endregion
//region language settings
data class LanguageData(
    val flag: Int,
    val name: String, //name in current language
    val nameLocal: String //name of this language in the language
)

@Composable
fun LanguageMenu(){
    val context=LocalContext.current
    val sharedPrefs=context.getSharedPreferences(Constants.key_package, Context.MODE_PRIVATE)

    val languages=listOf(
        LanguageData(R.drawable.flag_lang_en,getLocal(id=R.string.langEnglish),getLocal(id=R.string.langEnglishLocal)),
        LanguageData(R.drawable.flag_pl,getLocal(id=R.string.langPolish),getLocal(id=R.string.langPolishLocal)),
        LanguageData(R.drawable.flag_fr,getLocal(id=R.string.langFrench),getLocal(id=R.string.langFrenchLocal)),
        LanguageData(R.drawable.flag_br,getLocal(id=R.string.langPortugueseBR),getLocal(id=R.string.langPortugueseBRLocal)),
        LanguageData(R.drawable.flag_ru,getLocal(id=R.string.langRussian),getLocal(id=R.string.langRussianLocal)),
        LanguageData(R.drawable.flag_ua,getLocal(id=R.string.langUkrainian),getLocal(id=R.string.langUkrainianLocal))
    )
    val selectedLanguage=remember{mutableStateOf<String?>(Utils.codeToLanguage(sharedPrefs.getString(Constants.key_language,"en")!!))}
    val languagesState=remember{mutableStateOf(languages)}
    val setupDone=sharedPrefs.getBoolean(Constants.key_isSetupDone,false)
    val languageSetupDone=sharedPrefs.getBoolean(Constants.key_isLanguageSetUp,false)

    Column(modifier=Modifier
        .fillMaxWidth(1f)
        .background(color=colorPrimary())){
        Text(
            color=colorSecondary(),
            text=getLocal(id=R.string.language),
            fontSize=38.sp,
            fontWeight=FontWeight.SemiBold,
            textAlign=TextAlign.Center,
            modifier=Modifier
                .padding(vertical=15.dp,horizontal=10.dp)
                .align(Alignment.CenterHorizontally)
        )
        LazyColumn{
            items(languagesState.value, key={it.nameLocal}){lang ->
                languageItem(flag=lang.flag, name=lang.name, nameLocal=lang.nameLocal,
                    isSelected=lang.nameLocal==selectedLanguage.value,
                    onClick={selectedLanguage.value=lang.nameLocal}
                )
            }
            item{
                Box(
                    modifier=Modifier
                        .fillMaxWidth()
                        .padding(top=25.dp),
                    contentAlignment=Alignment.Center
                ){
                    BetterButton(
                        onClick={
                            if(!languageSetupDone) sharedPrefs.edit().putBoolean(Constants.key_isLanguageSetUp,true).apply()
                            Utils.changeLanguage(Utils.langToCodeNew(selectedLanguage.value!!),context)
                                },
                        modifier=Modifier
                            .width(200.dp)
                            .height(45.dp)
                    ){
                        Text(
                            text=getLocal(id=R.string.save),
                            color=colorSecondary(),
                            fontSize=20.sp
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun languageItem(flag: Int, name:String, nameLocal:String, isSelected:Boolean, onClick:()->Unit){
//    val colorSecondary=getColor(color=com.google.android.material.R.attr.colorSecondary)
//    val colorTertiary=getColor(color=com.google.android.material.R.attr.colorTertiary)
    Row(
        modifier=Modifier
            .padding(horizontal=10.dp,vertical=5.dp)
            .border(
                width=2.dp,shape=CircleShape,
                color=if(isSelected) colorSecondary() else colorTertiary()
            )
            .fillMaxSize(1f)
            .heightIn(min=50.dp)
            .clickable {onClick()}
    ){
        Image(
            painter=painterResource(id=flag),
            contentDescription=null,
            modifier=Modifier
                .align(Alignment.CenterVertically)
                .padding(vertical=10.dp,horizontal=16.dp)
                .sizeIn(maxWidth=40.dp)
        )
        Text(
            text=name,
            fontSize=20.sp,
            color=colorSecondary(),
            modifier=Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal=5.dp)
        )
        Text(
            text=nameLocal,
            fontSize=16.sp,
            color=colorSecondary(),
            textAlign=TextAlign.End,
            modifier=Modifier
                .fillMaxWidth(1f)
                .align(Alignment.CenterVertically)
                .padding(horizontal=14.dp)
                .alpha(0.8f)
        )
    }
}

//endregion

//region themes
data class ThemesData(
    val name:String,
    val img:Int,
    var selected:Boolean
)
var themes=listOf(
    ThemesData("White",R.drawable.white,false),
    ThemesData("Gray",R.drawable.gray,false),
    ThemesData("Black",R.drawable.black,false),
    ThemesData("Blue",R.drawable.blue,false),
    ThemesData("Navy Blue",R.drawable.nblue,false),
    ThemesData("Green",R.drawable.dgreen,false),
    ThemesData("Dark Purple",R.drawable.dpurple,false),
    ThemesData("Khaki",R.drawable.khaki,false),
    ThemesData("Purple",R.drawable.purple,false),
    ThemesData("Pride",R.drawable.pride100, false)
)

@Composable
fun ThemesSettings(){
    val sharedPrefs=LocalContext.current.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
    val theme=sharedPrefs.getString(Constants.key_theme,"Gray")

    for(tm in themes){
        tm.selected=tm.name==theme
    }

    val themesState=remember{mutableStateOf(themes)}
    Column(modifier=Modifier
        .fillMaxWidth(1f)
        .background(colorPrimary())){
        Text(
            color=colorSecondary(),
            text="Choose theme", //todo: strings
            fontSize=44.sp,
            fontWeight=FontWeight.SemiBold,
            modifier=Modifier
                .padding(top=30.dp,bottom=10.dp)
                .align(Alignment.CenterHorizontally)
        )
        LazyRow{
            items(themesState.value, key={it.name}){theme ->
                themeItem(name=theme.name,imgResource=theme.img,selected=theme.selected)
            }
        }
    }
}

@Composable
fun themeItem(name: String, imgResource:Int, selected:Boolean){
    val context=LocalContext.current
    val sharedPrefs=LocalContext.current.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
    val themeSetupDone=sharedPrefs.getBoolean(Constants.key_isThemeSetUp,false)
    var isSelected by remember {mutableStateOf(selected)}
    Column(
        horizontalAlignment=Alignment.CenterHorizontally,
        modifier=Modifier.padding(all=10.dp)
    ){
        Text(
            text=name,
            color=colorSecondary(),
            fontSize=34.sp,
            fontWeight=FontWeight.SemiBold,
            modifier=Modifier.padding(vertical=20.dp)
        )
        Box(modifier=Modifier.heightIn(max=470.dp)){ //todo: change max heightIn so it matches real photos
            Image(
                painter=painterResource(id=imgResource),
                contentDescription=null,
                modifier=Modifier
                    .width(250.dp)
                    .fillMaxSize(1f)
                    .padding(horizontal=5.dp)
                    .border(width=2.dp,color=Color.Black)
            )
        }
        RadioButton(
            selected=(isSelected),
            modifier=Modifier
                .scale(1.5f)
                .padding(top=15.dp),
            colors=RadioButtonDefaults.colors(selectedColor=colorSecondary(),unselectedColor=colorSecondary()),
            onClick={
                if(!themeSetupDone) sharedPrefs.edit().putBoolean(Constants.key_isThemeSetUp,true).apply()
                isSelected=!isSelected
                if(isSelected){
                    sharedPrefs.edit().putString(Constants.key_theme,name).apply()
                    Handler(Looper.getMainLooper()).postDelayed({
                        context.startActivity(Intent(context,MainActivity::class.java))
                    }, 100)
                }
            })
    }
}
//endregion

//region colors
@Composable
fun colorPrimary():Color{
    return getColor(color=com.google.android.material.R.attr.colorPrimary)
}
@Composable
fun colorSecondary():Color{
    return getColor(color=com.google.android.material.R.attr.colorSecondary)
}
@Composable
fun colorTertiary():Color{
    return getColor(color=com.google.android.material.R.attr.colorTertiary)
}
@Composable
fun colorQuaternary():Color{
    return getColor(color=com.google.android.material.R.attr.itemTextColor)
}
//endregion