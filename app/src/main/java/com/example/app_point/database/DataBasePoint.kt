package com.example.app_point.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBasePoint(context: Context?) : SQLiteOpenHelper(context, DATA_NAME, null, DATA_VERSION) {

    companion object {
        private const val DATA_NAME: String = "employee.db"
        private const val DATA_VERSION: Int = 1
    }

    private val createTablePoint = """ CREATE TABLE 
            ${ConstantsPoint.POINT.TABLE_NAME} (
            ${ConstantsPoint.POINT.COLUMNS.ID} integer primary key autoincrement ,
            ${ConstantsPoint.POINT.COLUMNS.HOUR} text ,
            ${ConstantsPoint.POINT.COLUMNS.DATE} text ,
            ${ConstantsPoint.POINT.COLUMNS.EMPLOYEE} text 
    );"""

    private val removeTablePoint = "drop table if exists ${ConstantsPoint.POINT.TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createTablePoint)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(removeTablePoint)
        db.execSQL(createTablePoint)
    }
}