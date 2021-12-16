package com.example.app_point.business

import com.example.app_point.repository.RepositoryPoint
import com.example.app_point.utils.CalculateHours

class BusinessPoints(private val mRepositoryPoint: RepositoryPoint,
                     private val converterHours: CalculateHours
) {

    fun setPoints(name: String, date: String, hour: String): Boolean{
        val hourInt = converterHours.converterHoursInMinutes(hour)
        return mRepositoryPoint.setPoint(name, date, hour, hourInt)
    }
}