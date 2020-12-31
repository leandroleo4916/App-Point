package com.example.app_point.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseUser(context: Context) : SQLiteOpenHelper(context, DATA_NAME, null, DATA_VERSION) {

    companion object {
        private const val DATA_NAME: String = "user.db"
        private const val DATA_VERSION: Int = 1
    }

    private val createTable = """ CREATE TABLE 
            ${ConstantsUser.USER.TABLE_NAME} (
            ${ConstantsUser.USER.COLUNAS.ID} integer primary key autoincrement ,
            ${ConstantsUser.USER.COLUNAS.NAME} text ,
            ${ConstantsUser.USER.COLUNAS.EMAIL} text ,
            ${ConstantsUser.USER.COLUNAS.PASSWORD} text 

    );"""

    private val removeTable = "drop table if exists ${ConstantsUser.USER.TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(removeTable)
        db.execSQL(createTable)
    }


}