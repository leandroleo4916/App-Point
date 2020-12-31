package com.example.app_point.business

import android.content.Context
import com.example.app_point.repository.ReposiitoryUser

class BusinessUser(context: Context) {

    private val mRepositoryUser: ReposiitoryUser = ReposiitoryUser(context)

    fun getUser(name:String, email: String, password: String): Boolean{
        return mRepositoryUser.getUser(name, email, password)
    }
    fun storeUser(user: String, password: String): Boolean{
        return mRepositoryUser.storeUser(user, password)
    }
}