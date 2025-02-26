package com.example.rainbowcalendar

import android.content.Context
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.example.rainbowcalendar.db.DateCycle
import com.vsnappy1.timepicker.data.model.TimePickerTime
import java.time.LocalDate
import java.time.ZoneId

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
    val theme=sharedPrefs.getString("theme","Black")
    LazyColumn(
        modifier=if(theme=="Pride")
            Modifier
                .fillMaxSize()
                .paint(painterResource(id=R.drawable.pride50),contentScale=ContentScale.FillBounds)
        else Modifier
            .fillMaxSize()
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
        item{BetterHeader(
            text=stringResource(id=R.string.termsAndConditionsTitle),
            fontSize="L",
            textAlign=TextAlign.Center,
            modifier=Modifier
                .padding(vertical=15.dp).fillMaxWidth()
            )
        }
        item{
            index.forEach{index->
                Row(
                    horizontalArrangement=Arrangement.Start,
                    verticalAlignment=Alignment.CenterVertically,
                    modifier=Modifier
                        .fillMaxWidth().padding(vertical=14.dp,horizontal=10.dp)
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
                        modifier=Modifier.fillMaxWidth().align(Alignment.CenterVertically)
                    )
                }
            }
        }
        item{
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
        item{
            VerticalSpacer()
        }
    }
}

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
    val theme=sharedPrefs.getString("theme","Black")
    LazyColumn(
        modifier=if(theme=="Pride")
            Modifier.fillMaxSize().paint(painterResource(id=R.drawable.pride50),contentScale=ContentScale.FillBounds)
        else Modifier.fillMaxSize().background(colorPrimary())
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
            BetterText(
                text=stringResource(id=R.string.yourIdentityHeader),
                fontSize=42.sp,
                modifier=Modifier.padding(15.dp).fillMaxWidth(),
                textAlign=TextAlign.Center
            )
        }
        item{
            BetterText(
                text=stringResource(id=R.string.chooseMostFittingOption),
                fontSize=25.sp,
                modifier=Modifier .padding(start=15.dp,end=15.dp,top=5.dp,bottom=15.dp).fillMaxWidth(),
                textAlign=TextAlign.Center
            )
        }
        item{
            genderItems.forEach{item->
                Row(
                    modifier=Modifier
                        .padding(vertical=10.dp,horizontal=12.dp)
                        .border(
                            width=2.dp,
                            color=if(selectedOption==item.id) colorQuaternary() else colorTertiary(),
                            shape=RoundedCornerShape(20)
                        )
                        .fillMaxWidth()
                        .clickable{selectedOption=item.id}
                ){
                    Column{
                        BetterText(
                            text=item.name,
                            fontSize=32.sp,
                            modifier=Modifier.padding(start=20.dp, top=6.dp,end=6.dp,bottom=6.dp)
                        )
                        if(item.description.isNotEmpty()){
                            BetterText(
                                text=item.description,
                                fontSize=19.sp,
                                modifier=Modifier.padding(start=20.dp, bottom=12.dp, end=8.dp)
                            )
                        }
                    }
                }
            }
        }

        if(!neutralAvailable){
            item{
                BetterHeader(
                    text=stringResource(id=R.string.noGenderNeutral),
                    fontSize="MS",
                    modifier=Modifier.padding(vertical=10.dp,horizontal=6.dp)
                )
            }
            item{
                BetterHeader(
                    text=stringResource(id=R.string.noGenderNeutralSuggestion),
                    fontSize="S",
                    modifier=Modifier.padding(vertical=4.dp,horizontal=12.dp).fillMaxWidth()
                )
            }
            item{
                Row(modifier=Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.Center){
                    BetterButton(modifier=Modifier.padding(vertical=12.dp),onClick={onNavigate(
                        Screens.sLanguage)}){
                        BetterText(text=stringResource(id=R.string.go_to_language_screen),fontSize=20.sp,modifier=Modifier.padding(10.dp))
                    }
                }
            }
        }
        item{
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
                    modifier=Modifier.padding(top=30.dp,end=35.dp)
                ){
                    Image(
                        painter=painterResource(id=R.drawable.icon_checkmark_circle),
                        contentDescription=null,
                        modifier=Modifier.scale(3.5f)
                    )
                }
            }
        }
        item{
            VerticalSpacer()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameBirthDayOptionsScreen(onNavigate:(String)->Unit,thisScreen:String?){
    //on navigate stealth
    val context=LocalContext.current
    val sharedPrefs=context.getSharedPreferences(Constants.key_package, Context.MODE_PRIVATE)
    if(sharedPrefs.getBoolean(Constants.key_isNameBirthDayMenuComplete,false))
        onNavigate(Screens.sStealthOptions)

    var name by remember{mutableStateOf("")}
    val birthdayDatePickerState=rememberDatePickerState(
        initialSelectedDateMillis=LocalDate.now().minusYears(21).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        selectableDates=Utils.OldEnoughSelectableDates
    )
    val birthdaySelectedDate=birthdayDatePickerState.selectedDateMillis?.let{
        Utils.convertMillisToDate(it)
    }?:""
    var errorText by remember{mutableStateOf("")}
    var skipBirthday by remember{mutableStateOf(false)}

    val theme=sharedPrefs.getString("theme","Black")
    LazyColumn(
        modifier=if(theme=="Pride")
            Modifier
                .fillMaxSize()
                .paint(painterResource(id=R.drawable.pride50),contentScale=ContentScale.FillBounds)
        else Modifier
            .fillMaxSize()
            .background(colorPrimary())
    ){
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
                modifier=Modifier.padding(vertical=6.dp,horizontal=8.dp).fillMaxWidth()
            )
        }
        item{
            BetterHeader(text="it will appear on main screen", fontSize="S",
                modifier=Modifier.padding(vertical=6.dp).fillMaxWidth()
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
                    modifier=Modifier.padding(vertical=6.dp,horizontal=8.dp).fillMaxWidth()
                )
            }
            item{
                BetterHeader(
                    text="Selected date: $birthdaySelectedDate",fontSize="M",fontStyle=FontStyle.Italic,
                    modifier=Modifier.padding(top=14.dp,bottom=6.dp).fillMaxWidth(),
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
                else
                    errorText="Name cannot be empty"
            })
        }
        item{
            VerticalSpacer()
        }
    }
}

@Composable
fun StealthOptionsScreen(onNavigate:(String) -> Unit,thisScreen:String?){
    val context=LocalContext.current
    val sharedPrefs=context.getSharedPreferences(Constants.key_package, Context.MODE_PRIVATE)
    val gender=sharedPrefs.getInt(Constants.key_gender,-1)
    //0-tm 1-nb 2-female
    var censorPeriod by remember{mutableStateOf(Utils.isPeriodCensored(context))}
    val stealthDone=sharedPrefs.getBoolean(Constants.key_isStealthDone,false)
    val next=if(gender==0||gender==1) Screens.sTOptions else Screens.sPeriodOptions

    SideEffect{
        if(stealthDone) onNavigate(next)
    }

    LaunchedEffect(censorPeriod){} //doesn't work
    val theme=sharedPrefs.getString("theme","Black")
    LazyColumn(
        modifier=if(theme=="Pride")
            Modifier
                .fillMaxSize()
                .paint(painterResource(id=R.drawable.pride50),contentScale=ContentScale.FillBounds)
        else Modifier
            .fillMaxSize()
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
            BetterText(
                text=stringResource(id=R.string.useStealthModeQuestion),
                fontSize=40.sp,
                textAlign=TextAlign.Center,
                modifier=Modifier.padding(top=18.dp,bottom=25.dp).fillMaxWidth()
            )
        }
        item{
            BetterText(
                text=stringResource(id=R.string.stealthModeDescription),
                fontSize=25.sp,
                textAlign=TextAlign.Center,
                modifier=Modifier.fillMaxWidth().padding(horizontal=15.dp),
            )
        }
        item{
            Row(
                modifier=Modifier.fillMaxWidth(),
                horizontalArrangement=Arrangement.Center
            ){
                BetterButton(
                    onClick={Utils.toggleStealthMode(context)},
                    shape=RoundedCornerShape(15.dp),
                    modifier=Modifier.width(190.dp).height(100.dp).padding(vertical=20.dp)
                ){
                    BetterText(
                        text=if(Utils.isStealthModeOn(context)) stringResource(id=R.string.normal) else stringResource(id=R.string.stealth),
                        fontSize=24.sp
                    )
                }
            }
        }

        item{
            BetterText(
                text=stringResource(id=R.string.censorPeriodQuestion),
                fontSize=26.sp,
                modifier=Modifier.fillMaxWidth().padding(vertical=5.dp),
                textAlign=TextAlign.Center
            )
        }
        item{
            Row( //turn on/off period censor
                modifier=Modifier.fillMaxWidth(),
                horizontalArrangement=Arrangement.Center
            ){
                BetterButton(
                    onClick={Utils.togglePeriodCensor(context);censorPeriod=!censorPeriod},
                    shape=RoundedCornerShape(15.dp),
                    modifier=Modifier.width(190.dp).height(100.dp).padding(vertical=20.dp)
                ){
                    BetterText(
                        text=if(censorPeriod) stringResource(id=R.string.usePeriod) else stringResource(id=R.string.useCensorPeriod),
                        fontSize=24.sp
                    )
                }
            }
        }

        item{
            CheckmarkButtonRow(onClick={
                sharedPrefs.edit().putBoolean(Constants.key_isStealthDone,true).apply()
                //0-tm 1-nb 2-female
                if(gender==0||gender==1)
                    onNavigate(Screens.sTOptions)
                else
                    onNavigate(Screens.sAgeConsentOptions)
            })
        }
        item{
            VerticalSpacer()
        }

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
        selectableDates=Utils.PastOrPresentSelectableDates
    )
    val lastDoseSelectedDate=lastDoseDatePickerState.selectedDateMillis?.let{
        Utils.convertMillisToDate(it)
    }?:""

    val firstDoseDatePickerState=rememberDatePickerState(
        selectableDates=Utils.PastOrPresentSelectableDates
    )
    val firstDoseSelectedDate=firstDoseDatePickerState.selectedDateMillis?.let{
        Utils.convertMillisToDate(it)
    }?:""

    val datePickerColors=Utils.datePickerColors()


    sharedPrefs.edit().putString(Constants.key_firstTestosteroneDate,firstDoseSelectedDate).apply()
    sharedPrefs.edit().putString(Constants.key_lastTestosteroneDate,lastDoseSelectedDate).apply()

    var errorText by remember{mutableStateOf("")}
    LaunchedEffect(errorText){}
    val testosteroneNameIsEmptyError=stringResource(id=R.string.nameIsEmptyError)
    val intervalIsEmptyError=stringResource(id=R.string.intervalIsEmptyError)
    val lastDoseIsEmptyError=stringResource(id=R.string.lastDoseIsEmptyError)
    val testosteroneIntervalIsNotDigit=stringResource(id=R.string.intervalNotANumberError)

    val skipFirstDose=stringResource(id=R.string.skipLater)
    var skipFirstDoseDateChecked by remember{mutableStateOf(false)}

    var tShotsReminders by remember{mutableStateOf(false)}
    var notificationHour by remember{mutableStateOf(6)}
    var notificationMinute by remember{mutableStateOf(0)}

    val theme=sharedPrefs.getString("theme","Black")
    LazyColumn(
        modifier=if(theme=="Pride")
            Modifier
                .fillMaxSize()
                .paint(painterResource(id=R.drawable.pride50),contentScale=ContentScale.FillBounds)
        else Modifier
            .fillMaxSize()
            .background(colorPrimary())
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
                    modifier=Modifier.fillMaxWidth().padding(vertical=12.dp,horizontal=20.dp)
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
                    modifier=Modifier.fillMaxWidth().padding(bottom=5.dp)
                )
            }
            item{
                Row(
                    modifier=Modifier.padding(top=10.dp)
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
                        modifier=Modifier.fillMaxWidth().padding(top=6.dp,start=20.dp,end=20.dp,bottom=20.dp),
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
                                            else
                                                skipFirstDoseDateChecked=true
                                        }
                                        else
                                            errorText=lastDoseIsEmptyError
                                    }
                                    else
                                        errorText=testosteroneIntervalIsNotDigit
                                }
                                else
                                    errorText=testosteroneIntervalIsNotDigit
                            }
                            else
                                errorText=intervalIsEmptyError
                        }
                        else
                            errorText=testosteroneNameIsEmptyError
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
                    modifier=Modifier.fillMaxWidth().padding(horizontal=20.dp,vertical=10.dp)
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
fun PeriodOptionsScreen(onNavigate:(String)->Unit,thisScreen:String?){
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

    val theme=sharedPrefs.getString("theme","Black")
    LazyColumn(
        modifier=if(theme=="Pride")
            Modifier
                .fillMaxSize()
                .paint(painterResource(id=R.drawable.pride50),contentScale=ContentScale.FillBounds)
        else Modifier
            .fillMaxSize()
            .background(colorPrimary())
    ){
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
                if(Utils.canBeIntParsed(cycleLengthValue)){
                    errorText=""
                    sharedPrefs.edit().putInt(Constants.key_averagePeriodCycleLength,cycleLengthValue.toInt()).apply()
                    Utils.addNewCycleType(context=context, cycleName="period",correctInterval=cycleLengthValue.toInt(),active=true)

                    cycleDates2.forEachIndexed{index,cycle->
                        Log.i("newDateCycleDebug",cycle[2]+"-"+cycle[1]+"-"+cycle[0])
                        if(Utils.canBeIntParsed(cycle[2])&&Utils.canBeIntParsed(cycle[1])&&Utils.canBeIntParsed(cycle[0])){
                            if(Utils.isValidPastDate(cycle[2].toInt(),cycle[1].toInt(),cycle[0].toInt())){
                                //Log.i("newDateCycleDebug",index.toString())
                                if(index>0){
                                    //Log.i("newDateCycleDebug","here3")
                                    val indexDate=Utils.createDateFromIntegers(cycle[2].toInt(),cycle[1].toInt(),cycle[0].toInt())
                                    val previousDate=Utils.createDateFromIntegers(cycleDates2[index-1][2].toInt(),cycleDates2[index-1][1].toInt(),cycleDates2[index-1][0].toInt())
                                    if(Utils.isDate1AfterDate2(indexDate,previousDate)){
                                        errorText=datesNotDescendingError
                                        //Log.i("newDateCycleDebugDates","date: $previousDate previous date: $indexDate")
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
                                        else
                                            errorText=periodLengthNotANumberError
                                    }
                                    if(errorText.isEmpty()){
                                        if(Utils.addNewDateCycle(context,
                                                DateCycle(indexDate,cycleId,0)
                                            )){
                                            amountOfCyclesToEnter=0
                                            cycleDates2=listOf(arrayOf("","",""))
                                            sharedPrefs.edit().putBoolean(Constants.key_isPeriodMenuComplete,true).apply()
                                            onNavigate(Screens.sContraceptiveOptions)
                                        }
                                        else //cycle type not created
                                            errorText="Can't add cycle data yet, try again in 5 seconds."
                                    }


                                }
                                //}
                            }
                            else{
                                //Log.v("newDateCycleInvalidDate",cycle[2]+"-"+cycle[1]+"-"+cycle[0])
                                errorText=invalidDatesError
                            }
                        }
                        else
                            errorText=notNumberDatesError
                    }
                }
                else
                    errorText=cycleLengthNotANumberError
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
        selectableDates=Utils.PastOrPresentSelectableDates
    )
    val lastDoseSelectedDate=lastDoseDatePickerState.selectedDateMillis?.let{
        Utils.convertMillisToDate(it)
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

    val theme=sharedPrefs.getString("theme","Black")
    LazyColumn(
        modifier=if(theme=="Pride")
            Modifier
                .fillMaxSize()
                .paint(painterResource(id=R.drawable.pride50),contentScale=ContentScale.FillBounds)
        else Modifier
            .fillMaxSize()
            .background(colorPrimary())
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
                    modifier=Modifier.fillMaxWidth().padding(vertical=12.dp,horizontal=20.dp)
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
                    modifier=Modifier.fillMaxWidth().padding(bottom=5.dp)
                )
            }
            item{
                Row(
                    modifier=Modifier.padding(top=10.dp)
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
