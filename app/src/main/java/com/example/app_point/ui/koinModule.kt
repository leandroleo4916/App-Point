package com.example.app_point.ui

import com.example.app_point.business.BusinessEmployee
import com.example.app_point.business.BusinessPoints
import com.example.app_point.business.BusinessUser
import com.example.app_point.model.*
import com.example.app_point.repository.RepositoryEmployee
import com.example.app_point.repository.RepositoryFirebase
import com.example.app_point.repository.RepositoryPoint
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

val businessModule = module {
    factory { BusinessEmployee(get()) }
}

val pointsModule = module {
    factory { BusinessPoints(get())
    }
}
val userModule = module {
    factory { BusinessUser(get()) }
}

val repositoryModule = module {
    single { RepositoryFirebase(get()) }
}

val viewModelModule = module {
    viewModel { ViewModelEmployee(get()) }
}

val viewModelPoints = module {
    viewModel { ViewModelPoints(get()) }
}

val adapterModule = module {
    factory { AdapterPoints(get()) }
}

val appModules = listOf(
    retrofitModule, repositoryModule, viewModelModule, businessModule,
    pointsModule, userModule, adapterModule, viewModelPoints
)