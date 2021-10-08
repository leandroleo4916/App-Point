package com.example.app_point.business

import com.example.app_point.entity.EntityExtra
import com.example.app_point.entity.HoursEntity
import com.example.app_point.entity.PointsHours
import kotlin.collections.ArrayList

class CalculationHours {

    fun calculationHours(entity: PointsHours, hours: ArrayList<HoursEntity>): ArrayList<EntityExtra>{

        val ret: ArrayList<EntityExtra> = arrayListOf()
        val schedule = scheduleWork(entity)

        val value = hours.size
        var position = 0
        var extra = 0
        var horas = 0
        
        while (value != position){

            val h1 = convertHoursInMinutes(hours[position].hora1!!)
            val h2 = convertHoursInMinutes(hours[position].hora2!!)
            val h3 = convertHoursInMinutes(hours[position].hora3!!)
            val h4 = convertHoursInMinutes(hours[position].hora4!!)

            if (h1 != null || h2 != null || h3 != null || h4 != null){
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
            else{
                position++
            }
        }

        val ex = convertMinutesInHours(extra)
        val hr = convertMinutesInHours(horas)
        ret.add(EntityExtra(ex[0], ex[1], hr[0], hr[1]))

        return ret
    }

    private fun scheduleWork(entity: PointsHours): Int{
        val h1 = convertHoursInMinutes(entity.horario1)
        val h2 = convertHoursInMinutes(entity.horario2)
        val h3 = convertHoursInMinutes(entity.horario3)
        val h4 = convertHoursInMinutes(entity.horario4)
        return (h2 - h1) + (h4 - h3)
    }

    private fun convertHoursInMinutes(hora: String): Int{
        val minutes = hora.split(":")
        return (minutes[0].toInt() * 60) + minutes[1].toInt()
    }

    private fun convertMinutesInHours(minutes: Int): ArrayList<Int>{
        val array: ArrayList<Int> = arrayListOf()
        var hour = 0
        while (minutes >= 60){
            minutes - 60
            hour++
        }
        array.add(hour, minutes)
        return array
    }
}