package com.example.app_point.entity

import java.io.Serializable

data class EmployeeNameAndPhoto(
    val id: Int,
    val photo: ByteArray,
    val name: String
):Serializable
