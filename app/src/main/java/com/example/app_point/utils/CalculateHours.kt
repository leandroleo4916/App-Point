package com.example.app_point.utils

import com.example.app_point.entity.*

class CalculateHours {

    fun calculateHoursDone(p: HourEntityInt?): Int {

        return if (p != null) {
            if (p.hora1 != null && p.hora2 != null && p.hora3 != null && p.hora4 != null){

                (p.hora2 - p.hora1) + (p.hora4 - p.hora3)
            } else{ 0 }

        }else{ 0 }
    }

    fun calculateHoursExtra(time: Int, p: HourEntityInt): Int {

        return if (p.hora1 != null && p.hora2 != null && p.hora3 != null && p.hora4 != null){

                ((p.hora2 - p.hora1) + (p.hora4 - p.hora3)) - time
            }
            else{ 0 }
    }

    fun calculateTimeEmployee(points: HourEntityInt): Int {

        val hour1 = points.hora1
        val hour2 = points.hora2
        val hour3 = points.hora3
        val hour4 = points.hora4

        return (hour2!! - hour1!!) + (hour4!! - hour3!!)
    }

    private fun formatHourToString(ret: EntityHourAndMinute): String{

        return if (ret.hour < 10 && ret.minute < 10){ "0${ret.hour}:0${ret.minute}" }
        else if(ret.hour < 10 && ret.minute >= 10){ "0${ret.hour}:${ret.minute}" }
        else if(ret.hour >= 10 && ret.minute < 10){ "${ret.hour}:0${ret.minute}" }
        else{ "${ret.hour}:${ret.minute}" }
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
        return if (hour < 10 && minute < 10){ "0${hour}:0${minute}" }
        else if(hour < 10 && minute >= 10){ "0${hour}:${minute}" }
        else if(hour >= 10 && minute < 10){ "${hour}:0${minute}" }
        else{ "${hour}:${minute}" }
    }
}