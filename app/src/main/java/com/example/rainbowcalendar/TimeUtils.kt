package com.example.rainbowcalendar

import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
enum class TIMEUNIT{
    ERROR,
    DAYS,
    WEEKS,
    MONTHS,
    YEARS
}
data class MilestoneDate(
    var days:Int,
    var amount:Int,
    var timeUnit:TIMEUNIT
)
object TimeUtils{
    fun timeSinceDate(start:String):MilestoneDate{
        val today=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(Calendar.getInstance().time)
        val dateFormat=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        dateFormat.isLenient=false

        try{
            val startDate=dateFormat.parse(start)!!
            val todayDate=dateFormat.parse(today)!!
            val timeSinceStart=todayDate.time-startDate.time
            val daysSinceStart=timeSinceStart/(24*60*60*1000)

            if(daysSinceStart>1095){//>36 months -> years
                return MilestoneDate(daysSinceStart.toInt(),(daysSinceStart/365.25).toInt(),TIMEUNIT.YEARS)
                //todo: next date? in another function? update: i have no idea what the fuck this means
            }
            if(daysSinceStart>168){ //>24 weeks -> months
                return MilestoneDate(daysSinceStart.toInt(),(daysSinceStart/30.437).toInt(),TIMEUNIT.MONTHS)
            }
            if(daysSinceStart>35){//>5 weeks -> in weeks
                return MilestoneDate(daysSinceStart.toInt(),(daysSinceStart/7).toInt(),TIMEUNIT.WEEKS)
            }
            return MilestoneDate(daysSinceStart.toInt(),daysSinceStart.toInt(),TIMEUNIT.DAYS)
        }
        catch(e:ParseException){
            return MilestoneDate(-1,-1,TIMEUNIT.ERROR)
        }
    }
    fun convertMillisToDate(millis:Long):String{
        val formatter=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        return formatter.format(Date(millis))
    }


    fun isValidDate(date:String):Boolean{
        return try{
            val formatter=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
            formatter.isLenient=false
            formatter.parse(date)
            true
        }
        catch(e:Exception){
            false
        }
    }

    fun isValidPastDate(year:Int,month:Int,day:Int):Boolean{
        val formatter=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        val today=formatter.format(Calendar.getInstance().time)
        val calendar=Calendar.getInstance()
        calendar.set(year,month-1,day)
        val date=formatter.format(calendar.time)
        return formatter.parse(today)!!>=formatter.parse(date)
    }

    fun isValidPastOrPresentYear(year:Int):Boolean{
        val currentYear=Calendar.getInstance().get(Calendar.YEAR)
        return (2000..currentYear).contains(year)
    }

    fun createDateFromIntegers(year:Int,month:Int,day:Int):String{
        return "%04d-%02d-%02d".format(year, month, day)
    }

    fun localDateString(date:String):LocalDate{
        val year=date.split("-")[0].toInt()
        val month=date.split("-")[1].toInt()
        val day=date.split("-")[2].toInt()
        return LocalDate.of(year,month,day)
    }

    fun isDate1AfterDate2(date1:String,date2:String):Boolean{
        val format=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        return format.parse(date1)!!.after(format.parse(date2))
    }
    fun isDate1AfterOrSameAsDate2(date1:String,date2:String):Boolean{
        val format=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        return !format.parse(date1)!!.before(format.parse(date2))
    }

    fun smartLastPeriodDate(date:String):String{
        if(!isValidDate(date)) return "1970-01-01"

        val dateFormat=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        val lastDate=dateFormat.parse(date)!!
        val beforeDate=lastDate.time-28*24*3600*1000L

        return dateFormat.format(Date(beforeDate))
        //return "0000-00-00"
    }

    /**
     * @param lastDoseDate yyyy-MM-dd of last testosterone dose
     * @param interval correct interval between T doses, in days
     * @see getDaysTillNextShot
     * @return date, (type Date) of next dose, can be in the past!, check that later when using the result
     */
    fun getNextDoseDate(lastDoseDate:String,interval:Int):Date {
        val dateFormat=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        val lastDate=dateFormat.parse(lastDoseDate)!!
        return Date(lastDate.time+interval*3600*24*1000) //todo: if returned days is in the past show "overdue" and the old correct day and "today"
    }
    /**
     * @param lastDoseDate yyyy-MM-dd of last testosterone dose
     * @param interval correct interval between T doses, in days
     * @see getNextDoseDate
     * @return number of days left to next shot, can be a negative number!, check that later when using the result
     **/
    fun getDaysTillNextShot(lastDoseDate:String,interval:Int):Int {
        val dateFormat=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        val today=dateFormat.format(Calendar.getInstance().time)
        val lastDate=dateFormat.parse(lastDoseDate)!!
        val todayDate=dateFormat.parse(today)!!
        val nextDate=Date(lastDate.time+interval*3600*24*1000)
        return ((nextDate.time-todayDate.time)/(3600*24*1000)).toInt()
    }

    fun iterateDateString(dateS:String):String{
        val dateFormat=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        val date=dateFormat.parse(dateS)!!
        val resultDate=date.time+86400000

        return dateFormat.format(Date(resultDate))
    }

    fun addDateString(dateS:String,days:Int):String{
        val dateFormat=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        val date=dateFormat.parse(dateS)!!
        val resultDate=date.time+days*86400000

        return dateFormat.format(Date(resultDate))
    }


    fun monthsSinceFirstDate(date:String):Int{
        val today=LocalDate.now()
        val date1=LocalDate.parse(date)
        return((today.year-date1.year)*12)+(today.monthValue-date1.monthValue)+1
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun datePickerColors():DatePickerColors {
        return DatePickerDefaults.colors(
            titleContentColor=colorSecondary(),
            headlineContentColor=colorSecondary(),
            weekdayContentColor=colorSecondary(),
            dayContentColor=colorSecondary(),

            todayDateBorderColor=colorSecondary(),
            todayContentColor=colorSecondary(),
            selectedDayContainerColor=colorTertiary(),
            selectedDayContentColor=colorSecondary(),

            containerColor=colorPrimary(),
            subheadContentColor=Color.Red,
            yearContentColor=colorSecondary(),
            currentYearContentColor=colorQuaternary(),
            selectedYearContentColor=colorQuaternary(),
            selectedYearContainerColor=colorTertiary(),

            disabledDayContentColor=colorTertiary(),

            disabledSelectedYearContentColor=colorTertiary(),
            disabledSelectedYearContainerColor=colorTertiary(),
            disabledSelectedDayContentColor=colorTertiary(),
        )
    }


    @OptIn(ExperimentalMaterial3Api::class)
    object PastOrPresentSelectableDates:SelectableDates {
        override fun isSelectableDate(utcTimeMillis:Long):Boolean{
            return utcTimeMillis<=System.currentTimeMillis()
        }
        override fun isSelectableYear(year:Int):Boolean{
            return year<=LocalDate.now().year
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    object OldEnoughSelectableDates:SelectableDates {
        override fun isSelectableDate(utcTimeMillis:Long):Boolean{
            return (System.currentTimeMillis()-22090320000000..System.currentTimeMillis()-410240038000).contains(utcTimeMillis)
            //return utcTimeMillis<=System.currentTimeMillis()-410240038000
        }
        override fun isSelectableYear(year:Int):Boolean{
            return (LocalDate.now().year-70..LocalDate.now().year-13).contains(year)
            //return year<=LocalDate.now().year-13
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    object MetricsSelectableDates:SelectableDates {
        override fun isSelectableDate(utcTimeMillis:Long):Boolean{
            return (System.currentTimeMillis()-315569510000..System.currentTimeMillis()).contains(utcTimeMillis)
        }
        override fun isSelectableYear(year:Int):Boolean{
            return (LocalDate.now().year-10..LocalDate.now().year).contains(year)
        }
    }
}