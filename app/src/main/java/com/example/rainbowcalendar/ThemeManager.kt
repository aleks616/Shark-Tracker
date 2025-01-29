package com.example.rainbowcalendar

import android.content.Context

internal object ThemeManager{
    operator fun set(context:Context,theme:String?) {
        var themeRecourseID:Int=R.style.Base_Theme_RainbowCalendar
        when(theme){
            "Beige"-> themeRecourseID=R.style.Beige_Theme_RainbowCalendar
            "Light"-> themeRecourseID=R.style.Base_Theme_RainbowCalendar
            "Dark"->themeRecourseID=R.style.Dark_Theme_RainbowCalendar
            "Monochrome-Light"->themeRecourseID=R.style.Monochrome_White_RainbowCalendar
            "Monochrome-Dark"->themeRecourseID=R.style.Monochrome_Black_RainbowCalendar
            "Blue"->themeRecourseID=R.style.Blue_RainbowCalendar
            "Navy Blue"->themeRecourseID=R.style.Navy_Blue_RainbowCalendar
            "Gray"->themeRecourseID=R.style.Gray_RainbowCalendar
            "Green"->themeRecourseID=R.style.Green_RainbowCalendar
        }

        context.setTheme(themeRecourseID)
    }
}