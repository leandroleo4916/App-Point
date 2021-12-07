package com.example.app_point.utils

import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.util.*

class CaptureDateCurrent {

    private val calendar = Calendar.getInstance().time
    private val local = Locale("pt", "BR")
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", local)
    private val hora = SimpleDateFormat("HH:mm", local)

    fun captureDateCurrent(): String{
        return dateFormat.format(calendar)
    }

    fun captureHoraCurrent(): String{
        return hora.format(calendar)
    }

    fun captureDate(): String {
        val calendar = Calendar.getInstance()
        val dateCurrent = captureDateCurrent()
        val dateDiv = dateCurrent.split("/")

        var day = 1
        val dateFirst = if (day < 10){
            "0"+day+"/"+dateDiv[1]+"/"+dateDiv[2]
        } else {
            "$day"+"/"+dateDiv[1]+"/"+dateDiv[2]
        }

        if (dateFirst == "0"+day+"/"+dateDiv[1]+"/"+dateDiv[2]){
            return dateFirst
        }

        var data = dateFormat.parse(dateFirst)
        while (dateFirst != dateCurrent){

            calendar.time = data
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            data = calendar.time
            day++
        }

        return data!!.toString()
    }
}