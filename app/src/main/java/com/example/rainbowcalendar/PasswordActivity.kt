package com.example.rainbowcalendar
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class PasswordActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        //region initializing
        val passwordLayout=findViewById<LinearLayout>(R.id.passwordL)
        val passwordHeader=findViewById<TextView>(R.id.passwordText)
        val eyeShowPassword=findViewById<Button>(R.id.eyeShowPassword)
        val passwordT=findViewById<EditText>(R.id.password)
        val passwordEnterButton=findViewById<Button>(R.id.passwordEnterButton)
        val confirmPassword=findViewById<EditText>(R.id.passwordConfirm)
        val createPasswordButton=findViewById<Button>(R.id.buttonCreatePassword)
        val repeatPasswordHeader=findViewById<TextView>(R.id.repeatPasswordHeader)

        val pinCodeLayout=findViewById<LinearLayout>(R.id.pinCodeL)

        val pinDigitsLayout=findViewById<LinearLayout>(R.id.pinDigitsL)
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

        //endregion

        val sharedPrefPasswordType=applicationContext.getSharedPreferences("com.example.rainbowcalendar_passwordType", Context.MODE_PRIVATE)
        val passwordType=sharedPrefPasswordType.getInt("com.example.rainbowcalendar_passwordType", 1) //todo change 1 to 0
        //doc:
        // 0 - no password,
        // 1 - text password,
        // 2 - pin
        // 3 - finger???
        when(passwordType){
            1->{ //text
                passwordLayout.visibility=View.VISIBLE
                pinCodeLayout.visibility=View.GONE
            }
            2->{ //pin
                passwordLayout.visibility=View.GONE
                pinCodeLayout.visibility=View.VISIBLE
            }
        }
        val sharedPrefPasswordText=applicationContext.getSharedPreferences("com.example.rainbowcalendar_passwordtext",Context.MODE_PRIVATE)
        val passwordValue=sharedPrefPasswordText.getString("com.example.rainbowcalendar_passwordtext","")

        //region text password
        //doc: show password when tapping eye
        eyeShowPassword.setOnTouchListener{_, event->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    if(passwordT.text.isNotEmpty()){
                        eyeShowPassword.setBackgroundResource(R.drawable.icon_eye)
                        passwordT.inputType=InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    eyeShowPassword.setBackgroundResource(R.drawable.icon_eye_closed)
                    passwordT.inputType=InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    true
                }
                else -> false
            }
        }

        //doc: password handling
        val enteredPassword=passwordT.text.toString()
        passwordEnterButton.setOnClickListener {
            if (enteredPassword==passwordValue){
                errorText.text=""
                startActivity(Intent(this, MainActivity::class.java))
            }
            else
                errorText.text="wrong password"
        }
        //doc: switching version of activity
        if(passwordValue.isNullOrEmpty()&&passwordType==1){
            eyeShowPassword.visibility=View.INVISIBLE
            passwordHeader.text="Create a password"
            passwordEnterButton.visibility=View.GONE
            confirmPassword.visibility=View.VISIBLE
            createPasswordButton.visibility=View.VISIBLE
            repeatPasswordHeader.visibility=View.VISIBLE
        }
        else{
            eyeShowPassword.visibility=View.VISIBLE
            passwordHeader.text=getString(R.string.enter_password)
            passwordEnterButton.visibility=View.VISIBLE
            confirmPassword.visibility=View.GONE
            createPasswordButton.visibility=View.GONE
            repeatPasswordHeader.visibility=View.GONE
        }

        //doc: creating passwords with error handling
        createPasswordButton.setOnClickListener {
            if(passwordT.text.toString()!=confirmPassword.text.toString())
                errorText.text="Passwords aren't the same"
            else{
                errorText.text=""
                if(passwordT.text.length<6)
                    errorText.text="This is for your safety, password has to be at least 6 characters."
                else{
                    errorText.text=""
                    if(isNumeric(passwordT.text.toString()))
                        errorText.text="Oh come on, if you want a digit only password, set a pin instead"
                    else{
                        errorText.text=""
                        //handle
                        with(sharedPrefPasswordText.edit()){
                            putString("com.example.rainbowcalendar_passwordtext",passwordValue)
                            apply()
                        }
                        Toast.makeText(this@PasswordActivity, "password saved",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
            }
        }

        //endregion
        //todo: fix password not actually saving into sharedpref

        //region pin
        var digitsEntered=0
        var pin=""
        val handler = Handler(Looper.getMainLooper())
        val setDigitsIndicators= Runnable{
            when(digitsEntered){
                0->{ pinDigit1.setBackgroundResource(R.drawable.rounded_button_filled) }
                1->{ pinDigit2.setBackgroundResource(R.drawable.rounded_button_filled) }
                2->{ pinDigit3.setBackgroundResource(R.drawable.rounded_button_filled) }
                3->{ pinDigit4.setBackgroundResource(R.drawable.rounded_button_filled) }
            }
            digitsEntered++
            Toast.makeText(this@PasswordActivity, pin, Toast.LENGTH_SHORT).show()
        }
        val removeDigitsIndicators= Runnable {
            when(digitsEntered){
                4->{pinDigit4.setBackgroundResource(R.drawable.rounded_button)}
                3->{pinDigit3.setBackgroundResource(R.drawable.rounded_button)}
                2->{pinDigit2.setBackgroundResource(R.drawable.rounded_button)}
                1->{pinDigit1.setBackgroundResource(R.drawable.rounded_button)}
            }
            digitsEntered--
        }
        //region pin buttons
        pinButton1.setOnClickListener{
            handler.post(setDigitsIndicators)
            pin+="1"
        }
        pinButton2.setOnClickListener{
            handler.post(setDigitsIndicators)
            pin+="2"
        }
        pinButton3.setOnClickListener{
            handler.post(setDigitsIndicators)
            pin+="3"
        }
        pinButton4.setOnClickListener{
            handler.post(setDigitsIndicators)
            pin+="4"
        }
        pinButton5.setOnClickListener{
            handler.post(setDigitsIndicators)
            pin+="5"
        }
        pinButton6.setOnClickListener{
            handler.post(setDigitsIndicators)
            pin+="6"
        }
        pinButton7.setOnClickListener{
            handler.post(setDigitsIndicators)
            pin+="7"
        }
        pinButton8.setOnClickListener{
            handler.post(setDigitsIndicators)
            pin+="8"
        }
        pinButton9.setOnClickListener{
            handler.post(setDigitsIndicators)
            pin+="9"
        }
        pinButton0.setOnClickListener{
            handler.post(setDigitsIndicators)
            pin+="0"
        }
        //endregion

        pinDelButton.setOnClickListener {
            pin=pin.dropLast(1)
            handler.post(removeDigitsIndicators)
        }

        pinEnter.setOnClickListener {
            if(pin.length!=4){
                errorText.text= getString(R.string.pin_length_error)
            }
            //todo: handle correct pin and creating it
        }
        //endregion
        //todo: normal pin handling + creating pin (show pin two times??? idfk lmao)

    }
    private fun isNumeric(word:String):Boolean{
        return word.all{char->char.isDigit()}
    }
}














