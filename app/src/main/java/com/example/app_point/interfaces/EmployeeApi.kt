package com.example.app_point.interfaces

import retrofit2.Response
import retrofit2.http.GET

interface EmployeeApi {
    @GET("/funcionarios.json")
    suspend fun getEmployeeApi(): Response<Any>
}