package com.example.app_point.utils

class ConverterHours {

    fun converterHoursInMinutes(hour: String): Int{
        val int = hour.split(":")
        val hours = Integer.parseInt(int[0])
        val min = Integer.parseInt(int[1])
        return ((hours * 60) + min)
    }
}