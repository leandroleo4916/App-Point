package com.example.app_point.utils

import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.util.*

class CaptureDateCurrent {
    fun captureDateCurrent(): String{
        val calendar = Calendar.getInstance().time
        val local = Locale("pt", "BR")
        val dateCalendar = SimpleDateFormat("dd/MM/yyyy", local)
        return dateCalendar.format(calendar)
    }
    fun captureHoraCurrent(): String{
        val date = java.util.Calendar.getInstance().time
        val local = Locale("pt", "BR")
        val hora = SimpleDateFormat("HH:mm", local)
        return hora.format(date)
    }
}