package com.example.app_point.business

import com.example.app_point.repository.RepositoryPoint

class BusinessPoints(private val mRepositoryPoint: RepositoryPoint) {

    fun setPoints(name: String, date: String, hour: String): Boolean{
        return mRepositoryPoint.setPoint(name, date, hour)
    }
}