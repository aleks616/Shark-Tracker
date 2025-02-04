package com.example.rainbowcalendar

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/*data class LanguageData(
    val flag: Int,
    val name: String, //name in current language
    val nameLocal: String //name of this language in the language
)*/
/*
@Composable
fun LanguageMenu(){
    val context=LocalContext.current
    val colorTertiary=getColor(color=com.google.android.material.R.attr.colorTertiary)
    val colorSecondary=getColor(color=com.google.android.material.R.attr.colorSecondary)
    val sharedPrefs=context.getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)

    val languages=listOf(
        LanguageData(R.drawable.glag_lang_en,getLocal(id=R.string.langEnglish),getLocal(id=R.string.langEnglishLocal)),
        LanguageData(R.drawable.flag_pl,getLocal(id=R.string.langPolish),getLocal(id=R.string.langPolishLocal)),
        LanguageData(R.drawable.flag_fr,getLocal(id=R.string.langFrench),getLocal(id=R.string.langFrenchLocal)),
        LanguageData(R.drawable.flag_br,getLocal(id=R.string.langPortugueseBR),getLocal(id=R.string.langPortugueseBRLocal)),
        LanguageData(R.drawable.flag_ru,getLocal(id=R.string.langRussian),getLocal(id=R.string.langRussianLocal)),
        LanguageData(R.drawable.flag_ua,getLocal(id=R.string.langUkrainian),getLocal(id=R.string.langUkrainianLocal))
    )
    val selectedLanguage=remember{mutableStateOf<String?>(Utils.codeToLanguage(sharedPrefs.getString("lang","en")!!))}
    val languagesState=remember{mutableStateOf(languages)}


    Column(modifier=Modifier.fillMaxWidth(1f)){
        Text(
            color=colorSecondary,
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
            item(){
                Box(
                    modifier=Modifier.fillMaxWidth().padding(top=25.dp),
                    contentAlignment=Alignment.Center
                ){
                    Button(
                        colors=ButtonDefaults.buttonColors(backgroundColor=colorTertiary),
                        onClick={Utils.changeLanguage(Utils.langToCodeNew(selectedLanguage.value!!),context)},
                        modifier=Modifier.width(200.dp).height(45.dp)
                        ){
                        Text(
                            text=getLocal(id=R.string.save),
                            color=colorSecondary,
                            fontSize=20.sp
                        )
                    }
                }
            }
        }
    }

}*/

/*@Composable
fun languageItem(flag: Int, name:String, nameLocal:String, isSelected:Boolean, onClick:()->Unit){
    val colorSecondary=getColor(color=com.google.android.material.R.attr.colorSecondary)
    val colorTertiary=getColor(color=com.google.android.material.R.attr.colorTertiary)
    Row(
        modifier=Modifier
            .padding(horizontal=10.dp,vertical=5.dp)
            .border(
                width=2.dp,shape=CircleShape,
                color=if(isSelected) colorSecondary else colorTertiary
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
            color=colorSecondary,
            modifier=Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal=5.dp)
        )
        Text(
            text=nameLocal,
            fontSize=16.sp,
            color=colorSecondary,
            textAlign=TextAlign.End,
            modifier=Modifier
                .fillMaxWidth(1f)
                .align(Alignment.CenterVertically)
                .padding(horizontal=14.dp)
                .alpha(0.8f)
        )
    }
}*/
