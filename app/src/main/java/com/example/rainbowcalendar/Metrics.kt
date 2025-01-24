package com.example.rainbowcalendar

import android.content.Context
import android.util.Log
import android.util.TypedValue
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

/*val metricIcons=mapOf(
    "crampLevel" to listOf(R.drawable.cramps_0,R.drawable.cramps_1,R.drawable.cramps_2),
    "headache" to listOf(R.drawable.headache_n_0,R.drawable.headache_n_1,R.drawable.headache_n_2,R.drawable.headache_n_3,R.drawable.headache_n_4),
    "energyLevel" to listOf(R.drawable.energy_0,R.drawable.energy_1,R.drawable.energy_2,R.drawable.energy_3,R.drawable.energy_4),
    "sleepQuality" to listOf(R.drawable.sleep_0, R.drawable.sleep_1, R.drawable.sleep_2),
    "cravings" to listOf(R.drawable.cravings_0, R.drawable.cravings_1, R.drawable.cravings_2),
    "skinCondition" to listOf(R.drawable.acne_0, R.drawable.acne_1, R.drawable.acne_2),
    "digestiveIssues" to listOf(R.drawable.digestive_0, R.drawable.digestive_1, R.drawable.digestive_2, R.drawable.digestive_3),
    "moodSwings" to listOf(R.drawable.mood_swings_0, R.drawable.mood_swings_1, R.drawable.mood_swings_2),
    "overallMood" to listOf(R.drawable.mood_0,R.drawable.mood_1,R.drawable.mood_2,R.drawable.mood_3,R.drawable.mood_4),
    "dysphoria" to listOf(R.drawable.dysphoria_0,R.drawable.dysphoria_1,R.drawable.dysphoria_2,R.drawable.dysphoria_3,R.drawable.dysphoria_4),
    "bleeding" to listOf(R.drawable.blood_0,R.drawable.blood_1,R.drawable.blood_2,R.drawable.blood_3,R.drawable.blood_4),
    "musclePain" to listOf(R.drawable.muscle_pain_0, R.drawable.muscle_pain_1, R.drawable.muscle_pain_2),
    "customColumn1" to listOf(R.drawable.customa_0,R.drawable.customa_1,R.drawable.customa_2,R.drawable.customa_3),
    "customColumn2" to listOf(R.drawable.customb_0,R.drawable.customb_1,R.drawable.customb_2,R.drawable.customb_3,R.drawable.customb_4),
    "customColumn3" to listOf(R.drawable.customc_0,R.drawable.customc_1,R.drawable.customc_2,R.drawable.customc_3, R.drawable.customc_4),

    //kcalBalance
    //weight
    //notes
)*/
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


@Composable
fun ScrollableMetricsView(){
    val padding=12.dp
    val selectedPositions=remember{mutableStateOf(MutableList(15){-1})}
    
    LazyColumn(modifier=Modifier.fillMaxSize()){
        item{
            Text(
                text="Metrics",
                fontSize=35.sp,
                fontWeight=FontWeight.W500,
                modifier=Modifier
                    .padding(start=20.dp, top=20.dp, bottom=5.dp)

            )
        }
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
        item{
            InputRow(title="Weight",modifier=Modifier.fillMaxWidth())
        }
        item{
            InputRow(title="Kcal Balance",modifier=Modifier.fillMaxWidth())
        }
        item{
            LongInputRow(title="Notes",modifier=Modifier.fillMaxWidth())
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
                    }
                ){
                    Text(text="Save")
                }
            }
        }
    }

}

@Composable
fun MetricRow(title: String,metricName: String,modifier:Modifier=Modifier, selectedIndex1:Int, onSelectionChange:(Int)->Unit){
    //val icons=metricIcons[metricName]?:emptyList()
    val icons1=createIcons()[metricName]?:emptyList()
    var selectedIndex by remember{mutableStateOf(selectedIndex1)}

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
            items(icons1){(iconResId,label)->
                val index=icons1.indexOfFirst{it.first==iconResId&&it.second==label}
                IconItem(iconResId,label, modifier=Modifier
                    .padding(10.dp)
                    .border(
                        width=if(selectedIndex==index) 2.dp else 0.dp,
                        color=if(selectedIndex==index) Color.Black else Color.Transparent,
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

@Composable
fun InputRow(title: String,modifier:Modifier=Modifier){
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
            value="",
            onValueChange={},
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
fun LongInputRow(title: String,modifier:Modifier=Modifier){
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
            value="",
            onValueChange={},
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
    val color=getColor(color=com.google.android.material.R.attr.colorSecondary)
    val imgSize=75.dp
    val circleSize=with(LocalDensity.current){(imgSize.toPx()*sqrt(2f))/density}.dp
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
            text=label,
            textAlign=TextAlign.Center,
            modifier=Modifier.widthIn(max=80.dp),
            overflow=TextOverflow.Clip,
            softWrap=true
            //todo: fix it so it doesn't cut word half through
        )
    }
}