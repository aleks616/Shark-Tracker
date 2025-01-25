package com.example.rainbowcalendar

import android.content.Context
import android.util.Log
import android.util.TypedValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.sqrt

@Composable
fun getColor(color: Int): Color{
    return colorResource(LocalContext.current.getColorFromAttrs(color).resourceId)
}

fun Context.getColorFromAttrs(attr: Int): TypedValue{
    return TypedValue().apply{
        theme.resolveAttribute(attr,this,true)
    }
}

@Composable
fun getLocal(id: Int): String{
    return LocalContext.current.getString(id)
}

@Composable
fun createIcons():Map<String, List<Pair<Int, String>>>{
    return mapOf(
        "crampLevel" to listOf(R.drawable.cramps_0 to getLocal(R.string.metrics_crampLevel0),R.drawable.cramps_1 to getLocal(R.string.metrics_crampLevel1),R.drawable.cramps_2 to getLocal(R.string.metrics_crampLevel2)),
        "headache" to listOf(R.drawable.headache_n_0 to getLocal(R.string.metrics_headache0),R.drawable.headache_n_1 to getLocal(R.string.metrics_headache1),R.drawable.headache_n_2 to getLocal(R.string.metrics_headache2),R.drawable.headache_n_3 to getLocal(R.string.metrics_headache3),R.drawable.headache_n_4 to getLocal(R.string.metrics_headache4)),
        "energyLevel" to listOf(R.drawable.energy_0 to getLocal(R.string.metrics_energyLevel0),R.drawable.energy_1 to getLocal(R.string.metrics_energyLevel1),R.drawable.energy_2 to getLocal(R.string.metrics_energyLevel2),R.drawable.energy_3 to getLocal(R.string.metrics_energyLevel3),R.drawable.energy_4 to getLocal(R.string.metrics_energyLevel4)),
        "sleepQuality" to listOf(R.drawable.sleep_0 to getLocal(R.string.metrics_sleepQuality0), R.drawable.sleep_1 to getLocal(R.string.metrics_sleepQuality1), R.drawable.sleep_2 to getLocal(R.string.metrics_sleepQuality2)),
        "cravings" to listOf(R.drawable.cravings_0 to getLocal(R.string.metrics_cravings0), R.drawable.cravings_1 to getLocal(R.string.metrics_cravings1), R.drawable.cravings_2 to getLocal(R.string.metrics_cravings2)),
        "skinCondition" to listOf(R.drawable.acne_0 to getLocal(R.string.metrics_skinCondition0), R.drawable.acne_1  to getLocal(R.string.metrics_skinCondition1), R.drawable.acne_2 to getLocal(R.string.metrics_skinCondition2)),
        "digestiveIssues" to listOf(R.drawable.digestive_0 to getLocal(R.string.metrics_digestiveIssues0), R.drawable.digestive_1 to getLocal(R.string.metrics_digestiveIssues1), R.drawable.digestive_2 to getLocal(R.string.metrics_digestiveIssues2), R.drawable.digestive_3 to getLocal(R.string.metrics_digestiveIssues3)),
        "moodSwings" to listOf(R.drawable.mood_swings_0 to getLocal(R.string.metrics_moodSwings0), R.drawable.mood_swings_1 to  getLocal(R.string.metrics_moodSwings1), R.drawable.mood_swings_2 to  getLocal(R.string.metrics_moodSwings2)),
        "overallMood" to listOf(R.drawable.mood_0 to getLocal(R.string.metrics_overallMood0),R.drawable.mood_1 to getLocal(R.string.metrics_overallMood1),R.drawable.mood_2 to getLocal(R.string.metrics_overallMood2),R.drawable.mood_3 to getLocal(R.string.metrics_overallMood3),R.drawable.mood_4 to getLocal(R.string.metrics_overallMood4)),
        "dysphoria" to listOf(R.drawable.dysphoria_0 to getLocal(R.string.metrics_dysphoria0),R.drawable.dysphoria_1 to getLocal(R.string.metrics_dysphoria1),R.drawable.dysphoria_2 to getLocal(R.string.metrics_dysphoria2),R.drawable.dysphoria_3 to getLocal(R.string.metrics_dysphoria3),R.drawable.dysphoria_4 to getLocal(R.string.metrics_dysphoria4)),
        "bleeding" to listOf(R.drawable.blood_0 to getLocal(R.string.metrics_bleeding0),R.drawable.blood_1 to getLocal(R.string.metrics_bleeding1),R.drawable.blood_2 to getLocal(R.string.metrics_bleeding2),R.drawable.blood_3 to getLocal(R.string.metrics_bleeding3),R.drawable.blood_4 to getLocal(R.string.metrics_bleeding4)),
        "musclePain" to listOf(R.drawable.muscle_pain_0 to getLocal(R.string.metrics_musclePain0), R.drawable.muscle_pain_1 to getLocal(R.string.metrics_musclePain1), R.drawable.muscle_pain_2 to getLocal(R.string.metrics_musclePain2)),
        "customColumn1" to listOf(R.drawable.customa_0 to " ",R.drawable.customa_1 to " ",R.drawable.customa_2 to " ",R.drawable.customa_3 to " "),
        "customColumn2" to listOf(R.drawable.customb_0 to " ",R.drawable.customb_1 to " ",R.drawable.customb_2 to " ",R.drawable.customb_3 to " ",R.drawable.customb_4 to " "),
        "customColumn3" to listOf(R.drawable.customc_0 to " ",R.drawable.customc_1 to " ",R.drawable.customc_2 to " ",R.drawable.customc_3 to " ", R.drawable.customc_4 to " "),

        //kcalBalance
        //weight
        //notes
    )
}
var usedDateState=mutableStateOf(SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(Calendar.getInstance().time))
//var usedDate:String=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(Calendar.getInstance().time)
private lateinit var cycleDao:CycleDao
@Composable
fun ScrollableMetricsView(){
    val padding=12.dp
    val selectedPositions=remember{mutableStateOf(MutableList(15){-1})}
    val weight=remember{mutableStateOf("")}
    val kcalBalance=remember{mutableStateOf("")}
    val notes=remember{mutableStateOf("")}

    val sharedPrefs=LocalContext.current.getSharedPreferences("com.example.rainbowcalendar_pref",Context.MODE_PRIVATE)
    val lang=sharedPrefs.getString("lang","en")
    val appLocale:Locale=if(lang=="pt-br")
        Locale("pt","BR")
    else
        Locale(lang)
    //val appLocale=Locale("ru")

    val formatter=DateFormat.getDateInstance(DateFormat.LONG,appLocale)
    val calendar=Calendar.getInstance()
    val context=LocalContext.current
    var today=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(Calendar.getInstance().time)

    cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
    //val lifecycleOwner=LocalLifecycleOwner.current

    LaunchedEffect(usedDateState.value){
        withContext(Dispatchers.IO){
            //val cycle=cycleDao.getCycleByDate(today)
            val cycle=cycleDao.getCycleByDate(usedDateState.value)
            if(cycle!=null){
                selectedPositions.value=mutableListOf(
                    cycle.crampLevel,
                    cycle.headache,
                    cycle.energyLevel,
                    cycle.sleepQuality,
                    cycle.cravings,
                    cycle.skinCondition,
                    cycle.digestiveIssues,
                    cycle.moodSwings,
                    cycle.overallMood,
                    cycle.dysphoria,
                    cycle.bleeding,
                    cycle.musclePain,
                    cycle.customColumn1,
                    cycle.customColumn2,
                    cycle.customColumn3
                ).map {it ?: -1}.toMutableList()

                weight.value=(cycle.weight ?: "").toString()
                kcalBalance.value=(cycle.kcalBalance?:"").toString()
                notes.value=cycle.notes?:""
            }
            else{
                selectedPositions.value=List(15) {-1}.toMutableList()
                weight.value=""
                kcalBalance.value=""
                notes.value=""
            }
        }
    }
    //todo:
    // 1: actually reading custom name from sharedpref
    // 2: HARD showing/hiding and changing order of metrics
    LazyColumn(modifier=Modifier.fillMaxSize()){
        item{
            Box(
                contentAlignment=Alignment.Center,
                modifier=Modifier
                    .fillMaxSize()
                    .padding(bottom=20.dp)
            ) {
                Column(horizontalAlignment=Alignment.CenterHorizontally){
                    Text(
                        text="Metrics",
                        fontSize=35.sp,
                        textAlign=TextAlign.Center,
                        fontWeight=FontWeight.W500,
                        modifier=Modifier
                            .padding(vertical=10.dp)
                    )
                    Row(
                        modifier=Modifier.fillMaxWidth(),
                        verticalAlignment=Alignment.CenterVertically
                    ){
                        Button( //doc: left button
                            onClick={changeDate(amount=-1)},
                            Modifier
                                .height(45.dp)
                                .width(80.dp)
                                .align(Alignment.CenterVertically)
                                .padding(start=10.dp)
                        ){
                            Image(
                                modifier=Modifier
                                    .fillMaxSize()
                                    .scale(2.0f),
                                painter=painterResource(id=R.drawable.icon_arrow_left_triangle),
                                contentDescription=null,
                                contentScale=ContentScale.FillHeight,
                            )
                        }
                        Text(
                            //text=today,
                            text=usedDateState.value,
                            fontSize=32.sp,
                            textAlign=TextAlign.Center,
                            modifier=Modifier
                                .padding(vertical=10.dp)
                                .align(Alignment.CenterVertically)
                                .weight(1f)
                        )
                        Button(//doc: right button
                            onClick={changeDate(amount=1)},
                            Modifier
                                .height(45.dp)
                                .width(80.dp)
                                .align(Alignment.CenterVertically)
                                .padding(end=10.dp)
                                .alpha(if(usedDateState.value>=today)0f else 1f),
                            enabled=usedDateState.value<today
                        ){
                            Image(
                                modifier=Modifier
                                    .fillMaxSize()
                                    .scale(2.0f),
                                painter=painterResource(id=R.drawable.icon_arrow_right_triangle),
                                contentDescription=null,
                                contentScale=ContentScale.FillHeight,
                            )
                        }
                    }
                }
            }
        }

        //region metric items
        item{
            MetricRow(title=getLocal(id=R.string.metrics_crampLevelTitle),metricName="crampLevel",modifier=Modifier.padding(padding),selectedIndex1=selectedPositions.value[0],onSelectionChange={selectedPositions.value[0]=it})
        }
        item{
            MetricRow(title=getLocal(id=R.string.metrics_headacheTitle),metricName="headache",modifier=Modifier.padding(padding),selectedIndex1=selectedPositions.value[1],onSelectionChange={selectedPositions.value[1]=it})
        }
        item{
            MetricRow(title=getLocal(id=R.string.metrics_energyLevelTitle),metricName="energyLevel",modifier=Modifier.padding(padding),selectedIndex1=selectedPositions.value[2],onSelectionChange={selectedPositions.value[2]=it})
        }
        item{
            MetricRow(title=getLocal(id=R.string.metrics_SleepQualityTitle),metricName="sleepQuality",modifier=Modifier.padding(padding),selectedIndex1=selectedPositions.value[3],onSelectionChange={selectedPositions.value[3]=it})
        }
        item{
            MetricRow(title=getLocal(id=R.string.metrics_CravingsTitle),metricName="cravings",modifier=Modifier.padding(padding),selectedIndex1=selectedPositions.value[4],onSelectionChange={selectedPositions.value[4]=it})
        }
        item{
            MetricRow(title=getLocal(id=R.string.metrics_SkinConditionTitle),metricName="skinCondition",modifier=Modifier.padding(padding),selectedIndex1=selectedPositions.value[5],onSelectionChange={selectedPositions.value[5]=it})
        }
        item{
            MetricRow(title=getLocal(id=R.string.metrics_DigestiveIssuesTitle),metricName="digestiveIssues",modifier=Modifier.padding(padding),selectedIndex1=selectedPositions.value[6],onSelectionChange={selectedPositions.value[6]=it})
        }
        item{
            MetricRow(title=getLocal(id=R.string.metrics_MoodSwingsTitle),metricName="moodSwings",modifier=Modifier.padding(padding),selectedIndex1=selectedPositions.value[7],onSelectionChange={selectedPositions.value[7]=it})
        }
        item{
            MetricRow(title=getLocal(id=R.string.metrics_OverallMoodTitle),metricName="overallMood",modifier=Modifier.padding(padding),selectedIndex1=selectedPositions.value[8],onSelectionChange={selectedPositions.value[8]=it})
        }
        item{
            MetricRow(title=getLocal(id=R.string.metrics_DysphoriaTitle),metricName="dysphoria",modifier=Modifier.padding(padding),selectedIndex1=selectedPositions.value[9],onSelectionChange={selectedPositions.value[9]=it})
        }
        item{
            MetricRow(title=getLocal(id=R.string.metrics_BleedingTitle),metricName="bleeding",modifier=Modifier.padding(padding),selectedIndex1=selectedPositions.value[10],onSelectionChange={selectedPositions.value[10]=it})
        }
        item{
            MetricRow(title=getLocal(id=R.string.metrics_MusclePainTitle),metricName="musclePain",modifier=Modifier.padding(padding),selectedIndex1=selectedPositions.value[11],onSelectionChange={selectedPositions.value[11]=it})
        }
        item{
            MetricRow(title="*read custom name from shared pref*",metricName="customColumn1",modifier=Modifier.padding(padding),selectedIndex1=selectedPositions.value[12],onSelectionChange={selectedPositions.value[12]=it})
        }
        item{
            MetricRow(title="*read custom name from shared pref*",metricName="customColumn2",modifier=Modifier.padding(padding),selectedIndex1=selectedPositions.value[13],onSelectionChange={selectedPositions.value[13]=it})
        }
        item{
            MetricRow(title="*read custom name from shared pref*",metricName="customColumn3",modifier=Modifier.padding(padding),selectedIndex1=selectedPositions.value[14],onSelectionChange={selectedPositions.value[14]=it})
        }
        //endregion
        item{
            InputRow(title="Weight",modifier=Modifier.fillMaxWidth(),value=weight.value,onValueChange={weight.value=it})
        }
        item{
            InputRow(title="Kcal Balance",modifier=Modifier.fillMaxWidth(),value=kcalBalance.value,onValueChange={kcalBalance.value=it})
        }
        item{
            LongInputRow(title="Notes",modifier=Modifier.fillMaxWidth(),value=notes.value,onValueChange={notes.value=it})
        }
        item{
            Box(
                modifier=Modifier.fillMaxWidth(),
                contentAlignment=Alignment.Center
            ){
                Button(
                    modifier=Modifier
                        .padding(all=20.dp)
                        .height(50.dp)
                        .width(200.dp),
                    onClick={
                        Log.v("metrics values",selectedPositions.value.joinToString(", "){if(it==-1) "NULL" else it.toString()})
                        saveToDB(content=selectedPositions.value,context,weight.value,kcalBalance.value,notes.value)
                    }
                ){
                    Text(text="Save")
                }
            }
        }
    }

}

fun changeDate(amount: Int){
    val formatter=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
    val calendar=Calendar.getInstance()
    val today=formatter.format(calendar.time)
    calendar.apply{
        time=formatter.parse(usedDateState.value)!!
        add(Calendar.DAY_OF_YEAR,amount)
    }
    if(calendar.time<=formatter.parse(today)){
        usedDateState.value=formatter.format(calendar.time)
    }
}
fun saveToDB(content:List<Int>,context:Context,weight:String,kcalBalance:String,notes:String){
    cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()

    //val today=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(Calendar.getInstance().time)
    Thread{
        //val existingCycle=cycleDao.getCycleByDate(today)
        val existingCycle=cycleDao.getCycleByDate(usedDateState.value)
        val newCycle=Cycle(
            //date=today,
            date=usedDateState.value,
            crampLevel=content[0].takeIf {it!=-1},
            headache=content[1].takeIf {it!=-1},
            energyLevel=content[2].takeIf {it!=-1},
            sleepQuality=content[3].takeIf {it!=-1},
            cravings=content[4].takeIf {it!=-1},
            skinCondition=content[5].takeIf {it!=-1},
            digestiveIssues=content[6].takeIf {it!=-1},
            moodSwings=content[7].takeIf {it!=-1},
            overallMood=content[8].takeIf {it!=-1},
            dysphoria=content[9].takeIf {it!=-1},
            bleeding=content[10].takeIf {it!=-1},
            musclePain=content[11].takeIf {it!=-1},
            customColumn1=content[12].takeIf {it!=-1},
            customColumn2=content[13].takeIf {it!=-1},
            customColumn3=content[14].takeIf {it!=-1},
            weight=weight.toIntOrNull(),
            kcalBalance=kcalBalance.toIntOrNull(),
            notes=notes.ifBlank{null}
        )
        if(existingCycle!=null){
            cycleDao.update(newCycle)
        }
        else{
            cycleDao.insert(newCycle)
        }

    }.start()
}

@Composable
fun MetricRow(title: String,metricName: String,modifier:Modifier=Modifier, selectedIndex1:Int, onSelectionChange:(Int)->Unit){
    //val icons=metricIcons[metricName]?:emptyList()
    val icons1=createIcons()[metricName]?:emptyList()
    var selectedIndex by remember{mutableStateOf(selectedIndex1)}

    LaunchedEffect(selectedIndex1){
        selectedIndex=selectedIndex1
    }

    //Log.v("selected index","$metricName ${selectedIndexState.value}")
    Column(modifier=modifier){
        Text(
            text=title,
            fontSize=24.sp,
            color=Color.Black, //todo: change so it's dynamic!!!
            modifier=Modifier.padding(
                start=5.dp
            )
        )
        LazyRow{
            items(icons1,key={it.first}){(iconResId,label)->
                val index=icons1.indexOfFirst{it.first==iconResId&&it.second==label}
                IconItem(iconResId,label, modifier=Modifier
                    .padding(10.dp)
                    .border(
                        width=if(selectedIndex==index) 2.dp else 0.dp,
                        color=if(selectedIndex==index) Color.Black else Color.Transparent,
                        shape=CircleShape
                    )
                    .clickable {
                        selectedIndex=if(selectedIndex==index) -1 else index
                        onSelectionChange(selectedIndex)
                    }
                )
            }
        }
    }
}

@Composable
fun InputRow(title: String,modifier:Modifier=Modifier,value:String,onValueChange:(String)->Unit){
    Column(
        modifier=modifier
            .fillMaxSize()
            .padding(
                start=20.dp,
                top=10.dp,
                end=0.dp,
                bottom=10.dp
            )
    ){
        Text(
            text=title,
            fontSize=24.sp,
            color=Color.Black,
            modifier=modifier.padding(bottom=5.dp)
        )
        TextField(
            value=value,
            onValueChange=onValueChange,
            modifier=Modifier
                .width(250.dp),
            shape=RoundedCornerShape(5.dp),
            keyboardOptions=KeyboardOptions(
                keyboardType=KeyboardType.Number,
                imeAction=androidx.compose.ui.text.input.ImeAction.Done
            ),
            placeholder={Text(title)}
        )
    }
}

@Composable
fun LongInputRow(title: String,modifier:Modifier=Modifier,value:String, onValueChange:(String)->Unit){
    Column(
        modifier=modifier
            .fillMaxSize()
            .padding(
                start=20.dp,
                top=10.dp,
                end=0.dp,
                bottom=10.dp
            )
    ){
        Text(
            text=title,
            fontSize=24.sp,
            color=Color.Black,
            modifier=modifier.padding(bottom=5.dp)
        )
        TextField(
            value=value,
            onValueChange=onValueChange,
            modifier=Modifier
                .width(300.dp)
                .height(100.dp),
            shape=RoundedCornerShape(5.dp),
            keyboardOptions=KeyboardOptions(
                keyboardType=KeyboardType.Text,
                imeAction=androidx.compose.ui.text.input.ImeAction.Done
            ),
            placeholder={Text(title)}
        )
    }
}

@Composable
fun IconItem(iconResId:Int,label:String, modifier:Modifier=Modifier){
    //val color=getColor(color=com.google.android.material.R.attr.colorSecondary)
    val imgSize=75.dp
    val circleSize=with(LocalDensity.current){(imgSize.toPx()*sqrt(2f))/density}.dp

    val readyText=remember(label){
        label.split(" ").fold(mutableListOf<String>()) {lines,word->
            if(lines.isEmpty()){
                lines.add(word)
            }
            else if(lines.last().length+word.length+1<=25){
                lines[lines.size-1]="${lines.last()} $word"
            }
            else{
                lines.add(word)
            }
            lines
        }.joinToString("\n")
    }

    Column(
        horizontalAlignment=Alignment.CenterHorizontally,
        verticalArrangement=Arrangement.Center,
        modifier=Modifier
    ){
        Box(
            contentAlignment=Alignment.Center,
            modifier=modifier
                .size(circleSize)
                .clip(CircleShape)
                .background(Color.LightGray)
            //.background(color)
            //.border(1.dp, Color.Black, shape=CircleShape)
        ){
            Icon(
                painter=painterResource(id=iconResId),
                contentDescription=null,
                modifier=Modifier
                    .then(
                        Modifier
                            .sizeIn(maxWidth=imgSize,maxHeight=imgSize)
                            .aspectRatio(1f,false)
                    ),
                tint=Color.Unspecified
            )
        }
        Text(
            text=readyText,
            textAlign=TextAlign.Center,
            modifier=Modifier.widthIn(max=90.dp),
            overflow=TextOverflow.Clip,
            softWrap=true,
            style=TextStyle(lineBreak=LineBreak.Simple)
            //todo: fix it so it doesn't cut word half through
        )
    }
}