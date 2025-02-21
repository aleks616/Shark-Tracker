package com.example.rainbowcalendar.fragments

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.contentcapture.ContentCaptureManager
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.example.rainbowcalendar.Constants
import com.example.rainbowcalendar.R
import com.example.rainbowcalendar.ScrollableMetricsView
import com.example.rainbowcalendar.Utils
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import com.example.rainbowcalendar.AutoComplete
import com.example.rainbowcalendar.BetterButton
import com.example.rainbowcalendar.BetterHeader
import com.example.rainbowcalendar.BetterText
import com.example.rainbowcalendar.BetterTextField
import com.example.rainbowcalendar.CheckboxRow
import com.example.rainbowcalendar.CheckmarkButtonRow
import com.example.rainbowcalendar.DateCycle
import com.example.rainbowcalendar.DateInputField
import com.example.rainbowcalendar.ErrorText
import com.example.rainbowcalendar.GoBackButtonRow
import com.example.rainbowcalendar.MyDatePicker
import com.example.rainbowcalendar.NumberInput
import com.example.rainbowcalendar.StyledTimePicker
import com.example.rainbowcalendar.VerticalSpacer
import com.example.rainbowcalendar.YesNoRow
import com.example.rainbowcalendar.colorPrimary
import com.example.rainbowcalendar.colorQuaternary
import com.example.rainbowcalendar.colorSecondary
import com.example.rainbowcalendar.colorTertiary
import com.vsnappy1.timepicker.data.model.TimePickerTime
import java.time.ZoneId

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
    var currentScreen by remember{mutableStateOf(Screens.sWelcome)}
    Log.v("currentScreen",currentScreen)

    when(currentScreen){
        Screens.sWelcome->WelcomeScreen{screen->currentScreen=screen}
        Screens.sLanguage->LanguageScreen()
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
fun AgeConsentOptions(onNavigate:(String)->Unit,thisScreen:String?){
    val context=LocalContext.current
    val sharedPrefs=context.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
    val consentDone=sharedPrefs.getBoolean(Constants.key_isConsentDone,false)
    if(consentDone){
        onNavigate(Screens.sGenderOptions)
    }
    var consentChecked by remember{mutableStateOf(arrayOf(false,false,false,false))}
    val consentTexts=listOf(
        stringResource(id=R.string.termsAndConditions1),
        stringResource(id=R.string.termsAndConditions2),
        stringResource(id=R.string.termsAndConditions3),
        stringResource(id=R.string.termsAndConditions4),
    )
    val index=listOf(0,1,2,3)

    Column(
        modifier=Modifier.fillMaxSize()
    ){
        val prevScreen=Utils.getPreviousScreen(thisScreen,context)
        val previousKey=Utils.previousScreenKey(prevScreen)
        if(thisScreen!=null&&previousKey!=""){
            if(previousKey==Constants.key_gender)
                GoBackButtonRow(onClick={sharedPrefs.edit().putInt(Utils.previousScreenKey(prevScreen),0).apply();onNavigate(prevScreen)})
            else
                GoBackButtonRow(onClick={sharedPrefs.edit().putBoolean(Utils.previousScreenKey(prevScreen),false).apply();onNavigate(prevScreen)})
        }
        BetterHeader(
            text=stringResource(id=R.string.termsAndConditionsTitle),
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
fun GenderOptionsScreen(onNavigate:(String)->Unit,thisScreen:String?){
    val context=LocalContext.current
    val sharedPrefs=context.getSharedPreferences(Constants.key_package, Context.MODE_PRIVATE)
    var selectedOption by remember{mutableStateOf(sharedPrefs.getInt(Constants.key_gender,-1))}
    val genderItems=mutableListOf(
        GenderOption(0,stringResource(id=R.string.genderTransMan),stringResource(id=R.string.tmanDescription)),
        GenderOption(1,stringResource(id=R.string.genderNB),stringResource(id=R.string.nbDescription)),
        GenderOption(2,stringResource(id=R.string.genderWoman),stringResource(id=R.string.womanDescription))
    )
    val neutralAvailable=when(sharedPrefs.getString(Constants.key_language,"en")){"en"->true;"pl"->true;"pt"->false;"pt-br"->false;"ru"->false;"uk"->false;"fr"->false;else->false}
    if(!neutralAvailable){
        genderItems.removeAt(1)
    }

    SideEffect{
        if(sharedPrefs.getInt(Constants.key_gender,-1)!=-1)
            onNavigate(Screens.sNameBirthDayOptions)
    }

    Column(modifier=Modifier.fillMaxWidth()){
        val prevScreen=Utils.getPreviousScreen(thisScreen,context)
        val previousKey=Utils.previousScreenKey(prevScreen)
        if(thisScreen!=null&&previousKey!=""){
            if(previousKey==Constants.key_gender)
                GoBackButtonRow(onClick={sharedPrefs.edit().putInt(Utils.previousScreenKey(prevScreen),0).apply();onNavigate(prevScreen)})
            else
                GoBackButtonRow(onClick={sharedPrefs.edit().putBoolean(Utils.previousScreenKey(prevScreen),false).apply();onNavigate(prevScreen)})
        }
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
        if(!neutralAvailable){
            BetterHeader(
                text=stringResource(id=R.string.noGenderNeutral),
                fontSize="MS",
                modifier=Modifier.padding(vertical=10.dp,horizontal=6.dp)
            )
            BetterHeader(
                text=stringResource(id=R.string.noGenderNeutralSuggestion),
                fontSize="S",
                modifier=Modifier.padding(vertical=4.dp,horizontal=12.dp).fillMaxWidth()
            )
            Row(modifier=Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.Center){
                BetterButton(modifier=Modifier.padding(vertical=12.dp),onClick={onNavigate(Screens.sLanguage)}){
                    BetterText(text=stringResource(id=R.string.go_to_language_screen),fontSize=20.sp,modifier=Modifier.padding(10.dp))
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
                    onNavigate(Screens.sNameBirthDayOptions)
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
fun StealthOptionsScreen(onNavigate:(String) -> Unit,thisScreen:String?){
    val context=LocalContext.current
    val sharedPrefs=context.getSharedPreferences(Constants.key_package, Context.MODE_PRIVATE)
    val gender=sharedPrefs.getInt(Constants.key_gender,-1)
    //0-tm 1-nb 2-female
    var censorPeriod by remember{mutableStateOf(isPeriodCensored(context))}
    val stealthDone=sharedPrefs.getBoolean(Constants.key_isStealthDone,false)
    val next=if(gender==0||gender==1)Screens.sTOptions else Screens.sAgeConsentOptions

    SideEffect{
        if(stealthDone)
            onNavigate(next)
    }

    LaunchedEffect(censorPeriod){} //doesn't work
    Column(
        modifier=Modifier.fillMaxWidth()
    ){
        val prevScreen=Utils.getPreviousScreen(thisScreen,context)
        val previousKey=Utils.previousScreenKey(prevScreen)
        if(thisScreen!=null&&previousKey!=""){
            if(previousKey==Constants.key_gender)
                GoBackButtonRow(onClick={sharedPrefs.edit().putInt(Utils.previousScreenKey(prevScreen),0).apply();onNavigate(prevScreen)})
            else
                GoBackButtonRow(onClick={sharedPrefs.edit().putBoolean(Utils.previousScreenKey(prevScreen),false).apply();onNavigate(prevScreen)})
        }
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
                    text=if(isStealthModeOn(context)) stringResource(id=R.string.normal) else stringResource(id=R.string.stealth),
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
object OldEnoughSelectableDates:SelectableDates{
    override fun isSelectableDate(utcTimeMillis:Long):Boolean{
        return (System.currentTimeMillis()-22090320000000..System.currentTimeMillis()-410240038000).contains(utcTimeMillis)
        //return utcTimeMillis<=System.currentTimeMillis()-410240038000
    }
    override fun isSelectableYear(year:Int):Boolean{
        return (LocalDate.now().year-70..LocalDate.now().year-13).contains(year)
        //return year<=LocalDate.now().year-13
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TOptionsScreen(onNavigate:(String)->Unit,thisScreen:String?){
    val context=LocalContext.current
    //Utils.deleteAllCycleTypes(context,true)
    val sharedPrefs=context.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
    /**0-trans man 1-non binary 2-female**/
    val gender=sharedPrefs.getInt(Constants.key_gender,-1)
    val tMenuComplete=sharedPrefs.getBoolean(Constants.key_testosteroneMenuComplete,false)
    if(gender==2||tMenuComplete){
        onNavigate(Screens.sPeriodOptions)
    }


    val temp=if(sharedPrefs.contains("isOnT"))sharedPrefs.getBoolean(Constants.key_isTakingTestosterone,false) else null
    /**nullable, null->ask if taking T if null, show the screens if true/false **/
    var isOnT:Boolean? by remember{mutableStateOf(temp)}
    LaunchedEffect(isOnT){}
    var testosteroneName by remember{mutableStateOf("")}
    var testosteroneInterval by remember{mutableStateOf("")}
    var isMicrodosing:Boolean by remember{mutableStateOf(false)}

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

    val datePickerColors=Utils.datePickerColors()

    //val theme=sharedPrefs.getString("theme","Black")

    sharedPrefs.edit().putString(Constants.key_firstTestosteroneDate,firstDoseSelectedDate).apply()
    sharedPrefs.edit().putString(Constants.key_lastTestosteroneDate,lastDoseSelectedDate).apply()

    var errorText by remember{mutableStateOf("")}
    LaunchedEffect(errorText){}
    //todo: put errors in strings
    val testosteroneNameIsEmptyError=stringResource(id=R.string.nameIsEmptyError)
    val intervalIsEmptyError=stringResource(id=R.string.intervalIsEmptyError)
    val lastDoseIsEmptyError=stringResource(id=R.string.lastDoseIsEmptyError)
    val testosteroneIntervalIsNotDigit=stringResource(id=R.string.intervalNotANumberError)

    val skipFirstDose=stringResource(id=R.string.skipLater)
    var skipFirstDoseDateChecked by remember{mutableStateOf(false)}

    var tShotsReminders by remember{mutableStateOf(false)}
    var notificationHour by remember{mutableStateOf(6)}
    var notificationMinute by remember{mutableStateOf(0)}

    LazyColumn(
        modifier=//if(theme=="Pride") //theme
        Modifier
            .fillMaxSize()
            //.paint(painterResource(id=R.drawable.pride50),contentScale=ContentScale.FillBounds)
        //else Modifier.fillMaxSize()
    ){
        if(isOnT==null){
            val prevScreen=Utils.getPreviousScreen(thisScreen,context)
            val previousKey=Utils.previousScreenKey(prevScreen)
            if(thisScreen!=null&&previousKey!=""){
                if(previousKey==Constants.key_gender)
                    item{GoBackButtonRow(onClick={sharedPrefs.edit().putInt(Utils.previousScreenKey(prevScreen),0).apply();onNavigate(prevScreen)})}
                else
                    item{GoBackButtonRow(onClick={sharedPrefs.edit().putBoolean(Utils.previousScreenKey(prevScreen),false).apply();onNavigate(prevScreen)})}
            }


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
                    question=stringResource(id=R.string.testosteroneQuestion),
                    questionFontSize="ML"
                )
            }
            /*item{
                com.vsnappy1.datepicker.DatePicker(onDateSelected={day,month,year->})
            }*/
        }
        if(isOnT==true){
            item{GoBackButtonRow(onClick={isOnT=null})}
            item{
                BetterHeader(
                    text=stringResource(id=R.string.enterTestosteroneName),
                    fontSize="ML",
                    modifier=Modifier
                        .fillMaxWidth()
                        .padding(vertical=12.dp,horizontal=20.dp)
                )
            }
            item{
                AutoComplete(
                    testosteroneName,
                    placeholderText=stringResource(id=R.string.testosterone),
                    onValueChange={testosteroneName=it}
                )
            }
            item{
                VerticalSpacer()
            }
            item{
                BetterHeader(
                    text=stringResource(id=R.string.howOftenDoYouTakeIt),
                    fontSize="M",
                    modifier=Modifier
                        .fillMaxWidth()
                        .padding(bottom=5.dp)
                )
            }
            item{
                Row(
                    modifier=Modifier
                        .padding(top=10.dp)
                ){
                    BetterTextField(
                        value=testosteroneInterval,
                        onValueChange={testosteroneInterval=it},
                        placeholderText=stringResource(id=R.string.daysSpecify),
                        imeAction=ImeAction.Done,
                        keyboardType=KeyboardType.Number
                    )
                }
            }//testosteroneName, testosteroneInterval
            item{
                VerticalSpacer()
            }
            item{
                BetterHeader(
                    text=stringResource(id=R.string.lastDoseQuestion),
                    fontSize="M",
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
            }//testosteroneName, testosteroneInterval, lastDoseSelectedDate
            item{
                CheckboxRow(
                    checked=tShotsReminders,
                    onCheckedChange={tShotsReminders=!tShotsReminders},
                    text=stringResource(id=R.string.getRemindersQuestion)
                )
            }
            if(tShotsReminders){
                item{
                    Row(modifier=Modifier.fillMaxWidth()){
                        StyledTimePicker(
                            onTimeSelected={hour,minute->
                            notificationHour=hour
                            notificationMinute=minute
                        },
                            time=TimePickerTime(notificationHour,notificationMinute),
                        )
                    }
                }
            }
            item{
                VerticalSpacer()
            }
            item{
                BetterHeader(
                    text=stringResource(id=R.string.firstTestosteroneQuestion),
                    fontSize="M"
                )
            }
            item{
                CheckboxRow(
                    onCheckedChange={skipFirstDoseDateChecked=!skipFirstDoseDateChecked},
                    checked=skipFirstDoseDateChecked,
                    text=skipFirstDose
                )
            }
            if(!skipFirstDoseDateChecked){
                item{
                    BetterHeader(
                        text=stringResource(id=R.string.enterDateDescription),
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
                }//testosteroneName, testosteroneInterval, lastDoseSelectedDate, firstDoseSelectedDate
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
                    Spacer(modifier=Modifier.padding(vertical=10.dp))
                }
            }
            else{
                item{androidx.compose.material.Divider(color=colorTertiary(), thickness=2.dp,modifier=Modifier.padding(vertical=12.dp))}
            }
            item{
                CheckboxRow(
                    checked=isMicrodosing,
                    onCheckedChange={isMicrodosing=!isMicrodosing},
                    text=stringResource(id=R.string.microdosing)
                )
            }

            item{
                ErrorText(text=errorText)
            }
            item{
                CheckmarkButtonRow(
                    onClick={
                        //testosteroneName, testosteroneInterval, lastDoseSelectedDate, firstDoseSelectedDate, microdosing in sharedpref
                        if(testosteroneName.isNotEmpty()){
                            //Utils.testNotif(context)
                            errorText=""
                            if(testosteroneInterval.isNotEmpty()){
                                errorText=""
                                if(testosteroneInterval.isDigitsOnly()){
                                    errorText=""
                                    if(testosteroneInterval.toInt()>0){
                                        errorText=""
                                        if(lastDoseSelectedDate.isNotEmpty()){
                                            errorText=""
                                            if(firstDoseSelectedDate.isNotEmpty()||skipFirstDoseDateChecked){ //both are ok, but there's no start date if it's skipped (duh)
                                                errorText=""
                                                sharedPrefs.edit().putInt(Constants.key_currentTestosteroneInterval,testosteroneInterval.toInt()).apply()
                                                sharedPrefs.edit().putBoolean(Constants.key_isMicrodosing,isMicrodosing).apply()
                                                Utils.addNewCycleType(context,testosteroneName,testosteroneInterval.toInt(),true)
                                                onNavigate(Screens.sPeriodOptions)
                                                sharedPrefs.edit().putBoolean(Constants.key_testosteroneMenuComplete,true).apply()
                                                if(firstDoseSelectedDate.isNotEmpty()){
                                                    sharedPrefs.edit().putString(Constants.key_firstTestosteroneDate,firstDoseSelectedDate).apply()
                                                }
                                                if(tShotsReminders){
                                                    /*val testIntent=Intent(context,AlarmReceiver::class.java)
                                                    context.sendBroadcast(testIntent)*/

                                                    sharedPrefs.edit().putBoolean(Constants.key_tRemindersOn,true).apply()

                                                    Utils.createTestosteroneNotificationChannel(context)
                                                    val daysTillNextT=Utils.getDaysTillNextShot(lastDoseSelectedDate,testosteroneInterval.toInt())
                                                    Utils.scheduleNotifications(context=context, interval=testosteroneInterval.toInt(),
                                                        daysTillNext=daysTillNextT, notificationHour=notificationHour, notificationMinute=notificationMinute,type=Constants.key_lastTNotification)
                                                    //todo: NOTIFICATION SYSTEM
                                                }
                                            }
                                            else{
                                                skipFirstDoseDateChecked=true
                                            }
                                        }
                                        else{
                                            errorText=lastDoseIsEmptyError
                                        }
                                    }
                                    else{
                                        errorText=testosteroneIntervalIsNotDigit
                                    }
                                }
                                else{
                                    errorText=testosteroneIntervalIsNotDigit
                                }
                            }
                            else{
                                errorText=intervalIsEmptyError
                            }
                        }
                        else{
                            errorText=testosteroneNameIsEmptyError
                            Log.e("error",testosteroneName)
                        }
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
                    question=stringResource(id=R.string.testostroneInFutureQuestion))
            }
            item{
                BetterHeader(
                    text=stringResource(id=R.string.testostroneInFutureDescription),
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
fun PeriodOptionsScreen(onNavigate:(String) -> Unit,thisScreen:String?){
    val context=LocalContext.current
    val sharedPrefs=context.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
    val periodMenuComplete=sharedPrefs.getBoolean(Constants.key_isPeriodMenuComplete,false)
    if(periodMenuComplete){
        onNavigate(Screens.sContraceptiveOptions)
    }

    /**0-trans man 1-non binary 2-female**/
    /*val gender=sharedPrefs.getInt(Constants.key_gender,-1)
    val takingT=sharedPrefs.getBoolean(Constants.key_isTakingTestosterone,false)*/
    val censor=sharedPrefs.getBoolean(Constants.key_censorPeriod,false)

    var periodRegular by remember{mutableStateOf(false)}
    var cycleLengthValue by remember{mutableStateOf("0")}
    var periodLengthValue by remember{mutableStateOf("0")}

    var amountOfCyclesToEnter by remember{mutableStateOf(1)}
    var cycleDates2 by remember{mutableStateOf(listOf(arrayOf("","","")))}
    LaunchedEffect(amountOfCyclesToEnter){}
    Log.v("important",cycleDates2.toString())

    var periodReminders by remember{mutableStateOf(false)}
    var notificationHour by remember{mutableStateOf(10)}
    var notificationMinute by remember{mutableStateOf(0)}
    var remindXDaysBeforePeriod by remember{mutableStateOf("")}

    var errorText by remember{mutableStateOf("")}
    val cycleLengthNotANumberError=stringResource(id=R.string.cycleLengthNotANumberError)
    val periodLengthNotANumberError=stringResource(id=R.string.periodLengthNotANumberError)
    val notNumberDatesError=stringResource(id=R.string.notNumberDatesError)
    val invalidDatesError=stringResource(id=R.string.invalidDatesError)
    val invalidDaysBeforePeriod=stringResource(id=R.string.invalidDaysBeforePeriod)
    val datesNotDescendingError=stringResource(id=R.string.datesNotDescendingError)
    LazyColumn{
        val prevScreen=Utils.getPreviousScreen(thisScreen,context)
        val previousKey=Utils.previousScreenKey(prevScreen)

        if(thisScreen!=null&&previousKey!=""){
            Log.v("previous screen key",previousKey)
            if(previousKey==Constants.key_gender)
                item{GoBackButtonRow(onClick={sharedPrefs.edit().putInt(Utils.previousScreenKey(prevScreen),0).apply();onNavigate(prevScreen)})}
            else
                item{GoBackButtonRow(onClick={sharedPrefs.edit().putBoolean(Utils.previousScreenKey(prevScreen),false).apply();onNavigate(prevScreen)})}
        }
        item{
            YesNoRow(
                onClickYes={periodRegular=true},
                onClickNo={periodRegular=false},
                question="Are your periods regular?"
            )
        }
        if(periodRegular){
            item{
                VerticalSpacer()
            }
            item{
                BetterText(
                    text="Enter your average cycle length",
                    fontSize=28.sp,
                    modifier=Modifier.padding(bottom=15.dp,start=15.dp,end=10.dp)
                )
            }
            item{
                NumberInput(onValueChange={cycleLengthValue=it},startValue=28)
            }
        }
        item{
            VerticalSpacer()
        }
        item{
            BetterText(
                text="Enter your average period length", //todo censored and gendered strings
                fontSize=28.sp,
                modifier=Modifier.padding(bottom=15.dp,start=15.dp,end=10.dp)
            )
        }
        item{
            NumberInput(onValueChange={periodLengthValue=it},startValue=5)
        }
        item{
            VerticalSpacer()
        }
        item{
            BetterText(
                text="Enter your cycle dates, from more recent ones.",
                fontSize=28.sp,
                modifier=Modifier.padding(bottom=15.dp,start=15.dp,end=10.dp)
            )
        }
        items(cycleDates2.size,key={it}){index->
            Row(
                modifier=Modifier.padding(start=15.dp,top=8.dp,bottom=8.dp)
            ){
                DateInputField(
                    onDayValueChange={value->
                        cycleDates2=cycleDates2.toMutableList().apply{
                            this[index][0]=value
                            Log.i("newValueD: ",value)
                        }
                    },
                    onMonthValueChange={value->
                        cycleDates2=cycleDates2.toMutableList().apply{
                            this[index][1]=value
                            Log.i("newValueM: ",value)
                        }
                    },
                    onYearValueChange={value->
                        cycleDates2=cycleDates2.toMutableList().apply{
                            this[index][2]=value
                            Log.i("newValueY: ",value)
                        }
                    }
                )
            }
        }
        item{
            Row(modifier=Modifier.padding(top=20.dp,start=15.dp,bottom=15.dp)){
                BetterButton(
                    modifier=Modifier
                        .padding(end=10.dp)
                        .size(width=70.dp,height=40.dp),
                    onClick={
                        //cycleDates+=""
                        cycleDates2+=listOf(arrayOf("","",""))
                        amountOfCyclesToEnter++
                    }
                ){
                    Icon(
                        imageVector=Icons.Rounded.Add,
                        tint=colorSecondary(),
                        contentDescription="",
                        modifier=Modifier.scale(1.4f)
                    )
                }
                BetterButton(
                    modifier=Modifier
                        .padding(start=10.dp)
                        .size(width=70.dp,height=40.dp)
                        .animateContentSize(),
                    onClick={
                        //if(cycleDates.size>1) cycleDates=cycleDates.dropLast(1)
                        if(cycleDates2.size>1) cycleDates2=cycleDates2.dropLast(1)
                        if(amountOfCyclesToEnter>0)amountOfCyclesToEnter--
                    }
                ){
                    Icon(
                        imageVector=Icons.Rounded.Remove,
                        tint=colorSecondary(),
                        contentDescription="",
                        modifier=Modifier.scale(1.4f)
                    )
                }
            }
        }
        if(periodRegular){
            item{
                CheckboxRow(
                    checked=periodReminders,
                    onCheckedChange={periodReminders=!periodReminders},
                    text="Get reminders?"
                )
            }
            if(periodReminders){
                item{
                    Row(modifier=Modifier.fillMaxWidth()){
                        StyledTimePicker(
                            onTimeSelected={hour,minute->
                                notificationHour=hour
                                notificationMinute=minute
                            },
                            time=TimePickerTime(notificationHour,notificationMinute),
                        )
                    }
                }
                item{
                    BetterHeader(
                        text="How many days before the period start should the reminder be?",
                        fontSize="MS",
                        modifier=Modifier.padding(top=15.dp)
                    )
                }
                item{
                    NumberInput(
                        onValueChange={remindXDaysBeforePeriod=it},
                        startValue=if(Utils.canBeIntParsed(remindXDaysBeforePeriod)) remindXDaysBeforePeriod.toInt() else 2,
                        maxValue=30,
                        modifier=Modifier.padding(start=15.dp,top=15.dp),
                        arrowScale=1.5f,
                        arrowHeight=30.dp
                    )
                }
            }
            item{
                VerticalSpacer()
            }
        }
        else{
            item{
                VerticalSpacer()
            }
        }
        item{
            ErrorText(text=errorText)
        }
        item{
            CheckmarkButtonRow(onClick={
                //mandatory: average cycle length, at least 1 cycle date
                //mandatory if regular:period length
                if(Utils.canBeIntParsed(cycleLengthValue)){
                    errorText=""
                    sharedPrefs.edit().putInt(Constants.key_averagePeriodCycleLength,cycleLengthValue.toInt()).apply()
                    Utils.addNewCycleType(context=context, cycleName="period",correctInterval=cycleLengthValue.toInt(),active=true)

                    cycleDates2.forEachIndexed{index,cycle->
                        Log.i("newDateCycleDebug",cycle[2]+"-"+cycle[1]+"-"+cycle[0])
                        if(Utils.canBeIntParsed(cycle[2])&&Utils.canBeIntParsed(cycle[1])&&Utils.canBeIntParsed(cycle[0])){
                            //Log.i("newDateCycleDebug","here1")
                            if(Utils.isValidPastDate(cycle[2].toInt(),cycle[1].toInt(),cycle[0].toInt())){
                                Log.i("newDateCycleDebug",index.toString())
                                if(index>0){
                                    //Log.i("newDateCycleDebug","here3")
                                    val indexDate=Utils.createDateFromIntegers(cycle[2].toInt(),cycle[1].toInt(),cycle[0].toInt())
                                    val previousDate=Utils.createDateFromIntegers(cycleDates2[index-1][2].toInt(),cycleDates2[index-1][1].toInt(),cycleDates2[index-1][0].toInt())
                                    if(Utils.isDate1AfterDate2(indexDate,previousDate)){
                                        errorText=datesNotDescendingError
                                        Log.i("newDateCycleDebugDates","date: $previousDate previous date: $indexDate")
                                    }
                                }
                                    if(errorText!=datesNotDescendingError){
                                    //else{
                                        val indexDate=Utils.createDateFromIntegers(cycle[2].toInt(),cycle[1].toInt(),cycle[0].toInt())
                                        errorText=""
                                        //Log.i("newDateCycleDebug","here4")
                                        //TODO: PUT OTHER DAYS OF CYCLES TO DB (now only 0s gets inserted)
                                        val cycleId=Utils.getCycleIdByName(context,"period")
                                        if(periodRegular){
                                            if(Utils.canBeIntParsed(periodLengthValue)){
                                                errorText=""
                                                sharedPrefs.edit().putInt(Constants.key_averagePeriodLength,periodLengthValue.toInt()).apply()
                                                //Log.i("newDateCycleDebug","here0")
                                                if(periodReminders){
                                                    sharedPrefs.edit().putBoolean(Constants.key_periodRemindersOn,true).apply()
                                                    Utils.createPeriodNotificationChannel(context)
                                                    val isDateError=errorText!=datesNotDescendingError&&errorText!=notNumberDatesError&&errorText!=invalidDatesError
                                                    if(Utils.canBeIntParsed(remindXDaysBeforePeriod)&&!isDateError){
                                                        errorText=""
                                                        val lastDate=Utils.createDateFromIntegers(cycleDates2[0][2].toInt(),cycleDates2[0][1].toInt(),cycleDates2[0][0].toInt())
                                                        val nextDoseDate=Utils.getNextDoseDate(lastDate,cycleLengthValue.toInt())
                                                        //todo: ADD REMINDERS at [nextDoseDate] !!!

                                                    }
                                                    else{
                                                        Log.v("daysBefore",remindXDaysBeforePeriod)
                                                        errorText=invalidDaysBeforePeriod
                                                    }
                                                }
                                            }
                                            else{
                                                errorText=periodLengthNotANumberError
                                            }
                                        }
                                        if(errorText.isEmpty()){
                                            if(Utils.addNewDateCycle(context,DateCycle(indexDate,cycleId,0))){
                                                amountOfCyclesToEnter=0
                                                cycleDates2=listOf(arrayOf("","",""))
                                                sharedPrefs.edit().putBoolean(Constants.key_isPeriodMenuComplete,true).apply()
                                                onNavigate(Screens.sContraceptiveOptions)
                                            }
                                            else{ //cycle type not created
                                                errorText="Can't add cycle data yet, try again in 30 seconds."
                                            }
                                        }


                                    }
                                //}
                            }
                            else{
                                Log.v("newDateCycleInvalidDate",cycle[2]+"-"+cycle[1]+"-"+cycle[0])
                                errorText=invalidDatesError
                            }
                        }
                        else{
                            errorText=notNumberDatesError
                        }
                    }
                }
                else{
                    errorText=cycleLengthNotANumberError
                }
            })
        }
        item{
            VerticalSpacer()
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContraceptiveOptionsScreen(onNavigate:(String)->Unit,thisScreen:String?=null){
    val context=LocalContext.current
    val sharedPrefs=context.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)

    val bcMenuComplete=sharedPrefs.getBoolean(Constants.key_BCMenuComplete,false)
    if(bcMenuComplete){
        onNavigate(Screens.sMain)
    }

    val temp=if(sharedPrefs.contains("isOnBC")) sharedPrefs.getBoolean(Constants.key_isTakingBirthControlContraceptive,false) else null
    /**nullable, null->ask if taking T if null, show the screens if true/false **/
    var isOnBC:Boolean? by remember{mutableStateOf(temp)}
    LaunchedEffect(isOnBC){}
    var contraceptiveName by remember{mutableStateOf("")}
    var contraceptiveInterval by remember{mutableStateOf("")}
    ///region datePickers setup
    val lastDoseDatePickerState=rememberDatePickerState(
        selectableDates=PastOrPresentSelectableDates
    )
    val lastDoseSelectedDate=lastDoseDatePickerState.selectedDateMillis?.let{
        convertMillisToDate(it)
    } ?: ""


    val datePickerColors=Utils.datePickerColors()
    //endregion

    //val theme=sharedPrefs.getString("theme","Black")

    sharedPrefs.edit().putString(Constants.key_lastBCDateContraceptive,lastDoseSelectedDate).apply()

    var errorText by remember{mutableStateOf("")}
    LaunchedEffect(errorText){}
    //todo: put errors in strings
    val contraceptiveNameIsEmptyError=stringResource(id=R.string.nameIsEmptyError)
    val intervalIsEmptyError=stringResource(id=R.string.intervalIsEmptyError)
    val lastDoseIsEmptyError=stringResource(id=R.string.lastDoseIsEmptyError)
    val contraceptiveIntervalIsNotDigit=stringResource(id=R.string.intervalNotANumberError)


    var bcReminders by remember{mutableStateOf(false)}
    var notificationHour by remember{mutableStateOf(6)}
    var notificationMinute by remember{mutableStateOf(0)}

    LazyColumn(
        modifier=//if(theme=="Pride") //theme
        Modifier
            .fillMaxSize()
        //.paint(painterResource(id=R.drawable.pride50),contentScale=ContentScale.FillBounds)
        //else Modifier.fillMaxSize()
    ){
        if(isOnBC==null){
            val prevScreen=Utils.getPreviousScreen(thisScreen,context)
            val previousKey=Utils.previousScreenKey(prevScreen)

            if(thisScreen!=null&&previousKey!=""){
                Log.v("previous screen key",previousKey)
                if(previousKey==Constants.key_gender)
                    item{GoBackButtonRow(onClick={sharedPrefs.edit().putInt(Utils.previousScreenKey(prevScreen),0).apply();onNavigate(prevScreen)})}
                else
                    item{GoBackButtonRow(onClick={sharedPrefs.edit().putBoolean(Utils.previousScreenKey(prevScreen),false).apply();onNavigate(prevScreen)})}
            }
            item{
                YesNoRow(
                    onClickYes={
                        isOnBC=true
                        sharedPrefs.edit().putBoolean(Constants.key_isTakingBirthControlContraceptive,true).apply()
                    },
                    onClickNo={
                        isOnBC=false
                        sharedPrefs.edit().putBoolean(Constants.key_BCMenuComplete,true).apply()
                        sharedPrefs.edit().putBoolean(Constants.key_isTakingBirthControlContraceptive,false).apply()
                        onNavigate(Screens.sMain)
                    },
                    question=stringResource(id=R.string.birthControlQuestion),
                    questionFontSize="ML"
                )
            }
        }
        if(isOnBC==true){
            item{GoBackButtonRow(onClick={isOnBC=null})}
            item{
                BetterHeader(
                    text=stringResource(id=R.string.birthControlName),
                    fontSize="ML",
                    modifier=Modifier
                        .fillMaxWidth()
                        .padding(vertical=12.dp,horizontal=20.dp)
                )
            }
            item{
                AutoComplete(
                    contraceptiveName,
                    placeholderText=stringResource(id=R.string.contraceptive),
                    onValueChange={contraceptiveName=it},
                    bc=true
                )
            }
            item{
                VerticalSpacer()
            }
            item{
                BetterHeader(
                    text=stringResource(id=R.string.howOftenDoYouTakeIt),
                    fontSize="M",
                    modifier=Modifier
                        .fillMaxWidth()
                        .padding(bottom=5.dp)
                )
            }
            item{
                Row(
                    modifier=Modifier
                        .padding(top=10.dp)
                ){
                    BetterTextField(
                        value=contraceptiveInterval,
                        onValueChange={contraceptiveInterval=it},
                        placeholderText=stringResource(id=R.string.daysSpecify),
                        imeAction=ImeAction.Done,
                        keyboardType=KeyboardType.Number
                    )
                }
            }//testosteroneName, testosteroneInterval
            item{
                VerticalSpacer()
            }
            item{
                BetterHeader(stringResource(id=R.string.lastDoseQuestion),fontSize="M")
            }
            item{
                BetterHeader(lastDoseSelectedDate,fontSize="MS")
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
            }//testosteroneName, testosteroneInterval, lastDoseSelectedDate
            item{
                CheckboxRow(
                    checked=bcReminders,
                    onCheckedChange={bcReminders=!bcReminders},
                    text=stringResource(id=R.string.getRemindersQuestion)
                )
            }
            if(bcReminders){
                item{
                    Row(modifier=Modifier.fillMaxWidth()){
                        StyledTimePicker(
                            onTimeSelected={hour,minute->
                                notificationHour=hour
                                notificationMinute=minute
                            },
                            time=TimePickerTime(notificationHour,notificationMinute),
                        )
                    }
                }
            }
            item{
                VerticalSpacer()
            }
            item{
                CheckmarkButtonRow(
                    onClick={
                        //testosteroneName, testosteroneInterval, lastDoseSelectedDate, firstDoseSelectedDate, microdosing in sharedpref
                        if(contraceptiveName.isNotEmpty()){
                            //Utils.testNotif(context)
                            errorText=""
                            if(contraceptiveInterval.isNotEmpty()){
                                errorText=""
                                if(contraceptiveInterval.isDigitsOnly()){
                                    errorText=""
                                    if(contraceptiveInterval.toInt()>0){
                                        errorText=""
                                        if(lastDoseSelectedDate.isNotEmpty()){
                                            errorText=""
                                                errorText=""
                                                sharedPrefs.edit().putInt(Constants.key_bcContraceptiveInterval,contraceptiveInterval.toInt()).apply()
                                                Utils.addNewCycleType(context,contraceptiveName,contraceptiveInterval.toInt(),true)
                                                sharedPrefs.edit().putBoolean(Constants.key_BCMenuComplete,true).apply()
                                                sharedPrefs.edit().putBoolean(Constants.key_isSetupDone,true).apply()
                                                if(bcReminders){
                                                    sharedPrefs.edit().putBoolean(Constants.key_bcRemindersOn,true).apply()
                                                    Utils.createContraceptiveNotificationChannel(context)
                                                    //val daysTillNextBC=Utils.getDaysTillNextShot(lastDoseSelectedDate,contraceptiveInterval.toInt())
                                                }
                                                if(sharedPrefs.getString(Constants.key_passwordValue,"").isNullOrEmpty())
                                                    onNavigate(Screens.sPassword)
                                                else
                                                    onNavigate(Screens.sPeriodOptions)
                                        }
                                        else{
                                            errorText=lastDoseIsEmptyError
                                        }
                                    }
                                    else{
                                        errorText=contraceptiveIntervalIsNotDigit
                                    }
                                }
                                else{
                                    errorText=contraceptiveIntervalIsNotDigit
                                }
                            }
                            else{
                                errorText=intervalIsEmptyError
                            }
                        }
                        else{
                            errorText=contraceptiveNameIsEmptyError
                            Log.e("error",contraceptiveName)
                        }
                    },
                    rowModifier=Modifier.padding(bottom=50.dp)
                )
            }
        }
    }
}

//region MainActivity
@Composable
fun MainScreen(onNavigate:(String)->Unit){
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

//TODO BIG:
// 1 FIX PASSWORD ERRORS NOT SHOWING!
// 2. ADD RECOVERY WHEN FORGOT PASSWORD
// 3. REDO INTRODUCTIONS/SETTINGS ( AND make it better)
// 4. (don't forget setup complete)
// 5. BRING BACK CREATE PASSWORD/RECOVERY POPUP
// 6. SOCIALS/CREDITS TAB
//: TODO FROM OLD ACTIVITIES: home_fragment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameBirthDayOptionsScreen(onNavigate:(String) -> Unit,thisScreen:String?){
    //on navigate stealth
    val context=LocalContext.current
    val sharedPrefs=context.getSharedPreferences(Constants.key_package, Context.MODE_PRIVATE)
    if(sharedPrefs.getBoolean(Constants.key_isNameBirthDayMenuComplete,false))
        onNavigate(Screens.sStealthOptions)

    var name by remember{mutableStateOf("")}
    val birthdayDatePickerState=rememberDatePickerState(
        initialSelectedDateMillis=LocalDate.now().minusYears(21).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        selectableDates=OldEnoughSelectableDates
    )
    val birthdaySelectedDate=birthdayDatePickerState.selectedDateMillis?.let{
        convertMillisToDate(it)
    }?:""
    var errorText by remember{mutableStateOf("")}
    var skipBirthday by remember{mutableStateOf(false)}

    LazyColumn(modifier=Modifier.fillMaxSize()){
        val prevScreen=Utils.getPreviousScreen(thisScreen,context)
        val previousKey=Utils.previousScreenKey(prevScreen)
        if(thisScreen!=null&&previousKey!=""){
            if(previousKey==Constants.key_gender)
                item{GoBackButtonRow(onClick={sharedPrefs.edit().putInt(Utils.previousScreenKey(prevScreen),-1).apply();onNavigate(prevScreen)})}
            else
                item{GoBackButtonRow(onClick={sharedPrefs.edit().putBoolean(Utils.previousScreenKey(prevScreen),false).apply();onNavigate(prevScreen)})}
        }
        item{
            BetterHeader(text="Enter your (nick)name.", fontSize="ML",
                modifier=Modifier
                    .padding(vertical=6.dp,horizontal=8.dp)
                    .fillMaxWidth()
            )
        }
       item{
           BetterHeader(text="it will appear on main screen", fontSize="S",
               modifier=Modifier
                   .padding(vertical=6.dp)
                   .fillMaxWidth()
           )
       }
        item{
            BetterTextField(
                value=name,
                onValueChange={name=it},
                textFieldModifier=Modifier.padding(all=5.dp),
                placeholderText="enter your name"
            )
        }
        item{
            VerticalSpacer()
        }
        if(!skipBirthday){
            item{
                BetterHeader(text="Enter your birthday to get birthday notifications", fontSize="ML",
                    modifier=Modifier
                        .padding(vertical=6.dp,horizontal=8.dp)
                        .fillMaxWidth()
                )
            }
            item{
                BetterHeader(
                    text="Selected date: $birthdaySelectedDate",fontSize="M",fontStyle=FontStyle.Italic,
                    modifier=Modifier
                        .padding(top=14.dp,bottom=6.dp)
                        .fillMaxWidth(),
                )
            }
            item{
                MyDatePicker(
                    state=birthdayDatePickerState
                )
            }
        }
        item{
            CheckboxRow(checked=skipBirthday,onCheckedChange={skipBirthday=it},text="I don't want to enter my birthday")
        }
        item{
            ErrorText(text=errorText,modifier=Modifier.fillMaxWidth())
        }
        item{
            CheckmarkButtonRow(onClick={
                if(name.isNotEmpty()){
                    errorText=""
                    sharedPrefs.edit().putString(Constants.key_userName,name).apply()
                    sharedPrefs.edit().putBoolean(Constants.key_isNameBirthDayMenuComplete,true).apply()
                    if(!skipBirthday){
                        sharedPrefs.edit().putString(Constants.key_birthDate,birthdaySelectedDate).apply()
                    }
                    onNavigate(Screens.sStealthOptions)
                }
                else{
                    errorText="Name cannot be empty"
                }
            })
        }
        item{
            VerticalSpacer()
        }
    }
}

@Composable
fun WelcomeScreen(onNavigate:(String)->Unit){
    val context=LocalContext.current

    val sharedPrefs=context.getSharedPreferences(Constants.key_package, Context.MODE_PRIVATE)
    val setupDone=sharedPrefs.getBoolean(Constants.key_isSetupDone,false)
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


    Column(modifier=Modifier.fillMaxSize()){
        Image(
            painter=painterResource(id=R.drawable.icon_shark_normal),
            contentDescription=null,
            modifier=Modifier
                .fillMaxWidth()
                .padding(top=50.dp)
        )
        BetterText(
            text=stringResource(id=R.string.welcome_text),
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


    when(passwordScreenType){
        0->{
            //display choose type
            Column(modifier=Modifier.fillMaxWidth()){
                BetterText(
                    text=stringResource(id=R.string.password_type_title),
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
                        text=stringResource(id=R.string.password_text_password),
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
                        text=stringResource(id=R.string.password_pin_password),
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
                        text=stringResource(id=R.string.dont_want_password),
                        fontSize=30.sp
                    )
                }
                Row(modifier=Modifier.fillMaxWidth()){
                    IconButton(onClick={
                        when(selectedOption){
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
        1->{
            val creatingPassword=passwordValue.isNullOrEmpty()
//        creatingPassword=!creatingPassword
            Column(modifier=Modifier.fillMaxWidth()){
                BetterText(
                    text=if(creatingPassword) stringResource(id=R.string.create_password)
                    else stringResource(id=R.string.enter_password),
                    fontSize=40.sp,
                    textAlign=TextAlign.Center,
                    modifier=Modifier
                        .fillMaxWidth()
                        .padding(vertical=30.dp)
                )
                if(creatingPassword){
                    BetterText(
                        text=stringResource(id=R.string.password_warning),
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
                                text=stringResource(id=R.string.password),
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
                        text=stringResource(id=R.string.password_repeat),
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
                                text=stringResource(id=R.string.password),
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
        2->{
            Column(
                modifier=Modifier.fillMaxSize(),
                horizontalAlignment=Alignment.CenterHorizontally
            ){
                BetterText(
                    text=
                    when(pinScreenType){
                        0->stringResource(id=R.string.enter_pin)
                        1->stringResource(id=R.string.create_pin)
                        else->stringResource(id=R.string.enter_pin_again)
                    }, //2
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
                            androidx.compose.material.Divider(color=colorTertiary(),thickness=1.dp)
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
                    if(enteredAnswers.all{it.isNotEmpty()}){
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
                        if(menuText.all{it!=questionsOptions[0]}){
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
/*private fun createNotificationChannel(context: Context){
    val channel=NotificationChannel("hrt","Hrt reminders", NotificationManager.IMPORTANCE_HIGH).apply{
        description="This channel is for hrt reminders"
    }

    val notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}*/

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

fun isStealthModeOn(context:Context):Boolean{
    val packageManager=context.packageManager
    val stealth=ComponentName(context,"com.example.rainbowcalendar.MainActivityStealth")

    return packageManager.getComponentEnabledSetting(stealth)==PackageManager.COMPONENT_ENABLED_STATE_ENABLED
}
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
        LanguageData(R.drawable.flag_lang_en,stringResource(id=R.string.langEnglish),stringResource(id=R.string.langEnglishLocal)),
        LanguageData(R.drawable.flag_pl,stringResource(id=R.string.langPolish),stringResource(id=R.string.langPolishLocal)),
        LanguageData(R.drawable.flag_fr,stringResource(id=R.string.langFrench),stringResource(id=R.string.langFrenchLocal)),
        LanguageData(R.drawable.flag_br,stringResource(id=R.string.langPortugueseBR),stringResource(id=R.string.langPortugueseBRLocal)),
        LanguageData(R.drawable.flag_ru,stringResource(id=R.string.langRussian),stringResource(id=R.string.langRussianLocal)),
        LanguageData(R.drawable.flag_ua,stringResource(id=R.string.langUkrainian),stringResource(id=R.string.langUkrainianLocal))
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
            text=stringResource(id=R.string.language),
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
                            text=stringResource(id=R.string.save),
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
fun ThemesSettings(onNavigate:(String)->Unit,thisScreen:String?){
    val context=LocalContext.current
    val sharedPrefs=context.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
    val theme=sharedPrefs.getString(Constants.key_theme,"Gray")

    for(tm in themes){
        tm.selected=tm.name==theme
    }

    val themeRow=themes.chunked(2)
    LazyColumn(modifier=Modifier
        .fillMaxWidth()
        .background(colorPrimary())
    ){
        val prevScreen=Utils.getPreviousScreen(thisScreen,context)
        val previousKey=Utils.previousScreenKey(prevScreen)
        if(thisScreen!=null&&previousKey!=""){
            if(previousKey==Constants.key_gender)
                item{GoBackButtonRow(onClick={sharedPrefs.edit().putInt(Utils.previousScreenKey(prevScreen),0).apply();onNavigate(prevScreen)})}
            else
                item{GoBackButtonRow(onClick={sharedPrefs.edit().putBoolean(Utils.previousScreenKey(prevScreen),false).apply();onNavigate(prevScreen)})}
        }
        item{
            Text(
                color=colorSecondary(),
                text=stringResource(R.string.chooseAppTheme),
                fontSize=44.sp,
                fontWeight=FontWeight.SemiBold,
                modifier=Modifier
                    .padding(top=30.dp,bottom=10.dp)
            )
        }
        themeRow.forEach{row->
            item{
                Column(
                    modifier=Modifier
                        .fillMaxWidth()
                        .heightIn(max=300.dp)
                ){
                    Row(
                        modifier=Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    ){
                        row.forEach{item->
                            Box(modifier=Modifier.weight(1f)){
                                themeItem2(name=item.name,imgResource=item.img,onNavigate=onNavigate,isSelected=item.selected)
                            }
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun themeItem2(name: String, imgResource:Int,onNavigate:(String)->Unit,isSelected:Boolean=false){
    val context=LocalContext.current as Activity
    val sharedPrefs=LocalContext.current.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
    val themeSetupDone=sharedPrefs.getBoolean(Constants.key_isThemeSetUp,false)

    val selected=remember {mutableStateOf(false)}
    val scale=animateFloatAsState(if(selected.value) 1.1f else 1f,label="")

    val onClick={if(!themeSetupDone) sharedPrefs.edit().putBoolean(Constants.key_isThemeSetUp,true).apply()
        sharedPrefs.edit().putString(Constants.key_theme,name).apply()
        Handler(Looper.getMainLooper()).postDelayed({
            onNavigate(Screens.sMain)
            context.recreate()
        },100)}

            Column(
                modifier=Modifier
                    .fillMaxWidth()
                    .padding(vertical=10.dp)
                    .clickable {onClick()}
                    .pointerInteropFilter {
                        if(ContentCaptureManager.isEnabled) {
                            when(it.action) {
                                MotionEvent.ACTION_DOWN-> {
                                    selected.value=true
                                }

                                MotionEvent.ACTION_UP-> {
                                    selected.value=false
                                    onClick()
                                }

                                MotionEvent.ACTION_CANCEL-> {
                                    selected.value=false
                                }
                            }
                        }
                        else selected.value=false
                        true
                    }
                    .scale(scale.value)
            ){
                Image(
                    painterResource(id=imgResource),
                    contentDescription=name,
                    modifier=Modifier
                        .fillMaxSize()
                        .border(
                            width=if(isSelected) 3.dp else 0.dp,
                            color=if(isSelected) colorSecondary() else Color.Transparent
                        )
                )
            }

}
//endregion
