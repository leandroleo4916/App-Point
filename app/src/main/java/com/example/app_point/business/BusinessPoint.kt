package com.example.app_point.business

import android.content.Context
import com.example.app_point.repository.RepositoryPoint

class BusinessPoint(context: Context) {

    private val mRepositoryPoint: RepositoryPoint = RepositoryPoint(context)

    fun getPoint(hour: String, date: String, employee: String): Boolean{
        return mRepositoryPoint.getPoint(hour, date, employee)
    }
    fun storePointHour(): List<String>{
        return mRepositoryPoint.storePointHour()
    }
    fun storePointDate(): List<String>{
        return mRepositoryPoint.storePointDate()
    }

}