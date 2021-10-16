package com.example.app_point.business

import com.example.app_point.constants.ConstantsUser
import com.example.app_point.repository.RepositoryUser
import com.example.app_point.utils.SecurityPreferences
import com.example.app_point.entity.UserEntity

class BusinessUser(private val securityPreferences: SecurityPreferences,
                   private val repositoryUser: RepositoryUser) {

    fun setUser(name: String, email: String, password: String): Boolean {
        return when {
            repositoryUser.setUser(name, email, password) -> {
                securityPreferences.run {
                    storeString(ConstantsUser.USER.COLUNAS.NAME, name)
                    storeString(ConstantsUser.USER.COLUNAS.EMAIL, email)
                    storeString(ConstantsUser.USER.COLUNAS.PASSWORD, password)
                }
                true
            }
            else -> false
        }
    }

    fun storeUser(name: String, password: String): Boolean {

        val user: UserEntity? = repositoryUser.storeUser(name, password)
        return when {
            user != null -> {
                securityPreferences.run {
                    storeString(ConstantsUser.USER.COLUNAS.NAME, user.nome)
                    storeString(ConstantsUser.USER.COLUNAS.EMAIL, user.email)
                    storeString(ConstantsUser.USER.COLUNAS.PASSWORD, user.senha)
                }
                true
            }
            else -> false
        }
    }
}