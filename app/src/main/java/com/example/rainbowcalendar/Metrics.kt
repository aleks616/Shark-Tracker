package com.example.rainbowcalendar

import android.app.Activity
import android.content.Context
import android.util.Log
import android.util.TypedValue
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rainbowcalendar.db.Cycle
import com.example.rainbowcalendar.db.CycleDao
import com.example.rainbowcalendar.db.CycleRoomDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.sqrt

@Composable
fun getColor(color: Int): Color{
    return colorResource(LocalContext.current.getColorFromAttrs(color).resourceId)
}

fun Context.getColorFromAttrs(attr: Int):TypedValue{
    return TypedValue().apply{
        theme.resolveAttribute(attr,this,true)
    }
}

@Composable
fun createIcons():Map<String, List<Pair<Int, String>>>{
    return mapOf(
        "crampLevel" to listOf(R.drawable.cramps_0 to stringResource(R.string.metrics_crampLevel0),R.drawable.cramps_1 to stringResource(R.string.metrics_crampLevel1),R.drawable.cramps_2 to stringResource(R.string.metrics_crampLevel2)),
        "headache" to listOf(R.drawable.headache_n_0 to stringResource(R.string.metrics_headache0),R.drawable.headache_n_1 to stringResource(R.string.metrics_headache1),R.drawable.headache_n_2 to stringResource(R.string.metrics_headache2),R.drawable.headache_n_3 to stringResource(R.string.metrics_headache3),R.drawable.headache_n_4 to stringResource(R.string.metrics_headache4)),
        "energyLevel" to listOf(R.drawable.energy_0 to stringResource(R.string.metrics_energyLevel0),R.drawable.energy_1 to stringResource(R.string.metrics_energyLevel1),R.drawable.energy_2 to stringResource(R.string.metrics_energyLevel2),R.drawable.energy_3 to stringResource(R.string.metrics_energyLevel3),R.drawable.energy_4 to stringResource(R.string.metrics_energyLevel4)),
        "sleepQuality" to listOf(R.drawable.sleep_0 to stringResource(R.string.metrics_sleepQuality0), R.drawable.sleep_1 to stringResource(R.string.metrics_sleepQuality1), R.drawable.sleep_2 to stringResource(R.string.metrics_sleepQuality2)),
        "cravings" to listOf(R.drawable.cravings_0 to stringResource(R.string.metrics_cravings0), R.drawable.cravings_1 to stringResource(R.string.metrics_cravings1), R.drawable.cravings_2 to stringResource(R.string.metrics_cravings2)),
        "skinCondition" to listOf(R.drawable.acne_0 to stringResource(R.string.metrics_skinCondition0), R.drawable.acne_1  to stringResource(R.string.metrics_skinCondition1), R.drawable.acne_2 to stringResource(R.string.metrics_skinCondition2)),
        "digestiveIssues" to listOf(R.drawable.digestive_0 to stringResource(R.string.metrics_digestiveIssues0), R.drawable.digestive_1 to stringResource(R.string.metrics_digestiveIssues1), R.drawable.digestive_2 to stringResource(R.string.metrics_digestiveIssues2), R.drawable.digestive_3 to stringResource(R.string.metrics_digestiveIssues3)),
        "moodSwings" to listOf(R.drawable.mood_swings_0 to stringResource(R.string.metrics_moodSwings0), R.drawable.mood_swings_1 to  stringResource(R.string.metrics_moodSwings1), R.drawable.mood_swings_2 to  stringResource(R.string.metrics_moodSwings2)),
        "overallMood" to listOf(R.drawable.mood_0 to stringResource(R.string.metrics_overallMood0),R.drawable.mood_1 to stringResource(R.string.metrics_overallMood1),R.drawable.mood_2 to stringResource(R.string.metrics_overallMood2),R.drawable.mood_3 to stringResource(R.string.metrics_overallMood3),R.drawable.mood_4 to stringResource(R.string.metrics_overallMood4)),
        "dysphoria" to listOf(R.drawable.dysphoria_0 to stringResource(R.string.metrics_dysphoria0),R.drawable.dysphoria_1 to stringResource(R.string.metrics_dysphoria1),R.drawable.dysphoria_2 to stringResource(R.string.metrics_dysphoria2),R.drawable.dysphoria_3 to stringResource(R.string.metrics_dysphoria3),R.drawable.dysphoria_4 to stringResource(R.string.metrics_dysphoria4)),
        "bleeding" to listOf(R.drawable.blood_0 to stringResource(R.string.metrics_bleeding0),R.drawable.blood_1 to stringResource(R.string.metrics_bleeding1),R.drawable.blood_2 to stringResource(R.string.metrics_bleeding2),R.drawable.blood_3 to stringResource(R.string.metrics_bleeding3),R.drawable.blood_4 to stringResource(R.string.metrics_bleeding4)),
        "musclePain" to listOf(R.drawable.muscle_pain_0 to stringResource(R.string.metrics_musclePain0), R.drawable.muscle_pain_1 to stringResource(R.string.metrics_musclePain1), R.drawable.muscle_pain_2 to stringResource(R.string.metrics_musclePain2)),
        "customColumn1" to listOf(R.drawable.customa_0 to " ",R.drawable.customa_1 to " ",R.drawable.customa_2 to " ",R.drawable.customa_3 to " "),
        "customColumn2" to listOf(R.drawable.customb_0 to " ",R.drawable.customb_1 to " ",R.drawable.customb_2 to " ",R.drawable.customb_3 to " ",R.drawable.customb_4 to " "),
        "customColumn3" to listOf(R.drawable.customc_0 to " ",R.drawable.customc_1 to " ",R.drawable.customc_2 to " ",R.drawable.customc_3 to " ", R.drawable.customc_4 to " "),

        //kcalBalance
        //weight
        //notes
    )
}
var usedDateState=mutableStateOf(SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(Calendar.getInstance().time))
lateinit var cycleDao:CycleDao

data class MetricRowData(
    val title:String,
    val metricName:String,
    val selectedIndex:Int,
    var visible:Boolean=true
)


data class MetricPersistence2(
    val metricName: String,
    var order: Int,
    val visible: Boolean,
    val title: String,
    val selectedIndex:Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetricsScreen(){
    val padding=12.dp
    //val selectedPositions=remember{mutableStateOf(MutableList(15){-1})}
    val selectedPositions=remember{mutableStateOf<Map<String,Int>>(emptyMap())}
    val weight=remember{mutableStateOf("")}
    val kcalBalance=remember{mutableStateOf("")}
    val notes=remember{mutableStateOf("")}

    val sharedPrefs=LocalContext.current.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
    val lang=sharedPrefs.getString("lang","en")!!
    if(lang=="pt-br")
        Locale("pt","BR")
    else
        Locale(lang)
    //val appLocale=Locale("ru")

    val context=LocalContext.current
    val today=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(Calendar.getInstance().time)

    cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()


    val customName1=sharedPrefs.getString("customMetric1","custom1-missing")!!
    val customName2=sharedPrefs.getString("customMetric2","custom2-missing")!!
    val customName3=sharedPrefs.getString("customMetric3","custom3-missing")!!

    val customNames=listOf(
        sharedPrefs.getString("customMetric1","custom1-missing")!!,
        sharedPrefs.getString("customMetric2","custom2-missing")!!,
        sharedPrefs.getString("customMetric3","custom3-missing")!!
    )

    val metricRows=listOf(
        MetricRowData(context.getString(R.string.metrics_crampLevelTitle),"crampLevel",selectedPositions.value["crampLevel"]?:-1),
        MetricRowData(context.getString(R.string.metrics_headacheTitle),"headache",selectedPositions.value["headache"]?:-1),
        MetricRowData(context.getString(R.string.metrics_energyLevelTitle),"energyLevel",selectedPositions.value["energyLevel"]?:-1),
        MetricRowData(context.getString(R.string.metrics_SleepQualityTitle),"sleepQuality",selectedPositions.value["sleepQuality"]?:-1),
        MetricRowData(context.getString(R.string.metrics_CravingsTitle),"cravings",selectedPositions.value["cravings"]?:-1),
        MetricRowData(context.getString(R.string.metrics_SkinConditionTitle),"skinCondition",selectedPositions.value["skinCondition"]?:-1),
        MetricRowData(context.getString(R.string.metrics_DigestiveIssuesTitle),"digestiveIssues",selectedPositions.value["digestiveIssues"]?:-1),
        MetricRowData(context.getString(R.string.metrics_MoodSwingsTitle),"moodSwings",selectedPositions.value["moodSwings"]?:-1),
        MetricRowData(context.getString(R.string.metrics_OverallMoodTitle),"overallMood",selectedPositions.value["overallMood"]?:-1),
        MetricRowData(context.getString(R.string.metrics_DysphoriaTitle),"dysphoria",selectedPositions.value["dysphoria"]?:-1),
        MetricRowData(context.getString(R.string.metrics_BleedingTitle),"bleeding",selectedPositions.value["bleeding"]?:-1),
        MetricRowData(context.getString(R.string.metrics_MusclePainTitle),"musclePain",selectedPositions.value["musclePain"]?:-1),
        MetricRowData(customName1,"customColumn1",selectedPositions.value["customColumn1"]?:-1),
        MetricRowData(customName2,"customColumn2",selectedPositions.value["customColumn2"]?:-1),
        MetricRowData(customName3,"customColumn3",selectedPositions.value["customColumn3"]?:-1)
    )
    val metricRowsState=remember{mutableStateOf(metricRows)}
    val loadedMetrics=loadMetricsJson(context)
    if(loadedMetrics!=null){
        metricRowsState.value=loadedMetrics.map{savedMetric->
            val realTitle:String=when(savedMetric.metricName){
                "customColumn1"->customName1
                "customColumn2"->customName2
                "customColumn3"->customName3
                else->savedMetric.title
            }

            MetricRowData(
                title=realTitle,
                metricName=savedMetric.metricName,
                selectedIndex=savedMetric.selectedIndex,
                visible=savedMetric.visible
            )
        }
    }

    LaunchedEffect(usedDateState.value){
        withContext(Dispatchers.IO){
            val cycle=cycleDao.getCycleByDate(usedDateState.value)
            if(cycle!=null){
                selectedPositions.value=mapOf(
                    "crampLevel" to cycle.crampLevel,
                    "headache" to cycle.headache,
                    "energyLevel" to cycle.energyLevel,
                    "sleepQuality" to cycle.sleepQuality,
                    "cravings" to cycle.cravings,
                    "skinCondition" to cycle.skinCondition,
                    "digestiveIssues" to cycle.digestiveIssues,
                    "moodSwings" to cycle.moodSwings,
                    "overallMood" to cycle.overallMood,
                    "dysphoria" to cycle.dysphoria,
                    "bleeding" to cycle.bleeding,
                    "musclePain" to cycle.musclePain,
                    "customColumn1" to cycle.customColumn1,
                    "customColumn2" to cycle.customColumn2,
                    "customColumn3" to cycle.customColumn3
                ).mapValues{it.value?:-1}
                weight.value=(cycle.weight ?: "").toString()
                kcalBalance.value=(cycle.kcalBalance?:"").toString()
                notes.value=cycle.notes?:""
            }
            else{
                selectedPositions.value=emptyMap()
                weight.value=""
                kcalBalance.value=""
                notes.value=""
            }
          /*  if(cycle!=null){

                selectedPositions.value=metricRowsState.value.map {row->
                    selectionMap[row.metricName]?: -1
                }.toMutableList()

               *//* selectedPositions.value=mutableListOf(
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
                ).map{it ?: -1}.toMutableList()*//*

               *//* metricRowsState.value=metricRowsState.value.mapIndexed{index,metric->
                    metric.copy(selectedIndex=selectedPositions.value[index])
                }*//*

                weight.value=(cycle.weight ?: "").toString()
                kcalBalance.value=(cycle.kcalBalance?:"").toString()
                notes.value=cycle.notes?:""
            }
            else{
                selectedPositions.value=List(15){-1}.toMutableList()
                metricRowsState.value=metricRowsState.value.map{it.copy(selectedIndex=-1)}
                weight.value=""
                kcalBalance.value=""
                notes.value=""
            }*/
        }
    }
    if(customNames[0]=="custom1-missing"){
        metricRowsState.value[12].visible=false
    }
    if(customNames[1]=="custom2-missing"){
        metricRowsState.value[13].visible=false
    }
    if(customNames[2]=="custom3-missing"){
        metricRowsState.value[14].visible=false
    }
    val showReorderView=remember{mutableStateOf(false)}

    var showDialog by remember{mutableStateOf(false)}
    val datePickerState=rememberDatePickerState(
        selectableDates=Utils.MetricsSelectableDates,
        initialSelectedDateMillis=System.currentTimeMillis()
    )
    val selectedDate=datePickerState.selectedDateMillis?.let{
        Utils.convertMillisToDate(it)
    }?:""



    val theme=sharedPrefs.getString("theme","Black")
    LazyColumn(
        modifier=if(theme=="Pride")
            Modifier
                .fillMaxSize()
                .paint(painterResource(id=R.drawable.pride50),contentScale=ContentScale.FillBounds)
        else Modifier
            .fillMaxSize()
            .background(colorPrimary())
    ){
        item{
            Box(
                contentAlignment=Alignment.Center,
                modifier=Modifier.fillMaxSize() .padding(bottom=20.dp)
            ){
                Column(horizontalAlignment=Alignment.CenterHorizontally){
                    Text(
                        color=colorSecondary(),
                        text=stringResource(id=R.string.metrics),
                        fontSize=35.sp,
                        textAlign=TextAlign.Center,
                        fontWeight=FontWeight.W500,
                        modifier=Modifier.padding(vertical=10.dp)
                    )
                    Row(
                        modifier=Modifier.fillMaxWidth(),
                        verticalAlignment=Alignment.CenterVertically
                    ){
                        Button(
                            //left button
                            onClick={changeDate(amount=-1)},
                            Modifier
                                .height(48.dp)
                                .width(80.dp)
                                .align(Alignment.CenterVertically)
                                .padding(start=10.dp),
                            colors=buttonColors(backgroundColor=colorTertiary()),
                        ){
                            Image(
                                painter=painterResource(id=R.drawable.icon_arrow_left_triangle),
                                contentDescription=null,
                                contentScale=ContentScale.FillHeight,
                                modifier=Modifier
                                    .fillMaxSize()
                                    .scale(2.0f)
                            )
                        }
                        Button(
                            onClick={
                                showDialog=true
                            },
                            modifier=Modifier
                                .padding(vertical=10.dp,horizontal=4.dp)
                                .align(Alignment.CenterVertically)
                                .border(width=2.dp,color=colorTertiary())
                                .fillMaxSize()
                                .weight(1f),
                            colors=buttonColors(backgroundColor=colorPrimary()),
                        ){
                            Text(
                                color=colorSecondary(),
                                text=usedDateState.value,
                                fontSize=28.sp,
                                textAlign=TextAlign.Center,

                                )
                        }
                        Button(
                            // right button
                            onClick={changeDate(amount=1)},
                            Modifier
                                .height(48.dp)
                                .width(80.dp)
                                .align(Alignment.CenterVertically)
                                .padding(end=10.dp)
                                .alpha(if(usedDateState.value>=today) 0f else 1f),
                            enabled=usedDateState.value<today,
                            colors=buttonColors(backgroundColor=colorTertiary()),
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
        }//header
        if(showDialog){
            item{
                CustomDatePickerDialog(state=datePickerState,confirmButton={usedDateState.value=selectedDate},onClose={showDialog=false},onDismissRequest={showDialog=false})
            }
        }
        item{
            Box(
                contentAlignment=Alignment.Center,
                modifier=Modifier
                    .fillMaxSize()
                    .padding(bottom=20.dp)
            ){
                Column(horizontalAlignment=Alignment.CenterHorizontally){
                    Row(
                        modifier=Modifier.fillMaxWidth(),
                        verticalAlignment=Alignment.CenterVertically,
                        horizontalArrangement=Arrangement.SpaceBetween
                    ){
                        Button(
                            onClick={showReorderView.value=!showReorderView.value},
                            Modifier
                                .height(45.dp)
                                .width(80.dp)
                                .align(Alignment.CenterVertically)
                                .padding(start=10.dp),
                            colors=buttonColors(backgroundColor=colorTertiary()),
                        ){}
                    }
                }
            }
        } //button
        if(showReorderView.value){
            item{
                Box(modifier=Modifier
                    .fillMaxWidth()
                    .heightIn(max=500.dp)){
                    MetricReorderView(metricRows=metricRowsState,
                        onOrderChanged={
                            updatedList->metricRowsState.value=updatedList
                            saveMetricsJson(context,updatedList)
                        })
                }
            }
            item{
                VerticalSpacer()
            }
        }
        if(!showReorderView.value){
            items(metricRowsState.value, key={it.metricName}){metric ->
                MetricRow(title=metric.title, metricName=metric.metricName,modifier=Modifier.padding(padding),visible=metric.visible,selectedIndex1=selectedPositions.value[metric.metricName]?:-1,
                    onSelectionChange={/*selectedIndex->val index=metricRowsState.value.indexOf(metric)
                        if(index!=-1){
                            selectedPositions.value=selectedPositions.value.toMutableList().apply{
                                this[index]=selectedIndex}

                            val updatedMetrics=metricRowsState.value.toMutableList().apply{
                                this[index]=this[index].copy(selectedIndex=selectedIndex)
                            }
                            metricRowsState.value=updatedMetrics
                        }*/
                        newIndex->
                        selectedPositions.value=selectedPositions.value.toMutableMap().apply{
                            put(metric.metricName,newIndex)
                        }
                    }
                )

            }
            item{
                InputRow(title=stringResource(id=R.string.weight),modifier=Modifier.fillMaxWidth(),value=weight.value,onValueChange={weight.value=it})
            }
            item{
                InputRow(title=stringResource(id=R.string.kcal_balance),modifier=Modifier.fillMaxWidth(),value=kcalBalance.value,onValueChange={kcalBalance.value=it})
            }
            item{
                InputRow(title=stringResource(id=R.string.notes),modifier=Modifier.fillMaxWidth(),value=notes.value,onValueChange={notes.value=it},width=300.dp,height=100.dp,maxLines=5)
            }
            item{
                Box(
                    modifier=Modifier.fillMaxWidth(),
                    contentAlignment=Alignment.Center
                ){
                    Button(
                        modifier=Modifier
                            .padding(bottom=25.dp,top=20.dp,start=20.dp,end=20.dp)
                            .height(50.dp)
                            .width(200.dp),
                        onClick={saveToDB(selectedPositions.value,context,weight.value,kcalBalance.value,notes.value)},
                        colors=buttonColors(backgroundColor=colorTertiary()),
                    ){
                        Text(text=stringResource(id=R.string.save),color=colorQuaternary(),fontSize=20.sp) //todo: MAKE WEIGHT FLOAT IMPORTANT!!!!
                    }
                }
            }
        }
    }
}

fun saveToDB(/*content:List<Int>*/selections:Map<String,Int>,context:Context,weight:String,kcalBalance:String,notes:String){
    cycleDao=CycleRoomDatabase.getDatabase(context).cycleDao()
    
    Thread{
        val existingCycle=cycleDao.getCycleByDate(usedDateState.value)
        val newCycle=Cycle(
            date=usedDateState.value,
            /*crampLevel=content[0].takeIf{it!=-1},
            headache=content[1].takeIf{it!=-1},
            energyLevel=content[2].takeIf{it!=-1},
            sleepQuality=content[3].takeIf{it!=-1},
            cravings=content[4].takeIf{it!=-1},
            skinCondition=content[5].takeIf{it!=-1},
            digestiveIssues=content[6].takeIf{it!=-1},
            moodSwings=content[7].takeIf{it!=-1},
            overallMood=content[8].takeIf{it!=-1},
            dysphoria=content[9].takeIf{it!=-1},
            bleeding=content[10].takeIf{it!=-1},
            musclePain=content[11].takeIf{it!=-1},
            customColumn1=content[12].takeIf{it!=-1},
            customColumn2=content[13].takeIf{it!=-1},
            customColumn3=content[14].takeIf{it!=-1},*/
            crampLevel=selections["crampLevel"],
            headache=selections["headache"],
            energyLevel=selections["energyLevel"],
            sleepQuality=selections["sleepQuality"],
            cravings=selections["cravings"],
            skinCondition=selections["skinCondition"],
            digestiveIssues=selections["digestiveIssues"],
            moodSwings=selections["moodSwings"],
            overallMood=selections["overallMood"],
            dysphoria=selections["dysphoria"],
            bleeding=selections["bleeding"],
            musclePain=selections["musclePain"],
            customColumn1=selections["customColumn1"],
            customColumn2=selections["customColumn2"],
            customColumn3=selections["customColumn3"],
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
@Composable
fun MetricRow(title: String,metricName: String,modifier:Modifier=Modifier,visible:Boolean, selectedIndex1:Int, onSelectionChange:(Int)->Unit){
    val icons1=createIcons()[metricName]?:emptyList()
    var selectedIndex by remember{mutableStateOf(selectedIndex1)}

    LaunchedEffect(selectedIndex1){
        selectedIndex=selectedIndex1
    }
    if(visible){
        Column(modifier=modifier){
            Text(
                color=colorSecondary(),
                text=title,
                fontSize=24.sp,
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
                            width=if(selectedIndex==index) 4.dp else 0.dp,
                            color=if(selectedIndex==index) colorSecondary() else Color.Transparent,
                            shape=CircleShape
                        )
                        .clickable{
                            selectedIndex=if(selectedIndex==index) -1 else index
                            onSelectionChange(selectedIndex)
                        }
                    )

                }
            }
        }
    }
}
@Composable
fun InputRow(
    title:String,
    modifier:Modifier=Modifier,
    value:String,
    onValueChange:(String)->Unit,
    maxLines:Int=1,
    width:Dp=250.dp,
    height:Dp=Dp.Unspecified
){
    Column(
        modifier=modifier.fillMaxSize().padding(start=20.dp,top=10.dp,end=0.dp,bottom=10.dp)
    ){
        Text(
            color=colorSecondary(),
            text=title,
            fontSize=24.sp,
            modifier=modifier.padding(bottom=5.dp)
        )
        TextField(
            value=value,
            onValueChange=onValueChange,
            modifier=Modifier.width(width).height(height),
            shape=RoundedCornerShape(5.dp),
            keyboardOptions=KeyboardOptions(
                keyboardType=KeyboardType.Number,
                imeAction=androidx.compose.ui.text.input.ImeAction.Done
            ),
            placeholder={Text(title, color=colorSecondary())},
            maxLines=maxLines,
            colors=TextFieldDefaults.textFieldColors(textColor=colorSecondary(),focusedIndicatorColor=colorSecondary(),cursorColor=colorSecondary())
        )
    }
}

@Composable
fun IconItem(iconResId:Int,label:String, modifier:Modifier=Modifier){
    val imgSize=75.dp
    val circleSize=with(LocalDensity.current){(imgSize.toPx()*sqrt(2f))/density}.dp

    val readyText=remember(label){
        label.split(" ").fold(mutableListOf<String>()){lines,word->
            if(lines.isEmpty()){
                lines.add(word)
            }
            else if(lines.last().length+word.length+1<=30){
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
            modifier=modifier.size(circleSize).clip(CircleShape).background(colorTertiary())
        ){
            Icon(
                painter=painterResource(id=iconResId),
                contentDescription=null,
                modifier=Modifier.then(Modifier.sizeIn(maxWidth=imgSize,maxHeight=imgSize).aspectRatio(1f,false)),
                tint=Color.Unspecified
            )
        }
        Text(
            color=colorSecondary(),
            text=readyText,
            textAlign=TextAlign.Center,
            modifier=Modifier.widthIn(max=100.dp),
            overflow=TextOverflow.Clip,
            softWrap=true,
            fontSize=14.sp,
            style=TextStyle(lineBreak=LineBreak.Simple)
        )
    }
}


@Composable
fun MetricReorderView(metricRows:MutableState<List<MetricRowData>>,onOrderChanged:(List<MetricRowData>)->Unit){
    val lazyListState=rememberLazyListState()

    val context=LocalContext.current
    val contextAct=context as Activity
    val sharedPrefs=context.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
    val isSetUp=sharedPrefs.getBoolean(Constants.metricsSetUp,false)

    val reorderableLazyListState=rememberReorderableLazyListState(lazyListState){from,to->
        val updatedList=metricRows.value.toMutableList().apply{
            add(to.index,removeAt(from.index))
            if(!isSetUp) sharedPrefs.edit().putBoolean(Constants.metricsSetUp,true).apply()
        }
        onOrderChanged(updatedList)
    }


    var customNamesInput by remember{mutableStateOf(mutableListOf("","",""))}

    LazyColumn(state=lazyListState){
        items(metricRows.value,key={it.title}){metric->
            ReorderableItem(reorderableLazyListState,key=metric.title){isDragging->
                val elevation by animateDpAsState(if(isDragging) 4.dp else 0.dp,label="")
                Surface(elevation=elevation){
                    Row(
                        modifier=Modifier.border(1.dp,colorTertiary(),RectangleShape).fillMaxWidth().background(color=colorPrimary())
                    ){
                        Checkbox(
                            checked=metric.visible,
                            onCheckedChange={
                                val updatedMetric=metric.copy(visible=it)
                                val updatedList=metricRows.value.toMutableList().apply{
                                    val index=indexOf(metric)
                                    set(index,updatedMetric)
                                }
                                metricRows.value=updatedList.toList()
                                saveMetricsJson(context, updatedList)
                                onOrderChanged(updatedList)
                            },
                            colors=CheckboxDefaults.colors(checkedColor=colorTertiary())
                        )
                        Text(
                            color=colorSecondary(),
                            fontSize=20.sp,
                            text=metric.title,
                            modifier=Modifier.weight(1f).align(Alignment.CenterVertically)
                        )
                        IconButton(
                            modifier=Modifier.draggableHandle(),
                            onClick={}
                        ){
                            /*Image(
                                painter=painterResource(id=R.drawable.icon_arrow_right_triangle),
                                contentDescription="Reorder"
                            )*/
                            Icon(
                                imageVector=Icons.Rounded.Menu,
                                tint=colorSecondary(),
                                contentDescription="",
                                modifier=Modifier
                                    .scale(1.2f)
                                    .alpha(0.7f)
                            )
                        }
                    }
                }
            }
        }

        item{
            BetterHeader(text="Enter names for custom metrics to track",fontSize="MS",modifier=Modifier.fillMaxSize().padding(top=14.dp,start=12.dp,end=12.dp))
            BetterHeader(text="Changes will be visible after pressing the \"refresh\" button!", fontSize="S",modifier=Modifier.fillMaxSize().padding(vertical=14.dp,horizontal=12.dp))

            customNamesInput.forEachIndexed{index,_->
                BetterTextField(textFieldModifier=Modifier.padding(6.dp),placeholderText="custom metric"+(index+1),value=customNamesInput[index], onValueChange={new->
                    val updatedList=customNamesInput.toMutableList().apply{
                        this[index]=new
                    }
                    customNamesInput=updatedList
                })
                Row(modifier=Modifier.fillMaxWidth().padding(end=16.dp,bottom=10.dp), horizontalArrangement=Arrangement.End){
                    BetterButton(
                        onClick={
                            if(customNamesInput[index].isNotEmpty()){
                                sharedPrefs.edit().putString(("customMetric"+(index+1)),customNamesInput[index]).apply()
                                val updatedMetrics=metricRows.value.toMutableList().apply{
                                    for(i in 0..14){
                                        if(this[i].metricName==("customMetric"+(index+1)))
                                            this[i]=this[i].copy(title=customNamesInput[index])
                                    }
                                }
                                metricRows.value=updatedMetrics.toList()
                                saveMetricsJson(context,updatedMetrics)
                                customNamesInput[index]=""
                                onOrderChanged(updatedMetrics)
                            }
                        }
                    ){
                        BetterText(text="save")
                    }
                }
            }
            Row(
                modifier=Modifier.fillMaxWidth(),
                horizontalArrangement=Arrangement.Center){
                BetterButton(
                    modifier=Modifier.padding(bottom=25.dp,top=20.dp,start=20.dp,end=20.dp).height(50.dp).width(200.dp),
                    onClick={
                        contextAct.recreate()
                    }
                ){
                    BetterText(text="refresh",fontSize=20.sp)
                }
            }
        }
    }
}

fun saveMetricsJson(context: Context, metrics: List<MetricRowData>){
    val sharedPrefs=context.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
    val gson=Gson()

    val metricPersistence2List=metrics.mapIndexed{index,metric->
        Log.v("custom name?",metric.metricName)
        MetricPersistence2(metricName=metric.metricName,order=index,visible=metric.visible,title=metric.title,selectedIndex=metric.selectedIndex)
    }
    val metrics2Json=gson.toJson(metricPersistence2List)
    sharedPrefs.edit().putString("metricsOrder2", metrics2Json).apply()
}

fun loadMetricsJson(context: Context): List<MetricPersistence2>?{
    val sharedPrefs=context.getSharedPreferences(Constants.key_package,Context.MODE_PRIVATE)
    val gson=Gson()

    val metricsJson2=sharedPrefs.getString("metricsOrder2",null)

    return metricsJson2?.let{
        val type=object:TypeToken<List<MetricPersistence2>>(){}.type
        gson.fromJson(it,type)
    }
}