package com.example.app_point.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.app_point.constants.ConstantsEmployee
import com.example.app_point.constants.ConstantsPoint

class DataBaseEmployee(context: Context?) : SQLiteOpenHelper(context, DATA_NAME, null, DATA_VERSION) {

    companion object {
        private const val DATA_NAME: String = "employee.db"
        private const val DATA_VERSION: Int = 2
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        if (!db.isReadOnly) {
            db.execSQL("PRAGMA foreign_key=ON;")
        }
    }

    private val createTableEmployee = """ CREATE TABLE 
            ${ConstantsEmployee.EMPLOYEE.TABLE_NAME} (
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.ID} integer primary key autoincrement ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO} blob ,
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
    private val createTablePoint = """ CREATE TABLE 
            ${ConstantsPoint.POINT.TABLE_NAME}(
            ${ConstantsPoint.POINT.COLUMNS.ID} integer primary key autoincrement ,
            ${ConstantsPoint.POINT.COLUMNS.DATE} text ,
            ${ConstantsPoint.POINT.COLUMNS.HOUR1} text ,
            ${ConstantsPoint.POINT.COLUMNS.HOUR2} text ,
            ${ConstantsPoint.POINT.COLUMNS.HOUR3} text ,
            ${ConstantsPoint.POINT.COLUMNS.HOUR4} text ,
            ${ConstantsPoint.POINT.COLUMNS.EMPLOYEE} text not null ,
            FOREIGN KEY (${ConstantsPoint.POINT.COLUMNS.EMPLOYEE}) 
                REFERENCES ${ConstantsEmployee.EMPLOYEE.TABLE_NAME}
                (${ConstantsEmployee.EMPLOYEE.COLUMNS.NAME}) ON DELETE CASCADE ON UPDATE CASCADE
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