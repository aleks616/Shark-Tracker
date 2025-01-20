package com.example.rainbowcalendar

import android.content.Context

internal object ThemeManager{
    operator fun set(context:Context,theme:String?) {
        var themeRecourseID:Int=R.style.Base_Theme_RainbowCalendar
        when(theme){
            "beige"-> themeRecourseID=R.style.Beige_Theme_RainbowCalendar
            "light"-> themeRecourseID=R.style.Base_Theme_RainbowCalendar
            "dark"->themeRecourseID=R.style.Dark_Theme_RainbowCalendar
        }

        context.setTheme(themeRecourseID)
    }
}