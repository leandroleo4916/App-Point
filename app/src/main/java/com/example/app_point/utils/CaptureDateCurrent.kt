package com.example.app_point.utils

import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.util.*

class CaptureDateCurrent {

    private val local = Locale("pt", "BR")
    private val date = SimpleDateFormat("dd/MM/yyyy", local)
    private val hora = SimpleDateFormat("HH:mm", local)
    private val horaSecond = SimpleDateFormat("HH:mm:ss", local)

    fun captureDateCurrent(): String{
        val calendar = Calendar.getInstance().time
        return date.format(calendar)
    }

    fun captureHoraCurrent(): String{
        val calendar = Calendar.getInstance().time
        return hora.format(calendar)
    }

    fun captureHoraCurrentSecond(): String{
        val calendar = Calendar.getInstance().time
        return horaSecond.format(calendar)
    }

    fun captureFirstDayOfMonth (): String {
        val calendar = Calendar.getInstance()
        val dateCurrent = captureDateCurrent()
        val dateDiv = dateCurrent.split("/")

        var day = 1
        val dateFirst = if (day < 10) "0"+day+"/"+dateDiv[1]+"/"+dateDiv[2]
        else "$day"+"/"+dateDiv[1]+"/"+dateDiv[2]

        if (dateFirst == "0"+day+"/"+dateDiv[1]+"/"+dateDiv[2]){ return dateFirst }

        var data = date.parse(dateFirst)
        while (dateFirst != dateCurrent){

            calendar.time = data
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            data = calendar.time
            day++
        }

        return data!!.toString()
    }

    fun captureNextDate(date: String): String{
        val calendar = Calendar.getInstance()
        val data = this.date.parse(date)
        calendar.time = data
        calendar.add(Calendar.DAY_OF_MONTH, +1)
        return this.date.format(calendar.time)
    }
}