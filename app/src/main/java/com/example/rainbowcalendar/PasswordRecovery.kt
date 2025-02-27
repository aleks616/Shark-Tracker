package com.example.rainbowcalendar

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PasswordScreen(onNavigate:(String)->Unit){
    val context=LocalContext.current
    val sharedPrefs=context.getSharedPreferences(Constants.key_package, Context.MODE_PRIVATE)
    /**0 - select 1 - text 2 - pin**/
    var passwordScreenType by remember{mutableStateOf(sharedPrefs.getInt(Constants.key_passwordScreenType,0))}
    var selectedOption by remember{mutableStateOf(2)}
    /**0-create 1-confirm 2-enter**/
    var pinScreenType by remember{mutableStateOf(sharedPrefs.getInt(Constants.key_pinScreenType,1))} //previously pinButtonType
    /**THE password**/
    val passwordValue=sharedPrefs.getString(Constants.key_passwordValue,"")

    var failedAttemptsCount=sharedPrefs.getInt(Constants.key_failedAttempts,0)
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
                                painter=if(showPasswordPressed) painterResource(id=R.drawable.icon_eye)
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
                        visualTransformation=if(!showPasswordPressed) PasswordVisualTransformation() else VisualTransformation.None,
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
                    //endregion
                }
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
                                    modifier=Modifier.padding(10.dp).width(100.dp).height(100.dp),
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
                            modifier=Modifier.padding(10.dp).width(100.dp).height(100.dp),
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
                    modifier=Modifier.fillMaxWidth().padding(horizontal=10.dp,vertical=15.dp).height(50.dp).border(width=2.dp,color=colorTertiary()),
                    onClick={menuExpanded=menuExpanded.copyOf().apply{this[field]=true}},
                    colors=ButtonDefaults.buttonColors(backgroundColor=colorPrimary()),
                ){
                    BetterText(text=menuText[field])
                    DropdownMenu(
                        expanded=menuExpanded[field],
                        onDismissRequest={menuExpanded[field]=false},
                        modifier=Modifier.background(colorPrimary()).fillMaxWidth().padding(horizontal=10.dp),){
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
                    modifier=Modifier.fillMaxWidth().padding(top=20.dp,start=10.dp,end=10.dp)
                ) //read questionsOptions //idk what that means anymore
            }
            TextField(
                value=enteredAnswers[field],
                onValueChange={enteredAnswers=enteredAnswers.copyOf().apply{this[field]=it}},
                modifier=Modifier.padding(horizontal=10.dp),
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
                        text=if(recoverySet) enterAnswer else menuText[field],
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
                modifier=Modifier.fillMaxWidth().padding(30.dp)){
                Image(
                    painter=painterResource(id=R.drawable.icon_checkmark_circle),
                    contentDescription=null,
                    modifier=Modifier.scale(3f)
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
