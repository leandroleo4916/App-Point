package com.example.app_point.entity

import java.io.Serializable

data class EmployeeEntity(
    val id: Int,
    val status: String,
    val photo: ByteArray,
    val horario1: Int,
    val horario2: Int,
    val horario3: Int,
    val horario4: Int,
    val workload: Int,
    val nameEmployee: String,
    val cargoEmployee: String,
    val emailEmployee: String,
    val phoneEmployee: String,
    val admissaoEmployee: String,
    val aniversarioEmployee: String
): Serializable
