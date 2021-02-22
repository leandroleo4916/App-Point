package com.example.app_point.business

import android.content.Context
import com.example.app_point.constants.ConstantsUser
import com.example.app_point.repository.ReposiitoryUser
import com.example.app_point.utils.SecurityPreferences
import com.example.app_point.entity.UserEntity

class BusinessUser(context: Context) {

    private val mRepositoryUser: ReposiitoryUser = ReposiitoryUser(context)
    private val mSecurityPreferences: SecurityPreferences = SecurityPreferences(context)

    fun getUser(name: String, email: String, password: String): Boolean {
        return when {
            mRepositoryUser.getUser(name, email, password) -> {
                mSecurityPreferences.storeString(ConstantsUser.USER.COLUNAS.NAME, name)
                mSecurityPreferences.storeString(ConstantsUser.USER.COLUNAS.EMAIL, email)
                mSecurityPreferences.storeString(ConstantsUser.USER.COLUNAS.PASSWORD, password)

                true
            }
            else -> {
                false
            }
        }
    }

    fun storeUser(name: String, password: String): Boolean {

        val user: UserEntity? = mRepositoryUser.storeUser(name, password)
        return if (user != null) {
            mSecurityPreferences.storeString(ConstantsUser.USER.COLUNAS.NAME, user.nome)
            mSecurityPreferences.storeString(ConstantsUser.USER.COLUNAS.EMAIL, user.email)
            mSecurityPreferences.storeString(ConstantsUser.USER.COLUNAS.PASSWORD, user.senha)

            true
        } else {
            false
        }
    }
}