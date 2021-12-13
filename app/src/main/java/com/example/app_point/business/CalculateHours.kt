package com.example.app_point.business

import com.example.app_point.entity.*
import kotlin.collections.ArrayList

class CalculateHours {

    fun calculationHours(entity: PointsHours, hours: ArrayList<PointsEntity?>): EntityExtra{

        val hour1 = converterHoursInMinutes(entity.horario1!!)
        val hour2 = converterHoursInMinutes(entity.horario2!!)
        val hour3 = converterHoursInMinutes(entity.horario3!!)
        val hour4 = converterHoursInMinutes(entity.horario4!!)
        val schedule = (hour2 - hour1) + (hour4 - hour3)

        val value = hours.size
        var position = 0
        var extra = 0
        var horas = 0
        
        while (value != position){
            val h1 = hours[position]?.hora1?.let { converterHoursInMinutes(it) }
            val h2 = hours[position]?.hora2?.let { converterHoursInMinutes(it) }
            val h3 = hours[position]?.hora3?.let { converterHoursInMinutes(it) }
            val h4 = hours[position]?.hora4?.let { converterHoursInMinutes(it) }

            if (h1 == null || h2 == null || h3 == null || h4 == null){
                position++
            }
            else{
                val total = (h2 - h1) + (h4 - h3)
                if (total > schedule){
                    val x = total - schedule
                    extra += x
                    horas += schedule
                    position++
                }
                else{
                    horas += total
                    position++
                }
            }
        }

        return if (extra == 0){
            EntityExtra(0, 0, 0, 0)
        }else {
            val ex = convertMinutesInHours(extra)
            val hr = convertMinutesInHours(horas)
            EntityExtra(ex.hour, ex.minute, hr.hour, hr.minute)
        }
    }

    fun calculateHoursExtras(time: String?, points: HoursEntity): String {

        val hour1 = points.hora1?.let { converterHoursInMinutes(it) }
        val hour2 = points.hora2?.let { converterHoursInMinutes(it) }
        val hour3 = points.hora3?.let { converterHoursInMinutes(it) }
        val hour4 = points.hora4?.let { converterHoursInMinutes(it) }
        val workTime = time?.let { converterHoursInMinutes(it) }
        val total = ((hour2!! - hour1!!) + (hour4!! - hour3!!)) - workTime!!

        val ret = convertMinutesInHours(total)
        return formatHour(ret)
    }

    fun calculateTime(points: HoursEntity): String {

        val hour1 = points.hora1?.let { converterHoursInMinutes(it) }
        val hour2 = points.hora2?.let { converterHoursInMinutes(it) }
        val hour3 = points.hora3?.let { converterHoursInMinutes(it) }
        val hour4 = points.hora4?.let { converterHoursInMinutes(it) }
        val total = (hour2!! - hour1!!) + (hour4!! - hour3!!)

        val ret = convertMinutesInHours(total)
        return formatHour(ret)
    }

    private fun formatHour(ret: EntityHourAndMinute): String{
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

    private fun converterHoursInMinutes(hour: String): Int{
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
}