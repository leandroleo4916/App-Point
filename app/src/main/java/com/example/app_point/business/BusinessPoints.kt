package com.example.app_point.business

import android.content.Context
import com.example.app_point.repository.RepositoryPoint

class BusinessPoints(context: Context) {

    private val mRepositoryPoint: RepositoryPoint = RepositoryPoint(context)

    fun getPoints(name: String, hour: String, date: String): Boolean{

        return mRepositoryPoint.getPoint(name, hour, date)

    }

}