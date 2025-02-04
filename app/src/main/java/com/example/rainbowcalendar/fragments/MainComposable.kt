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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rainbowcalendar.MainActivity
import com.example.rainbowcalendar.R
import com.example.rainbowcalendar.ScrollableMetricsView
import com.example.rainbowcalendar.Utils
import com.example.rainbowcalendar.getColor
import com.example.rainbowcalendar.getLocal

@Composable
fun MainComposable(){
    var currentScreen by remember{mutableStateOf("WelcomeScreen")}

    when(currentScreen){
        "WelcomeScreen"->WelcomeScreen{screen->currentScreen=screen}
        "MainScreen"->MainScreen{screen->currentScreen=screen}
        "LanguageScreen"->LanguageScreen()
        "ThemeScreen"->ThemeScreen()
        "PasswordScreen"->PasswordScreen{screen->currentScreen=screen}
        "IntroScreen"->IntroScreen{screen->currentScreen=screen}
        "RecoveryScreen"->RecoveryScreen{screen->currentScreen=screen}
    }
}

//region MainActivity
@Composable
fun MainScreen(onNavigate:(String)->Unit){
    //onNavigate("RecoveryScreen")
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
                onClick={onButtonClick("ThemeScreen")},
                modifier=Modifier
                    .height(100.dp)
                    .width(150.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical=15.dp)
            ){BetterText(text="CHANGE THEME",textAlign=TextAlign.Center)}
        BetterButton(
            onClick={onButtonClick("LanguageScreen")},
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
        ){BetterText(text="TOGGLE STEALTH MODE",textAlign=TextAlign.Center)}
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
// 3. REDO INTRODUCTION AND SETTINGS (make it better)
// 4. (don't forget setup complete)
// 5. BRING BACK CREATE PASSWORD/RECOVERY POPUP
// 6. SOCIALS/CREDITS TAB

@Composable
fun WelcomeScreen(onNavigate:(String)->Unit){
    val context=LocalContext.current

    val sharedPrefs=context.getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)
    val setupDone=sharedPrefs.getBoolean("setupDone",false)
    val languageSetupDone=sharedPrefs.getBoolean("langSetup",false)
    val themeSetupDone=sharedPrefs.getBoolean("themeSetup",false)
    //sharedPrefs.edit().putBoolean("setupDone",false).apply()
    if(setupDone){
        SideEffect{
            if(!sharedPrefs.getString("passwordValue","").isNullOrEmpty()){ //there is password
                onNavigate("PasswordScreen")
            }
            else{
                Handler(Looper.getMainLooper()).postDelayed({
                    onNavigate("MainScreen")
                },500)
            }
        }
    }
    else{
        createNotificationChannel(context) //todo: i think i need to check if it exists before
        Handler(Looper.getMainLooper()).postDelayed({
            if(!languageSetupDone)
                onNavigate("LanguageScreen")
            else if(!themeSetupDone)
                onNavigate("ThemeScreen")
            else{
                onNavigate("PasswordScreen") //TODO: CHANGE TO INTRO SCREEN
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
    val sharedPrefs=context.getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)
    //passwordScreenType 0-select 1-text 2-pin
    var passwordScreenType by remember{mutableStateOf(sharedPrefs.getInt("passwordScreenType",0))}
    var selectedOption by remember{mutableStateOf(2)}
    //doc: pinScreenType 0-create 1-confirm 2-enter
    var pinScreenType by remember{mutableStateOf(sharedPrefs.getInt("pinScreenType",1))} //previously pinButtonType
    //passwordValue THE password
    val passwordValue=sharedPrefs.getString("passwordValue","")

    var failedAttemptsCount=sharedPrefs.getInt("failedAttempts",0)

    val setupDone=sharedPrefs.getBoolean("setupDone",false)

    val recoverySet=sharedPrefs.getBoolean("recoverySet",false)

    LaunchedEffect(passwordScreenType){

    }
    var showPasswordPressed by remember{mutableStateOf(false)}
    var enteredPassword by remember{mutableStateOf("")}
    var enteredRepeatedPassword by remember{mutableStateOf("")}
    var showError by remember{mutableStateOf("")}

    val differentPasswordsError=getLocal(id=R.string.passwords_different_error)
    val tooShortPasswordError=getLocal(id=R.string.password_length_error)
    val wrongPasswordError=getLocal(id=R.string.wrong_password)
    val wrong3TimesError=getLocal(id=R.string.wrong_password_wait)
    val wrongPinLengthError=getLocal(id=R.string.pin_length_error)
    val differentPinsError=getLocal(id=R.string.pins_different_error)
    
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
                            sharedPrefs.edit().putBoolean("setupDone",true).apply()
                            onNavigate("MainScreen")
                            sharedPrefs.edit().putInt("passwordScreenType",passwordScreenType).apply()
                        }
                    }
                    sharedPrefs.edit().putInt("passwordScreenType",passwordScreenType).apply()
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
                                    sharedPrefs.edit().putInt("failedAttempts",failedAttemptsCount).apply()
                                    onNavigate("MainScreen")
                                }
                                else{
                                    showError=wrongPasswordError
                                    failedAttemptsCount++
                                    sharedPrefs.edit().putInt("failedAttempts",failedAttemptsCount).apply()
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
                                            onNavigate("RecoveryScreen")
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
                                        sharedPrefs.edit().putString("passwordValue",enteredPassword).apply()
                                        if(recoverySet)
                                            onNavigate("MainScreen")
                                        else
                                            onNavigate("RecoveryScreen")
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
                                    sharedPrefs.edit().putInt("failedAttempts",failedAttemptsCount).apply()
                                    onNavigate("MainScreen")
                                }
                                else{
                                    failedAttemptsCount++
                                    sharedPrefs.edit().putInt("failedAttempts",failedAttemptsCount).apply()
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
                                            onNavigate("RecoveryScreen")
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
                                        showError="Something went wrong sorry" //todo create string resource
                                        digitsEntered=0
                                        enteredPin=""
                                    }
                                    else{
                                        pinScreenType=2
                                        sharedPrefs.edit().putInt("pinScreenType",pinScreenType).apply()
                                        val pinToSave=enteredPin
                                        sharedPrefs.edit().putString("tempPin",pinToSave).apply()
                                        digitsEntered=0
                                        enteredPin=""
                                    }
                                }


                            }
                            else if(pinScreenType==2){
                                val tempPin=sharedPrefs.getString("tempPin","")
                                if(enteredPin==tempPin){
                                    sharedPrefs.edit().putInt("pinScreenType",0).apply()
                                    sharedPrefs.edit().putString("tempPin","").apply()
                                    sharedPrefs.edit().putString("passwordValue",enteredPin).apply()
                                    if(recoverySet)
                                        onNavigate("MainScreen")
                                    else
                                        onNavigate("RecoveryScreen")
                                }
                                else{
                                    showError=differentPinsError
                                    pinScreenType=1
                                    sharedPrefs.edit().putInt("pinScreenType",pinScreenType).apply()
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
fun IntroScreen(onNavigate:(String)->Unit){
    BetterText(text="Intro Screen")


}

@Composable
fun RecoveryScreen(onNavigate:(String)->Unit){
    val context=LocalContext.current
    val sharedPrefs=context.getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)
    val recoverySet=sharedPrefs.getBoolean("recoverySet",false)
    val questionsOptions=stringArrayResource(id=R.array.recoveryQuestions)
    var menuExpanded by remember{mutableStateOf(arrayOf(false,false,false))}
    var menuText by remember{mutableStateOf(arrayOf(questionsOptions[0],questionsOptions[0],questionsOptions[0]))}
    
    val noQuestionError=stringResource(id=R.string.recovery_noquestion_error)
    val savedQuestions=arrayOf(
        sharedPrefs.getString("rQuestion1",noQuestionError)!!,
        sharedPrefs.getString("rQuestion2",noQuestionError)!!,
        sharedPrefs.getString("rQuestion3",noQuestionError)!!
    ) //todo display questions lmao
    val correctAnswers=arrayOf(
        sharedPrefs.getString("rAnswer1",""),
        sharedPrefs.getString("rAnswer2",""),
        sharedPrefs.getString("rAnswer3","")
    )
    var showError by remember{mutableStateOf("")}
    var enteredAnswers by remember{mutableStateOf(arrayOf("","",""))}

    val emptyQuestionsError=stringResource(id=R.string.recovery_answer_all)
    val repeatingQuestionsError=stringResource(id=R.string.recovery_questions_repeat)
    val noQuestionChosenError=stringResource(id=R.string.recovery_choose_all)
    val emptyAnswerError=stringResource(id=R.string.recovery_answer_empty)
    val wrongAnswerError=stringResource(id=R.string.recovery_wrong_answer)

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
                            .padding(top=20.dp, start=10.dp, end=10.dp)


                    ) //read questionsOptions
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
                                if(recoverySet) "Enter answer"//todo: STRING RES
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
                                        onNavigate("MainScreen")
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
                                        putString("rQuestion1",menuText[0])
                                        putString("rQuestion2",menuText[1])
                                        putString("rQuestion3",menuText[2])
                                        putString("rAnswer1",enteredAnswers[0])
                                        putString("rAnswer2",enteredAnswers[1])
                                        putString("rAnswer3",enteredAnswers[2])
                                        putBoolean("recoverySet",true)
                                        apply()
                                    }
                                    onNavigate("MainScreen")
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
        //create
}



fun toggleStealthMode(context:Context){
    val packageManager=context.packageManager
    val stealth=ComponentName(context,"com.example.rainbowcalendar.MainActivityStealth")
    val default=ComponentName(context,"com.example.rainbowcalendar.MainActivity")

    val stealthMode=packageManager.getComponentEnabledSetting(stealth)==PackageManager.COMPONENT_ENABLED_STATE_ENABLED
    packageManager.setComponentEnabledSetting((if(stealthMode) default else stealth),PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP)
    packageManager.setComponentEnabledSetting((if(stealthMode) stealth else default),PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP)
}

@Composable
fun BetterButton(
    onClick:()->Unit,
    modifier:Modifier=Modifier,
    enabled:Boolean=true,
    content:@Composable RowScope.()->Unit
){
    Button(
        onClick=onClick,
        modifier=modifier,
        enabled=enabled,
        colors=ButtonDefaults.buttonColors(backgroundColor=colorTertiary()),
        shape=RectangleShape,
        content=content,
        contentPadding=PaddingValues(0.dp),
        )
}

@Composable
fun BetterText(
    text:String,
    modifier:Modifier=Modifier,
    textAlign:TextAlign=TextAlign.Start,
    fontSize:TextUnit=16.sp,
){
    Text(
        fontSize=fontSize,
        text=text,
        textAlign=textAlign,
        modifier=modifier,
        letterSpacing=0.sp,
        color=colorSecondary()
    )
}

private fun createNotificationChannel(context: Context){
    val channel=NotificationChannel("hrt","Hrt reminders", NotificationManager.IMPORTANCE_HIGH).apply{
        description="This channel is for hrt reminders"
    }

    val notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
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
    //val colorPrimary=getColor(color=com.google.android.material.R.attr.colorPrimary)
    //val colorTertiary=getColor(color=com.google.android.material.R.attr.colorTertiary)
    //val colorSecondary=getColor(color=com.google.android.material.R.attr.colorSecondary)
    val sharedPrefs=context.getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)

    val languages=listOf(
        LanguageData(R.drawable.flag_lang_en,getLocal(id=R.string.langEnglish),getLocal(id=R.string.langEnglishLocal)),
        LanguageData(R.drawable.flag_pl,getLocal(id=R.string.langPolish),getLocal(id=R.string.langPolishLocal)),
        LanguageData(R.drawable.flag_fr,getLocal(id=R.string.langFrench),getLocal(id=R.string.langFrenchLocal)),
        LanguageData(R.drawable.flag_br,getLocal(id=R.string.langPortugueseBR),getLocal(id=R.string.langPortugueseBRLocal)),
        LanguageData(R.drawable.flag_ru,getLocal(id=R.string.langRussian),getLocal(id=R.string.langRussianLocal)),
        LanguageData(R.drawable.flag_ua,getLocal(id=R.string.langUkrainian),getLocal(id=R.string.langUkrainianLocal))
    )
    val selectedLanguage=remember{mutableStateOf<String?>(Utils.codeToLanguage(sharedPrefs.getString("lang","en")!!))}
    val languagesState=remember{mutableStateOf(languages)}
    val setupDone=sharedPrefs.getBoolean("setup",false)
    val languageSetupDone=sharedPrefs.getBoolean("langSetup",false)

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
                            if(!languageSetupDone) sharedPrefs.edit().putBoolean("langSetup",true).apply()
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
    ThemesData("Purple",R.drawable.purple,false)
)

@Composable
fun ThemesSettings(){
    //val colorPrimary=getColor(color=com.google.android.material.R.attr.colorPrimary)
    //val colorSecondary=getColor(color=com.google.android.material.R.attr.colorSecondary)
    val sharedPrefs=LocalContext.current.getSharedPreferences("com.example.rainbowcalendar_pref",Context.MODE_PRIVATE)
    val theme=sharedPrefs.getString("theme","Gray")

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
    val sharedPrefs=LocalContext.current.getSharedPreferences("com.example.rainbowcalendar_pref",Context.MODE_PRIVATE)
    val themeSetupDone=sharedPrefs.getBoolean("themeSetup",false)
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
                if(!themeSetupDone) sharedPrefs.edit().putBoolean("themeSetup",true).apply()
                isSelected=!isSelected
                if(isSelected){
                    sharedPrefs.edit().putString("theme",name).apply()
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