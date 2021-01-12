package com.example.app_point.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseEmployee(context: Context?) : SQLiteOpenHelper(context, DATA_NAME, null, DATA_VERSION) {

    companion object {
        private const val DATA_NAME: String = "employee.db"
        private const val DATA_VERSION: Int = 1
    }

    private val createTableEmployee = """ CREATE TABLE 
            ${ConstantsEmployee.EMPLOYEE.TABLE_NAME} (
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.ID} integer primary key autoincrement ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.NAME} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.OFFICE} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.EMAIL} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.PHONE} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.HOURINONE} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.HOURINTWO} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.HOUROUTONE} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.HOUROUTTWO} text
    );"""

    private val removeTableEmployee = "drop table if exists ${ConstantsEmployee.EMPLOYEE.TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createTableEmployee)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(removeTableEmployee)
        db.execSQL(createTableEmployee)
    }
}