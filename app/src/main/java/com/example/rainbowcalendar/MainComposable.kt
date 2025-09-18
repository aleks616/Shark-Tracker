package com.example.rainbowcalendar

import android.content.Context
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rainbowcalendar.Constants.key_firstTestosteroneDate
import com.example.rainbowcalendar.Constants.key_isPeriodRegular
import com.example.rainbowcalendar.Constants.key_isPlanningToTakeTestosterone
import com.example.rainbowcalendar.Constants.key_isTakingTestosterone
import com.example.rainbowcalendar.db.CyclesDateCycle
import com.example.rainbowcalendar.db.DBUtils
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import kotlin.math.floor
import androidx.compose.material.icons.rounded.Info
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntSize
import androidx.core.graphics.PathParser
import com.example.rainbowcalendar.Constants.key_averagePeriodCycleLength
import com.example.rainbowcalendar.db.DBUtils.getCycleStartDays
import com.example.rainbowcalendar.db.DateCycle
import org.xmlpull.v1.XmlPullParser
import kotlin.math.abs
import kotlin.math.min

@Suppress("UsingMaterialAndMaterial3Libraries")


//region navigation
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
            else
                onNavigate(Screens.sMain)
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
//endregion
@Composable
fun HomeScreen(){
    val context=LocalContext.current
    val today=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(Calendar.getInstance().time)
    val sharedPrefs=context.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
    Utils.setLanguage(context)


    DBUtils.debug(context)
    DBUtils.autoAddCycleDayData(context)
    Log.i("today",today)
    //TODO: CURRENTLY getAllActiveCyclesDataDate DEPENDS ON DATE, EVEN THO ONLY CYCLE DAY DEPENDS ON IT, AND THIS WILL CAUSE BUGS WHEN RUN FIRST TIME IN A DAY!!!
    val cycles=DBUtils.getCyclesDataDate(context,today)
    var periodCycle=CyclesDateCycle("",false,0,0,0)
    var testosteroneCycle=CyclesDateCycle("",false,0,0,1)
    var birthControlCycle=CyclesDateCycle("",false,0,0,2)

    val theme=sharedPrefs.getString("theme","Black")
    val firstTDate=sharedPrefs.getString(key_firstTestosteroneDate,"")
    val periodRegular=sharedPrefs.getBoolean(key_isPeriodRegular,false)
    //sharedPrefs.edit().putBoolean(key_isPeriodRegular,true).apply()
    val isOnT=sharedPrefs.getBoolean(key_isTakingTestosterone,false)
    val isPlanningT=sharedPrefs.getBoolean(key_isPlanningToTakeTestosterone,false)

    val timeOnT=if(!firstTDate.isNullOrEmpty()) TimeUtils.timeSinceDate(firstTDate) else MilestoneDate(0,0,TIMEUNIT.ERROR)

    var tDetailsDialogVisible by remember{mutableStateOf(false)}
    var tTakingDialogVisible by remember{mutableStateOf(false)}

    var periodDetailsDialogVisible by remember{mutableStateOf(false)}

    //DBUtils.removeFaultyData(context)

    val inactiveData=DBUtils.getInactiveCyclesOfType(context,1)
    inactiveData.forEach{
        //Log.v("archive data",it.cycleName+" "+it.firstDate+" "+it.lastDate)
    }
    DBUtils.lastCycleDay(context)

    val musclePartList=listOf(
        MusclePart(0,"leftAbdomen",stringResource(R.string.leftAbdomen),true,"M86,57v-7L45,50l1,29L86,79v-8a7,7 0,1 1,0 -14Z"),
        MusclePart(1,"rightAbdomen",stringResource(R.string.rightAbdomen),true,"M86,57v-7h41l-1,29L86,79v-8a7,7 0,1 0,0 -14Z"),
        MusclePart(2,"leftThigh",stringResource(R.string.leftThigh),true,"M52.9,87.77l-3,11.22 -3,6 -8,7L34,111.99L25,141.04l-1,22 3,16 4,3 11,-2 6,7 6,2 6,-9 2,-18 -0.07,-38L54.68,87.84A0.91,0.91 0,0 0,52.9 87.77Z"),
        MusclePart(3,"rightThigh",stringResource(R.string.rightThigh),true,"M118,88.78l3,11.22 3,6 8,7h5l9,29 1,22 -3,16 -4,3 -11,-2 -6,7 -6,2 -6,-9 -2,-18v-38L116.23,88.84A0.91,0.91 0,0 1,118 88.78Z"),
        MusclePart(4,"leftButtock",stringResource(R.string.leftButtock),false,"M66,64l-10,0l-12,8l-2,31l3,7l9,7l14,1l6,0l7,-6l2,-5l0,-30l-5,-7l-12,-6z"),
        MusclePart(5,"rightButtock",stringResource(R.string.rightButtock),false,"M102,64l10,0l12,8l2,31l-3,7l-9,7l-14,1l-6,0l-7,-6l-2,-5l0,-30l5,-7l12,-6z")
    )

    var musclePart by remember{mutableStateOf(
        MusclePart(-1,"None","None",false,"")
    )}
    var refresh by remember{mutableStateOf(0)}
    LaunchedEffect(refresh){}
    LaunchedEffect(musclePart){}

    val lastShotPlaces=Utils.readShotOrder(context).reversed()
    var lastShotPlacesText="Last 5 shots in:\n"
    lastShotPlaces.forEachIndexed{index,s->
        if(index<5&&Utils.canBeIntParsed(s)) lastShotPlacesText+="- "+musclePartList[s.toInt()].localName+"\n"
    }
    //Log.v("lastShotPlacesTex",lastShotPlacesText)

    cycles.forEach{
        when(it.cycleType){
            0->periodCycle=it
            1->testosteroneCycle=it
            2->birthControlCycle=it
        }
    }

    //DBUtils.addNewDateCycle(context,DateCycle("2025-03-13",2,9))

    Column(
        modifier=
        if(theme=="Pride") Modifier.paint(painterResource(id=R.drawable.pride50),contentScale=ContentScale.FillBounds)
        else Modifier.background(colorPrimary())
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ){
        //BetterHeader("Home",fontSize="L")


        if(isOnT){
            Spacer(modifier=Modifier.height(10.dp))
            //region T
            Row(modifier=Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.Center){
                BetterHeader("Testosterone",fontSize="L",modifier=Modifier)
                IconButton(onClick={tDetailsDialogVisible=true},modifier=Modifier.padding(start=4.dp)){
                    Icon(
                        imageVector=Icons.Rounded.Info,
                        tint=colorTertiary(),
                        contentDescription=null,
                        modifier=Modifier.scale(1.8f)
                    )
                }
                if(tDetailsDialogVisible)
                    CustomizableDialog({tDetailsDialogVisible=false},{
                        Column(modifier=Modifier.fillMaxWidth().padding(horizontal=12.dp,vertical=6.dp)){
                            BetterText("Currently on: "+testosteroneCycle.cycleName,fontSize="ML",modifier=Modifier.padding(vertical=6.dp))
                            val text="interval - "+if(testosteroneCycle.correctLength==1) "everyday" else "every "+testosteroneCycle.correctLength+" days."
                            BetterText(text,fontSize="ML",modifier=Modifier.padding(bottom=8.dp))
                            if(inactiveData.isNotEmpty()){
                                BetterText("previous testosterone types: ",fontSize="M",modifier=Modifier.padding(top=10.dp,bottom=4.dp)) //TODO: GET DATA FOR INACTIVE T CYCLES AND FIRST AND LAST DATE!
                                inactiveData.forEach{
                                    if(it.firstDate.isNotEmpty())
                                        BetterText(text="- "+it.cycleName+" from "+it.firstDate+" to "+it.lastDate,fontSize="MS",modifier=Modifier.padding(vertical=6.dp))
                                }
                            }
                        }
                    })
            }

            var lateBy=testosteroneCycle.cycleDay-testosteroneCycle.correctLength
            Log.v("t cycle day",testosteroneCycle.cycleDay.toString())
            LaunchedEffect(testosteroneCycle.cycleDay){
                lateBy=testosteroneCycle.cycleDay-testosteroneCycle.correctLength
            }
            val daysLeft=-1*lateBy

            if(timeOnT.timeUnit!=TIMEUNIT.ERROR){
                BetterHeader("You're on T since: "+timeOnT.amount.toString()+" "+timeOnT.timeUnit.toString().lowercase(),fontSize="M")
                val showLogChanges=Utils.showLogChanges(firstTDate!!) /**if it's null, timeOnT time unit is error so it'll work**/
                if(showLogChanges){//todo: SHOW MENU TOO IF IT WASN'T FILLED ON CORRECT DAY}
                    BetterText(text="menu for logging changes will be here",fontSize="S")
                    
                }
                BetterText(text=firstTDate,fontSize="S")
            }

            val formattedNextDate=TimeUtils.longDateFromString(TimeUtils.addDateString(today,daysLeft))

            Spacer(modifier=Modifier.height(14.dp))
            if(lateBy==0)
                BetterHeader("Time for your dose",fontSize="M")
            else if(lateBy>0){
                BetterHeader("You forgot your dose!",fontSize="ML",color=colorError())
                val lateByText="late by: "+lateBy+if(lateBy==1)" day" else " days"
                BetterHeader(lateByText,fontSize="M",color=colorError())
            }
            else{
                val nextDoseText="Next dose"+if(daysLeft==1)": tomorrow" else " in: "+(daysLeft)+" days - on "+formattedNextDate
                BetterText(nextDoseText,fontSize="M",modifier=Modifier.fillMaxWidth().padding(horizontal=12.dp))
            }

            Spacer(modifier=Modifier.height(14.dp))
            //BetterHeader(text="testosterone cycle day: "+testosteroneCycle.cycleDay+" length: "+testosteroneCycle.correctLength,fontSize="MS")
            Row(horizontalArrangement=Arrangement.Center,modifier=Modifier.fillMaxWidth()){
                val id=DBUtils.getCycleIdByName(context,testosteroneCycle.cycleName)
                val todayCycleDay=DBUtils.getCycleDayByIdAndDate(context,id,today)
                //Log.v("test",todayCycleDay.toString())
                //Log.v("today",today)

                if((daysLeft<=(testosteroneCycle.correctLength/6)+1)&&todayCycleDay!=0){ //sheet 5
                    BetterButton(modifier=Modifier.padding(start=12.dp).sizeIn(minWidth=80.dp,maxWidth=300.dp, minHeight=50.dp,maxHeight=60.dp),
                        onClick={
                            tTakingDialogVisible=true}){
                        BetterText("Take dose",fontSize="S",modifier=Modifier.padding(vertical=4.dp,horizontal=10.dp))
                    }
                }
            }
            var isOnGel by remember{mutableStateOf(testosteroneCycle.correctLength<3)}
            if(tTakingDialogVisible){
                CustomizableDialog({tTakingDialogVisible=false},{
                    Column(Modifier.verticalScroll(rememberScrollState())){
                        CheckboxRow(checked=isOnGel, onCheckedChange={isOnGel=!isOnGel},text="Gel (/not a shot)")
                        if(!isOnGel){
                            BetterText(text="Shot in: "+if(musclePart.name=="None") "not selected" else musclePart.localName,fontSize="m",modifier=Modifier.padding(start=10.dp,end=10.dp,bottom=14.dp,top=6.dp))
                            Row(Modifier.fillMaxHeight(0.8f)){
                                val muscleState=Utils.getMuscleStates(context)
                                val images=Utils.musclesStateToImages(muscleState.first,muscleState.second)
                                var (frontImage,backImage)=images
                                if(frontImage==-1) frontImage=R.drawable.body_front_green
                                if(backImage==-1) backImage=R.drawable.body_back_green

                                Box(Modifier.weight(1f).padding(6.dp)){
                                    DetectableColorsImage(backImage,musclePartList){
                                        val temp=it?:MusclePart(-1,"None","None",false,"")
                                        Log.v("body part back",temp.localName)
                                        musclePart=temp
                                    }
                                }
                                Box(Modifier.weight(1f).padding(4.dp)){
                                    DetectableColorsImage(frontImage,musclePartList){
                                        val temp=it?:MusclePart(-1,"None","None",true,"")
                                        Log.v("body part front",temp.localName)
                                        musclePart=temp
                                    }
                                }
                              }
                            Row{
                                BetterText(lastShotPlacesText,fontSize="MS",modifier=Modifier.padding(start=8.dp,end=8.dp,bottom=2.dp,top=14.dp))
                            }
                        }
                        Row{
                            Box(
                                contentAlignment=Alignment.BottomCenter,modifier=Modifier.fillMaxSize().padding(vertical=8.dp)
                            ){
                                BetterButton(modifier=Modifier.width(120.dp).height(40.dp),onClick={
                                    val cycleId=DBUtils.getCycleIdByName(context,testosteroneCycle.cycleName)
                                    DBUtils.addNewDateCycleNew(context,DateCycle(today,cycleId,0))
                                    refresh++

                                    if((!isOnGel&&musclePart.name!="None")) Utils.saveShotOrder(context,musclePart.id)
                                    if(musclePart.name!="None"||isOnGel) tTakingDialogVisible=false
                                }){
                                  BetterText(text="Save",fontSize="MS")
                                }
                            }
                        }
                    }
                })
            }
            //endregion
        }



        Spacer(modifier=Modifier.height(36.dp))
        Row(modifier=Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.Center){
            BetterHeader("Period",fontSize="L",modifier=Modifier)
            IconButton(onClick={periodDetailsDialogVisible=true},modifier=Modifier.padding(start=4.dp)){
                Icon(
                    imageVector=Icons.Rounded.Info,
                    tint=colorTertiary(),
                    contentDescription=null,
                    modifier=Modifier.scale(1.8f)
                )
            }
            if(periodDetailsDialogVisible)
                CustomizableDialog({periodDetailsDialogVisible=false},{
                    Column(modifier=Modifier.fillMaxWidth().padding(horizontal=12.dp,vertical=6.dp)){
                        sharedPrefs.edit().putInt(key_averagePeriodCycleLength,28).apply() //TODO: REMOVE! THIS IS JUST FOR TESTING
                        val cycleLength=sharedPrefs.getInt(Constants.key_averagePeriodCycleLength,28) //todo FIX! IT SHOULD NOT BE 0 WTF
                        val bleedingLength=/*sharedPrefs.getInt(Constants.key_averagePeriodLength,*/5//)
                        val avg=Utils.calcAverageCycleLength(context,DBUtils.getCycleIdByName(context,periodCycle.cycleName),cycleLength)
                        val phase=Utils.getPeriodPhase(periodCycle.cycleDay,cycleLength.toDouble(),bleedingLength)

                        BetterText(text=if(periodRegular) "Regular period" else "Irregular period",fontSize="M")
                        BetterText(text="average correct cycle length: "+avg,fontSize="MS",modifier=Modifier.padding(vertical=4.dp))
                        BetterText(text="current phase: "+phase,fontSize="MS",modifier=Modifier.padding(vertical=4.dp))
                        BetterText(text="your previous cycle starts:",fontSize=24.sp,modifier=Modifier.padding(top=10.dp,bottom=4.dp))
                        val cycleId=DBUtils.getCycleIdByName(context,"period")
                        val cycleStartDates=getCycleStartDays(context,cycleId).reversed()

                        cycleStartDates.forEachIndexed{i,it->
                            val length=it.second

                            var color=colorSecondary()
                            val display:String
                            if(i==0) display=it.first+" this is the current cycle";
                            else if(length==null||length==0) display=it.first+" unknown length";
                            else{
                                val correctL=periodCycle.correctLength
                                /**difference of length of this cycle and the correct length**/
                                val diff=abs(correctL-length)
                                /**e.g correct length 28, length of this cycle is 35, so the difference is greater than 5.6 days and text is red**/
                                if(diff>(correctL/6)){
                                    color=colorError()
                                    //Log.i("period data","length: "+length+" correctLength: "+correctL)
                                    display=it.first+" length: "+length+if(length<correctL)". Too short by " else {". Too long by "}+diff+" days."
                                }
                                else display=it.first+" length: "+length

                            }
                            BetterText(display,fontSize="S",modifier=Modifier.padding(top=5.dp,bottom=5.dp,start=6.dp),color=color)

                        }
                        
                    }
                })
        }
        Log.i("testosterone interval",sharedPrefs.getInt(Constants.key_currentTestosteroneInterval,-1).toString())

        DBUtils.removeFaultyData(context)
        val lateBy=periodCycle.cycleDay-periodCycle.correctLength
        //Log.v("dates","cycleday: "+periodCycle.cycleDay+" length: "+periodCycle.correctLength+periodCycle.cycleName)
        //Log.v("dates","cycleday: "+testosteroneCycle.cycleDay+" length: "+testosteroneCycle.correctLength+" "+testosteroneCycle.cycleName)
        if(isOnT){
            if(!periodRegular&&(periodCycle.cycleDay-periodCycle.correctLength>0))
                BetterHeader("Congrats, your last period was: "+periodCycle.cycleDay+" days ago!",fontSize="S")
            else if(periodCycle.cycleDay-periodCycle.correctLength-1>5)
                BetterHeader("Time to mark period as irregular already?",fontSize="MS")
        }
        Log.i("period regular",periodRegular.toString()+" "+periodCycle.correctLength+" next "+(-1*lateBy)+" day: "+periodCycle.cycleDay)
        if(periodRegular){
            val next=(lateBy*-1)
            Spacer(Modifier.height(10.dp))
            if(lateBy>0)
                BetterHeader("Your period is late by: "+(lateBy)+" days",fontSize="MS",color=colorError())
            else if(lateBy==0)
                BetterHeader("It should start today",fontSize="MS")
            else
                BetterHeader("Next period in: "+(next+2)+" days",fontSize="MS")

            Spacer(Modifier.height(10.dp))
            BetterHeader(text="today is day "+(periodCycle.cycleDay+1),fontSize="MS")/**+1 because database counts from 0**/

            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement=Arrangement.Center,modifier=Modifier.fillMaxWidth()){
                if(periodCycle.cycleDay>5){
                    BetterButton(modifier=Modifier.padding(start=12.dp).sizeIn(minWidth=80.dp,maxWidth=300.dp, minHeight=50.dp,maxHeight=60.dp),
                        onClick={
                            val cycleId=DBUtils.getCycleIdByName(context,periodCycle.cycleName)
                            refresh++
                            DBUtils.addNewDateCycleNew(context,DateCycle(today,cycleId,0))
                        }){
                        BetterText("Period started",fontSize="S",modifier=Modifier.padding(vertical=4.dp,horizontal=10.dp))
                    }
                }
            }

        }




        /*if(isPlanningT){
            val rnd=(0..9).random()
            if(rnd==0){
                BetterText("Starting T soon? Go to settings",fontSize=16.sp)
                //todo: button? idk
            }
        }

        BetterHeader("Binder",fontSize="L")
        Row(modifier=Modifier.fillMaxSize().padding(top=20.dp),Arrangement.Center){
            CountdownTimer(8,context)
        }*/

    }
}
@Composable
fun DetectableColorsImage(drawableId:Int,muscleParts:List<MusclePart>,onPartSelected:(MusclePart?)->Unit){
    val context=LocalContext.current
    var imageSize by remember {mutableStateOf(IntSize.Zero)}

    Box(Modifier.fillMaxSize().pointerInput(drawableId){
            detectTapGestures{offset->
                val part=getTouchedPartName(offset=offset,context=context,drawableId=drawableId,imageSize=imageSize,muscleParts)
                onPartSelected(part)
            }
        }
        .onSizeChanged{imageSize=it}){
        Image(
            painter=rememberVectorPainter(ImageVector.vectorResource(drawableId)),
            contentDescription=null,
            modifier=Modifier.fillMaxSize()
        )
    }
}
data class MusclePart(
    val id:Int,
    val name:String,
    val localName:String,
    val front:Boolean,
    val path:String
)


fun getTouchedPartName(offset:Offset,context:Context,drawableId:Int,imageSize:IntSize,muscleParts:List<MusclePart>):MusclePart?{
    val xml=context.resources.getXml(drawableId)
    val paths=mutableListOf<Pair<Path,String>>()
    var vw=0f
    var vh=0f
    val ns="http://schemas.android.com/apk/res/android"

    xml.use{
        while(xml.next()!=XmlPullParser.END_DOCUMENT){
            when{
                xml.eventType==XmlPullParser.START_TAG&&xml.name=="vector"->{
                    vw=xml.getAttributeValue(ns,"viewportWidth").toFloat()
                    vh=xml.getAttributeValue(ns,"viewportHeight").toFloat()
                }
                xml.eventType==XmlPullParser.START_TAG&&xml.name=="path"->{
                    val data=xml.getAttributeValue(ns,"pathData")
                    val color=xml.getAttributeValue(ns,"fillColor")?:""
                    if(data!=null){
                        paths.add(PathParser.createPathFromPathData(data) to color)
                    }
                }
            }
        }
    }

   /* Log.v("part imageSize.height==0",(imageSize.height==0).toString())
    Log.v("part imageSize.width==0",(imageSize.width==0).toString())
    Log.v("part vw==0",(vw==0f).toString())
    Log.v("part vh==0",(vh==0f).toString())
    Log.v("part check",(vw==0f||vh==0f||imageSize.width==0||imageSize.height==0).toString())*/
    if(vw==0f||vh==0f||imageSize.width==0||imageSize.height==0) return null else Log.v("part","all good")

    val scale=min(imageSize.width/vw,imageSize.height/vh)
    val offsetX=(imageSize.width-vw*scale)/2
    val offsetY=(imageSize.height-vh*scale)/2
    val x=(offset.x-offsetX)/scale
    val y=(offset.y-offsetY)/scale

    for(i in paths.indices.reversed()){
        Log.w("part in for loop","here")
        val(path,color)=paths[i]
        val bounds=RectF()
        path.computeBounds(bounds,true)
        val region=Region()
        region.setPath(path,Region(
            bounds.left.toInt(),
            bounds.top.toInt(),
            bounds.right.toInt(),
            bounds.bottom.toInt()
        ))
        if(region.contains(x.toInt(),y.toInt())){
            muscleParts.forEachIndexed {i, it ->
                Log.v("body part",i.toString()+" "+it.name+" "+Utils.arePathsTheSame(path,it.path))
                if(Utils.arePathsTheSame(path,it.path)) return it
            }
        }
    }
   // Utils.arePathsTheSame(muscleParts[2].path,"M52.9,87.77l-3,11.22 -3,6 -8,7L34,111.99L25,141.04l-1,22 3,16 4,3 11,-2 6,7 6,2 6,-9 2,-18 -0.07,-38L54.68,87.84A0.91,0.91 0,0 0,52.9 87.77Z")

    return null
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


    val firstDate=DBUtils.getFirstMetricDate(context)
    val months=if(!firstDate.isNullOrEmpty()) TimeUtils.monthsSinceFirstDate(firstDate) else 12


    var expanded by remember{mutableStateOf(false)}
    var menuText by remember{mutableStateOf("Average")}

    val metrics=mutableListOf("Average","Overall Mood","Headache","Muscle/back pain",
        "Skin condition", "Digestive issues","Sleep quality","Energy level","Mood swings",
        "Bleeding","Cramps","Cravings")
    if(DBUtils.hasDysphoria(context)) metrics.add(1,"Dysphoria")


    val theme=context.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE).getString("theme","Black")

    val data=DBUtils.getAllMoodData(context)
    if(data.isNotEmpty()){
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

        dates+=DayColor(TimeUtils.localDateString(it.date),color)
    }

    Column(
        modifier=if(theme=="Pride")
            Modifier.paint(painterResource(id=R.drawable.pride50),contentScale=ContentScale.FillBounds)
        else Modifier.background(colorPrimary())
            .fillMaxSize()
    ){
        BetterButton(
            modifier=Modifier.fillMaxWidth().padding(horizontal=10.dp,vertical=15.dp).height(50.dp).border(width=2.dp,color=colorTertiary()),
            onClick={expanded=true},){
            BetterText(text=menuText,fontSize="XS")
            DropdownMenu(
                expanded=expanded,
                onDismissRequest={expanded=false},
                modifier=Modifier.background(colorPrimary()).fillMaxWidth().padding(horizontal=10.dp).heightIn(max=250.dp)){
                metrics.forEach{question->
                    DropdownMenuItem(
                        text={BetterText(text=question,fontSize="XS")},
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
    else{
        LazyColumn(
            modifier=if(theme=="Pride")
                Modifier.paint(painterResource(id=R.drawable.pride50),contentScale=ContentScale.FillBounds)
            else Modifier.background(colorPrimary())
                .fillMaxSize()
            ){
            item{
                BetterHeader("No data to display in calendar yet",fontSize="ML")
            }
        }
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
            BetterText(text="CHANGE THEME",textAlign=TextAlign.Center,fontSize=16.sp)
        }
        BetterButton(
            onClick={onButtonClick(Screens.sLanguage)},
            modifier=Modifier.height(100.dp).width(150.dp) .align(Alignment.CenterHorizontally).padding(vertical=15.dp)
        ){
            BetterText(text="CHANGE LANGUAGE",textAlign=TextAlign.Center,fontSize=16.sp)
        }
        BetterButton(
            onClick={Utils.toggleStealthMode(context)},
            modifier=Modifier.height(100.dp).width(150.dp).align(Alignment.CenterHorizontally).padding(vertical=15.dp)
        ){
            BetterText(text="TOGGLE STEALTH MODE",textAlign=TextAlign.Center,fontSize=16.sp)
        }
    }


}


//TODO:
// 1 FIX PASSWORD ERRORS NOT SHOWING!
// 2. ADD RECOVERY WHEN FORGOT PASSWORD
// 4. SOCIALS/CREDITS TAB
//: TODO FROM OLD ACTIVITIES: home_fragment/cycles

