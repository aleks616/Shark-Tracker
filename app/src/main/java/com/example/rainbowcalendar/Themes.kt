package com.example.rainbowcalendar

import android.app.ListActivity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.RadioButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.paging.VERBOSE

/*
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
    val colorSecondary=getColor(color=com.google.android.material.R.attr.colorSecondary)
    val sharedPrefs=LocalContext.current.getSharedPreferences("com.example.rainbowcalendar_pref",Context.MODE_PRIVATE)
    val theme=sharedPrefs.getString("theme","Gray")

    for(tm in themes){
        tm.selected=tm.name==theme
    }

    val themesState=remember{mutableStateOf(themes)}
    Column(modifier=Modifier.fillMaxWidth(1f)){
        Text(
            color=colorSecondary,
            text="Choose theme",
            fontSize=44.sp,
            fontWeight=FontWeight.SemiBold,
            modifier=Modifier
                .padding(top=30.dp, bottom=10.dp)
                .align(Alignment.CenterHorizontally)
        )
        LazyRow{
            items(themesState.value, key={it.name}){theme ->
                themeItem(name=theme.name,imgResource=theme.img, theme.selected)
            }
        }
    }
}

@Composable
fun themeItem(name: String, imgResource:Int, selected:Boolean){
    val context=LocalContext.current
    val sharedPrefs=LocalContext.current.getSharedPreferences("com.example.rainbowcalendar_pref",Context.MODE_PRIVATE)
    val colorSecondary=getColor(color=com.google.android.material.R.attr.colorSecondary)
    val colorTertiary=getColor(color=com.google.android.material.R.attr.colorTertiary)
    var isSelected by remember {mutableStateOf(selected)}
    Column(
        horizontalAlignment=Alignment.CenterHorizontally,
        modifier=Modifier.padding(all=10.dp)
    ){
        Text(
            text=name,
            color=colorSecondary,
            fontSize=34.sp,
            fontWeight=FontWeight.SemiBold,
            modifier=Modifier.padding(vertical=20.dp)
        )
        Box(modifier=Modifier.heightIn(max=470.dp)){
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
            modifier=Modifier.scale(1.5f).padding(top=15.dp),
            colors=RadioButtonDefaults.colors(selectedColor=colorSecondary,unselectedColor=colorSecondary),
            onClick={
                isSelected=!isSelected
                if(isSelected){
                    sharedPrefs.edit().putString("theme",name).apply()
                    Handler(Looper.getMainLooper()).postDelayed({
                        context.startActivity(Intent(context,MainActivity::class.java))
                    }, 100)
                }
        })
    }
}*/
