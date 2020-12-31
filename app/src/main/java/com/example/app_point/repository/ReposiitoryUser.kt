package com.example.app_point.repository

import android.content.ContentValues
import android.content.Context
import com.example.app_point.database.ConstantsUser
import com.example.app_point.database.DataBaseUser

class ReposiitoryUser(context: Context) {

    private val mDataBaseUser: DataBaseUser = DataBaseUser(context)

    fun getUser(name: String, email: String, password: String): Boolean {

        return try{
            val db = mDataBaseUser.writableDatabase
            val insertValues = ContentValues()
            insertValues.put(ConstantsUser.USER.COLUNAS.NAME, name)
            insertValues.put(ConstantsUser.USER.COLUNAS.EMAIL, email)
            insertValues.put(ConstantsUser.USER.COLUNAS.PASSWORD, password)

            db.insert(ConstantsUser.USER.TABLE_NAME, null, insertValues)
            true

        } catch (e: Exception){
            false
        }
    }

    fun storeUser(email: String, password: String): Boolean{
        return try {
            val db = mDataBaseUser.readableDatabase

            true
        }catch (e: Exception) {
            false
        }
    }

}