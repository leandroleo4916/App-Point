package com.example.app_point.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.app_point.constants.ConstantsEmployee
import com.example.app_point.constants.ConstantsExtras
import com.example.app_point.constants.ConstantsPoint

class DataBaseEmployee(context: Context?) : SQLiteOpenHelper(context, DATA_NAME, null, DATA_VERSION) {

    companion object {
        private const val DATA_NAME: String = "employee.db"
        private const val DATA_VERSION: Int = 11
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        if (!db.isReadOnly) { db.execSQL("PRAGMA foreign_key=ON;") }
    }

    private val createTableEmployee = """ CREATE TABLE 
            ${ConstantsEmployee.EMPLOYEE.TABLE_NAME} (
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.ID} integer primary key autoincrement ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.STATUS} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO} blob ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO1} integer ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO2} integer ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO3} integer ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO4} integer ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.WORKLOAD} integer ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.NAME} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.EMAIL} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.PHONE} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.ANIVERSARIO} text ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.RG} integer ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.CPF} integer ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.CTPS} integer ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.SALARIO} integer ,
            ${ConstantsEmployee.EMPLOYEE.COLUMNS.ESTADOCIVIL} text 
    );"""
    private val createTablePoint = """ CREATE TABLE 
            ${ConstantsPoint.POINT.TABLE_NAME}(
            ${ConstantsPoint.POINT.COLUMNS.ID} integer primary key autoincrement ,
            ${ConstantsPoint.POINT.COLUMNS.EMPLOYEE} text ,
            ${ConstantsPoint.POINT.COLUMNS.DATE} text ,
            ${ConstantsPoint.POINT.COLUMNS.HOUR1INT} integer ,
            ${ConstantsPoint.POINT.COLUMNS.HOUR2INT} integer ,
            ${ConstantsPoint.POINT.COLUMNS.HOUR3INT} integer ,
            ${ConstantsPoint.POINT.COLUMNS.HOUR4INT} integer ,
            ${ConstantsPoint.POINT.COLUMNS.PUNCTUATION} integer ,
            ${ConstantsPoint.POINT.COLUMNS.HOUR1} text ,
            ${ConstantsPoint.POINT.COLUMNS.HOUR2} text ,
            ${ConstantsPoint.POINT.COLUMNS.HOUR3} text ,
            ${ConstantsPoint.POINT.COLUMNS.HOUR4} text ,
            ${ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE} text not null ,
            FOREIGN KEY (${ConstantsPoint.POINT.COLUMNS.ID}) 
                REFERENCES ${ConstantsEmployee.EMPLOYEE.TABLE_NAME}
                (${ConstantsEmployee.EMPLOYEE.COLUMNS.ID}) ON DELETE CASCADE ON UPDATE CASCADE
    );"""
    private val createTableExtra = """ CREATE TABLE 
            ${ConstantsExtras.EXTRA.TABLE_NAME}(
            ${ConstantsExtras.EXTRA.COLUMNS.ID} integer ,
            ${ConstantsExtras.EXTRA.COLUMNS.EXTRA} integer ,
            ${ConstantsExtras.EXTRA.COLUMNS.FEITAS} integer ,
            FOREIGN KEY (${ConstantsExtras.EXTRA.COLUMNS.ID}) 
                REFERENCES ${ConstantsEmployee.EMPLOYEE.TABLE_NAME}
                (${ConstantsEmployee.EMPLOYEE.COLUMNS.ID}) ON DELETE CASCADE ON UPDATE CASCADE
    );"""

    private val removeTableEmployee = "drop table if exists ${ConstantsEmployee.EMPLOYEE.TABLE_NAME}"
    private val removeTablePoint = "drop table if exists ${ConstantsPoint.POINT.TABLE_NAME}"
    private val removeTableExtra = "drop table if exists ${ConstantsExtras.EXTRA.TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createTableEmployee)
        db.execSQL(createTablePoint)
        db.execSQL(createTableExtra)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(removeTableEmployee)
        db.execSQL(removeTablePoint)
        db.execSQL(removeTableExtra)
        db.execSQL(createTableEmployee)
        db.execSQL(createTablePoint)
        db.execSQL(createTableExtra)
    }
}