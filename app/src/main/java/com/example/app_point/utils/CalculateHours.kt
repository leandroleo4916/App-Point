package com.example.app_point.utils

import com.example.app_point.entity.*

class CalculateHours {

    fun calculateHoursDone(p: HourEntityInt?): Int {

        return if (p != null) {
            if (p.hora1 != null && p.hora2 != null && p.hora3 != null &&
                p.hora4 != null && p.extra != null){
                ((p.hora2 - p.hora1) + (p.hora4 - p.hora3)) - p.extra
            } else{ 0 }

        }else{ 0 }
    }

    fun calculateHoursExtra(time: Int, p: HourEntityInt): ArrayList<Int> {

        val value: ArrayList<Int> = arrayListOf()
        if (p.hora1 != null && p.hora2 != null && p.hora3 != null && p.hora4 != null){
            val hours = (p.hora2 - p.hora1) + (p.hora4 - p.hora3)
            val extra = hours - time
            val feitas = hours - extra
            value.add(extra, feitas)
        }
        else { return value }
        return value
    }

    fun calculateTimeEmployee(points: HourEntityInt): Int {

        val hour1 = points.hora1
        val hour2 = points.hora2
        val hour3 = points.hora3
        val hour4 = points.hora4

        return (hour2!! - hour1!!) + (hour4!! - hour3!!)
    }

    private fun formatHourToString(hour: Int, min: Int): String{

        return if (hour < 10 && min < 10){ "0$hour:0$min" }
        else if(hour < 10 && min >= 10){ "0$hour:$min" }
        else if(hour >= 10 && min < 10){ "${hour}:0${min}" }
        else{ "${hour}:${min}" }
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

    fun convertMinutesInHours(minutes: Int): String{

        var min = minutes
        var hour = 0
        while (min >= 60){
            min -= 60
            hour++
        }
        return formatHourToString(hour, min)
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

    fun punctuation(i: EmployeePointsTime, point: HourEntityInt?): Int {

        var note = 0

        if (point == null) note += 0
        else {
            note += if (point.hora1 == null) 0
            else { contIn(point.hora1, i.horario1) }

            note += if (point.hora2 == null) 0
            else { contOut(point.hora2, i.horario2) }

            note += if (point.hora3 == null) 0
            else { contIn(point.hora3, i.horario3) }

            note += if (point.hora4 == null) 0
            else { contOut(point.hora4, i.horario4) }
        }
        return note
    }

    private fun contIn (p1: Int, p2: Int): Int{
        return if (p1 <= p2) 2
        else if (p1 > p2 && p1 <= (p2 + 15)) 1
        else 0
    }

    private fun contOut (p1: Int, p2: Int): Int{
        return if (p1 >= p2) 2
        else if (p1 < p2 && p1 >= (p2 - 15)) 1
        else 0
    }

}