package com.example.app_point.activitys

import android.app.Application
import com.example.app_point.di.Modules
import com.example.app_point.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApp)
            modules(appModules)
        }
        //Modules.load()
    }
}