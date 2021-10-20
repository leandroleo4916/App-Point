package com.example.app_point.entity

data class Employee(
    val id: Int,
    val photo: ByteArray,
    val name: String,
    val cargo: String,
    val admission: String,
    var date: String = "",
    var hour1: String = "",
    var hour2: String = "",
    var hour3: String = "",
    var hour4: String = "",
)
