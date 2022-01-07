package com.example.app_point.di

import com.example.app_point.activitys.ui.dashboard.AdapterDashboardDetail
import com.example.app_point.activitys.ui.dashboard.AdapterDashboardRanking
import com.example.app_point.activitys.ui.dashboard.DashboardViewModel
import com.example.app_point.activitys.ui.employee.EmployeeViewModel
import com.example.app_point.activitys.ui.login.BusinessUser
import com.example.app_point.adapters.AdapterPoints
import com.example.app_point.adapters.PointsAdapter
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.database.DataBaseEmployee
import com.example.app_point.database.DataBaseUser
import com.example.app_point.repository.RepositoryEmployee
import com.example.app_point.repository.RepositoryFirebase
import com.example.app_point.repository.RepositoryPoint
import com.example.app_point.repository.RepositoryUser
import com.example.app_point.utils.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryUserModule = module { single { RepositoryUser(get()) } }
val repositoryEmployeeModule = module { single { RepositoryEmployee(get()) } }
val repositoryPointModule = module { single { RepositoryPoint(get()) } }
val repositoryModule = module { single { RepositoryFirebase(get()) } }
val businessModule = module { factory { BusinessEmployee(get()) } }
val userModule = module { factory { BusinessUser(get(), get()) } }
val employeeModule = module { viewModel { EmployeeViewModel(get(), get(), get()) } }
val viewModelDashboardModule = module {viewModel{DashboardViewModel(get(),get(),get(),get())}}
val adapterModule = module { factory { AdapterPoints(get()) } }
val pointsAdapterModule = module { factory { PointsAdapter(get(), get()) } }
val dataBaseEmployeeModule = module { single { DataBaseEmployee(get()) } }
val securityPreferencesModule = module { single { SecurityPreferences(get()) } }
val databaseUserModule = module { single { DataBaseUser(get()) } }
val converterPhotoModule = module { factory { ConverterPhoto() } }
val adapterRankingModule = module { factory { AdapterDashboardRanking(get()) } }
val adapterDetailModule = module { factory { AdapterDashboardDetail(get()) } }
val calculationHoursModule = module { factory { CalculateHours() } }
val captureDateModule = module { factory { CaptureDateCurrent() } }
val colorModule = module { factory { GetColor() } }

val appModules = listOf(
    repositoryModule, employeeModule, businessModule, colorModule, adapterRankingModule,
    userModule, adapterModule, repositoryPointModule, captureDateModule,
    dataBaseEmployeeModule, securityPreferencesModule, pointsAdapterModule, databaseUserModule,
    converterPhotoModule, calculationHoursModule, repositoryUserModule, repositoryEmployeeModule,
    adapterDetailModule, viewModelDashboardModule
)