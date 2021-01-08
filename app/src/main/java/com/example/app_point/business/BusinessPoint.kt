package com.example.app_point.business

import android.content.Context
import com.example.app_point.repository.RepositoryPoint
import java.util.*

class BusinessPoint(context: Context) {

    private val mRepositoryPoint: RepositoryPoint = RepositoryPoint(context)

    fun getPoint(hour: String, date: String): Boolean{
        return mRepositoryPoint.getPoint(hour, date)
    }

}