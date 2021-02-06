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
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO1} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO2} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO3} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO4} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.NAME} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.EMAIL} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.PHONE} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.ANIVERSARIO} text 
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