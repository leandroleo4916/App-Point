package com.example.app_point.business

import android.content.Context
import com.example.app_point.repository.ReposiitoryUser
import com.example.app_point.utils.UserEntity

class BusinessUser(context: Context) {

    private val mReposiitoryUser: ReposiitoryUser = ReposiitoryUser(context)

    fun storeUser(name: String, senha: String): Boolean {
        val user: UserEntity? = mReposiitoryUser.storeUser(name, senha)
        return user != null
    }

}