package com.example.app_point.business

import android.content.Context
import com.example.app_point.repository.RepositoryPoint

class BusinessPoints(context: Context) {

    private val mRepositoryPoint: RepositoryPoint = RepositoryPoint(context)

    fun getPoints(name: String, date: String, hour: String): Boolean{
        return mRepositoryPoint.getPoint(name, date, hour)
    }
}