package com.example.app_point.business

import com.example.app_point.repository.ReposiitoryUser

class BusinessUser {

    private val mRepositoryUser: ReposiitoryUser = ReposiitoryUser()

    fun getUser(user: String, password: String): Boolean{
        return mRepositoryUser.getUser(user, password)
    }
}