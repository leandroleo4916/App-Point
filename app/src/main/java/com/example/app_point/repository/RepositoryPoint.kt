package com.example.app_point.repository

import android.content.ContentValues
import android.content.Context
import com.example.app_point.database.ConstantsPoint
import com.example.app_point.database.DataBaseEmployee

class RepositoryPoint(context: Context) {

    private val mDataBasePoint: DataBaseEmployee = DataBaseEmployee(context)

    fun getPoint(hour: String, date: String, employee: String): Boolean {

        return try{
            val db = mDataBasePoint.writableDatabase
            val insertValues = ContentValues()
            insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUR, hour)
            insertValues.put(ConstantsPoint.POINT.COLUMNS.DATE, date)
            insertValues.put(ConstantsPoint.POINT.COLUMNS.EMPLOYEE, employee)
            db.insert(ConstantsPoint.POINT.TABLE_NAME, null, insertValues)
            true

        } catch (e: Exception){
            false
        }
    }

}