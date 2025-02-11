package com.example.rainbowcalendar

import android.content.Context

internal object ThemeManager{
    operator fun set(context:Context,theme:String?){
        var themeRecourseID:Int=R.style.Base_Theme_RainbowCalendar
        when(theme){
            "White"->themeRecourseID=R.style.Base_Theme_RainbowCalendar
            "Gray"->themeRecourseID=R.style.Gray_RainbowCalendar
            "Black"->themeRecourseID=R.style.Black_RainbowCalendar
            "Blue"->themeRecourseID=R.style.Blue_RainbowCalendar
            "Navy Blue"->themeRecourseID=R.style.Navy_Blue_RainbowCalendar
            "Green"->themeRecourseID=R.style.Green_RainbowCalendar
            "Beige"-> themeRecourseID=R.style.Beige_Theme_RainbowCalendar
            "Dark Purple"->themeRecourseID=R.style.Dark_Purple_RainbowCalendar
            "Khaki"->themeRecourseID=R.style.Khaki_RainbowCalendar
            "Purple"->themeRecourseID=R.style.Light_Purple_RainbowCalendar
            "Pride"->themeRecourseID=R.style.Base_Theme_RainbowCalendar
            //light-deleted!
        }

        context.setTheme(themeRecourseID)
    }
    fun getThemeResId(theme:String?):Int{
        return when(theme) {
            "White"->R.style.Base_Theme_RainbowCalendar
            "Gray"->R.style.Gray_RainbowCalendar
            "Black"->R.style.Black_RainbowCalendar
            "Blue"->R.style.Blue_RainbowCalendar
            "Navy Blue"->R.style.Navy_Blue_RainbowCalendar
            "Green"->R.style.Green_RainbowCalendar
            "Beige"->R.style.Beige_Theme_RainbowCalendar
            "Dark Purple"->R.style.Dark_Purple_RainbowCalendar
            "Khaki"->R.style.Khaki_RainbowCalendar
            "Purple"->R.style.Light_Purple_RainbowCalendar
            else->R.style.Gray_RainbowCalendar
        }
    }
}