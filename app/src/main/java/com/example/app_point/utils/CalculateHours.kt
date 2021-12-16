package com.example.app_point.utils

import com.example.app_point.entity.*
import kotlin.collections.ArrayList

class CalculateHours {

    fun calculateHoursExtras(time: Int, points: HourEntityInt): Int {

        val hour1 = points.hora1
        val hour2 = points.hora2
        val hour3 = points.hora3
        val hour4 = points.hora4

        return ((hour2!! - hour1!!) + (hour4!! - hour3!!)) - time
    }

    fun calculationHours(work: Int, hours: ArrayList<PointsEntity?>): EntityExtra{

        val value = hours.size
        var position = 0
        var extra = 0
        var horas = 0
        
        while (value != position){
            val h1 = hours[position]?.hora1
            val h2 = hours[position]?.hora2
            val h3 = hours[position]?.hora3
            val h4 = hours[position]?.hora4

        }

        return if (extra == 0){
            EntityExtra(0, 0, 0, 0)
        }else {
            val ex = convertMinutesInHours(extra)
            val hr = convertMinutesInHours(horas)
            EntityExtra(ex.hour, ex.minute, hr.hour, hr.minute)
        }
    }
    /*

    */
    fun calculateTimeEmployee(points: HourEntityInt): Int {

        val hour1 = points.hora1
        val hour2 = points.hora2
        val hour3 = points.hora3
        val hour4 = points.hora4

        return (hour2!! - hour1!!) + (hour4!! - hour3!!)
    }

    private fun formatHourToString(ret: EntityHourAndMinute): String{
        return if (ret.hour < 10 && ret.minute < 10){
            "0${ret.hour}:0${ret.minute}"
        }else if(ret.hour < 10 && ret.minute >= 10){
            "0${ret.hour}:${ret.minute}"
        }else if(ret.hour >= 10 && ret.minute < 10){
            "${ret.hour}:0${ret.minute}"
        }else{
            "${ret.hour}:${ret.minute}"
        }
    }

    fun converterHoursInMinutes(hour: String): Int{
        return if (hour == "") 0
        else {
            val int = hour.split(":")
            val hours = Integer.parseInt(int[0])
            val min = Integer.parseInt(int[1])
            ((hours * 60) + min)
        }
    }

    private fun convertMinutesInHours(minutes: Int): EntityHourAndMinute{
        var min = minutes
        var hour = 0

        while (min >= 60){
            min -= 60
            hour++
        }
        return EntityHourAndMinute(hour, min)
    }

    fun convertMinutesInHoursString(minutes: Int): String{
        var minute = minutes
        var hour = 0

        while (minute >= 60){
            minute -= 60
            hour++
        }
        return if (hour < 10 && minute < 10){
            "0${hour}:0${minute}"
        }else if(hour < 10 && minute >= 10){
            "0${hour}:${minute}"
        }else if(hour >= 10 && minute < 10){
            "${hour}:0${minute}"
        }else{
            "${hour}:${minute}"
        }
    }
}