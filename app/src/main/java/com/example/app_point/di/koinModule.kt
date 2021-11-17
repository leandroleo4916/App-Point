package com.example.app_point.di

import com.example.app_point.activitys.ui.employee.EmployeeViewModel
import com.example.app_point.adapters.AdapterPoints
import com.example.app_point.adapters.PointsAdapter
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.business.BusinessPoints
import com.example.app_point.business.BusinessUser
import com.example.app_point.business.CalculationHours
import com.example.app_point.database.DataBaseEmployee
import com.example.app_point.database.DataBaseUser
import com.example.app_point.model.*
import com.example.app_point.repository.RepositoryEmployee
import com.example.app_point.repository.RepositoryFirebase
import com.example.app_point.repository.RepositoryPoint
import com.example.app_point.repository.RepositoryUser
import com.example.app_point.utils.CaptureDateCurrent
import com.example.app_point.utils.ConverterPhoto
import com.example.app_point.utils.SecurityPreferences
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryUserModule = module {
    single { RepositoryUser(get()) }
}

val repositoryEmployeeModule = module {
    single { RepositoryEmployee(get()) }
}

val repositoryPointModule = module {
    single { RepositoryPoint(get()) }
}

val repositoryModule = module {
    single { RepositoryFirebase(get()) }
}

val businessModule = module {
    factory { BusinessEmployee(get()) }
}

val pointsModule = module {
    factory { BusinessPoints(get()) }
}

val userModule = module {
    factory { BusinessUser(get(), get()) }
}

val EmployeeViewModelModule = module {
    viewModel { EmployeeViewModel(get(), get()) }
}

val viewModelPoints = module {
    viewModel { ViewModelPoints(get(), get()) }
}

val adapterModule = module {
    factory { AdapterPoints(get()) }
}

val pointsAdapterModule = module {
    factory { PointsAdapter(get(), get()) }
}

val dataBaseEmployeeModule = module {
    single { DataBaseEmployee(get()) }
}

val securityPreferencesModule = module {
    single { SecurityPreferences(get()) }
}

val databaseUserModule = module {
    single { DataBaseUser(get()) }
}

val converterPhotoModule = module {
    factory { ConverterPhoto() }
}

val calculationHoursModule = module {
    factory { CalculationHours() }
}

val captureDateModule = module {
    factory { CaptureDateCurrent() }
}

val appModules = listOf(
    repositoryModule, EmployeeViewModelModule, businessModule,
    pointsModule, userModule, adapterModule, viewModelPoints, repositoryPointModule,
    dataBaseEmployeeModule, securityPreferencesModule, pointsAdapterModule,
    converterPhotoModule, calculationHoursModule, repositoryUserModule,
    databaseUserModule, repositoryEmployeeModule, captureDateModule
)