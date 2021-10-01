package com.example.app_point.business

import com.example.app_point.entity.EntityExtra
import com.example.app_point.entity.HoursEntity
import com.example.app_point.entity.PointsHours
import kotlin.collections.ArrayList

class CalculationHours {

    fun calculationHours(entity: PointsHours, hours: ArrayList<HoursEntity>): ArrayList<EntityExtra>{

        val ret: ArrayList<EntityExtra> = arrayListOf()

        val horario1 = convertHoursInMinutes(entity.horario1)
        val horario2 = convertHoursInMinutes(entity.horario2)
        val horario3 = convertHoursInMinutes(entity.horario3)
        val horario4 = convertHoursInMinutes(entity.horario4)
        val horario  = (horario2 - horario1) + (horario4 - horario3)

        var position = 0
        var extra = 0
        var horas = 0
        while (hours[position].hora1 != "" || hours[position].hora2 != "" ||
               hours[position].hora3 != "" || hours[position].hora4 != "" ){

            val h1 = convertHoursInMinutes(hours[position].hora1!!)
            val h2 = convertHoursInMinutes(hours[position].hora2!!)
            val h3 = convertHoursInMinutes(hours[position].hora3!!)
            val h4 = convertHoursInMinutes(hours[position].hora4!!)
            val total = (h2 - h1) + (h4 - h3)

            if (total > horario){
                val x = total - horario
                extra += x
                horas += horario
                position++
            }
            else{
                horas += total
                position++
            }
        }

        val ex = convertMinutesInHours(extra)
        val hr = convertMinutesInHours(horas)
        ret.add(EntityExtra(ex[0], ex[1], hr[0], hr[1]))

        return ret
    }

    private fun convertHoursInMinutes(hora: String): Int{
        val minutes = hora.split(":")
        return (minutes[0].toInt() * 60) + minutes[1].toInt()
    }

    private fun convertMinutesInHours(minutes: Int): ArrayList<Int>{
        val array: ArrayList<Int> = arrayListOf()
        var min = minutes
        var hour = 0
        while (min >= 60){
            min - 60
            hour++
        }
        array.add(hour, min)
        return array

    }
}