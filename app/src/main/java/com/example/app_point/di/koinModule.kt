package com.example.app_point.di

import com.example.app_point.adapters.AdapterPoints
import com.example.app_point.adapters.EmployeeAdapter
import com.example.app_point.adapters.PointsAdapter
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.business.BusinessPoints
import com.example.app_point.business.BusinessUser
import com.example.app_point.business.CalculationHours
import com.example.app_point.database.DataBaseEmployee
import com.example.app_point.database.DataBaseUser
import com.example.app_point.interfaces.EmployeeApi
import com.example.app_point.model.*
import com.example.app_point.repository.RepositoryEmployee
import com.example.app_point.repository.RepositoryFirebase
import com.example.app_point.repository.RepositoryPoint
import com.example.app_point.repository.RepositoryUser
import com.example.app_point.utils.CaptureDateCurrent
import com.example.app_point.utils.ConverterPhoto
import com.example.app_point.utils.SecurityPreferences
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val database: DatabaseReference = Firebase.database.reference

val retrofitModule = module {
    val url = database.toString()
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(url)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<OkHttpClient> {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
    single<EmployeeApi> { get<Retrofit>().create(EmployeeApi::class.java) }
}

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

val viewModelEmployeeModule = module {
    viewModel { ViewModelEmployee(get(), get(), get()) }
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
    retrofitModule, repositoryModule, viewModelEmployeeModule, businessModule,
    pointsModule, userModule, adapterModule, viewModelPoints, repositoryPointModule,
    dataBaseEmployeeModule, securityPreferencesModule, pointsAdapterModule,
    converterPhotoModule, calculationHoursModule, repositoryUserModule,
    databaseUserModule, repositoryEmployeeModule, captureDateModule
)