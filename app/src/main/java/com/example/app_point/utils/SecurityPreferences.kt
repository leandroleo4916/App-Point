package com.example.app_point.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.app_point.constants.ConstantsUser

class SecurityPreferences(context: Context) {

    private val mSharedPreferences: SharedPreferences = context.getSharedPreferences("employ", Context.MODE_PRIVATE)

    fun storeString(key: String, value: String) {
        mSharedPreferences.edit().putString(key, value).apply()
    }

    fun getStoredString(key: String) : String{
        return mSharedPreferences.getString(key,"").toString()
    }

    fun removeString(){
        mSharedPreferences.edit().remove(ConstantsUser.USER.COLUNAS.NAME).apply()
        mSharedPreferences.edit().remove(ConstantsUser.USER.COLUNAS.EMAIL).apply()
        mSharedPreferences.edit().remove(ConstantsUser.USER.COLUNAS.PASSWORD).apply()
    }
}