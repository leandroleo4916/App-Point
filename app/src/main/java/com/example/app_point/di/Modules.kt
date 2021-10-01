package com.example.app_point.di

import com.example.app_point.interfaces.RepositoryData
import com.example.app_point.repository.RepositoryPoint
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object Modules {

    fun load(){
        loadKoinModules(repositoryModule())
    }

    private fun repositoryModule(): Module {
        return module {
            single<RepositoryData> { RepositoryPoint(get()) }
        }
    }
}