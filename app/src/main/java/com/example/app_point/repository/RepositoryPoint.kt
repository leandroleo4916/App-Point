package com.example.app_point.repository

import android.content.ContentValues
import android.content.Context
import com.example.app_point.database.ConstantsPoint
import com.example.app_point.database.DataBasePoint

class RepositoryPoint(context: Context) {

    private val mDataBasePoint: DataBasePoint = DataBasePoint(context)

    fun getPoint(hour: String, date: String): Boolean {

        return try{
            val db = mDataBasePoint.writableDatabase
            val insertValues = ContentValues()
            insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUR, hour)
            insertValues.put(ConstantsPoint.POINT.COLUMNS.DATE, date)

            db.insert(ConstantsPoint.POINT.TABLE_NAME, null, insertValues)
            true

        } catch (e: Exception){
            false
        }
    }

}