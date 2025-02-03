package com.example.rainbowcalendar
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast

class PasswordActivity : AppCompatActivity(){
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?){
        val sharedPrefs=applicationContext.getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)
        val theme=sharedPrefs.getString("theme","Light")
        ThemeManager[this]=theme
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        //region initializing
        val passwordLayout=findViewById<LinearLayout>(R.id.passwordL)
        val passwordHeader=findViewById<TextView>(R.id.passwordText)

        val passwordTypeLayout=findViewById<LinearLayout>(R.id.passwordTypeL)
        val rbPasswordText=findViewById<RadioButton>(R.id.radioButtonText)
        val rbPasswordPin=findViewById<RadioButton>(R.id.radioButtonPinCode)
        val chooseTypeButton=findViewById<Button>(R.id.chooseType)


        // 0 -> normal
        // 1 -> create 1st
        // 2- > create 2nd
        val eyeShowPassword=findViewById<Button>(R.id.eyeShowPassword)
        val passwordT=findViewById<EditText>(R.id.password)
        val passwordEnterButton=findViewById<Button>(R.id.passwordEnterButton)
        val confirmPassword=findViewById<EditText>(R.id.passwordConfirm)
        val createPasswordButton=findViewById<Button>(R.id.buttonCreatePassword)
        val repeatPasswordHeader=findViewById<TextView>(R.id.repeatPasswordHeader)
        val createPasswordHeader2=findViewById<TextView>(R.id.createPasswordHeader2)

        val pinCodeLayout=findViewById<LinearLayout>(R.id.pinCodeL)
        val pinDigit1=findViewById<ImageView>(R.id.pinDigit1)
        val pinDigit2=findViewById<ImageView>(R.id.pinDigit2)
        val pinDigit3=findViewById<ImageView>(R.id.pinDigit3)
        val pinDigit4=findViewById<ImageView>(R.id.pinDigit4)

        val pinButton1=findViewById<Button>(R.id.pinButton1)
        val pinButton2=findViewById<Button>(R.id.pinButton2)
        val pinButton3=findViewById<Button>(R.id.pinButton3)
        val pinButton4=findViewById<Button>(R.id.pinButton4)
        val pinButton5=findViewById<Button>(R.id.pinButton5)
        val pinButton6=findViewById<Button>(R.id.pinButton6)
        val pinButton7=findViewById<Button>(R.id.pinButton7)
        val pinButton8=findViewById<Button>(R.id.pinButton8)
        val pinButton9=findViewById<Button>(R.id.pinButton9)
        val pinDelButton=findViewById<Button>(R.id.pinDel)
        val pinButton0=findViewById<Button>(R.id.pinButton0)

        val pinEnter=findViewById<Button>(R.id.pinEnter)

        val errorText=findViewById<TextView>(R.id.errorText)
        val pinMainText=findViewById<TextView>(R.id.pinMainText)
        val noPasswordCb=findViewById<CheckBox>(R.id.noPasswordCB)
        //pinMainText.text="Enter pin"

        pinDigit4.setBackgroundResource(R.drawable.rounded_button)
        pinDigit3.setBackgroundResource(R.drawable.rounded_button)
        pinDigit2.setBackgroundResource(R.drawable.rounded_button)
        pinDigit1.setBackgroundResource(R.drawable.rounded_button)
        //endregion

        //region password type


        //val sharedPrefPasswordType=applicationContext.getSharedPreferences("com.example.rainbowcalendar_passwordType", Context.MODE_PRIVATE)
        val sharedPrefTemp=applicationContext.getSharedPreferences("temp",Context.MODE_PRIVATE)
        var passwordType=0
        var pinButtonType=sharedPrefTemp.getInt("temp",0)

        chooseTypeButton.setOnClickListener{
            if(noPasswordCb.isChecked)
                startActivity(Intent(this,MainActivity::class.java))

            if(rbPasswordText.isChecked)
                passwordType=1
            else if(rbPasswordPin.isChecked){
                passwordType=2
                pinButtonType=1
            }
            /*with(sharedPrefPasswordType.edit()){
                putInt("com.example.rainbowcalendar_passwordType",passwordType)
                apply()
            }*/
            sharedPrefs.edit().putInt("passwordType",passwordType).apply()
            sharedPrefTemp.edit().putInt("temp",pinButtonType).apply()

            this.recreate()
        }

        when(pinButtonType) {
            1->pinMainText.text=getString(R.string.create_pin)
            2->pinMainText.text= getString(R.string.enter_pin_again)
            0->pinMainText.text=getString(R.string.enter_pin)
        }


        //passwordType=sharedPrefPasswordType.getInt("com.example.rainbowcalendar_passwordType", 0)
        passwordType=sharedPrefs.getInt("passwordType",0)

        when(passwordType){
            0->{//set type
                passwordTypeLayout.visibility=View.VISIBLE
                passwordLayout.visibility=View.GONE
                pinCodeLayout.visibility=View.GONE
            }
            1->{ //text
                passwordTypeLayout.visibility=View.GONE
                passwordLayout.visibility=View.VISIBLE
                pinCodeLayout.visibility=View.GONE
            }
            2->{ //pin
                passwordTypeLayout.visibility=View.GONE
                passwordLayout.visibility=View.GONE
                pinCodeLayout.visibility=View.VISIBLE
            }
        }

        //endregion
        val passwordValue=sharedPrefs.getString("passwordValue","")
        var failedAttemptsCount=sharedPrefs.getInt("failedAttempts",0)

        //region text password
        //show password when tapping eye
        eyeShowPassword.setOnTouchListener{_, event->
            when(event.action){
                MotionEvent.ACTION_DOWN ->{
                    if(passwordT.text.isNotEmpty()){
                        eyeShowPassword.setBackgroundResource(R.drawable.icon_eye)
                        passwordT.inputType=InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    }
                    true
                }
                MotionEvent.ACTION_UP ->{
                    eyeShowPassword.setBackgroundResource(R.drawable.icon_eye_closed)
                    passwordT.inputType=InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    true
                }
                else -> false
            }
        }
        val sharedPrefRecovery=applicationContext.getSharedPreferences("com.example.rainbowcalendar_recovery", Context.MODE_PRIVATE)
        val recoverySet=sharedPrefRecovery.getBoolean("done",false)
        var canExecute=true
        //password handling
        passwordEnterButton.setOnClickListener{
            if(passwordT.text.toString()==passwordValue){
                errorText.text=""
                startActivity(Intent(this, MainActivity::class.java))
            }
            else{
                errorText.text= getString(R.string.wrong_password)
                failedAttemptsCount++
                sharedPrefs.edit().putInt("failedAttempts",failedAttemptsCount).apply()
                if(failedAttemptsCount in 3..4){
                    errorText.text=getString(R.string.wrong_password_wait)
                    canExecute=false
                    Handler(Looper.getMainLooper()).postDelayed({
                        canExecute=true
                        errorText.text=""
                    },30*1000)
                }
                else if(failedAttemptsCount>4){
                    if(recoverySet)
                        startActivity(Intent(this, RecoveryActivity::class.java))
                }
            }

        }
        //switching version of activity
        if(passwordValue.isNullOrEmpty()&&passwordType==1){
            //password not created
            passwordHeader.text=getString(R.string.create_password)
            createPasswordHeader2.visibility=View.VISIBLE
            eyeShowPassword.visibility=View.INVISIBLE
            passwordEnterButton.visibility=View.GONE

            repeatPasswordHeader.visibility=View.VISIBLE
            confirmPassword.visibility=View.VISIBLE
            createPasswordButton.visibility=View.VISIBLE
        }
        else{
            passwordHeader.text=getString(R.string.enter_password)
            createPasswordHeader2.visibility=View.GONE
            eyeShowPassword.visibility=View.VISIBLE
            passwordEnterButton.visibility=View.VISIBLE

            repeatPasswordHeader.visibility=View.GONE
            confirmPassword.visibility=View.GONE
            createPasswordButton.visibility=View.GONE
        }

        //creating passwords with error handling
        createPasswordButton.setOnClickListener{
            if(passwordT.text.toString()!=confirmPassword.text.toString())
                errorText.text=getString(R.string.passwords_different_error)
            else{
                errorText.text=""
                if(passwordT.text.length<6)
                    errorText.text=getString(R.string.password_length_error)
                else{
                    errorText.text=""
                    if(isNumeric(passwordT.text.toString()))
                        errorText.text=getString(R.string.password_digit_error)
                    else{
                        errorText.text=""
                        sharedPrefs.edit().putString("passwordValue",passwordT.text.toString()).apply()
                        Toast.makeText(this@PasswordActivity, passwordT.text.toString(),Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
            }
        }
        passwordT.text.clear()
        confirmPassword.text.clear()

        //endregion


        //region pin
        //general functioning
        var digitsEntered=0
        var pin=""
        val handler=Handler(Looper.getMainLooper())
        val setDigitsIndicators=Runnable{
            if(canExecute){
                when(digitsEntered){
                    0->{pinDigit1.setBackgroundResource(R.drawable.rounded_button_filled) }
                    1->{pinDigit2.setBackgroundResource(R.drawable.rounded_button_filled) }
                    2->{pinDigit3.setBackgroundResource(R.drawable.rounded_button_filled) }
                    3->{pinDigit4.setBackgroundResource(R.drawable.rounded_button_filled) }
                }
                digitsEntered++
            }
        }
        val removeDigitsIndicators=Runnable{
            if(canExecute){
                when(digitsEntered){
                    4->{pinDigit4.setBackgroundResource(R.drawable.rounded_button)}
                    3->{pinDigit3.setBackgroundResource(R.drawable.rounded_button)}
                    2->{pinDigit2.setBackgroundResource(R.drawable.rounded_button)}
                    1->{pinDigit1.setBackgroundResource(R.drawable.rounded_button)}
                }
                if(digitsEntered>0)
                    digitsEntered--
            }
        }
        //region pin buttons
        pinButton1.setOnClickListener{
            handler.post(setDigitsIndicators)
            if(digitsEntered<4) pin+="1"
        }
        pinButton2.setOnClickListener{
            handler.post(setDigitsIndicators)
            if(digitsEntered<4) pin+="2"
        }
        pinButton3.setOnClickListener{
            handler.post(setDigitsIndicators)
            if(digitsEntered<4) pin+="3"
        }
        pinButton4.setOnClickListener{
            handler.post(setDigitsIndicators)
            if(digitsEntered<4) pin+="4"
        }
        pinButton5.setOnClickListener{
            handler.post(setDigitsIndicators)
            if(digitsEntered<4) pin+="5"
        }
        pinButton6.setOnClickListener{
            handler.post(setDigitsIndicators)
            if(digitsEntered<4) pin+="6"
        }
        pinButton7.setOnClickListener{
            handler.post(setDigitsIndicators)
            if(digitsEntered<4) pin+="7"
        }
        pinButton8.setOnClickListener{
            handler.post(setDigitsIndicators)
            if(digitsEntered<4) pin+="8"
        }
        pinButton9.setOnClickListener{
            handler.post(setDigitsIndicators)
            if(digitsEntered<4) pin+="9"
        }
        pinButton0.setOnClickListener{
            handler.post(setDigitsIndicators)
            if(digitsEntered<4) pin+="0"
        }
        //endregion

        pinDelButton.setOnClickListener{
            pin=pin.dropLast(1)
            handler.post(removeDigitsIndicators)
        }
        //on pin enter
        var pinToSave: String
        Log.v("GAY",pinButtonType.toString())
        pinEnter.setOnClickListener{
            if(!canExecute)
                return@setOnClickListener

            val pinToSave1=sharedPrefTemp.getString("temp1","")
            if(!pinToSave1.isNullOrEmpty())
                Log.w("gay read pin to save as ", pinToSave1)

            when(pinButtonType){
                0->{
                    //normal login
                    if(pin!=passwordValue){
                        failedAttemptsCount++
                        sharedPrefs.edit().putInt("failedAttempts",failedAttemptsCount).apply()
                        errorText.text=getString(R.string.wrong_pin_try_again)
                        pin=""
                        digitsEntered=0
                        pinDigit4.setBackgroundResource(R.drawable.rounded_button)
                        pinDigit3.setBackgroundResource(R.drawable.rounded_button)
                        pinDigit2.setBackgroundResource(R.drawable.rounded_button)
                        pinDigit1.setBackgroundResource(R.drawable.rounded_button)

                        if(failedAttemptsCount in 3..4){
                            errorText.text=getString(R.string.wrong_password_wait)
                            canExecute=false
                            Handler(Looper.getMainLooper()).postDelayed({
                                canExecute=true
                                errorText.text=""
                            },30*1000)
                        }
                        else if(failedAttemptsCount>4){
                            if(recoverySet)
                                startActivity(Intent(this, RecoveryActivity::class.java))
                        }
                    }
                    else{ //correct pin
                        failedAttemptsCount=0
                        sharedPrefs.edit().putInt("failedAttempts",failedAttemptsCount).apply()
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
                1->{
                    Toast.makeText(this@PasswordActivity, pin, Toast.LENGTH_SHORT).show()
                    //create pin
                    if(pin.length!=4){
                        errorText.text=getString(R.string.pin_length_error)
                        this.recreate()
                    }

                    else{
                        pinToSave=pin
                        Log.v("gay pin to save is ",pinToSave) //value ok here
                        sharedPrefTemp.edit().putString("temp1",pinToSave).apply()
                        Log.v("gay immediate read of pin to save in shared pref: ",sharedPrefTemp.getString("temp1","").toString())
                        pinMainText.text=getString(R.string.enter_pin_again)
                        pinButtonType=2
                        sharedPrefTemp.edit().putInt("temp",pinButtonType).apply()
                        pin=""
                        this.recreate()
                    }
                }
                2->{
                    //confirm pin
                    Log.w("gay pins", "pin to save: $pinToSave1 pin entered now: $pin")
                    if(pin!=pinToSave1){
                        errorText.text= getString(R.string.pins_different_error)
                        pinMainText.text=getString(R.string.create_pin)
                        pinButtonType=1
                        sharedPrefTemp.edit().putInt("temp",pinButtonType).apply()
                        pin=""
                        this.recreate()
                    }
                    else{
                        sharedPrefTemp.edit().putInt("temp",0).apply()
                        sharedPrefs.edit().putString("passwordValue",pinToSave1).apply()
                        if(recoverySet)
                            startActivity(Intent(this, MainActivity::class.java))
                        else
                            startActivity(Intent(this,RecoveryActivity::class.java))
                    }
                }
            }
        }
        //endregion


        //todo: **if pin/password wasn't created show choose type screen**

    }
    fun isNumeric(word:String):Boolean{
        return word.all{char->char.isDigit()}
    }
}














