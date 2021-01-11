package com.example.app_point.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.app_point.database.ConstantsEmployee
import com.example.app_point.database.ConstantsPoint
import com.example.app_point.database.DataBaseEmployee

class RepositoryPoint(context: Context?) {

    private val mDataBasePoint: DataBaseEmployee = DataBaseEmployee(context)

    fun getPoint(hour: String, date: String, employee: String): Boolean {

        return try {
            val db = mDataBasePoint.writableDatabase
            val insertValues = ContentValues()
            insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUR, hour)
            insertValues.put(ConstantsPoint.POINT.COLUMNS.DATE, date)
            insertValues.put(ConstantsPoint.POINT.COLUMNS.EMPLOYEE, employee)
            db.insert(ConstantsPoint.POINT.TABLE_NAME, null, insertValues)
            true

        } catch (e: Exception) {
            false
        }
    }

    fun storePointHour (): List<String> {

        val list: ArrayList<String> = ArrayList()
        try {
            val cursor: Cursor
            val db = mDataBasePoint.readableDatabase
            val projection = arrayOf(ConstantsPoint.POINT.COLUMNS.HOUR)
            //val orderBy = ConstantsEmployee.EMPLOYEE.COLUMNS.NAME

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, null, null,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val hours =
                        cursor.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR))
                    list.add(hours)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    fun storePointDate (): List<String> {

        val list: ArrayList<String> = ArrayList()
        try {
            val cursor: Cursor
            val db = mDataBasePoint.readableDatabase
            val projection = arrayOf(ConstantsPoint.POINT.COLUMNS.DATE)
            val orderBy = ConstantsEmployee.EMPLOYEE.COLUMNS.NAME

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, null, null,
                null, null, orderBy
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val date =
                        cursor.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.DATE))
                    list.add(date)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }
}