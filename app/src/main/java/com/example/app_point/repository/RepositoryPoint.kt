package com.example.app_point.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.app_point.constants.ConstantsEmployee
import com.example.app_point.constants.ConstantsPoint
import com.example.app_point.database.DataBaseEmployee

class RepositoryPoint(context: Context?) {

    private val mDataBasePoint: DataBaseEmployee = DataBaseEmployee(context)

    fun getPoint(employee: String, date: String, hour: String): Boolean {

        return try {
            val db = mDataBasePoint.writableDatabase
            val insertValues = ContentValues()
            insertValues.put(ConstantsPoint.POINT.COLUMNS.EMPLOYEE, employee)
            insertValues.put(ConstantsPoint.POINT.COLUMNS.DATE, date)
            insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUR, hour)
            db.insert(ConstantsPoint.POINT.TABLE_NAME, null, insertValues)
            true

        } catch (e: Exception) {
            false
        }
    }

    fun storePointEmployee (): List<String> {

        val list: ArrayList<String> = ArrayList()
        try {
            val cursor: Cursor
            val db = mDataBasePoint.readableDatabase
            val projection = arrayOf(ConstantsPoint.POINT.COLUMNS.EMPLOYEE)
            val orderBy = ConstantsPoint.POINT.COLUMNS.ID

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, null, null,
                null, null, orderBy
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val employee =
                        cursor.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.EMPLOYEE))
                    list.add(employee)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    fun storePointHour (): List<String> {

        val list: ArrayList<String> = ArrayList()
        try {
            val cursor: Cursor
            val db = mDataBasePoint.readableDatabase
            val projection = arrayOf(ConstantsPoint.POINT.COLUMNS.HOUR)

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

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, null, null,
                null, null, null
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

    fun storeSelectName (nome: String): List<String> {

        val list: ArrayList<String> = arrayListOf()
        try {
            val cursor: Cursor
            val db = mDataBasePoint.readableDatabase
            val projection = arrayOf(ConstantsPoint.POINT.COLUMNS.EMPLOYEE)
            val selection = ConstantsEmployee.EMPLOYEE.COLUMNS.NAME + " = ?"
            val args = arrayOf(nome)

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val employee = cursor.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.EMPLOYEE))

                    list.add(employee)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    fun storeSelectDate (nome: String): List<String> {

        val list: ArrayList<String> = arrayListOf()
        try {
            val cursor: Cursor
            val db = mDataBasePoint.readableDatabase
            val projection = arrayOf(ConstantsPoint.POINT.COLUMNS.DATE)
            val selection = ConstantsEmployee.EMPLOYEE.COLUMNS.NAME + " = ?"
            val args = arrayOf(nome)

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val date = cursor.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.DATE))

                    list.add(date)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    fun storeSelectHours (nome: String): List<String> {

        val list: ArrayList<String> = arrayListOf()
        try {
            val cursor: Cursor
            val db = mDataBasePoint.readableDatabase
            val projection = arrayOf(ConstantsPoint.POINT.COLUMNS.HOUR)
            val selection = ConstantsEmployee.EMPLOYEE.COLUMNS.NAME + " = ?"
            val args = arrayOf(nome)

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val hours = cursor.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR))

                    list.add(hours)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

}