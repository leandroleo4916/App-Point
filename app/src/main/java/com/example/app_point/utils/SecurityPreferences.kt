package com.example.app_point.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.app_point.database.ConstantsUser

class SecurityPreferences(context: Context) {

    private val mSharedPreferences: SharedPreferences = context.getSharedPreferences("employ", Context.MODE_PRIVATE)

    // Insere chave e valor ao Shared
    fun storeString(key: String, value: String) {
        mSharedPreferences.edit().putString(key, value).apply()
    }

    // Captura valor do Shared
    fun getStoredString(key: String) : String{
        return mSharedPreferences.getString(key,"").toString()
    }

    // Remove valores do Shared
    fun removeString(){
        mSharedPreferences.edit().remove(ConstantsUser.USER.COLUNAS.NAME).apply()
        mSharedPreferences.edit().remove(ConstantsUser.USER.COLUNAS.EMAIL).apply()
        mSharedPreferences.edit().remove(ConstantsUser.USER.COLUNAS.PASSWORD).apply()
    }
}