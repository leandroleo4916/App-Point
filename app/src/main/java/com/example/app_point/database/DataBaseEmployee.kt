package com.example.app_point.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseEmployee(context: Context?) : SQLiteOpenHelper(context, DATA_NAME, null, DATA_VERSION) {

    companion object {
        private const val DATA_NAME: String = "employee.db"
        private const val DATA_VERSION: Int = 1
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        when {
            !db.isReadOnly -> {
                db.execSQL("PRAGMA foreign_keys=ON")
            }
        }
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
    private val createTablePoint = """ CREATE TABLE 
            ${ConstantsPoint.POINT.TABLE_NAME} (
            ${ConstantsPoint.POINT.COLUMNS.ID} integer primary key autoincrement ,
            ${ConstantsPoint.POINT.COLUMNS.HOUR} text ,
            ${ConstantsPoint.POINT.COLUMNS.DATE} text ,
            FOREIGN KEY (${ConstantsPoint.POINT.COLUMNS.HOUR}) 
            REFERENCES ${ConstantsEmployee.EMPLOYEE.TABLE_NAME} (${ConstantsEmployee.EMPLOYEE.COLUMNS.NAME})
    );"""

    private val removeTableEmployee = "drop table if exists ${ConstantsEmployee.EMPLOYEE.TABLE_NAME}"
    private val removeTablePoint = "drop table if exists ${ConstantsPoint.POINT.TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createTableEmployee)
        db.execSQL(createTablePoint)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(removeTableEmployee)
        db.execSQL(removeTablePoint)
        db.execSQL(createTableEmployee)
        db.execSQL(createTablePoint)
    }


}