package com.example.rainbowcalendar

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView

class Utils : Activity() {
    /**
     * Checks if password and recovery questions are set, shows or hides popup message accordingly
     * @author aleks
     * @param context
     *
     * */
    //todo: i think this is useless? idk
   /* fun popup(context: Context){
        val sharedPrefs=context.getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)
        val popUpText=findViewById<TextView>(R.id.popUpText)
        val popUpL=findViewById<LinearLayout>(R.id.popUp)
        //val popUpSpace=findViewById<Space>(R.id.popUpSpace)
        val passwordSet=sharedPrefs.getString("passwordValue","")!=""
        val sharedPrefRecovery=getSharedPreferences("com.example.rainbowcalendar_recovery", Context.MODE_PRIVATE)
        val recoverySet=sharedPrefRecovery.getBoolean("done",false)
        val goToActivity=findViewById<Button>(R.id.goToActivityButton)
        val buttonClosePopup=findViewById<Button>(R.id.buttonClosePopup)

        if(!passwordSet){
            popUpL.visibility= View.VISIBLE
            popUpText.text="Set up a password!"
            //popUpSpace.visibility= View.VISIBLE
        }
        else if(!recoverySet){
            popUpL.visibility= View.VISIBLE
            popUpText.text="Make recovery questions!"
            //popUpSpace.visibility= View.VISIBLE
        }
        else{
            popUpL.visibility= View.GONE
            //popUpSpace.visibility= View.GONE
        }
        goToActivity.setOnClickListener {
            if(!passwordSet)
                startActivity(Intent(this, PasswordActivity::class.java))
            else if(!recoverySet)
                startActivity(Intent(this, RecoveryActivity::class.java))
        }
        buttonClosePopup.setOnClickListener {
            popUpL.visibility= View.GONE
            //popUpSpace.visibility= View.GONE
        }
    }*/
}