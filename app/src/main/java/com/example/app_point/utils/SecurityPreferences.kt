package com.example.app_point.utils

import android.content.Context

class SecurityPreferences(context: Context) {

    private val mSharedPreferences = context.getSharedPreferences("App-Point", Context.MODE_PRIVATE)

    fun storeUser(key: String, value: String){
        mSharedPreferences.edit().putString(key, value).apply()
    }

    fun getUser(key: String): String? {
        return mSharedPreferences.getString(key, "")
    }

}