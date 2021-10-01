package com.example.app_point.business

import com.example.app_point.interfaces.RepositoryData

class BusinessPoints(private val mRepositoryPoint: RepositoryData) {

    fun getPoints(name: String, date: String, hour: String): Boolean{
        return mRepositoryPoint.setPoint(name, date, hour)
    }
}