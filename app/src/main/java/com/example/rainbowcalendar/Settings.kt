package com.example.rainbowcalendar

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.contentcapture.ContentCaptureManager
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


//region language settings
data class LanguageData(
    val flag: Int,
    val name: String, //name in current language
    val nameLocal: String //name of this language in the language
)

@Composable
fun LanguageMenu(){
    val context=LocalContext.current
    val sharedPrefs=context.getSharedPreferences(Constants.key_package, Context.MODE_PRIVATE)

    val languages=listOf(
        LanguageData(R.drawable.flag_lang_en,stringResource(id=R.string.langEnglish),stringResource(id=R.string.langEnglishLocal)),
        LanguageData(R.drawable.flag_pl,stringResource(id=R.string.langPolish),stringResource(id=R.string.langPolishLocal)),
        LanguageData(R.drawable.flag_fr,stringResource(id=R.string.langFrench),stringResource(id=R.string.langFrenchLocal)),
        LanguageData(R.drawable.flag_br,stringResource(id=R.string.langPortugueseBR),stringResource(id=R.string.langPortugueseBRLocal)),
        LanguageData(R.drawable.flag_ru,stringResource(id=R.string.langRussian),stringResource(id=R.string.langRussianLocal)),
        LanguageData(R.drawable.flag_ua,stringResource(id=R.string.langUkrainian),stringResource(id=R.string.langUkrainianLocal))
    )
    val selectedLanguage=remember{mutableStateOf<String?>(Utils.codeToLanguage(sharedPrefs.getString(Constants.key_language,"en")!!))}
    val languagesState=remember{mutableStateOf(languages)}
    //val setupDone=sharedPrefs.getBoolean(Constants.key_isSetupDone,false)
    val languageSetupDone=sharedPrefs.getBoolean(Constants.key_isLanguageSetUp,false)

    LazyColumn(modifier=Modifier
        .fillMaxWidth(1f)
        .background(color=colorPrimary())){
        item{
            Text(
                color=colorSecondary(),
                text=stringResource(id=R.string.language),
                fontSize=38.sp,
                fontWeight=FontWeight.SemiBold,
                textAlign=TextAlign.Center,
                modifier=Modifier.padding(vertical=15.dp,horizontal=10.dp)
                // .align(Alignment.CenterHorizontally)
            )
        }

        items(languagesState.value, key={it.nameLocal}){lang ->
            languageItem(flag=lang.flag, name=lang.name, nameLocal=lang.nameLocal,
                isSelected=lang.nameLocal==selectedLanguage.value,
                onClick={selectedLanguage.value=lang.nameLocal}
            )
        }
        item{
            Box(
                modifier=Modifier.fillMaxWidth().padding(top=25.dp),
                contentAlignment=Alignment.Center
            ){
                BetterButton(
                    onClick={
                        if(!languageSetupDone) sharedPrefs.edit().putBoolean(Constants.key_isLanguageSetUp,true).apply()
                        Utils.changeLanguage(Utils.langToCodeNew(selectedLanguage.value!!),context)
                    },
                    modifier=Modifier.width(200.dp).height(45.dp)
                ){
                    Text(
                        text=stringResource(id=R.string.save),
                        color=colorSecondary(),
                        fontSize=20.sp
                    )
                }
            }
        }

    }

}

@Composable
fun languageItem(flag: Int, name:String, nameLocal:String, isSelected:Boolean, onClick:()->Unit){
    Row(
        modifier=Modifier
            .padding(horizontal=10.dp,vertical=5.dp)
            .border(
                width=2.dp,shape=CircleShape,
                color=if(isSelected) colorSecondary() else colorTertiary()
            )
            .fillMaxSize(1f)
            .heightIn(min=50.dp)
            .clickable{onClick()}
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
            modifier=Modifier.align(Alignment.CenterVertically).padding(horizontal=5.dp)
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
    ThemesData("Purple",R.drawable.purple,false),
    ThemesData("Pride",R.drawable.pride100, false)
)

@Composable
fun ThemesSettings(onNavigate:(String)->Unit,thisScreen:String?){
    val context=LocalContext.current
    val sharedPrefs=context.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
    val theme=sharedPrefs.getString(Constants.key_theme,"Gray")

    for(tm in themes){
        tm.selected=tm.name==theme
    }

    val themeRow=themes.chunked(2)
    LazyColumn(modifier=Modifier.fillMaxWidth().background(colorPrimary())
    ){
        val prevScreen=Utils.getPreviousScreen(thisScreen,context)
        val previousKey=Utils.previousScreenKey(prevScreen)
        if(thisScreen!=null&&previousKey!=""){
            if(previousKey==Constants.key_gender)
                item{GoBackButtonRow(onClick={sharedPrefs.edit().putInt(Utils.previousScreenKey(prevScreen),0).apply();onNavigate(prevScreen)})}
            else
                item{GoBackButtonRow(onClick={sharedPrefs.edit().putBoolean(Utils.previousScreenKey(prevScreen),false).apply();onNavigate(prevScreen)})}
        }
        item{
            Text(
                color=colorSecondary(),
                text=stringResource(R.string.chooseAppTheme),
                fontSize=44.sp,
                fontWeight=FontWeight.SemiBold,
                modifier=Modifier.padding(top=30.dp,bottom=10.dp)
            )
        }
        themeRow.forEach{row->
            item{
                Column(
                    modifier=Modifier.fillMaxWidth().heightIn(max=300.dp)
                ){
                    Row(
                        modifier=Modifier.fillMaxWidth().weight(1f),
                    ){
                        row.forEach{item->
                            Box(modifier=Modifier.weight(1f)){
                                themeItem2(name=item.name,imgResource=item.img,onNavigate=onNavigate,isSelected=item.selected)
                            }
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun themeItem2(name: String, imgResource:Int,onNavigate:(String)->Unit,isSelected:Boolean=false){
    val context=LocalContext.current as Activity
    val sharedPrefs=LocalContext.current.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
    val themeSetupDone=sharedPrefs.getBoolean(Constants.key_isThemeSetUp,false)

    val selected=remember{mutableStateOf(false)}
    val scale=animateFloatAsState(if(selected.value) 1.1f else 1f,label="")

    val onClick={if(!themeSetupDone) sharedPrefs.edit().putBoolean(Constants.key_isThemeSetUp,true).apply()
        sharedPrefs.edit().putString(Constants.key_theme,name).apply()
        Handler(Looper.getMainLooper()).postDelayed({
            onNavigate(Screens.sMain)
            context.recreate()
        },100)}
    Column(
        modifier=Modifier
            .fillMaxWidth()
            .padding(vertical=10.dp)
            .clickable{onClick()}
            .pointerInteropFilter{
                if(ContentCaptureManager.isEnabled){
                    when(it.action){
                        MotionEvent.ACTION_DOWN->{
                            selected.value=true
                        }

                        MotionEvent.ACTION_UP->{
                            selected.value=false
                            onClick()
                        }

                        MotionEvent.ACTION_CANCEL->{
                            selected.value=false
                        }
                    }
                }
                else selected.value=false
                true
            }
            .scale(scale.value)
    ){
        Image(
            painterResource(id=imgResource),
            contentDescription=name,
            modifier=Modifier
                .fillMaxSize()
                .border(
                    width=if(isSelected) 3.dp else 0.dp,
                    color=if(isSelected) colorSecondary() else Color.Transparent
                )
        )
    }

}
//endregion
